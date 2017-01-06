package ua.adeptius.myo3.dao;

import com.gistlabs.mechanize.MechanizeAgent;
import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.Document;
import com.gistlabs.mechanize.document.html.form.Form;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import ua.adeptius.myo3.model.ChannelMegogo;
import ua.adeptius.myo3.utils.Settings;
import ua.adeptius.myo3.utils.Utilits;

public class Web {

    private static String sessionId;
    private static long sessionCreatedTime;

    public static String getSessionId() throws Exception {
        if (sessionId == null){
            setSessionId(getPhpSession());
            return getSessionId();
        }else {
            long currentTime = new GregorianCalendar().getTimeInMillis();
            long sessionTime = currentTime - sessionCreatedTime;
            int minutes = (int) sessionTime / 1000 / 60;
            if (minutes > 30) {
                setSessionId(getPhpSession());
                return getSessionId();
            } else {
                return sessionId;
            }
        }
    }

    private static void setSessionId(String sessionID) {
        sessionId = sessionID;
        sessionCreatedTime = new GregorianCalendar().getTimeInMillis();
    }

    private static String getPhpSession() throws Exception {
        MechanizeAgent agent = new MechanizeAgent();
        Document page = agent.get("https://my.o3.ua/login");
        Form form = page.form("login_form");
        form.get("_username").set(Settings.getCurrentLogin());
        form.get("_password").set(Settings.getCurrentPassword());
        Resource response = form.submit();
        if ("Redirecting to https://my.o3.ua/".equals(response.getTitle()) ||
                "Особистий кабінет".equals(response.getTitle())) {
            String session = agent.cookies().getAll().get(0).getValue();
            agent = null;
            page = null;
            response = null;
            form = null;
            return session;
        } else {
            throw new Exception();
        }
    }

//    public static boolean isLoggedNow() throws Exception {
//        String result = getJsonFromUrl("https://my.o3.ua/ajax/persons");
//        if (result.equals("<!DOCTYPE html>")) {
//            return false;
//        } else if (result.startsWith("{\"id\":")) {
//            return true;
//        }
//        return false;
//    }




    public static String getJsonFromUrl(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Cookie", "PHPSESSID=" + getSessionId());
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = in.readLine();
        result = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(result);
        Utilits.networkLog("Получен Json: " + result);
        in.close();
        return result;
    }

    public static String sendPost(String url, HashMap<String, String> jSonQuery, boolean itsJson) throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Cookie", "PHPSESSID=" + getSessionId());
        String urlParameters = "";
        if (itsJson){
            con.setRequestProperty("Content-Type", "application/json");
            urlParameters = new JSONObject(jSonQuery).toString();
        } else {
            Object[] keys = jSonQuery.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                String key = (String) keys[i];
                urlParameters += key + "=" + jSonQuery.get(key);
                if (!(i==keys.length-1)) urlParameters += "&";
            }
        }
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        Utilits.networkLog("Передаю параметры: " + urlParameters);
        wr.writeBytes(urlParameters);
        wr.flush(); wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = in.readLine();
        result = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(result);
        Utilits.networkLog("Ответ: " + result);
        in.close();
        return result;
    }

    public static List<ChannelMegogo> getMegogoChannels(String url) throws Exception {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        Elements divs = doc.body().getElementsByClass("videos-inner");
        List<ChannelMegogo> channelMegogos = new ArrayList<>();
        for (Element div : divs) {
            String category = "";
            try {
                category = div.getElementsByTag("h3").first().html();
            } catch (Exception e) {}
            Element ul = div.getElementsByClass("videos-mask").first()
                    .getElementsByTag("ul").first();
            Elements listOfLi = ul.getElementsByTag("li");
            for (Element li : listOfLi) {
                Element li2 = li;
                String description = li.attr("data-description");
                String iconUrl = li.getElementsByClass("voi__poster").first().attr("style");
                channelMegogos.add(new ChannelMegogo(description, iconUrl, category));
            }
        }
        return channelMegogos;
    }
}
