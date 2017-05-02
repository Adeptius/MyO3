package ua.freenet.cabinet.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import ua.freenet.cabinet.dao.Web;
import ua.freenet.cabinet.utils.Settings;

import static android.content.Context.MODE_PRIVATE;
import static ua.freenet.cabinet.utils.Utilits.log;

public class AutoRun extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.toString().contains("BOOT_COMPLETED")){
            log("Девайс загрузился. Запуск сервиса");
            context.startService(new Intent(context, BackgroundService.class));

        }else if (intent.toString().contains("AutoRun")){
            log("Сработал таймер. Вызываю проверку и задаю новый");
            new Checker(context);
            new MyAlarmManager().setUpAlarm(context);
        }
    }
}


