package ua.adeptius.myo3.utils;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utilits {

    public static final Handler HANDLER = new Handler();
    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

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

    public static float dpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}
