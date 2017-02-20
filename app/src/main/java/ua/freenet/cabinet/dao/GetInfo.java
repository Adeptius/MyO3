package ua.freenet.cabinet.dao;


import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.freenet.cabinet.model.BonusServiceSpending;
import ua.freenet.cabinet.model.ChannelDivan;
import ua.freenet.cabinet.model.ChannelDivanDetails;
import ua.freenet.cabinet.model.ChannelMegogo;
import ua.freenet.cabinet.model.ChannelOllTv;
import ua.freenet.cabinet.model.City;
import ua.freenet.cabinet.model.FriendInvite;
import ua.freenet.cabinet.model.Ip;
import ua.freenet.cabinet.model.AvailableTarif;
import ua.freenet.cabinet.model.DrWebSubscribe;
import ua.freenet.cabinet.model.MegogoPts;
import ua.freenet.cabinet.model.Operation;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.model.Servise;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.utils.Utilits.log;
import static ua.freenet.cabinet.utils.Utilits.splitJson;


public class GetInfo {


    public static City getCityPhones(String url) throws Exception {
        Utilits.networkLog("Получение списка телефонов по урл: " + url);
        Document doc = Jsoup.connect(url).get();
        Elements editors = doc.getElementsByClass("text-editor row");

        City phones = new City();

        for (Element editor : editors) {
            String raw = editor.toString();
            raw = raw.replaceAll("&nbsp;", " ");
            if (raw.contains("Телефон служби")) {
                phones.setSupport(Utilits.getPhonesFromString(raw));
            } else if (raw.contains("діл та відд")) {
                phones.setAbon(Utilits.getPhonesFromString(raw));
            } else if (url.equals("https://o3.ua/ua/contacts/")) {

                List<String> call = new ArrayList<>();
                call.add("(044) 593-39-29");
                List<String> abon = new ArrayList<>();
                abon.add("(044) 538-15-19");
                phones.setCall(call);
                phones.setAbon(abon);
            }
        }

        Element divContacts = doc.getElementsByTag("tbody").first();
        Element tr = divContacts.getElementsByTag("tr").get(1);
        Elements td = tr.getElementsByTag("td");
        String time = td.get(0).html().replaceAll("&nbsp;", " ");
        String reason = td.get(1).html().replaceAll("&nbsp;", " ");
        if (reason.contains("span")) {
            reason = td.get(1).getElementsByTag("span").html().replaceAll("&nbsp;", " ");
        }
        Element a = td.get(2).getElementsByTag("a").first();
        String adress = a.html().replaceAll("&nbsp;", " ");
        String moreAddress = td.get(2).toString();
        if (moreAddress.contains("<br>")) {
            moreAddress = moreAddress.substring(moreAddress.indexOf("<br>") + 4);
            moreAddress = moreAddress.substring(0, moreAddress.indexOf("<"));
            adress = adress + " " + moreAddress;
        }

        String coordinates = a.attr("data-lat") + " " + a.attr("data-lon");

        phones.setTime(time);
        phones.setReason(reason);
        phones.setAdress(adress);
        phones.setCoordinates(coordinates);

        return phones;
    }

    public static String getUrlOfCityPhones(String city) throws Exception {
        String result = getUrlOfCity(city, "http://o3.ua/ua/contacts/");
        Settings.setUrlPhone(result);
        return result;
    }

    public static String getUrlOfCityNews(String city) throws Exception {
        String result = getUrlOfCity(city, "http://o3.ua/ua/about/news/");
        Settings.setUrlNews(result);
        return result;
    }

    public static String getUrlOfCityAccii(String city) throws Exception {
        String result = getUrlOfCity(city, "http://o3.ua/ua/about/akzii_programma_loyalnosti/");
        Settings.setUrlAccii(result);
        return result;
    }

    private static String getUrlOfCity(String city, String generalUrl) throws Exception {
        Utilits.networkLog("Получение уникального урла на страницу города " + city);
        Document doc = Jsoup.connect(generalUrl).get();
        Element list = doc.getElementById("citySelect");
        Elements options = list.getElementsByTag("option");
        for (Element option : options) {
            if (city.equals(option.html())) {
                String url2 = option.attr("value");
                url2 = url2.replaceAll("\u200B", "");
                return url2;
            }
        }
        return "Ошибка получения урла для города " + city;
    }

