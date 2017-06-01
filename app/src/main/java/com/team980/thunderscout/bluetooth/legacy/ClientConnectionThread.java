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

package com.team980.thunderscout.bluetooth.legacy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.bluetooth.BluetoothInfo;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.scouting_flow.ScoutingFlowActivity;
import com.team980.thunderscout.util.TSNotificationBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.UUID;

@Deprecated
public class ClientConnectionThread extends Thread { //TODO move to AsyncTask
    private final BluetoothSocket mmSocket;

    private BluetoothAdapter mBluetoothAdapter;

    private TSNotificationBuilder notificationManager;

    private ScoutData scoutData;

    private Context context;

    private ScoutingFlowActivity activity;

    public ClientConnectionThread(BluetoothDevice device, ScoutData data, Context context) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        notificationManager = TSNotificationBuilder.getInstance(context);

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothInfo.UUID));
        } catch (IOException e) {
            FirebaseCrash.report(e);
        }
        mmSocket = tmp;

        scoutData = data;

        this.context = context;
    }

    /**
     * Version with callback support
     */
    public ClientConnectionThread(BluetoothDevice device, ScoutData data, Context context, ScoutingFlowActivity activity) {
        this(device, data, context);

        this.activity = activity;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        int notificationId = notificationManager.showBtTransferInProgress(mmSocket.getRemoteDevice().getName(), false);

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                FirebaseCrash.report(closeException);
            }
            manageError(notificationId, connectException);
            return;
        }

        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), "Connection to server device successful");

        scoutData.setDate(new Date(System.currentTimeMillis()));

        ObjectInputStream ioStream = null;
        ObjectOutputStream ooStream;
        try {
            ioStream = new ObjectInputStream(mmSocket.getInputStream());
            ooStream = new ObjectOutputStream(mmSocket.getOutputStream());
            ooStream.flush();
        } catch (IOException e) {
            FirebaseCrash.report(e);
            manageError(notificationId, e);
            return;
        }

        //TODO add version check?

        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), "Attempting to send scout data");

        try {
            ooStream.writeObject(scoutData);
            ooStream.flush();
        } catch (IOException e) {
            FirebaseCrash.report(e);
            manageError(notificationId, e);
            return;
        }

        notificationManager.showBtTransferFinished(notificationId);

        if (activity != null) { //TODO broadcast reciever

            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {

                @Override
                public void run() {
                    activity.dataOutputCallbackSuccess(ScoutingFlowActivity.OPERATION_SEND_BLUETOOTH);
                }
            });
        }

        try {
            ooStream.close();
            ioStream.close();
        } catch (IOException e) {
            FirebaseCrash.report(e);
        }
    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException ignored) {
        }
    }

    //TODO implement
    private void manageError(int notificationId, final Exception ex) {
        notificationManager.showBtTransferFinished(notificationId);

        if (activity != null) { //TODO broadcast reciever

            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {

                @Override
                public void run() {
                    activity.dataOutputCallbackFail(ScoutingFlowActivity.OPERATION_SEND_BLUETOOTH, ex);
                }
            });
        }
    }
}
