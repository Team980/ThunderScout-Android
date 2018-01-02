/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.preferences;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

import com.team980.thunderscout.bluetooth.util.BluetoothDeviceManager;

// An improved BluetoothDevicePreference that opens a system dialog and caches the device name
// ...not to mention it does the hard work of updating the summary itself
public class BluetoothDevicePickerPreference extends Preference {

    private String address;
    private String name;

    public BluetoothDevicePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setSummary("Not selected");
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        address = getSharedPreferences().getString(getKey(), null);
        name = getSharedPreferences().getString(getKey() + "_cached_name", "Not selected");

        setSummary(name);
    }

    @Override
    protected void onClick() {
        BluetoothDeviceManager bdm = new BluetoothDeviceManager(getContext());
        bdm.pickDevice(device -> {
            if (device != null) {
                address = device.getAddress();
                name = device.getName();

                setSummary(name);
            }

            getSharedPreferences().edit()
                    .putString(getKey(), address)
                    .putString(getKey() + "_cached_name", name)
                    .apply();
        });
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.address = address;
        myState.name = name;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        address = myState.address;
        name = myState.name;

        setSummary(name);
    }


    private static class SavedState extends BaseSavedState {

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        String address;
        String name;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);

            address = source.readString();
            name = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            dest.writeString(address);
            dest.writeString(name);
        }
    }
}
