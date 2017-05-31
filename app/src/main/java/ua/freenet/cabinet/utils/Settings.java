package ua.freenet.cabinet.utils;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ua.freenet.cabinet.model.City;
import ua.freenet.cabinet.model.PreviouslyPerson;

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
        setServiceCheckedDay(-1);
        setMonthPaydFutureMonth(-1);
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

    //ServiceCheckedDay
    public static void setServiceCheckedDay(int day) {
        settingsEditor.putString("checkedDay", String.valueOf(day));
        settingsEditor.commit();
    }

    public static int getServiceCheckedDay() {
        String s = sPref.getString("checkedDay", "");
        if (s.equals("")) {
            return -1;
        } else {
            return Integer.valueOf(s);
        }
    }

    //MonthPaydFutureMonth
    public static void setMonthPaydFutureMonth(int month) {
        settingsEditor.putString("monthPaydFutureMonth", String.valueOf(month));
        settingsEditor.commit();
    }

    public static int getMonthPaydFutureMonth() {
        String s = sPref.getString("monthPaydFutureMonth", "");
        if (s.equals("")) {
            return -1;
        } else {
            return Integer.valueOf(s);
        }
    }

    //MonthPaydFutureMonth
    public static void setMonthPaydCurrentMonth(int month) {
        settingsEditor.putString("monthPaydCurrentMonth", String.valueOf(month));
        settingsEditor.commit();
    }

    public static int getMonthPaydCurrentMonth() {
        String s = sPref.getString("monthPaydCurrentMonth", "");
        if (s.equals("")) {
            return -1;
        } else {
            return Integer.valueOf(s);
        }
    }

    //Login
    public static void setLastTimeSendReport(long time) {
        settingsEditor.putString("lastTimeSendReport", "" + time);
        settingsEditor.commit();
    }

    public static long getLastTimeSendReport() {
        try {
            return Long.parseLong(sPref.getString("lastTimeSendReport", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public static void setPreviouslyPersons(Set<PreviouslyPerson> set) {
        try {
            String json = new Gson().toJson(set);
            settingsEditor.putString("previouslyPersons", json);
            settingsEditor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Set<PreviouslyPerson> getPreviouslyPersons() {
        String s = sPref.getString("previouslyPersons", "");
        if (s == null || s.equals("")) {
            return new HashSet<>();
        }
        try {
            Type listType = new TypeToken<Set<PreviouslyPerson>>() {
            }.getType();
            Set<PreviouslyPerson> previouslyLoginAndPasswords = new Gson().fromJson(s, listType);
            return previouslyLoginAndPasswords;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}
