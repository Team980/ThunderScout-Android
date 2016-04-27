package com.team980.thunderscout.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.team980.thunderscout.R;
import com.team980.thunderscout.thread.ServerListenerThread;

public class BluetoothServerService extends Service {

    private NotificationCompat.Builder mBuilder;

    private ServerListenerThread acceptThread;

    public void onCreate() {

        //init notification
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_bluetooth_connected_white_24dp)
                .setContentTitle("ThunderScout Server is running")
                .setContentText("Waiting for scout data to be sent") //TODO make the notification more useful; new icon
                .setOngoing(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, mBuilder.build());

        acceptThread = new ServerListenerThread(getApplicationContext());
        acceptThread.start();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);

        acceptThread.cancel();
    }
}