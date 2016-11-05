package com.team980.thunderscout.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;

public class TSNotificationManager {

    private static TSNotificationManager ourInstance; //TODO the existence of this class is a memory leak

    private Context context;

    private NotificationCompat.Builder btServerRunning;

    private NotificationCompat.Builder btTransferInProgress;
    private NotificationCompat.Builder btTransferError;
    private int lastUsedId = 1;

    private TSNotificationManager(Context context) { //TODO add click intents
        this.context = context;

        //init notifications
        btServerRunning = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp)
                .setContentTitle("Bluetooth server is running")
                .setContentText("Open for connections via Bluetooth") //todo new icon
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setColor(context.getResources().getColor(R.color.primary))
                .setShowWhen(false)
                .setGroup("TS_SERVER_RUNNING");


        PendingIntent serverSettingsIntent = PendingIntent.getActivity(context, 1,
                new Intent(context, MainActivity.class)
                        .putExtra(MainActivity.INTENT_FLAG_SHOWN_FRAGMENT, MainActivity.INTENT_FLAGS_HOME),
                PendingIntent.FLAG_UPDATE_CURRENT);

        btServerRunning.setContentIntent(serverSettingsIntent);

        NotificationCompat.Action openServerSetting = new NotificationCompat.Action(
                R.drawable.ic_settings_white_24dp,
                "MORE OPTIONS",
                serverSettingsIntent
        );

        btServerRunning.addAction(openServerSetting);

        btTransferInProgress = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp) //TODO find icon
                .setContentTitle("Transferring data to device")
                .setProgress(100, 0, true)
                .setOngoing(true)
                .setColor(context.getResources().getColor(R.color.accent)) //TODO use nonyellow accent?
                .setGroup("TS_TRANSFER_ONGOING");

        btTransferError = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp) //TODO find icon
                .setContentTitle("Data transfer failed")
                .setContentText("Failed to receive data from device")
                .setColor(context.getResources().getColor(R.color.error))
                .setGroup("TS_TRANSFER_ERROR");
    }

    public static TSNotificationManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TSNotificationManager(context);
        }

        return ourInstance;
    }

    public Notification buildBtServerRunning() {
        return btServerRunning.build();
    }

    public Notification buildBtServerRunning(String[] data) { //TODO add data
        return btServerRunning.build();
    }

    @Deprecated
    public int showBtTransferInProgress(String deviceName, boolean isServer) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        lastUsedId++;

        String message;
        if (isServer) {
            message = "Receiving data from ";
        } else {
            message = "Sending data to ";
        }

        btTransferInProgress.setContentTitle(message + deviceName);
        btTransferInProgress.setWhen(System.currentTimeMillis());

        mNotifyMgr.notify(lastUsedId, btTransferInProgress.build());
        return lastUsedId;
    }

    @Deprecated
    public void showBtTransferError(String deviceName, int id) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        btTransferError.setContentText("Failed to receive data from " + deviceName);
        btTransferError.setWhen(System.currentTimeMillis());

        mNotifyMgr.notify(id, btTransferError.build());
    }

    public void showBtTransferFinished(int id) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyMgr.cancel(id);
    }
}
