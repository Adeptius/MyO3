package ua.adeptius.freenet.utils;


import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONObject;

import ua.adeptius.freenet.model.City;

public class Settings {

    private static SharedPreferences sPref;
    private static SharedPreferences.Editor settingsEditor;

    public static void setsPref(SharedPreferences sPref) {
        if (Settings.sPref == null) {
            Settings.sPref = sPref;
            Settings.settingsEditor = sPref.edit();
        }
    }

    public static void clearAllData() {
        setCurrentLogin("");
        setCurrentPassword("");
        setCurrentPassportSerial("");
        setCurrentPassportNumber("");
        setUrlAccii("");
        setUrlNews("");
        setUrlPhone("");
    }

    //Login
    public static void setCurrentLogin(String currentLogin) {
        settingsEditor.putString("login", currentLogin);
        settingsEditor.commit();
    }

    public static String getCurrentLogin() {
        return sPref.getString("login", "");
    }

    //Password
    public static void setCurrentPassword(String currentPassword) {
        settingsEditor.putString("password", currentPassword);
        settingsEditor.commit();
    }

    public static String getCurrentPassword() {
        return sPref.getString("password", "");
    }

    //passportSerial
    public static void setCurrentPassportSerial(String currentPassword) {
        settingsEditor.putString("passportSerial", currentPassword);
        settingsEditor.commit();
    }

    public static String getCurrentPassportSerial() {
        return sPref.getString("passportSerial", "");
    }

    //passportNumber
    public static void setCurrentPassportNumber(String currentPassword) {
        settingsEditor.putString("passportNumber", currentPassword);
        settingsEditor.commit();
    }

    public static String getCurrentPassportNumber() {
        return sPref.getString("passportNumber", "");
    }


    //passportNumber
    public static void setUrlNews(String url) {
        settingsEditor.putString("urlNews", url);
        settingsEditor.commit();
    }

    public static String getUrlNews() {
        return sPref.getString("urlNews", "");
    }


    //passportNumber
    public static void setUrlAccii(String url) {
        settingsEditor.putString("urlAccii", url);
        settingsEditor.commit();
    }

    public static String getUrlAccii() {
        return sPref.getString("urlAccii", "");
    }


    //passportNumber
    public static void setUrlPhone(String url) {
        settingsEditor.putString("urlPhone", url);
        settingsEditor.commit();
    }

    public static String getUrlPhone() {
        return sPref.getString("urlPhone", "");
    }


    //City
    public static void saveCity(City city) {
        Gson gson = new Gson();
        String s = gson.toJson(city);
        settingsEditor.putString("city", s);
        settingsEditor.commit();
    }

    public static City loadCity() {
        String s = sPref.getString("city", "");
        City c = new Gson().fromJson(s, City.class);
        return c;
    }
}