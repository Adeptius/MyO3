package ua.adeptius.myo3.model;


import android.content.SharedPreferences;

public class Settings {

    public static SharedPreferences sPref;
    public static SharedPreferences.Editor settingsEditor;

    public static void setsPref(SharedPreferences sPref) {
        if (Settings.sPref == null) {
            Settings.sPref = sPref;
            Settings.settingsEditor = sPref.edit();
        }
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

    //SessionID
    public static void setSessionID(String session) {
        settingsEditor.putString("session", session);
        settingsEditor.commit();
    }

    public static String getSessionID() {
        return sPref.getString("session", "");
    }







}
