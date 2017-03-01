package ua.freenet.cabinet.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import ua.freenet.cabinet.utils.Settings;
import static ua.freenet.cabinet.utils.Utilits.log;

public class BackgroundService extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log("Сервис запущен");
        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));
        new MyAlarmManager().setUpAlarm(this);
    }
}