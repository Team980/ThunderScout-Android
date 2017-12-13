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
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.team980.thunderscout.R;
import com.team980.thunderscout.bluetooth.util.BluetoothInfo;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.scouting_flow.ScoutingFlowActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class ClientConnectionTask extends AsyncTask<Void, Integer, ClientConnectionTask.TaskResult> {

    private static final int SUCCESS_SUMMARY_ID = 2;
    private static final int ERROR_SUMMARY_ID = 3;
    private Context context;
    private BluetoothDevice device;
    private ScoutData scoutData;
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
            NotificationChannel transferChannel = new NotificationChannel("bt_transfer", "Bluetooth Transfers", NotificationManager.IMPORTANCE_LOW);
            transferChannel.setDescription("Ongoing and erroneous Bluetooth transfers");
            notificationManager.createNotificationChannel(transferChannel);
        }

        btTransferInProgress = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_bluetooth_transfer_white_24dp) //TODO animated icon?
                .setOngoing(true)
                .setUsesChronometer(true)
                .setTicker("Bluetooth transfer in progress")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.accent))
                .setProgress(1, 0, true)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setGroup("BT_CLIENT_TRANSFER_ONGOING");

        btTransferSuccess = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_check_circle_white_24dp)
                .setTicker("Bluetooth transfer succeeded")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.success))
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setGroup("BT_CLIENT_TRANSFER_SUCCESS");

        btTransferError = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_warning_white_24dp)
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

        id = (int) System.currentTimeMillis(); //TODO implement unique ID creator

        btTransferInProgress.setContentTitle("Sending data to " + device.getName());
        btTransferInProgress.setWhen(System.currentTimeMillis());
        btTransferInProgress.setProgress(1, 0, true);

        NotificationManagerCompat.from(context).notify(id, btTransferInProgress.build());
    }

    @NonNull
    public TaskResult doInBackground(Void... params) {
        try {
            Thread.sleep(scoutData.getAllianceStation().getDelay()); //Variable delay based on AllianceStation
        } catch (InterruptedException e) {
            Crashlytics.logException(e);
        }

        publishProgress(0);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        BluetoothSocket mmSocket;
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            mmSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothInfo.UUID));
        } catch (IOException e) {
            Crashlytics.logException(e);
            return new TaskResult(scoutData, e);
        }

        publishProgress(25);

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out TODO retry?
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Crashlytics.logException(closeException);
            }
            return new TaskResult(scoutData, connectException);
        }

        Crashlytics.log(Log.INFO, this.getClass().getName(), "Connection to server device successful");
        publishProgress(50);

        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(mmSocket.getOutputStream());
            outputStream.flush();
        } catch (IOException e) {
            Crashlytics.logException(e);
            return new TaskResult(scoutData, e);
        }

        publishProgress(75);

        //TODO add version check?

        Crashlytics.log(Log.INFO, this.getClass().getName(), "Attempting to send scout data");
        try {
            outputStream.writeObject(scoutData);
            outputStream.flush();
        } catch (Exception e) {
            Crashlytics.logException(e);
            return new TaskResult(scoutData, e);
        }

        publishProgress(100);

        try {
            outputStream.close();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        publishProgress(-1);
        return new TaskResult(scoutData, null);
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
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                NotificationManagerCompat.from(context).notify(SUCCESS_SUMMARY_ID, btTransferSuccess.setGroupSummary(true).build());
            }

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

            //TODO Needs two Intents: RETRY that starts the Task again, and EDIT / MODIFY that opens the scouting flow
            //TODO Clicking the notification should issue RETRY
            //TODO This is actually much harder than it sounds

            //TODO a third VIEW STACK TRACE / VIEW ERROR / MORE INFO / INFO intent that shows a dialog of the stack trace would be EXTRA nice

            PendingIntent retryIntent = PendingIntent.getActivity(context, 1,
                    new Intent(context, ScoutingFlowActivity.class).putExtra(ScoutingFlowActivity.EXTRA_SCOUT_DATA,
                            result.getData()),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            btTransferError.setContentIntent(retryIntent);

            NotificationCompat.Action retryAction = new NotificationCompat.Action(
                    R.drawable.ic_refresh_white_24dp,
                    "Retry",
                    retryIntent
            );

            btTransferError.addAction(retryAction); //TODO this doesn't dismiss the notification

            NotificationManagerCompat.from(context).notify(id, btTransferError.build());
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                NotificationManagerCompat.from(context).notify(ERROR_SUMMARY_ID, btTransferError.setGroupSummary(true).build());
            }

        }

        //TODO send Home update Intent
    }

    class TaskResult {
        private ScoutData data;
        private Exception exception;

        TaskResult(ScoutData data, Exception exception) {
            this.data = data;
            this.exception = exception;
        }

        ScoutData getData() {
            return data;
        }

        Exception getException() {
            return exception;
        }
    }
}
