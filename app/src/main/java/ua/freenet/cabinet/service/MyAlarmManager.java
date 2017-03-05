package ua.freenet.cabinet.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;
import static ua.freenet.cabinet.utils.Utilits.log;

public class MyAlarmManager {

    void setUpAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AutoRun.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, getTimer(), pendingIntent);
    }

    private long getTimer(){
        long random = (long) (Math.random() * 10 * 60 * 60 * 1000);
        int seconds = (int) random / 1000;
        int sumMinutes = seconds / 60;
        int hours = sumMinutes / 60;
        int minutes;
        if (hours == 0) {
            minutes = sumMinutes;
        } else {
            minutes = sumMinutes % (hours * 60);
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + random);
        SimpleDateFormat format1 = new SimpleDateFormat("Следующая проверка в HH:mm:ss (через "
                + hours + " часов, " + minutes + " минут " + seconds%60 + " секунд)");
        log(format1.format(calendar.getTime()));

        return System.currentTimeMillis() + random;
    }
}