    public static boolean[] getBonusesStatus() throws Exception {
        Utilits.networkLog("Запрос состояния бонусов");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/check_bonus");
        boolean[] boo = new boolean[2];
        boo[0] = new JSONObject(s).getString("public_card").equals("1");
        boo[1] = new JSONObject(s).getBoolean("confirmed_bonus");
        return boo;
    }

    public static List<BonusServiceSpending> getBonusesSpending() throws Exception {
        Utilits.networkLog("Запрос списка сервисов для оплаты");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/spending");
        s = s.substring(1, s.length() - 1);
        List<BonusServiceSpending> list = new ArrayList<>();
        String[] splitted = Utilits.splitJson(s);
        for (String s1 : splitted) {
            list.add(new BonusServiceSpending(s1));
        }
        return list;
    }

    public static int getCountOfBonuses() throws Exception {
        Utilits.networkLog("Запрос количества бонусов");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/amount_bonus");
        double d = Double.parseDouble(s);
        return (int) d;
    }

    public static List<FriendInvite> getFriendInvites() throws Exception {
        Utilits.networkLog("Запрос приглашений друзей");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/refer");
        List<FriendInvite> invites = new ArrayList<>();
        if (s.equals("[]")) return new ArrayList<>();
        s = s.substring(1, s.length() - 1);
        String[] splitted = Utilits.splitJson(s);
        for (String s1 : splitted) {
            invites.add(new FriendInvite(s1));
        }
        return invites;
    }

    public static List<ChannelOllTv> getOllTvChanels(String url) throws Exception {
        Utilits.networkLog("Получение каналов олл тв");
        Document doc = Jsoup.connect(url).get();

        Elements lis = doc.getElementsByClass("tv-chan b-channels-list").first()
                .getElementsByTag("li");
        List<ChannelOllTv> list = new ArrayList<>();
        for (Element li : lis) {
            String imgUrl = li.getElementsByTag("img").first().attr("src");
            String name = li.getElementsByClass("head").first().html();
            list.add(new ChannelOllTv(name, imgUrl));
        }
        return list;
    }

    public static ChannelDivanDetails getDivanDetails(String chanelId) throws Exception {
        String url = "https://divan.tv/tv/channelInfo/" + chanelId;
        log("Запрос инфы о канале: " + chanelId);
        Document doc = Jsoup.connect(url).get();

        String image = doc.getElementsByClass("image").first().getElementsByTag("img").first().attr("src");
        String name = doc.getElementsByClass("tci-info").first().getElementsByTag("h1").first().html();
        String description = doc.getElementsByClass("presentation mt20").first().getElementsByTag("p").first().html();
        if ("".equals(description)) {
            description = doc.getElementsByClass("presentation mt20").first().getElementsByTag("p").get(1).html();
        }
        if (description.startsWith("<strong>Где смотреть:</strong>")) description = "";
        Elements availableOn = doc.getElementsByClass("tci-watch-block").first().getElementsByTag("a");
        String listAvailableOn = availableOn.get(0).html();
        for (int i = 1; i < availableOn.size(); i++) {
            listAvailableOn += "\n" + availableOn.get(i).html();
        }


        Elements availableIn = doc.getElementsByClass("presentation mt20").first()
                .getElementsByTag("p").last()
                .getElementsByTag("a");

        String listAvailableIn = availableIn.get(0).html();
        for (int i = 1; i < availableIn.size(); i++) {
            listAvailableIn += "\n" + availableIn.get(i).html();
        }

        return new ChannelDivanDetails(image, name, description, listAvailableIn, listAvailableOn);
    }

    public static List<ChannelDivan> getDivanChanels(String url) throws Exception {
        String subscription = URLDecoder.decode(url, "UTF-8");
        log("Запрос каналов в подписке: " + subscription.substring(subscription.indexOf("name=") + 5));
        Document doc = Jsoup.connect(url).get();
        Element ul = doc.getElementsByClass("ci-channels").first();
        Elements lis = ul.getElementsByTag("li");
        List<ChannelDivan> channels = new ArrayList<>();
        for (Element li : lis) {
            Element firstDiv = li.getElementsByTag("div").first();
            String id = firstDiv.attr("data-id");
            Element secondDiv = firstDiv.getElementsByClass("image").first();
            Element img = secondDiv.getElementsByTag("img").first();
            String name = img.attr("alt");
            String imageUrl = img.attr("src");
            ChannelDivan channel = new ChannelDivan(id, name, imageUrl);
            channels.add(channel);
        }
        log("Получено " + channels.size() + " каналов");
        return channels;
    }

