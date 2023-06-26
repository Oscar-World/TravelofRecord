package com.example.travelofrecord.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.travelofrecord.Activity.DirectMessage;
import com.example.travelofrecord.Activity.Home;
import com.example.travelofrecord.Activity.Loading;
import com.example.travelofrecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    String TAG = "파이어베이스 메세지 서비스";



    // 토큰을 서버로 전송
    // fcm 서버에 등록되었을 때 호출됨
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "token : " + token);
        Log.d(TAG, "FirebaseMessaging.getInstance().getToken().getResult() : " + FirebaseMessaging.getInstance().getToken().getResult());

        SharedPreferences sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String fcmToken = sharedPreferences.getString("fcmToken", "");

        if (fcmToken.equals("")) {
            Log.d(TAG, "fcmToken in Shared : 없음");

            editor.putString("fcmToken", token);
            editor.commit();

        } else {
            Log.d(TAG, "fcmToken in Shared : 있음");
        }



        // 토큰을 성공적으로 확인했을 때 호출됨
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {

                Log.d(TAG, "토큰 확인 성공 : " + token);

            }
        });

    }



    // 메세지 수신 후 처리
    // fcm 서버에서 메세지를 발송하면 호출됨
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived 호출됨()");

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API Level 26 이상은 NotificationChannel로 Builder를 생성해야 함.

            String CHANNEL_ID = "fcmChannel";
            String CHANNEL_NAME = "FCM 채널";
            int CHANNEL_IMPROTANCE = NotificationManager.IMPORTANCE_DEFAULT;

            if ( notificationManagerCompat.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPROTANCE);

                channel.enableLights(true);
                channel.enableVibration(true);


                notificationManagerCompat.createNotificationChannel(channel);
            }

            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);

        } else { // API Level 26 미만은 그냥 Builder를 생성.

            builder = new NotificationCompat.Builder(getApplicationContext());

        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        Log.d(TAG, "수신한 메시지 : " + title + " / " + body);

        Intent i = new Intent(getApplicationContext(), DirectMessage.class);
        i.putExtra("postNickname", title);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i,PendingIntent.FLAG_IMMUTABLE);

        builder.setSmallIcon(R.drawable.dm)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1,1000})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        Notification notification = builder.build();
        notificationManagerCompat.notify(1, notification);

    } // onMessageReceived()

}
