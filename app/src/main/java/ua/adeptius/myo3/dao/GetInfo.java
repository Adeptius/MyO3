package ua.adeptius.myo3.dao;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Operation;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.model.persons.Servise;
import ua.adeptius.myo3.utils.Utilits;

import static ua.adeptius.myo3.utils.Utilits.splitJson;


public class GetInfo {

    public static List<Servise> getServises() throws Exception {
        String s = Web.getJsonFromUrl("https://my.o3.ua/ajax/services");
        s = s.substring(1, s.length() - 1);
        List<Servise> servises = new ArrayList<>();
        String[] splitted = Utilits.splitJsonAltAlhoritm(s);
        for (String s1 : splitted) {
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
