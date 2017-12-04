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

package com.team980.thunderscout;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.quicksettings.TileService;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.team980.thunderscout.bluetooth.BluetoothServerService;
import com.team980.thunderscout.bluetooth.util.BluetoothQuickTileService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import io.fabric.sdk.android.Fabric;

public class ThunderScout extends MultiDexApplication implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Deprecated
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
            Crashlytics.logException(e);
            return null;
        }
    }

    @Deprecated
    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch (ClassNotFoundException | IOException e) {
            Crashlytics.logException(e);
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

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean runServer = sharedPref.getBoolean(getResources().getString(R.string.pref_enable_bluetooth_server), false);

        if (runServer) { //I must be launching multiple instances?
            startService(new Intent(this, BluetoothServerService.class));
        }

        sharedPref.registerOnSharedPreferenceChangeListener(this);

        //Firebase Analytics
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(sharedPref.getBoolean(getResources().getString(R.string.pref_enable_analytics), true));


        //Manually init Crashlytics
        if (sharedPref.getBoolean(getResources().getString(R.string.pref_enable_crashlytics), true)) {
            Crashlytics crashlyticsKit = new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                    .build();

            // Initialize Fabric with the debug-disabled crashlytics.
            Fabric.with(this, crashlyticsKit);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getResources().getString(R.string.pref_enable_bluetooth_server))) {
            Boolean isServer = sharedPreferences.getBoolean(getResources().getString(R.string.pref_enable_bluetooth_server), false);

            if (isServer) {
                startService(new Intent(this, BluetoothServerService.class));
            } else {
                stopService(new Intent(this, BluetoothServerService.class));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                TileService.requestListeningState(this, new ComponentName(this, BluetoothQuickTileService.class));
            }
        } else if (key.equals(getResources().getString(R.string.pref_enable_analytics))) {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(sharedPreferences.getBoolean(getResources().getString(R.string.pref_enable_analytics), true));
        }
    }
}