    public static List<ChannelMegogo> getMegogoChannels(String url) throws Exception {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        Elements divs = doc.body().getElementsByClass("videos-inner");
        List<ChannelMegogo> channelMegogos = new ArrayList<>();
        for (Element div : divs) {
            Element ul = div.getElementsByClass("videos-mask").first()
                    .getElementsByTag("ul").first();
            Elements listOfLi = ul.getElementsByTag("li");
            for (Element li : listOfLi) {
                Element li2 = li;
                String description = li.attr("data-description");
                String iconUrl = li.getElementsByClass("voi__poster").first().attr("style");
                channelMegogos.add(new ChannelMegogo(description, iconUrl));
            }
        }
        return channelMegogos;
    }

    public static String getMegogoActivationLink() throws Exception {
        Utilits.networkLog("Запрос ссылки на активацию мегого");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/megogo_auth_link");
        s = new JSONObject(s).getString("authLink");
        return s;
    }

    public static List<MegogoPts> getMegogoPts() throws Exception {
        Utilits.networkLog("Запрос подписок мегого");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/megogo");
        List<MegogoPts> pts = new ArrayList<>();

//        s = new JSONObject(s).getString("megogoPts");
        s = s.substring(s.indexOf("\"megogoPts\":{") + 12, s.length() - 1);
        s = s.substring(1, s.length() - 1);
        Matcher regexMatcher = Pattern.compile("\"\\d{1,5}\":").matcher(s);
        while (regexMatcher.find()) {
            String math = regexMatcher.group();
            s = s.replaceAll(math, "");
        }
        if (s.equals("")) return pts;
        String[] splitted = Utilits.splitJson(s);
        for (String s1 : splitted) {
            pts.add(new MegogoPts(s1));
        }
        return pts;
    }

    public static List<DrWebSubscribe> getDrWebServices() throws Exception {
        Utilits.networkLog("Запрос сервисов DrWeb");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/services/dr_web");
        s = s.substring(1, s.length() - 1);
        List<DrWebSubscribe> subscribes = new ArrayList<>();
        if (s.equals("")) return subscribes;
        String[] splitted = Utilits.splitJson(s);
        for (String s1 : splitted) {
            subscribes.add(new DrWebSubscribe(s1));
        }
        return subscribes;
    }

    public static Boolean[] getInternetSwitches() throws Exception {
        Utilits.networkLog("Запрос состояние вкл/выкл инета");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/internet/status");
        JSONObject jsonObject = new JSONObject(s);
        Boolean[] status = new Boolean[]{
                jsonObject.getBoolean("all"),
                jsonObject.getBoolean("world")
        };
        return status;
    }

    public static String getGarantedServiceStatus() throws Exception {
        Utilits.networkLog("Запрос состояние гарантированного сервиса");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/guaranteed_service");
        if (s.contains("e_date")) return s.substring(s.indexOf("e_date") + 9, s.lastIndexOf(" "));
        if (s.contains("pay_type")) return "enabled";
        if (s.equals("[]")) return "disabled";
        if (s.contains("\"pending_enable\": true")) return "enabling";
        if (s.contains("\"pending_disable\":true")) return "disabling";
        return "disabled";
    }

    public static String getCreditStatus() throws Exception {
        Utilits.networkLog("Запрос состояния кредита доверия");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/check_credit");
        JSONObject json = new JSONObject(s);

        if (s.contains("\"pending_enable\":true")) return "enabling";
        if (s.contains("\"pending_restore\":true")) return "restoring";
        if (s.contains("\"allow\":false")) return "disabled";
        return json.getString("active");
    }

    public static List<String> getTurboDayStatistics() throws Exception {
        Utilits.networkLog("Запрос статистики по турбо дню");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/turbo_history");
        List<String> statistics = new ArrayList<>();
        if (s.equals("[]")) return new ArrayList<>();
        s = s.substring(1, s.length() - 1);
        String[] splitted = Utilits.splitJson(s);
        for (String s1 : splitted) {
            JSONObject json = new JSONObject(s1);
            if (json.getString("payTypeName").contains("ТурбоДень")) {
                String sDate = json.getString("sDate");
                String eDate = json.getString("eDate");
                sDate = sDate.substring(0, sDate.lastIndexOf(":"));
                eDate = eDate.substring(0, eDate.lastIndexOf(":"));
                statistics.add("Від " + sDate + " до " + eDate);
            }
        }
        return statistics;
    }

