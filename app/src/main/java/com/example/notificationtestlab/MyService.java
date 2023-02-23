package com.example.notificationtestlab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Created by okwon on 2023/02/23.
 * Description :
 */
public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;

    static NotificationCompat.Builder builder;
    int importance = NotificationManager.IMPORTANCE_HIGH;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //서비스 시작할 때
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("NotificationTest", "onStartCommand ");
        System.out.println("켜짐");
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스 종료될 때
    public void onDestroy() {
        System.out.println("꺼짐");
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Log.d("NotificationTest", "handleMessage ");
            try {
                Thread.sleep(5000);
//                createActionNotification();
                startForeground();
//                createActionNotificationCallType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //토스트 띄우기
            Toast.makeText(MyService.this, "서비스 시작", Toast.LENGTH_LONG).show();
        }
    }

    private void startForeground() {
        Intent intent = new Intent(MyService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.nature_img);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(getApplicationContext(), MainActivity.CHANNEL_ID)
                    .setOngoing(true)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setContentTitle("Content Title")
                    .setContentText("ContestText 입니다")
                    .setDeleteIntent(pendingIntent)
                    .setTimeoutAfter(60 * 1000)
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.nature_img)
                    .setLargeIcon(largeIcon)
//                    .setFullScreenIntent(requestPermissionPendingIntent, true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notification.flags = Notification.FLAG_INSISTENT;
            startForeground(1, notification);
        }
    }

    private void createActionNotification() {

        NotificationManagerCompat mNotificationManagerCompat = NotificationManagerCompat.from(MyService.this);
        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();


        Intent intent = new Intent(getApplicationContext(), Receiver.class);
        intent.setAction("ACTION_CANCEL");

        //passing notificationId to receiver class through intent
        intent.putExtra("id", 101);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        //action fire on click of notification Dismiss action button.
        NotificationCompat.Action actionDismiss =
                new NotificationCompat.Action.Builder(0,
                        "Dismiss", pendingIntent)
                        .build();

        //action fire on click of notification Delete action button.
        NotificationCompat.Action actionDelete =
                new NotificationCompat.Action.Builder(0,
                        "Delete", pendingIntent)
                        .build();


        Notification notification = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("title test zzz")
                .setContentText(" context test zfdkjfkljf3klfj")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is an example of BigTextStyle notification with action."))
                //Add actions Dismiss and Delete to this notification.
                .addAction(actionDismiss)
                .addAction(actionDelete)
                .build();

        mNotificationManagerCompat.notify(101, notification);
    }

    private void createActionNotificationCallType() {

        NotificationManagerCompat mNotificationManagerCompat = NotificationManagerCompat.from(MyService.this);
        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();


        Intent intent = new Intent(getApplicationContext(), Receiver.class);
        intent.setAction("ACTION_CANCEL");

        //passing notificationId to receiver class through intent
        intent.putExtra("id", 101);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent requestPermissionPendingIntent;
        Intent requestPermissionIntent = new Intent(getApplicationContext(), MainActivity.class)
                .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        requestPermissionIntent.putExtra("type", "request_voice_call_permissions");
        Intent timeoutIntent = new Intent(getApplicationContext(), Receiver.class);
        timeoutIntent.setAction("TIMEOUT_VOICE_CALL_ACTION");

        if (true) {
            requestPermissionIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        } else {
            requestPermissionIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPermissionPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, requestPermissionIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            requestPermissionPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, requestPermissionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        //action fire on click of notification Dismiss action button.
        NotificationCompat.Action actionDismiss =
                new NotificationCompat.Action.Builder(0,
                        "Dismiss", pendingIntent)
                        .build();

        //action fire on click of notification Delete action button.
        NotificationCompat.Action actionDelete =
                new NotificationCompat.Action.Builder(0,
                        "Delete", pendingIntent)
                        .build();

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.nature_img);
        Notification notification = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setContentTitle("Content Title")
//                .setContentIntent(pendingIntent)
                .setContentText("ContestText 입니다")
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setDeleteIntent(pendingIntent)
                .setFullScreenIntent(requestPermissionPendingIntent, true)
                .setTimeoutAfter(60 * 1000)
//                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setLargeIcon(largeIcon)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is an example of BigTextStyle notification with action."))
                //Add actions Dismiss and Delete to this notification.
                .addAction(actionDismiss)
                .addAction(actionDelete)
                .build();
        notification.flags = Notification.FLAG_INSISTENT;

        mNotificationManagerCompat.notify(101, notification);
    }

}