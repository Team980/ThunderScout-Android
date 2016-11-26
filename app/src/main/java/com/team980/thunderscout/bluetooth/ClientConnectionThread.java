package com.team980.thunderscout.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.match.ScoutingFlowActivity;
import com.team980.thunderscout.util.TSNotificationBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

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
                closeException.printStackTrace();
            }
            manageError(notificationId, connectException);
            return;
        }

        postToastMessage("Successfully connected to server device");

        scoutData.setDateAdded(System.currentTimeMillis());

        ObjectInputStream ioStream = null;
        ObjectOutputStream ooStream;
        try {
            ioStream = new ObjectInputStream(mmSocket.getInputStream());
            ooStream = new ObjectOutputStream(mmSocket.getOutputStream());
            ooStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            manageError(notificationId, e);
            return;
        }

        //TODO add version check

        postToastMessage("Sending data...");

        try {
            ooStream.writeObject(scoutData);
            ooStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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

    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
