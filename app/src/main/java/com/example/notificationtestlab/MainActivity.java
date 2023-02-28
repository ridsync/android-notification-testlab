package com.example.notificationtestlab;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CHANNEL_ID = "Important_mail_channel";
    Button mBtnForegroundService, mBtnForegroundServiceStop, mBtnSimpleNotification, mBtnBigTextNotification, mBtnBigPictureNotification, mBtnInboxNotification, mBtnActionNotification;
    NotificationManagerCompat mNotificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        mBtnForegroundService = findViewById(R.id.btn_foreground_service);
        mBtnForegroundServiceStop = findViewById(R.id.btn_foreground_service_stop);
        mBtnSimpleNotification = findViewById(R.id.btn_simple_notification);
        mBtnBigTextNotification = findViewById(R.id.btn_bigtextstyle_notification);
        mBtnBigPictureNotification = findViewById(R.id.btn_bigpicturestyle_notification);
        mBtnInboxNotification = findViewById(R.id.btn_inboxstyle_notification);
        mBtnActionNotification = findViewById(R.id.btn_action_notification);
        mBtnForegroundService.setOnClickListener(this);
        mBtnForegroundServiceStop.setOnClickListener(this);
        mBtnSimpleNotification.setOnClickListener(this);
        mBtnBigTextNotification.setOnClickListener(this);
        mBtnBigPictureNotification.setOnClickListener(this);
        mBtnInboxNotification.setOnClickListener(this);
        mBtnActionNotification.setOnClickListener(this);

        mNotificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case (R.id.btn_foreground_service):
                createForegroundServiceNotification(getString(R.string.simple_foreground_title), getString(R.string.simple_foreground_title), 1);
                break;
            case (R.id.btn_foreground_service_stop):
                stopService();
                break;
            case (R.id.btn_simple_notification):
                createSimpleNotification(getString(R.string.simple_notification_title), getString(R.string.simple_notification_text), 1);
                break;

            case (R.id.btn_bigtextstyle_notification):
                createBigTextNotification(getString(R.string.bigtext_notification_title), getString(R.string.bigtext_notification_text), 2);
                break;

            case (R.id.btn_bigpicturestyle_notification):
                createBigPictureNotification(getString(R.string.bigPicture_notification_title), getString(R.string.bigPicture_notification_text), 3);
                break;

            case (R.id.btn_inboxstyle_notification):
                createInboxNotification(getString(R.string.inbox_notification_title), getString(R.string.inbox_notification_text), 4);
                break;

            case (R.id.btn_action_notification):
                createActionNotification(getString(R.string.action_notification_title), getString(R.string.action_notification_text), 5);
                break;

        }
    }

    private void stopService() {
        stopService(new Intent(this, MyService.class));
    }

    private void createForegroundServiceNotification(String title, String text, int notificationId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, MyService.class));
        } else {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        }

        Log.d("NotificationTest","createForegroundServiceNotification ");
    }

        private void createSimpleNotification(String title, String text, int notificationId) {

        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.nature_img);

        //open the url when user taps the notification
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.c1ctech.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // notificationId is a unique int for each notification that you must define
        mNotificationManagerCompat.notify(notificationId, notification);

    }


    private void createBigTextNotification(String title, String text, int notificationId) {

        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.nature_img);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle().bigText(text + " used for generating large-format notifications that include a lot of text.")
                //set different title in expanded mode.
                .setBigContentTitle(null)
                //needed if an app sends notification from multiple sources(accounts).
                .setSummaryText("BigTextStyle");


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(text + " used for generating large-format notifications that include a lot of text.")
                //set Big text template
                .setStyle(style)
                //Set the large icon in the notification.
                .setLargeIcon(bitmap)
                .build();


        mNotificationManagerCompat.notify(notificationId, notification);

    }


    private void createBigPictureNotification(String title, String text, int notificationId) {

        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.nature_img);


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                //set big picture
                .bigPicture(bitmap)
                //set the content text in expanded form.
                .setSummaryText("BigPicture style is used to show large image.")
                //set different title in expanded mode.
                .setBigContentTitle(null)
                //set different large icon in expanded mode.
                .bigLargeIcon(null);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(text)
                //Set the large icon in the notification.
                .setLargeIcon(bitmap)
                //set Big picture template
                .setStyle(style)
                .build();

        mNotificationManagerCompat.notify(notificationId, notification);

    }

    private void createInboxNotification(String title, String text, int notificationId) {

        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();

        String line1 = "This is line1 ";
        String line2 = "This is line2 ";
        String line3 = "This is line3 ";
        String line4 = "This is line4 ";
        String line5 = "This is line5 ";
        String line6 = "This is line6 ";
        String line7 = "This is line7 ";

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle()
                //To add n lines, call it n times
                .addLine(line1)
                .addLine(line2)
                .addLine(line3)
                .addLine(line4)
                .addLine(line5)
                .addLine(line6)
                .addLine(line7)
                //needed if an app sends notification from multiple sources(accounts).
                .setSummaryText("InboxStyle")
                //set different title in expanded mode.
                .setBigContentTitle(null);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText("It is used for notifications includes a list of (up to 5) strings.")
                //set inbox style in notification
                .setStyle(style)
                .build();

        mNotificationManagerCompat.notify(notificationId, notification);


    }

    private void createActionNotification(String title, String text, int notificationId) {

        //removes all previously shown notifications.
        mNotificationManagerCompat.cancelAll();


        Intent intent = new Intent(getApplicationContext(), Receiver.class);
        intent.setAction("ACTION_CANCEL");

        //passing notificationId to receiver class through intent
        intent.putExtra("id", notificationId);

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


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is an example of BigTextStyle notification with action."))
                //Add actions Dismiss and Delete to this notification.
                .addAction(actionDismiss)
                .addAction(actionDelete)
                .build();

        mNotificationManagerCompat.notify(notificationId, notification);
    }


    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Channel name
            CharSequence name = "Important_mail_channel";

            //Channel description
            String description = "This channel will show notification only to important people";

            //The importance level you assign to a channel applies to all notifications that you post to it.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            //Create the NotificationChannel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            //Set channel description
            channel.setDescription(description);
            // set ringtone
            Uri ringtoneUri = Uri.parse("android.resource://" + getPackageName() +
                    "/" + R.raw.incallmanager_ringtone);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(ringtoneUri, audioAttributes);
            long[] pattern = {1000,800};
            channel.setVibrationPattern(pattern);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}