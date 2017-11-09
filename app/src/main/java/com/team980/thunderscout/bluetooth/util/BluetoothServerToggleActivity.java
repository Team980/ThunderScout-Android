package com.team980.thunderscout.bluetooth.util;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.team980.thunderscout.R;

public class BluetoothServerToggleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Toggle Bluetooth server
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean currentValue = sharedPref.getBoolean(getResources().getString(R.string.pref_enable_bluetooth_server), false);
        sharedPref.edit().putBoolean(getResources().getString(R.string.pref_enable_bluetooth_server), !currentValue).apply();

        String actionString;
        if (!currentValue) {
            actionString = "enabled";
        } else {
            actionString = "disabled";
        }

        Toast.makeText(this, "Bluetooth Server " + actionString, Toast.LENGTH_SHORT).show();

        finish();
    }
}
