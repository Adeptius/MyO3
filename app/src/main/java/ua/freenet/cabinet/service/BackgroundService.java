package ua.freenet.cabinet.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import ua.freenet.cabinet.utils.Settings;

import static ua.freenet.cabinet.utils.Utilits.log;

public class BackgroundService extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        log("Сервис запущен");
        if (preferences == null){
            preferences = getSharedPreferences("settings", MODE_PRIVATE);
        }
        Settings.setsPref(preferences);
        new MyAlarmManager().setUpAlarm(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Checker.sendInfo(BackgroundService.this);
//            }
//        }).start();
    }
}