/*
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Luke Myers (FRC Team 980 ThunderBots)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.team980.thunderscout.bluetooth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.team980.thunderscout.R;
import com.team980.thunderscout.bluetooth.util.BluetoothServerToggleActivity;
import com.team980.thunderscout.preferences.SettingsActivity;
import com.team980.thunderscout.util.NotificationIdFactory;

public class BluetoothServerService extends Service {

    private static int SERVER_NOTIFICATION_ID = NotificationIdFactory.getNewNotificationId();
    private ServerListenerThread acceptThread;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder running;
    private NotificationCompat.Builder adapterDisabled;
    private NotificationCompat.Builder adapterMissing;
    private BroadcastReceiver receiver;

    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initNotifications();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                Crashlytics.log(Log.INFO, this.getClass().getName(), "Change detected in Bluetooth adapter");

                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);

                    Crashlytics.log(Log.INFO, this.getClass().getName(), "Bluetooth adapter state: " + state);
                    switch (state) {
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            NotificationManagerCompat.from(context).notify(SERVER_NOTIFICATION_ID, adapterDisabled.build());

                            if (acceptThread != null) {
                                acceptThread.cancel();
                            }
                            break;
                        case BluetoothAdapter.STATE_ON:
                            NotificationManagerCompat.from(context).notify(SERVER_NOTIFICATION_ID, running.build());

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
        Crashlytics.log(Log.INFO, this.getClass().getName(), "Starting Bluetooth service");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            NotificationManagerCompat.from(this).notify(SERVER_NOTIFICATION_ID, adapterMissing.build());
            Crashlytics.log(Log.ERROR, this.getClass().getName(), "NULL Bluetooth adapter");
        } else if (!mBluetoothAdapter.isEnabled()) {

            //mBluetoothAdapter.enable(); //Applications should NEVER call this directly
            NotificationManagerCompat.from(this).notify(SERVER_NOTIFICATION_ID, adapterDisabled.build());
            Crashlytics.log(Log.INFO, this.getClass().getName(), "Requesting Bluetooth to be enabled");
        } else {

            acceptThread = new ServerListenerThread(getApplicationContext());
            acceptThread.start();
            Crashlytics.log(Log.INFO, this.getClass().getName(), "Bluetooth on, starting server accept thread");
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

        Crashlytics.log(Log.INFO, this.getClass().getName(), "Stopping Bluetooth service");

        unregisterReceiver(receiver);

        if (acceptThread != null) {
            acceptThread.cancel();
        }
    }

    public void initNotifications() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel serverChannel = new NotificationChannel("bluetooth_server", "Bluetooth server", NotificationManager.IMPORTANCE_LOW);
            serverChannel.setDescription("Persistent notification for the Bluetooth server");
            notificationManager.createNotificationChannel(serverChannel);
        }

        running = new NotificationCompat.Builder(this, "bluetooth_server")
                .setSmallIcon(R.drawable.ic_bluetooth_searching_white_24dp)
                .setContentTitle("Bluetooth server")
                .setContentText("Click to open settings") //todo new icon?
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.primary))
                .setShowWhen(false)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setGroup("BT_SERVER");

        adapterDisabled = new NotificationCompat.Builder(this, "bluetooth_server")
                .setSmallIcon(R.drawable.ic_warning_white_24dp)
                .setContentTitle("Bluetooth is disabled")
                .setContentText("Please enable Bluetooth before using the Bluetooth Server")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.error))
                .setShowWhen(false)
                .setCategory(NotificationCompat.CATEGORY_ERROR)
                .setGroup("BT_SERVER");

        adapterMissing = new NotificationCompat.Builder(this, "bluetooth_server")
                .setSmallIcon(R.drawable.ic_warning_white_24dp)
                .setContentTitle("This device doesn't support Bluetooth")
                .setContentText("Please disable the Bluetooth Server")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.error))
                .setShowWhen(false)
                .setCategory(NotificationCompat.CATEGORY_ERROR)
                .setGroup("BT_SERVER");

        PendingIntent serverSettingsIntent = PendingIntent.getActivity(this, 1,
                new Intent(this, SettingsActivity.class).putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT,
                        SettingsActivity.BluetoothServerPreferenceFragment.class.getName()),
                PendingIntent.FLAG_UPDATE_CURRENT);

        running.setContentIntent(serverSettingsIntent);

        PendingIntent enableBluetoothIntent = PendingIntent.getActivity(this, 1,
                new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                PendingIntent.FLAG_UPDATE_CURRENT);

        adapterDisabled.setContentIntent(enableBluetoothIntent);

        NotificationCompat.Action enableBluetoothSetting = new NotificationCompat.Action(
                R.drawable.ic_bluetooth_searching_white_24dp,
                "Enable Bluetooth",
                enableBluetoothIntent
        );

        PendingIntent disableServerIntent = PendingIntent.getActivity(this, 1,
                new Intent(this, BluetoothServerToggleActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        adapterMissing.setContentIntent(disableServerIntent);

        NotificationCompat.Action disableServerSetting = new NotificationCompat.Action(
                R.drawable.ic_clear_white_24dp,
                "Disable Server",
                disableServerIntent
        );

        running.addAction(disableServerSetting);

        adapterDisabled.addAction(enableBluetoothSetting);
        adapterDisabled.addAction(disableServerSetting);
    }
}