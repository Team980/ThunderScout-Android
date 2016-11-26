package com.team980.thunderscout.bluetooth;

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.team980.thunderscout.util.TSNotificationBuilder;

public class BluetoothServerService extends Service {

    private ServerListenerThread acceptThread;

    private NotificationManager notificationManager;
    private TSNotificationBuilder notificationBuilder;
    private static int SERVER_NOTIFICATION_ID = 1;

    private BroadcastReceiver receiver;

    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = TSNotificationBuilder.getInstance(this);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                Log.d("BT", "recieved something");

                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);

                    Log.d("BT", state + "");
                    switch (state) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            notificationManager.notify(SERVER_NOTIFICATION_ID, notificationBuilder.buildBtServerError());

                            if (acceptThread != null) {
                                acceptThread.cancel();
                            }
                            break;
                        case BluetoothAdapter.STATE_ON:
                            notificationManager.notify(SERVER_NOTIFICATION_ID, notificationBuilder.buildBtServerRunning());

                            acceptThread = new ServerListenerThread(getApplicationContext());
                            acceptThread.start();
                            break;
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(SERVER_NOTIFICATION_ID, notificationBuilder.buildBtServerRunning());
        //TODO manage notifications in here; more diverse and explanatory descriptions and states

        registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        Log.d("BT", "service starting");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            notificationManager.notify(SERVER_NOTIFICATION_ID, notificationBuilder.buildBtServerError());
            Log.d("BT", "bluetooth dead");
        } else if (!mBluetoothAdapter.isEnabled()) {

            mBluetoothAdapter.enable(); //TODO prompt user as Google says not doing so is evil
            notificationManager.notify(SERVER_NOTIFICATION_ID, notificationBuilder.buildBtServerError());
            Log.d("BT", "bluetooth off, turning on");
        } else {

            acceptThread = new ServerListenerThread(this);
            acceptThread.start();
            Log.d("BT", "bluetooth on, starting server");
        }

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

        Log.d("BT", "goodbye");

        unregisterReceiver(receiver);

        if (acceptThread != null) {
            acceptThread.cancel();
        }
    }
}