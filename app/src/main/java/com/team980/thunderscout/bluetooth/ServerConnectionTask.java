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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.legacy.feed.EntryOperationWrapper;
import com.team980.thunderscout.legacy.feed.EntryOperationWrapper.EntryOperationStatus;
import com.team980.thunderscout.legacy.feed.EntryOperationWrapper.EntryOperationType;
import com.team980.thunderscout.legacy.feed.FeedEntry;
import com.team980.thunderscout.legacy.feed.task.FeedDataWriteTask;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.util.TSNotificationBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;

public class ServerConnectionTask extends AsyncTask<Void, Integer, ScoutData> {

    private final BluetoothSocket mmSocket;

    private Context context;

    private TSNotificationBuilder notificationManager;

    public ServerConnectionTask(BluetoothSocket socket, Context context) {
        mmSocket = socket;

        this.context = context;

        notificationManager = TSNotificationBuilder.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected ScoutData doInBackground(Void[] params) {
        int notificationId = notificationManager.showBtTransferInProgress(mmSocket.getRemoteDevice().getName(), true);

        ObjectInputStream inputStream;

        try {
            inputStream = new ObjectInputStream(mmSocket.getInputStream());
        } catch (IOException e) {
            FirebaseCrash.report(e);
            notificationManager.showBtTransferError(mmSocket.getRemoteDevice().getName(),
                    notificationId);
            return null;
        }

        Gson gson = new GsonBuilder()
                .setDateFormat(DateFormat.DEFAULT) //todo
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        //TODO version check?
        ScoutData data = null;
        try {
            data = gson.fromJson((String) inputStream.readObject(), ScoutData.class);
        } catch (Exception e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
            notificationManager.showBtTransferError(mmSocket.getRemoteDevice().getName(),
                    notificationId);
            return null;
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            FirebaseCrash.report(e);
        }

        notificationManager.showBtTransferFinished(notificationId);
        return data;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        //Runs on UI thread when publishProgress() is called
        super.onProgressUpdate(values);

        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), "Inserted ScoutData into DB, row=" + values[0]);
    }

    @Override
    protected void onPostExecute(ScoutData o) {
        super.onPostExecute(o);

        if (o != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            FeedEntry feedEntry = new FeedEntry(FeedEntry.EntryType.SERVER_RECEIVED_MATCH, System.currentTimeMillis());

            if (prefs.getBoolean("bt_send_to_local_storage", true)) {
                //Put the fetched ScoutData in the local database
                AccountScope.getStorageWrapper(AccountScope.LOCAL, context).writeData(o, null); //TODO assumes LOCAL, no callback

                feedEntry.addOperation(new EntryOperationWrapper(EntryOperationType.SAVED_TO_LOCAL_STORAGE,
                        EntryOperationStatus.OPERATION_SUCCESSFUL)); //TODO determine this based on callback?
            }

            if (prefs.getBoolean("bt_send_to_bt_server", false)) {
                String address = prefs.getString("bt_bt_server_device", null);

                BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                if (device.getName() == null) { //This should catch both the no device selected error and the bluetooth off error
                    throw new NullPointerException("Error initializing Bluetooth!"); //todo better way to notify?
                }

                ClientConnectionThread connectThread = new ClientConnectionThread(device, o, context, null);
                connectThread.start();

                feedEntry.addOperation(new EntryOperationWrapper(EntryOperationType.SENT_TO_BLUETOOTH_SERVER,
                        EntryOperationStatus.OPERATION_SUCCESSFUL)); //TODO determine this based on callback?
            }

            /*if (prefs.getBoolean("bt_send_to_linked_sheet", false)) {
                SheetsUpdateTask task = new SheetsUpdateTask(context);
                task.execute(o);
            }*/

            FeedDataWriteTask feedDataWriteTask = new FeedDataWriteTask(feedEntry, context);
            feedDataWriteTask.execute();
        } else {
            FirebaseCrash.logcat(Log.ERROR, this.getClass().getName(), "Failed to start FeedDataWriteTask!");
        }
    }

}
