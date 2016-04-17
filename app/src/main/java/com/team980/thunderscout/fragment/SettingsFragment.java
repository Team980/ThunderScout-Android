package com.team980.thunderscout.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.team980.thunderscout.R;
import com.team980.thunderscout.service.BluetoothServerService;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context storedContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This should fix the errors in running getSharedPreferences
        storedContext = getActivity();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        PreferenceManager.getDefaultSharedPreferences(storedContext).registerOnSharedPreferenceChangeListener(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean isServer = sharedPref.getBoolean("pref_isServer", false);

        if (isServer) {
            getPreferenceManager().findPreference("pref_serverDevice").setEnabled(false);
        } else {
            getPreferenceManager().findPreference("pref_serverDevice").setEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_isServer")) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(storedContext); //TODO why does getActivity() return null when closing and reopening the app with the server running?
            Boolean isServer = sharedPref.getBoolean(key, false);

            if (isServer) { //start server
                getPreferenceManager().findPreference("pref_serverDevice").setEnabled(false);

                getActivity().startService(new Intent(storedContext, BluetoothServerService.class));
            } else { //stop server
                getPreferenceManager().findPreference("pref_serverDevice").setEnabled(true);

                getActivity().stopService(new Intent(storedContext, BluetoothServerService.class));
            }
        }

    }
}
