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

import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.quicksettings.TileService;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.bluetooth.BluetoothQuickTileService;
import com.team980.thunderscout.bluetooth.BluetoothServerService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ThunderScout extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

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
            FirebaseCrash.report(e);
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
            FirebaseCrash.report(e);
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
        boolean runServer = sharedPref.getBoolean("enable_bt_server", false);

        if (runServer) { //TODO I must be launching multiple instances?
            startService(new Intent(this, BluetoothServerService.class));
        }

        sharedPref.registerOnSharedPreferenceChangeListener(this);

        //TODO integrate into scouting flow?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {  //Pins shortcut to home screen - for testing adaptive icon
            ShortcutManager mShortcutManager = getSystemService(ShortcutManager.class);

            if (mShortcutManager.isRequestPinShortcutSupported()) {
                // Assumes there's already a shortcut with the ID "match_scout".
                // The shortcut must be enabled.
                ShortcutInfo pinShortcutInfo =
                        new ShortcutInfo.Builder(this, "match_scout").build();

                // Create the PendingIntent object only if your app needs to be notified
                // that the user allowed the shortcut to be pinned. Note that, if the
                // pinning operation fails, your app isn't notified. We assume here that the
                // app has implemented a method called createShortcutResultIntent() that
                // returns a broadcast intent.
                Intent pinnedShortcutCallbackIntent =
                        mShortcutManager.createShortcutResultIntent(pinShortcutInfo);

                // Configure the intent so that your app's broadcast receiver gets
                // the callback successfully.
                PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                        pinnedShortcutCallbackIntent, 0);

                mShortcutManager.requestPinShortcut(pinShortcutInfo,
                        successCallback.getIntentSender());
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("enable_bt_server")) {
            Boolean isServer = sharedPreferences.getBoolean("enable_bt_server", false);

            if (isServer) {
                startService(new Intent(this, BluetoothServerService.class));
            } else {
                stopService(new Intent(this, BluetoothServerService.class));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                TileService.requestListeningState(this, new ComponentName(this, BluetoothQuickTileService.class));
            }
        }
    }
}
