package ua.freenet.cabinet.service;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;


public class BackgroundService extends Service {

    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Checker checker = new Checker(nm, this);
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