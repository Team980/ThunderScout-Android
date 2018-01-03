/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.bluetooth.util;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.team980.thunderscout.bluetooth.ClientConnectionTask;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.scouting_flow.ScoutingFlowActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BluetoothTransferNotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_NOTIFICATION_ID = "com.team980.thunderscout.NOTIFICATION_ID";

    public static final String EXTRA_SCOUT_DATA = "com.team980.thunderscout.SCOUT_DATA";
    public static final String EXTRA_TARGET_DEVICE_ADDRESS = "com.team980.thunderscout.TARGET_DEVICE_ADDRESS";
    //public static final String EXTRA_CAUSING_STACK_TRACE = "com.team980.thunderscout.CAUSING_STACK_TRACE";

    public static final String EXTRA_NOTIFICATION_TASK = "com.team980.thunderscout.NOTIFICATION_TASK";
    public static final int TASK_VIEW_SCOUTING_FLOW = 1;
    public static final int TASK_RETRY_BLUETOOTH_TRANSFER = 2;
    //public static final int TASK_VIEW_STACK_TRACE = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);

        if (notificationId <= -1) {
            throw new IllegalArgumentException("ID must not be zero");
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(notificationId);

        ScoutData data = (ScoutData) intent.getSerializableExtra(EXTRA_SCOUT_DATA);
        String targetDeviceAddress = intent.getStringExtra(EXTRA_TARGET_DEVICE_ADDRESS);
        //String causingStackTrace = intent.getStringExtra(EXTRA_CAUSING_STACK_TRACE);

        switch (intent.getIntExtra(EXTRA_NOTIFICATION_TASK, -1)) {
            case TASK_VIEW_SCOUTING_FLOW:
                context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

                Intent viewIntent = new Intent(context, ScoutingFlowActivity.class)
                        .putExtra(ScoutingFlowActivity.EXTRA_SCOUT_DATA, data)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(viewIntent);
                break;
            case TASK_RETRY_BLUETOOTH_TRANSFER:
                BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(targetDeviceAddress);

                ClientConnectionTask connectTask = new ClientConnectionTask(device, data, context.getApplicationContext());
                connectTask.execute();
                break;
            //case TASK_VIEW_STACK_TRACE:
            //TODO
            //return;
        }
    }
}
