
//http://www.androiddeft.com/2017/11/18/push-notification-android-firebase-php/
package com.example.webq.testapp;

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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MessagingService extends FirebaseMessagingService {
    Bitmap bitmap;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Log.d("From", remoteMessage.getFrom());
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        //String imageUri = "https://webqueuesolution.com/samples/projects/sandip/android_development/uploads/push-notification/1547467726Chrysanthemum.jpg";


        bitmap = getBitmapfromUrl(body);

            showNotification(title,bitmap);



        //Log.d("Firebase_img",imageUri);

    }

    private void showNotification(String title, Bitmap imageUri) {

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHENNEL_ID = "com.example.webq.firebaseexample";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =new NotificationChannel(NOTIFICATION_CHENNEL_ID,"MyNotification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("EDMT Chennel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHENNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_msg))
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(imageUri))
                //.setContentText(body)
                .setContentInfo("Info");
        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());
    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("token",s);
    }

}
