package ua.adeptius.myo3.utils;


import android.content.SharedPreferences;

public class Settings {

    private static SharedPreferences sPref;
    private static SharedPreferences.Editor settingsEditor;

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
}