package com.team980.thunderscout;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.team980.thunderscout.bluetooth.BluetoothServerService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ThunderScout extends Application {

    @WorkerThread
    public static byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            // Get the bytes of the serialized object
            byte[] buf = bos.toByteArray();

            return buf;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @WorkerThread
    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("THUNDERSCOUT", "Application.onCreate");

        Log.d("THUNDERSCOUT", "Fetching shared prefences");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean runServer = sharedPref.getBoolean("enable_bluetooth_server", false);

        if (runServer) { //TODO I must be launching multiple instances?
            Log.d("THUNDERSCOUT", "Starting service...");
            startService(new Intent(this, BluetoothServerService.class));

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d("THUNDERSCOUT", "Enabling Bluetooth as it's off");
                mBluetoothAdapter.enable(); //TODO prompt user
            }
        }
        Log.d("THUNDERSCOUT", "Finished onCreate");

    }
}
