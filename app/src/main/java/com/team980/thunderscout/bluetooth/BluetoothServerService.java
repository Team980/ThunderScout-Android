package com.team980.thunderscout.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;

import com.team980.thunderscout.util.TSNotificationManager;

public class BluetoothServerService extends Service {

    private ServerListenerThread acceptThread;

    private TSNotificationManager notificationManager;

    public void onCreate() {
        notificationManager = TSNotificationManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, notificationManager.buildBtServerRunning());

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable(); //TODO prompt user
        }

        acceptThread = new ServerListenerThread(getApplicationContext(), this);
        acceptThread.start();

        // If we get killed, after returning from here, restart - TODO is this why it runs twice?
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