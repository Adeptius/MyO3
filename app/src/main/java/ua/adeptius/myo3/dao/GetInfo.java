package ua.adeptius.myo3.dao;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.AvailableTarif;
import ua.adeptius.myo3.model.persons.DrWebSubscribe;
import ua.adeptius.myo3.model.persons.Operation;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.model.persons.Servise;
import ua.adeptius.myo3.utils.Utilits;

import static ua.adeptius.myo3.utils.Utilits.splitJson;


public class GetInfo {

    public static List<DrWebSubscribe> getDrWebServices() throws Exception{
        Utilits.networkLog("Запрос сервисов DrWeb");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/services/dr_web");
        s = s.substring(1, s.length()-1);
        List<DrWebSubscribe> subscribes = new ArrayList<>();
        if (s.equals("")) return subscribes;
        String[] splitted = Utilits.splitJson(s);
        for (String s1 : splitted) {
            subscribes.add(new DrWebSubscribe(s1));
        }
        return subscribes;
    }

    public static Boolean[] getInternetSwitches() throws Exception{
        Utilits.networkLog("Запрос состояние вкл/выкл инета");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/internet/status");
        JSONObject jsonObject = new JSONObject(s);
        Boolean[] status = new Boolean[]{
                jsonObject.getBoolean("all"),
                jsonObject.getBoolean("world")
        };
        return status;
    }

    public static String isGarantedServiceEnabled() throws Exception{
        Utilits.networkLog("Запрос состояние гарантированного сервиса");
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/guaranteed_service");
        if (s.contains("e_date")) return s.substring(s.indexOf("e_date")+9, s.lastIndexOf(" "));
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
        Utilits.networkLog("Запрос доступных тарифов");
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
