package com.team980.thunderscout.bluetooth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.team980.thunderscout.R;
import com.team980.thunderscout.preferences.SettingsActivity;

public class BluetoothServerService extends Service {

    private ServerListenerThread acceptThread;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder running;
    private NotificationCompat.Builder adapterDisabled;
    private NotificationCompat.Builder adapterMissing;
    private static int SERVER_NOTIFICATION_ID = 1;

    private BroadcastReceiver receiver;

    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initNotifications();

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
                            notificationManager.notify(SERVER_NOTIFICATION_ID, adapterDisabled.build());

                            if (acceptThread != null) {
                                acceptThread.cancel();
                            }
                            break;
                        case BluetoothAdapter.STATE_ON:
                            notificationManager.notify(SERVER_NOTIFICATION_ID, running.build());

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
        startForeground(SERVER_NOTIFICATION_ID, running.build());
        //TODO manage notifications in here; more diverse and explanatory descriptions and states

        registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        Log.d("BT", "service starting");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            notificationManager.notify(SERVER_NOTIFICATION_ID, adapterMissing.build());
            Log.d("BT", "bluetooth dead");
        } else if (!mBluetoothAdapter.isEnabled()) {

            //mBluetoothAdapter.enable(); //Applications should NEVER call this directly
            notificationManager.notify(SERVER_NOTIFICATION_ID, adapterDisabled.build());
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

    public void initNotifications() {
        running = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp)
                .setContentTitle("Bluetooth server is running")
                .setContentText("Open for scout data to be sent") //todo new icon
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setColor(getResources().getColor(R.color.primary))
                .setShowWhen(false)
                .setGroup("BT_SERVER");

        adapterDisabled = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_warning_white_24dp)
                .setContentTitle("Bluetooth is disabled")
                .setContentText("Please enable Bluetooth before using the Bluetooth server")
                .setOngoing(true)
                .setColor(getResources().getColor(R.color.error))
                .setShowWhen(false)
                .setGroup("BT_SERVER");

        adapterMissing = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_warning_white_24dp)
                .setContentTitle("This device doesn't support Bluetooth")
                .setContentText("Please disable the Bluetooth server")
                .setOngoing(true)
                .setColor(getResources().getColor(R.color.error))
                .setShowWhen(false)
                .setGroup("BT_SERVER");

        PendingIntent serverSettingsIntent = PendingIntent.getActivity(this, 1,
                new Intent(this, SettingsActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        running.setContentIntent(serverSettingsIntent);
        adapterMissing.setContentIntent(serverSettingsIntent);

        NotificationCompat.Action openServerSetting = new NotificationCompat.Action(
                R.drawable.ic_settings_white_24dp,
                "MORE OPTIONS",
                serverSettingsIntent
        );

        running.addAction(openServerSetting);
        adapterMissing.addAction(openServerSetting);

        PendingIntent enableBluetoothIntent = PendingIntent.getActivity(this, 1,
                new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                PendingIntent.FLAG_UPDATE_CURRENT);

        adapterDisabled.setContentIntent(enableBluetoothIntent);

        NotificationCompat.Action enableBluetoothSetting = new NotificationCompat.Action(
                R.drawable.ic_settings_bluetooth_white_24dp,
                "ENABLE BLUETOOTH",
                enableBluetoothIntent
        );

        adapterDisabled.addAction(enableBluetoothSetting);
        adapterDisabled.addAction(openServerSetting);
    }
}