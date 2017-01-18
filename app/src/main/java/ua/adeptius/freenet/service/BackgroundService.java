package ua.adeptius.freenet.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import ua.adeptius.freenet.model.Servise;

/**
 * Created by Владимир on 17.01.2017.
 */

public class BackgroundService  extends Service {

    public static ArrayList<String> newTasksIds;
    public static ArrayList<String> wasTasksIds;
    public static String currentLogin;
    public static String currentPassword;
    public static final int NOTIFICATION_ID = 1;
    public static int wasNewTaskCountInLastTime = 0;
    static Context context;
    static NotificationManager mNotificationManager;

    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(500000);
                        System.out.println("!!!!");
                    } catch (Exception ignored) {
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }




    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("asd","Сервис запущен");
//        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));
//        currentLogin = Settings.getCurrentLogin();
//        currentPassword = Settings.getCurrentPassword();
//        context = this;
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("asd","Сервис уничтожен: " + stopSelfResult(1));
    }
}