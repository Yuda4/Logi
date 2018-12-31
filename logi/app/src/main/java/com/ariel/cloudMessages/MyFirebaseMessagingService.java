package com.ariel.cloudMessages;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ariel.logi.logi.CustomerActivity;
import com.ariel.logi.logi.MainActivity;
import com.ariel.logi.logi.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int BROADCAST_NOTIFICATION_ID = 1;

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() >0){

            try{
                Map<String, String> data = remoteMessage.getData();
                Map<String, String> notification = remoteMessage.getData();
                showNotification(data, notification);

            }catch (NullPointerException e){
                Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage() );
            }
        }
    }

    private void showNotification(Map<String,String> data, Map<String,String> notification){
        try{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    getString(R.string.default_notification_channel_id));
            builder.setSmallIcon(R.drawable.ic_clear_all_black_24dp)
                    .setContentTitle(notification.get("title"))
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_logo))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setColor(getColor(R.color.colorPrimaryDark))
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.get("text")))
                    .setOnlyAlertOnce(true);

            Intent pendingIntent = new Intent(this, MainActivity.class);
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(pendingIntent);

            PendingIntent notifyPendingIntent =
                    taskStackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                );

            builder.setContentIntent(notifyPendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());


        }catch (NullPointerException e){
            Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage() );
        }
            Log.d(TAG, "onMessageReceived: data: " + notification.get("title"));
            Log.d(TAG, "onMessageReceived: notification text: " + notification.get("text"));
    }

    /**
     * Build a push notification for a chat message
     * @param title
     * @param message
     */
//    private void sendMessageNotification(String title, String message, String messageId){
//        Log.d(TAG, "sendChatmessageNotification: building a chatmessage notification");
//
//        //get the notification id
//        int notificationId = buildNotificationId(messageId);
//
//        // Instantiate a Builder object.
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
//                getString(R.string.default_notification_channel_id));
//        // Creates an Intent for the Activity
//        Intent pendingIntent = new Intent(this, MainActivity.class);
//        // Sets the Activity to start in a new, empty task
//        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        // Creates the PendingIntent
//        PendingIntent notifyPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        pendingIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//        //add properties to the builder
//        builder.setSmallIcon(R.drawable.ic_clear_all_black_24dp)
//                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                        R.drawable.ic_logo))
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContentTitle(title)
//                .setColor(getColor(R.color.colorPrimaryDark))
//                .setAutoCancel(true)
//                //.setSubText(message)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setOnlyAlertOnce(true);
//
//        builder.setContentIntent(notifyPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        mNotificationManager.notify(notificationId, builder.build());
//
//    }


//    private int buildNotificationId(String id){
//        Log.d(TAG, "buildNotificationId: building a notification id.");
//
//        int notificationId = 0;
//        for(int i = 0; i < 9; i++){
//            notificationId = notificationId + id.charAt(0);
//        }
//        Log.d(TAG, "buildNotificationId: id: " + id);
//        Log.d(TAG, "buildNotificationId: notification id:" + notificationId);
//        return notificationId;
//    }

}
