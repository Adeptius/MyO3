package ua.adeptius.freenet.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import ua.adeptius.freenet.utils.Settings;
import ua.adeptius.freenet.utils.Utilits;


public class BackgroundService  extends Service {

    public int onStartCommand(Intent intent, int flags, int startId) {
        Checker checker = new Checker();
        checker.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utilits.log("Сервис запущен");
        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utilits.log("Сервис уничтожен");
    }
}