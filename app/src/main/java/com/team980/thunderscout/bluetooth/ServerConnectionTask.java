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
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.schema.ScoutData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ServerConnectionTask extends AsyncTask<Void, Integer, ServerConnectionTask.TaskResult> {

    private final BluetoothSocket mmSocket;
    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder btTransferInProgress;
    private NotificationCompat.Builder btTransferSuccess;
    private NotificationCompat.Builder btTransferError;
    private int id;

    public ServerConnectionTask(BluetoothSocket socket, Context context) {
        this.context = context;

        mmSocket = socket;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel transferChannel = new NotificationChannel("bt_transfer", "Bluetooth transfers", NotificationManager.IMPORTANCE_LOW);
            transferChannel.setDescription("Ongoing Bluetooth data transfers between devices");
            notificationManager.createNotificationChannel(transferChannel);
        }

        //TODO these should be a different group from the client notifications
        btTransferInProgress = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_bluetooth_transfer_white_24dp) //TODO animated icon?
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.accent))
                .setProgress(1, 0, true)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setGroup("BT_TRANSFER_ONGOING");

        btTransferSuccess = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_check_circle_white_24dp)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.success))
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setGroup("BT_TRANSFER_SUCCESS");

        btTransferError = new NotificationCompat.Builder(context, "bt_transfer")
                .setSmallIcon(R.drawable.ic_warning_white_24dp)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.error))
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ERROR)
                .setGroup("BT_TRANSFER_ERROR");
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();

        id = (int) System.currentTimeMillis(); //TODO implement unique ID creator

        btTransferInProgress.setContentTitle("Receiving data from " + mmSocket.getRemoteDevice().getName());
        btTransferInProgress.setWhen(System.currentTimeMillis());
        btTransferInProgress.setProgress(1, 0, true);

        notificationManager.notify(id, btTransferInProgress.build());
    }

    @Override
    @NonNull
    protected TaskResult doInBackground(Void[] params) {
        publishProgress(0);

        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(mmSocket.getInputStream()); //TODO fix the IOException caused by the missing socket...
        } catch (IOException e) {
            Crashlytics.logException(e);
            return new TaskResult(null, e);
        }

        //TODO version check?
        ScoutData data = null;
        try {
            data = (ScoutData) inputStream.readObject();
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            return new TaskResult(null, e);
        }

        publishProgress(100);

        try {
            inputStream.close();
        } catch (IOException e) {
            Crashlytics.logException(e);
        }

        publishProgress(-1);
        return new TaskResult(data, null);
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        //Runs on UI thread when publishProgress() is called
        super.onProgressUpdate(values);

        btTransferInProgress.setContentTitle("Receiving data from " + mmSocket.getRemoteDevice().getName());
        btTransferInProgress.setWhen(System.currentTimeMillis());

        if (values[0] == -1) { //Indeterminate
            btTransferInProgress.setProgress(1, 0, true);
        } else {
            btTransferInProgress.setProgress(100, values[0], false);
        }

        notificationManager.notify(id, btTransferInProgress.build());
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);

        //Intent intent = new Intent(HomeFragment.TaskUpdateReceiver.ACTION_UPDATE_ONGOING_TASK);

        if (result.getException() == null) { //No exception, so we were successful
            //intent.putExtra(HomeFragment.TaskUpdateReceiver.KEY_UPDATE_TYPE,
            //HomeFragment.TaskUpdateReceiver.UpdateType.FINISHED);

            btTransferSuccess.setContentTitle("Successfully received data from " + mmSocket.getRemoteDevice().getName());
            btTransferSuccess.setContentText("Team " + result.getData().getTeam()
                    + " - Qualification Match " + result.getData().getMatchNumber());
            btTransferSuccess.setWhen(System.currentTimeMillis());

            notificationManager.notify(id, btTransferSuccess.build());

            new Handler().postDelayed(() -> notificationManager.cancel(id), 10000); //10 seconds
        } else {
            //intent.putExtra(HomeFragment.TaskUpdateReceiver.KEY_UPDATE_TYPE,
            //HomeFragment.TaskUpdateReceiver.UpdateType.ERROR);

            btTransferError.setContentTitle("Failed to receive data from " + mmSocket.getRemoteDevice().getName());
            btTransferError.setContentText("Expand to see the full error message");
            btTransferError.setStyle(new NotificationCompat.BigTextStyle().bigText("Error: " + result.getException().getMessage()));
            btTransferError.setWhen(System.currentTimeMillis());

            notificationManager.notify(id, btTransferError.build());
        }

        //TODO send Home update intent

        if (result.getData() != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            if (prefs.getBoolean(context.getResources().getString(R.string.pref_bt_save_to_local_device), true)) {
                //Put the fetched ScoutData in the local database
                AccountScope.getStorageWrapper(AccountScope.LOCAL, context).writeData(result.getData(), new StorageWrapper.StorageListener() {
                    @Override
                    public void onDataWrite(@Nullable List<ScoutData> dataWritten) {
                        Intent refreshIntent = new Intent().setAction(MainActivity.ACTION_REFRESH_DATA_VIEW);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(refreshIntent);
                    }
                }); //TODO assumes LOCAL
            }

            if (prefs.getBoolean(context.getResources().getString(R.string.pref_bt_send_to_bluetooth_server), false)) {
                String address = prefs.getString(context.getResources().getString(R.string.pref_bt_bluetooth_server_device), null);

                try {
                    if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        throw new NullPointerException("Bluetooth is disabled"); //todo better way to notify
                    }

                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    ClientConnectionTask connectTask = new ClientConnectionTask(device, result.getData(), context);
                    connectTask.execute();
                } catch (IllegalArgumentException e) {
                    throw e; //todo better way to notify
                }
            }
        }
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
