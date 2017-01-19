package ua.adeptius.freenet.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ua.adeptius.freenet.utils.Utilits;

public class AutoRun extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Utilits.log("onReceive " + intent.getAction());
        context.startService(new Intent(context, BackgroundService.class));
    }
}


