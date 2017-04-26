package ua.freenet.cabinet.service;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "-------------------";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("СРАБОТАЛ onMessageReceived");
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: ");
            Map<String, String> map = remoteMessage.getData();
//            sendNotification(encode(remoteMessage.getData().get("message")));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + encode(entry.getValue()));
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private String encode(String s){
//        return URLDecoder.decode(s);
        return s;
    }



}