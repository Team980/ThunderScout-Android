package com.team980.thunderscout.util;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.team980.thunderscout.R;

@Deprecated
public class TSNotificationBuilder {

    private static TSNotificationBuilder ourInstance; //TODO the existence of this class is a memory leak

    private Context context;

    private NotificationCompat.Builder btTransferInProgress;
    private NotificationCompat.Builder btTransferError;
    private int lastUsedId = 1;

    private TSNotificationBuilder(Context context) { //TODO add click intents
        this.context = context;

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
