package com.team980.thunderscout;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.team980.thunderscout.service.BluetoothServerService;

public class ThunderScout extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("THUNDERSCOUT", "Application.onCreate");


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d("THUNDERSCOUT", "Enabling Bluetooth as it's off");
            mBluetoothAdapter.enable(); //TODO prompt user
        }

        Log.d("THUNDERSCOUT", "Fetching shared prefences");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean runServer = sharedPref.getBoolean("pref_isServer", false);

        if (runServer) { //TODO I must be launching multiple instances?
            Log.d("THUNDERSCOUT", "Starting service...");
            startService(new Intent(this, BluetoothServerService.class));
        }
        Log.d("THUNDERSCOUT", "Finished onCreate");
    }

}
