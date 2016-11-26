package com.team980.thunderscout.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.team980.thunderscout.R;
import com.team980.thunderscout.preferences.SettingsActivity;

public class TSNotificationBuilder {

    private static TSNotificationBuilder ourInstance; //TODO the existence of this class is a memory leak :(

    private Context context;

    private NotificationCompat.Builder btServerError; //server paused when Bluetooth is off or not working
    private NotificationCompat.Builder btServerRunning;

    private NotificationCompat.Builder btTransferInProgress;
    private NotificationCompat.Builder btTransferError;
    private int lastUsedId = 1;

    private TSNotificationBuilder(Context context) { //TODO add click intents
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
                .setGroup("BT_SERVER");

        btServerError = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_warning_white_24dp)
                .setContentTitle("Bluetooth server could not be started")
                .setContentText("Is the Bluetooth adapter enabled?")
                .setOngoing(true)
                .setColor(context.getResources().getColor(R.color.error))
                .setShowWhen(false)
                .setGroup("BT_SERVER");


        PendingIntent serverSettingsIntent = PendingIntent.getActivity(context, 1,
                new Intent(context, SettingsActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        btServerRunning.setContentIntent(serverSettingsIntent);
        btServerError.setContentIntent(serverSettingsIntent);

        NotificationCompat.Action openServerSetting = new NotificationCompat.Action(
                R.drawable.ic_settings_white_24dp,
                "MORE OPTIONS",
                serverSettingsIntent
        );

        btServerRunning.addAction(openServerSetting);
        btServerError.addAction(openServerSetting);

        btTransferInProgress = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp) //TODO find icon
                .setContentTitle("Transferring data to device")
                .setProgress(100, 0, true)
                .setOngoing(true)
                .setColor(context.getResources().getColor(R.color.accent)) //TODO use nonyellow accent?
                .setGroup("BT_TRANSFER_ONGOING");

        btTransferError = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp) //TODO find icon
                .setContentTitle("Data transfer failed")
                .setContentText("Failed to receive data from device")
                .setColor(context.getResources().getColor(R.color.error))
                .setGroup("BT_TRANSFER_ERROR");
    }

    public static TSNotificationBuilder getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TSNotificationBuilder(context);
        }

        return ourInstance;
    }

    public Notification buildBtServerRunning() {
        return btServerRunning.build();
    }

    public Notification buildBtServerError() {
        return btServerError.build();
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
