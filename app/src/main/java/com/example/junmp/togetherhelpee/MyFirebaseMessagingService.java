package com.example.junmp.togetherhelpee;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.junmp.togetherhelpee.MatchActivity;
import com.example.junmp.togetherhelpee.R;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData().get("message"));
        }
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());

        }
    }

    private void sendNotification(String message1) {
        JSONObject object = null;
        try {
            object = new JSONObject(message1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JSON OBJECT", object.toString());
        String message = "";
        String id="";
        try {
            message = object.getString("message");
            id = object.getString("volunteerId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("received message : " + message);
        Intent intent = new Intent(this, MatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher) )
                .setContentTitle("Together")
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000})
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);

        Intent popupIntent = new Intent(getApplicationContext(), fcm_popup.class);
        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        popupIntent.putExtra("msg", message);
        popupIntent.putExtra("id", id);
        startActivity(popupIntent); // 메시지 팝업창을 바로 띄운다.

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
