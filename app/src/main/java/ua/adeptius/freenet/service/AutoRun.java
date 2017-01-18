package ua.adeptius.freenet.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoRun extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        Log.d("afsadf","onReceive " + intent.getAction());
        context.startService(new Intent(context, BackgroundService.class));
    }
}


