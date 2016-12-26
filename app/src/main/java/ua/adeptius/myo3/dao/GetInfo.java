package ua.adeptius.myo3.dao;


import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.utils.Utilits;

public class GetInfo {

    public static Person getPersonInfo() throws Exception{
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/persons");
        return new Person(json);
    }

    public static String getMountlyFee(){
        try {
            String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/monthly_fee");
            return new JSONObject(json).getString("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<Ip> getIP() throws Exception {
        String json = Web.getJsonFromUrl("https://my.o3.ua/ajax/network_settings");
        JSONObject jobject = new JSONObject(json.trim());
        json = jobject.get("ips").toString();
        json = json.substring(1, json.length() - 1).trim();
        String[] splitted = Utilits.splitJson(json);
        List<Ip> ips = new ArrayList<>();
        for (String s : splitted) {
            ips.add(new Ip(s));
        }
        return ips;
    }


}
