package ua.adeptius.myo3.utils;


import android.util.Log;

public class Utilits {

    private static boolean networkLogEnable = true;
    private static boolean miscLogEnable = true;

    public static void networkLog(String message){
        if (networkLogEnable) Log.d("***O3 Logger***", message);
    }

    public static void miscLog(String message){
        if (miscLogEnable) Log.d("***O3 Logger***", message);
    }

    public static String[] splitJson(String json){
        String[] splittedJson;
        if (json.contains("},{")){
            splittedJson = json.split("\\},\\{");
            for (int i = 0; i < splittedJson.length; i++) {
                if (i==0) splittedJson[i] += "}";
                if (i==splittedJson.length-1) splittedJson[i] = "{" + splittedJson[i];
                if (i!=0 && i!=splittedJson.length-1) splittedJson[i] = "{" + splittedJson[i] + "}";
            }
        }else {
            splittedJson = new String[1];
            splittedJson[0] = json;
        }
        return splittedJson;
    }

}
