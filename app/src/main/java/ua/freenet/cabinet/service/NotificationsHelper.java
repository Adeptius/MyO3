package ua.freenet.cabinet.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.activities.LoginActivity;

import static ua.freenet.cabinet.utils.Utilits.log;

class NotificationsHelper {

    private static Context appContext; // контекст приложения
    private static final int NOTIFICATION_ID = 1;
    private static NotificationManager manager; // менеджер уведомлений

    // метод инциализации данного хелпера
    public static void init(Context context) {
        if (manager == null) {
            appContext = context.getApplicationContext(); // на случай инициализации Base Context-ом
            manager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    private static NotificationCompat.Builder getNotificationBuilder(String notAnothMessage) {
        return new NotificationCompat.Builder(appContext)
                .setAutoCancel(true) // чтобы уведомление закрылось после тапа по нему
                .setOnlyAlertOnce(true) // уведомить однократно
                .setWhen(System.currentTimeMillis()) // время создания уведомления, будет отображено в стандартном уведомлении справа
                .setContentTitle("ФРИНЕТ") //заголовок
                .setDefaults(Notification.DEFAULT_ALL) // alarm при выводе уведомления: звук, вибратор и диод-индикатор - по умолчанию
                .setContentText(notAnothMessage) // сообщение, которое будет отображаться в самом уведомлении
                .setTicker(notAnothMessage) //сообщение, которое будет показано в статус-баре при создании уведомления, ставлю тот же
                .setSmallIcon(R.mipmap.ic_launcher);
    }


    static void createNotification(int notAnoth, final String message) {
        String notAnothMessage = "Не вистачає " + notAnoth + " грн на наступний місяць";
        Intent resultIntent = new Intent(appContext, LoginActivity.class);
        resultIntent.putExtra("notAnothMessage", message);
        notifyAbon(notAnothMessage,resultIntent);
    }

    static void createNotificationFirstMonth(int notAnoth) {
        String notAnothMessage = "Не вистачило " + notAnoth + " грн на цей місяць";
        Intent resultIntent = new Intent(appContext, LoginActivity.class);
        notifyAbon(notAnothMessage, resultIntent);
    }

    private static void notifyAbon(String notAnothMessage, Intent resultIntent){
        if (manager == null) {
            log("notifyAbon() NotificationUtils not initialized! manager is null");
            return;
        }
        final NotificationCompat.Builder nb = getNotificationBuilder(notAnothMessage); // получаем из хелпера generic Builder, и далее донастраиваем его
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.setContentIntent(resultPendingIntent);
        final Notification notification = nb.build(); //генерируем уведомление, getNotification() - deprecated!
        manager.notify(NOTIFICATION_ID, notification); // "запускаем" уведомление
    }
}