package ua.adeptius.myo3.dao;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.AvailableTarif;
import ua.adeptius.myo3.model.persons.Operation;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.model.persons.Servise;
import ua.adeptius.myo3.utils.Utilits;

import static ua.adeptius.myo3.utils.Utilits.splitJson;


public class GetInfo {

    public static HashMap<String, String> getCreditStatus() throws Exception {
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/check_credit");
        JSONObject json = new JSONObject(s);
        HashMap<String, String> map = new HashMap<>();
        map.put("pending_enable", json.getString("pending_enable"));

        if(json.has("active")) map.put("active", json.getString("active"));
        if(json.has("allow")) map.put("allow", json.getString("allow"));
        if(json.has("pending_restore")) map.put("pending_restore", json.getString("pending_restore"));
        return map;
    }

    public static List<String> getTurboDayStatistics() throws Exception {
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
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/free_days");
        HashMap<String, Integer> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject(s);
        map.put("daysLeft", Integer.parseInt(jsonObject.get("daysLeft").toString()));
        map.put("daysTotal", Integer.parseInt(jsonObject.get("daysTotal").toString()));
        return map;
    }

    public static List<AvailableTarif> getAvailableTarifs(String serviceId) throws Exception {
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

    public static List<Operation> getWildraws(String date) throws Exception {
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
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/persons");
        return new Person(json);
    }

    public static String getMountlyFee() throws Exception {
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/monthly_fee");
        return new JSONObject(json).getString("total");
    }

    public static List<Ip> getIP() throws Exception {
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
