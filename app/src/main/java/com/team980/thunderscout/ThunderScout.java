package com.team980.thunderscout;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.quicksettings.TileService;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.team980.thunderscout.bluetooth.BluetoothQuickTileService;
import com.team980.thunderscout.bluetooth.BluetoothServerService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ThunderScout extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

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

    public static boolean isInteger(String str) { //TODO use this for all the int checks
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
    public void onCreate() { //This isn't why loading is slow
        super.onCreate();
        Log.d("THUNDERSCOUT", "Application.onCreate");

        Log.d("THUNDERSCOUT", "Fetching shared preferences");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean runServer = sharedPref.getBoolean("enable_bt_server", false);

        if (runServer) { //TODO I must be launching multiple instances?
            Log.d("THUNDERSCOUT", "Starting service...");
            startService(new Intent(this, BluetoothServerService.class));
        }

        Log.d("THUNDERSCOUT", "Registering onPreferenceChangeListener");
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        Log.d("THUNDERSCOUT", "Finished onCreate");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("enable_bt_server")) {
            Boolean isServer = sharedPreferences.getBoolean("enable_bt_server", false);

            Log.d("PREFLISTEN", "Server preference changed");

            if (isServer) {
                Log.d("PREFLISTEN", "enabling BT server");
                startService(new Intent(this, BluetoothServerService.class));
            } else {
                Log.d("PREFLISTEN", "disabling BT server");
                stopService(new Intent(this, BluetoothServerService.class));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Log.d("TILETRACE", "requesting tile listening state");
                TileService.requestListeningState(this, new ComponentName(this, BluetoothQuickTileService.class));
            }
        }
    }
}
