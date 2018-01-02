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

        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        ScoutData data = (ScoutData) intent.getSerializableExtra(EXTRA_SCOUT_DATA);
        String targetDeviceAddress = intent.getStringExtra(EXTRA_TARGET_DEVICE_ADDRESS);
        //String causingStackTrace = intent.getStringExtra(EXTRA_CAUSING_STACK_TRACE);

        switch (intent.getIntExtra(EXTRA_NOTIFICATION_TASK, -1)) {
            case TASK_VIEW_SCOUTING_FLOW:
                Intent viewIntent = new Intent(context, ScoutingFlowActivity.class)
                        .putExtra(ScoutingFlowActivity.EXTRA_SCOUT_DATA, data);
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
