package com.team980.thunderscout.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.team980.thunderscout.R;

public class TSNotificationManager {

    private static TSNotificationManager ourInstance;

    private Context context;

    private NotificationCompat.Builder btServerRunning;

    private NotificationCompat.Builder btTransferInProgress;
    private NotificationCompat.Builder btTransferSuccessful;
    private NotificationCompat.Builder btTransferError;
    private int lastUsedId = 1;

    private TSNotificationManager(Context context) { //TODO add click intents
        this.context = context;

        //init notifications
        btServerRunning = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp)
                .setContentTitle("Bluetooth server is running")
                .setContentText("Open for connections via Bluetooth") //todo new icon
                .setOngoing(true) //TODO remove ugly time
                .setColor(context.getResources().getColor(R.color.primary))
                .setGroup("TS_SERVER_RUNNING");

        btTransferInProgress = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp) //TODO find icon
                .setContentTitle("Transferring data to device")
                .setProgress(100, 0, true)
                .setOngoing(true)
                .setColor(context.getResources().getColor(R.color.primary)) //TODO use nonyellow accent?
                .setGroup("TS_TRANSFER_ONGOING");

        btTransferSuccessful = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp) //TODO find icon
                .setContentTitle("Data transfer successful")
                .setContentText("Data received from device")
                .setColor(context.getResources().getColor(R.color.primary)) //TODO use nonyellow accent?
                .setGroup("TS_TRANSFER_SUCCESS");

        btTransferError = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp) //TODO find icon
                .setContentTitle("Data transfer failed")
                .setContentText("Failed to receive data from device")
                .setColor(context.getResources().getColor(R.color.primary)) //TODO use nonyellow accent?
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

    public int showBtTransferInProgress(String deviceName) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        lastUsedId++;

        btTransferInProgress.setContentTitle("Transferring data [to|from] " + deviceName);

        mNotifyMgr.notify(lastUsedId, btTransferInProgress.build());
        return lastUsedId;
    }

    public void showBtTransferSuccessful(String deviceName, int id) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        btTransferSuccessful.setContentText("Data [received from|sent to] " + deviceName);

        mNotifyMgr.notify(id, btTransferSuccessful.build());
    }

    public void showBtTransferError(String deviceName, int id) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        btTransferError.setContentText("Failed to [receive|send] data [from|to] " + deviceName);

        mNotifyMgr.notify(id, btTransferError.build());
    }
}
