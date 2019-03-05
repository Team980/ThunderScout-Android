/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.team980.thunderscout.R;
import com.team980.thunderscout.bluetooth.util.BluetoothInfo;
import com.team980.thunderscout.bluetooth.util.BluetoothTransferNotificationReceiver;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.util.NotificationIdFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class ClientConnectionTask extends AsyncTask<Void, Integer, ClientConnectionTask.TaskResult> {

    public static final int RESULT_CODE_SUCCESSFUL = 1;
    public static final int RESULT_CODE_VERSION_MISMATCH = 2;

    private static final int SUCCESS_SUMMARY_ID = NotificationIdFactory.getNewNotificationId();
    private static final int ERROR_SUMMARY_ID = NotificationIdFactory.getNewNotificationId();

    private Context context;

    private BluetoothDevice device;

    private ScoutData scoutData;

    private BluetoothSocket mmSocket;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder btTransferInProgress;
    private NotificationCompat.Builder btTransferSuccess;
    private NotificationCompat.Builder btTransferError;

    private int id;

    public ClientConnectionTask(BluetoothDevice device, ScoutData data, Context context) {
        this.context = context;

        this.device = device;

        scoutData = data;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel transferChannel = new NotificationChannel("bt_transfer", "Bluetooth transfers", NotificationManager.IMPORTANCE_LOW);
            transferChannel.setDescription("Ongoing and erroneous Bluetooth transfers");
            notificationManager.createNotificationChannel(transferChannel);
        }

        btTransferInProgress = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_bluetooth_transfer_24dp) //TODO animated icon?
                .setOngoing(true)
                .setUsesChronometer(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.accent))
                .setProgress(1, 0, true)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS);

        btTransferSuccess = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_check_circle_24dp)
                .setTicker("Bluetooth transfer succeeded")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.success))
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setGroup("BT_CLIENT_TRANSFER_SUCCESS");

        btTransferError = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_warning_24dp)
                .setTicker("Bluetooth transfer failed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.error))
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ERROR)
                .setGroup("BT_CLIENT_TRANSFER_ERROR");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        id = NotificationIdFactory.getNewNotificationId();

        btTransferInProgress.setContentTitle("Sending data to " + device.getName());
        btTransferInProgress.setWhen(System.currentTimeMillis());
        btTransferInProgress.setProgress(1, 0, true);

        NotificationManagerCompat.from(context).notify(id, btTransferInProgress.build());
    }

    @NonNull
    public TaskResult doInBackground(Void... params) {
        TaskResult result = new TaskResult(scoutData, new Exception("Unreachable statement"));
        int attempt = 1;

        while (attempt <= 3) { //Maximum of three attempts
            result = sendScoutData(attempt); //blocking

            if (result.getException() == null || result.isTerminated()) {
                return result;
            } else {
                attempt++;
            }
        }

        return result;
    }

    @NonNull
    private TaskResult sendScoutData(int trial) {
        try {
            Thread.sleep(scoutData.getAllianceStation().getDelay()); //Variable delay based on AllianceStation
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publishProgress(0, trial);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        mmSocket = null;
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            mmSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothInfo.UUID));
        } catch (IOException e) {
            e.printStackTrace();
            return new TaskResult(scoutData, e);
        }

        publishProgress(20, trial);

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
            return new TaskResult(scoutData, connectException);
        }

        System.out.println(this.getClass().getName() + " Connection to server device successful");
        publishProgress(40, trial);

        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(mmSocket.getOutputStream());
            inputStream = new ObjectInputStream(mmSocket.getInputStream());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return new TaskResult(scoutData, e);
        }

        publishProgress(60, trial);

        System.out.println(this.getClass().getName() + " Attempting to send scout data");
        try {
            outputStream.writeObject(scoutData);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskResult(scoutData, e);
        }

        publishProgress(80, trial);

        int resultCode;
        try {
            resultCode = inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskResult(scoutData, e);
        }

        publishProgress(100, trial);

        try {
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        publishProgress(-1, trial);
        if (resultCode == RESULT_CODE_SUCCESSFUL) { //Server reported that the operation succeeded
            return new TaskResult(scoutData, null);
        } else if (resultCode == RESULT_CODE_VERSION_MISMATCH) { //Server caught the InvalidClassException and responded
            Exception e = new Exception("App version mismatch; data rejected by server");
            e.printStackTrace();
            return new TaskResult(scoutData, e, true); //Explicitly prevent more attempts
        } else { //No result code or improper value (?)
            Exception e = new Exception("Failed to receive confirmation from server");
            e.printStackTrace();
            return new TaskResult(scoutData, e);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        btTransferInProgress.setContentTitle("Sending data to " + device.getName());
        btTransferInProgress.setWhen(System.currentTimeMillis());

        if (values[0] == -1) { //Indeterminate
            btTransferInProgress.setProgress(1, 0, true);
        } else {
            btTransferInProgress.setProgress(100, values[0], false);
        }

        btTransferInProgress.setContentText("Attempt " + values[1] + " of 3");

        NotificationManagerCompat.from(context).notify(id, btTransferInProgress.build());
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);

        //Intent intent = new Intent(HomeFragment.TaskUpdateReceiver.ACTION_UPDATE_ONGOING_TASK);

        if (result.getException() == null) { //No exception, so we were successful
            //intent.putExtra(HomeFragment.TaskUpdateReceiver.KEY_UPDATE_TYPE,
            //HomeFragment.TaskUpdateReceiver.UpdateType.FINISHED);

            btTransferSuccess.setContentTitle("Successfully sent data to " + device.getName());
            btTransferSuccess.setContentText("Team " + result.getData().getTeam()
                    + " - Qualification Match " + result.getData().getMatchNumber());
            btTransferSuccess.setWhen(System.currentTimeMillis());

            NotificationManagerCompat.from(context).notify(id, btTransferSuccess.build());
            //NotificationManagerCompat.from(context).notify(SUCCESS_SUMMARY_ID, btTransferSuccess.setGroupSummary(true).build());

            new Handler().postDelayed(() -> {
                notificationManager.cancel(id);
            }, 7000); //7 seconds
        } else {
            //intent.putExtra(HomeFragment.TaskUpdateReceiver.KEY_UPDATE_TYPE,
            //HomeFragment.TaskUpdateReceiver.UpdateType.ERROR);

            btTransferError.setContentTitle("Failed to send data to " + device.getName());
            btTransferError.setContentText("Team " + result.getData().getTeam()
                    + " - Qualification Match " + result.getData().getMatchNumber());
            btTransferError.setStyle(new NotificationCompat.BigTextStyle().bigText("Team " + result.getData().getTeam()
                    + " - Qualification Match " + result.getData().getMatchNumber()
                    + "\nError: " + result.getException().getMessage()));
            btTransferError.setWhen(System.currentTimeMillis());

            //TODO intent to display stack trace?

            Intent editIntent = new Intent(context, BluetoothTransferNotificationReceiver.class);
            editIntent.putExtra(BluetoothTransferNotificationReceiver.EXTRA_NOTIFICATION_ID, id);
            editIntent.putExtra(BluetoothTransferNotificationReceiver.EXTRA_SCOUT_DATA, scoutData);
            editIntent.putExtra(BluetoothTransferNotificationReceiver.EXTRA_NOTIFICATION_TASK,
                    BluetoothTransferNotificationReceiver.TASK_EDIT_SCOUTING_FLOW);

            PendingIntent editPendingIntent = PendingIntent.getBroadcast(context, NotificationIdFactory.getNewRequestCode(), editIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            btTransferError.addAction(new NotificationCompat.Action(
                    R.drawable.ic_create_24dp,
                    "Edit",
                    editPendingIntent
            ));

            Intent retryIntent = new Intent(context, BluetoothTransferNotificationReceiver.class);
            retryIntent.putExtra(BluetoothTransferNotificationReceiver.EXTRA_NOTIFICATION_ID, id);
            retryIntent.putExtra(BluetoothTransferNotificationReceiver.EXTRA_SCOUT_DATA, scoutData);
            retryIntent.putExtra(BluetoothTransferNotificationReceiver.EXTRA_TARGET_DEVICE_ADDRESS, device.getAddress());
            retryIntent.putExtra(BluetoothTransferNotificationReceiver.EXTRA_NOTIFICATION_TASK,
                    BluetoothTransferNotificationReceiver.TASK_RETRY_BLUETOOTH_TRANSFER);

            PendingIntent retryPendingIntent = PendingIntent.getBroadcast(context, NotificationIdFactory.getNewRequestCode(), retryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            btTransferError.addAction(new NotificationCompat.Action(
                    R.drawable.ic_refresh_24dp,
                    "Retry",
                    retryPendingIntent
            ));

            btTransferError.setContentIntent(editPendingIntent);

            NotificationManagerCompat.from(context).notify(id, btTransferError.build());
            //NotificationManagerCompat.from(context).notify(ERROR_SUMMARY_ID, btTransferError.setGroupSummary(true).build());
        }

        //TODO send Home update Intent
    }

    class TaskResult {
        private ScoutData data;
        private Exception exception;
        private boolean terminate;

        TaskResult(ScoutData data, Exception exception, boolean terminate) {
            this.data = data;
            this.exception = exception;
            this.terminate = terminate;
        }

        TaskResult(ScoutData data, Exception exception) {
            this(data, exception, false);
        }

        ScoutData getData() {
            return data;
        }

        Exception getException() {
            return exception;
        }

        boolean isTerminated() {
            return terminate;
        }
    }
}
