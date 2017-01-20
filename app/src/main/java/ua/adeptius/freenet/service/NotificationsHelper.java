package ua.adeptius.freenet.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import ua.adeptius.freenet.R;
import ua.adeptius.freenet.activities.LoginActivity;

import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

public class NotificationsHelper {

    private static Context appContext; // контекст приложения
    private static int lastNotificationId = 0; //уин последнего уведомления
    public static final int NOTIFICATION_ID = 1;

    private static NotificationManager manager; // менеджер уведомлений

    // метод инциализации данного хелпера
    public static void init(Context context) {
        if (manager == null) {
            appContext = context.getApplicationContext(); // на случай инициализации Base Context-ом
            manager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    /**
     * Создает и возвращает общий NotificationCompat.Builder
     */
    private static NotificationCompat.Builder getNotificationBuilder() {
        final NotificationCompat.Builder nb = new NotificationCompat.Builder(appContext)
                .setAutoCancel(true) // чтобы уведомление закрылось после тапа по нему
                .setOnlyAlertOnce(true) // уведомить однократно
                .setWhen(System.currentTimeMillis()) // время создания уведомления, будет отображено в стандартном уведомлении справа
                .setContentTitle("Фрінет особистий кабінет") //заголовок
                .setDefaults(Notification.DEFAULT_ALL); // alarm при выводе уведомления: звук, вибратор и диод-индикатор - по умолчанию
        return nb;
    }

    // удаляет все уведомления, созданные приложением
    public static void cancelAllNotifications() {
        manager.cancelAll();
    }

    /**
     * @param message             - текст уведомления
     * @param targetActivityClass - класс целевой активити
     * @param iconResId           - R.drawable необходимой иконки
     */
    public static void createNotification(int notAnoth, final String message, final Class targetActivityClass, final int iconResId) {

        // некоторые проверки на null не помешают, зачем нам NPE?
        if (targetActivityClass == null) {
            new Exception("createNotification() targetActivity is null!").printStackTrace();
        }
        if (manager == null) {
            new Exception("createNotification() NotificationUtils not initialized!").printStackTrace();
        }

        String notAnothMessage = "Не вистачає " + notAnoth + " грн на наступний місяць";
        final NotificationCompat.Builder nb = getNotificationBuilder() // получаем из хелпера generic Builder, и далее донастраиваем его
                .setContentText(notAnothMessage) // сообщение, которое будет отображаться в самом уведомлении
                .setTicker(notAnothMessage) //сообщение, которое будет показано в статус-баре при создании уведомления, ставлю тот же
                .setSmallIcon(iconResId != 0 ? iconResId : R.mipmap.ic_launcher);// иконка, если 0, то используется иконка самого аппа

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);
        stackBuilder.addParentStack(LoginActivity.class);
        Intent resultIntent = new Intent(appContext, LoginActivity.class);
        resultIntent.putExtra("notAnothMessage", message);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.setContentIntent(resultPendingIntent);

        final Notification notification = nb.build(); //генерируем уведомление, getNotification() - deprecated!
        manager.notify(NOTIFICATION_ID, notification); // "запускаем" уведомление

    }

    public static void createNotificationFirstMonth(int notAnoth, final Class targetActivityClass, final int iconResId) {

        // некоторые проверки на null не помешают, зачем нам NPE?
        if (targetActivityClass == null) {
            new Exception("createNotification() targetActivity is null!").printStackTrace();
        }
        if (manager == null) {
            new Exception("createNotification() NotificationUtils not initialized!").printStackTrace();
        }

        String notAnothMessage = "Не вистачило " + notAnoth + " грн на цей місяць";
        final NotificationCompat.Builder nb = getNotificationBuilder() // получаем из хелпера generic Builder, и далее донастраиваем его
                .setContentText(notAnothMessage) // сообщение, которое будет отображаться в самом уведомлении
                .setTicker(notAnothMessage) //сообщение, которое будет показано в статус-баре при создании уведомления, ставлю тот же
                .setSmallIcon(iconResId != 0 ? iconResId : R.mipmap.ic_launcher);// иконка, если 0, то используется иконка самого аппа

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);
        stackBuilder.addParentStack(LoginActivity.class);
        Intent resultIntent = new Intent(appContext, LoginActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.setContentIntent(resultPendingIntent);

        final Notification notification = nb.build(); //генерируем уведомление, getNotification() - deprecated!
        manager.notify(NOTIFICATION_ID, notification); // "запускаем" уведомление

    }
}