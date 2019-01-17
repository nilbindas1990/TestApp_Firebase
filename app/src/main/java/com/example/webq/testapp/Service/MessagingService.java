
//http://www.androiddeft.com/2017/11/18/push-notification-android-firebase-php/
//https://github.com/firebase/quickstart-android/issues/88
//https://medium.com/@cdmunoz/working-easily-with-fcm-push-notifications-in-android-e1804c80f74
package com.example.webq.testapp.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.webq.testapp.BlankActivity;
import com.example.webq.testapp.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class MessagingService extends FirebaseMessagingService {
    String TAG = "Firebasemsg";
    Bitmap bitmap;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Hello","hello");
        String imageUri = "https://webqueuesolution.com/samples/projects/sandip/android_development/uploads/push-notification/1547467726Chrysanthemum.jpg";

        if (remoteMessage.getData().size() > 0){
            //Map<String,String> data = remoteMessage.getData();
            Log.d("Hello", String.valueOf(remoteMessage.getData()));

            bitmap = getBitmapfromUrl(imageUri);
            showNotification(remoteMessage,bitmap);
        }


        //Log.d("Firebase_img",imageUri);

    }

    private void showNotification(RemoteMessage remoteMessage, Bitmap bitmap) {
        Log.d("Hello", "on showNotification");
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Nilabhra";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("Nialbhra");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(android.support.v4.R.color.notification_icon_bg_color);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(getApplicationContext(), BlankActivity.class);
        intent.putExtra("testkey",title);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.support.loader.R.drawable.notification_bg))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setContentText(body)
                .setContentInfo("Info")
                .setContentIntent(pendingIntent);
        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());
    }
    //Usable when Image is needed.

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //sendTokenToServer(FirebaseInstanceId.getInstance().getInstanceId());
    }

    private void sendTokenToServer(Task<InstanceIdResult> token) {
        Log.d("Token",String.valueOf(token));
    }
}
