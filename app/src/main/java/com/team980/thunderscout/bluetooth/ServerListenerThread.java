package com.team980.thunderscout.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ServerListenerThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;

    private BluetoothAdapter mBluetoothAdapter;

    private Context context;

    public ServerListenerThread(Context context) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothInfo.SERVICE_NAME, UUID.fromString(BluetoothInfo.UUID));
        } catch (IOException e) {
        }
        mmServerSocket = tmp;

        this.context = context;
    }

    public void run() {
        // Keep listening until exception occurs
        while (true) {
            final BluetoothSocket socket;
            try {
                postToastMessage("Listening for incoming connections");
                socket = mmServerSocket.accept(); //this is failing why? - needs more testing
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)

                postToastMessage("Connected to " + socket.getRemoteDevice().getName());

                ServerConnectionTask readTask = new ServerConnectionTask(socket, context);
                readTask.execute();
            }
        }
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {

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