    public static List<String> getFreeDayStatistics() throws Exception {
        Utilits.networkLog("Запрос статистики по свободному дню");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/turbo_history");
        List<String> statistics = new ArrayList<>();
        if (s.equals("[]")) return new ArrayList<>();
        s = s.substring(1, s.length() - 1);
        String[] splitted = Utilits.splitJson(s);
        for (String s1 : splitted) {
            JSONObject json = new JSONObject(s1);
            if (json.get("payTypeName").equals("FreeDay")) {
                String sDate = json.getString("sDate");
                String eDate = json.getString("eDate");
                if (Utilits.isDateIsCurrentMonth(sDate)) {
                    sDate = sDate.substring(0, sDate.lastIndexOf(":"));
                    eDate = eDate.substring(0, eDate.lastIndexOf(":"));
                    statistics.add("Від " + sDate + " до " + eDate);
                }
            }
        }
        return statistics;
    }

    public static HashMap<String, Integer> getFreeDayInfo() throws Exception {
        Utilits.networkLog("Запрос доступных дней по фридею");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/free_days");
        HashMap<String, Integer> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject(s);
        map.put("daysLeft", Integer.parseInt(jsonObject.get("daysLeft").toString()));
        map.put("daysTotal", Integer.parseInt(jsonObject.get("daysTotal").toString()));
        return map;
    }

    public static List<AvailableTarif> getAvailableTarifs(String serviceId) throws Exception {
        Utilits.networkLog("Запрос доступных тарифов мегого");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/new_pt?service_id=" + serviceId);
        s = s.substring(1, s.length() - 1);
        List<AvailableTarif> availableTarifList = new ArrayList<>();
        String[] splitted = Utilits.splitJsonAltAlhoritm(s);
        for (String s1 : splitted) {
            if (!s1.contains("Аренда приставок"))
                availableTarifList.add(new AvailableTarif(s1));
        }
        return availableTarifList;
    }

    public static List<Servise> getServises() throws Exception {
        Utilits.networkLog("Запрос подключенных сервисов");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/services");
        s = s.substring(1, s.length() - 1);
        List<Servise> servises = new ArrayList<>();
        String[] splitted = Utilits.splitJsonAltAlhoritm(s);
        for (String s1 : splitted) {
            if (!s1.contains("Аренда приставок"))
                servises.add(new Servise(s1));
        }
        return servises;
    }

    public static List<Operation> getWildrowsByFewMonth(int count) throws Exception {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "-" + Utilits.doTwoSymb(month);
        List<Operation> operationOneMonth = GetInfo.getWildraws(date);
        for (int i = 0; i < count; i++) {
            date = Utilits.oneMounthAgo(date);
            List<Operation> next = GetInfo.getWildraws(date);
            for (Operation operation : next) {
                operationOneMonth.add(operation);
            }
        }
        return operationOneMonth;
    }

    public static List<Operation> getWildraws(String date) throws Exception {
        Utilits.networkLog("Запрос платежей и списаний");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/balance/" + date);
        s = s.replaceAll("\"Повторная активация услуги \"Кредит доверия\"\"",
                "\"Повторная активация услуги Кредит доверия\"");
        List<Operation> operations = new ArrayList<>();
        if (s.equals("[]")) {
            Operation operation = new Operation("{\"date\":\"" + date + "\",\"money\":0,\"note\":\"Нема проплат та списань\",\"bonus\":null,\"p_id\":7077763}");
            operations.add(operation);
            return operations;
        }
        s = s.substring(1, s.length() - 1);
        String[] splitted = splitJson(s);
        for (String s1 : splitted) {
            operations.add(new Operation(s1));
        }
        return operations;
    }

    public static Person getPersonInfo() throws Exception {
        Utilits.networkLog("Запрос информации о пользователе");
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/persons");
        return new Person(json);
    }

    public static String getMountlyFeefromLK() throws Exception {
        Utilits.networkLog("Запрос абонентской платы");
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/monthly_fee");
        return new JSONObject(json).getString("total");
    }

    public static List<Ip> getIP() throws Exception {
        Utilits.networkLog("Запрос айпишек");
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/network_settings");
        JSONObject jobject = new JSONObject(json.trim());
        json = jobject.get("ips").toString();
        json = json.substring(1, json.length() - 1).trim();
        String[] splitted = splitJson(json);
        List<Ip> ips = new ArrayList<>();
        for (String s : splitted) {
            ips.add(new Ip(s));
        }
        return ips;
    }


}
