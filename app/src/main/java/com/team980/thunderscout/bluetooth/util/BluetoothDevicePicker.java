/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.bluetooth.util;

import android.bluetooth.BluetoothDevice;

/**
 * A helper to show a system "Device Picker" activity to the user.
 * Taken from the same gist as BluetoothDeviceManager, and from Android Source (TODO license?)
 */
public interface BluetoothDevicePicker {
    String EXTRA_NEED_AUTH = "android.bluetooth.devicepicker.extra.NEED_AUTH";
    String EXTRA_FILTER_TYPE = "android.bluetooth.devicepicker.extra.FILTER_TYPE";
    String EXTRA_LAUNCH_PACKAGE = "android.bluetooth.devicepicker.extra.LAUNCH_PACKAGE";
    String EXTRA_LAUNCH_CLASS = "android.bluetooth.devicepicker.extra.DEVICE_PICKER_LAUNCH_CLASS";

    /**
     * Broadcast when one BT device is selected from BT device picker screen.
     * Selected {@link BluetoothDevice} is returned in extra data named
     * {@link BluetoothDevice#EXTRA_DEVICE}.
     */
    String ACTION_DEVICE_SELECTED = "android.bluetooth.devicepicker.action.DEVICE_SELECTED";

    /**
     * Broadcast when someone want to select one BT device from devices list.
     * This intent contains below extra data:
     * - {@link #EXTRA_NEED_AUTH} (boolean): if need authentication
     * - {@link #EXTRA_FILTER_TYPE} (int): what kinds of device should be
     * listed
     * - {@link #EXTRA_LAUNCH_PACKAGE} (string): where(which package) this
     * intent come from
     * - {@link #EXTRA_LAUNCH_CLASS} (string): where(which class) this intent
     * come from
     */
    String ACTION_LAUNCH = "android.bluetooth.devicepicker.action.LAUNCH";

    /**
     * Ask device picker to show all kinds of BT devices
     */
    int FILTER_TYPE_ALL = 0;
    /**
     * Ask device picker to show BT devices that support AUDIO profiles
     */
    int FILTER_TYPE_AUDIO = 1;
    /**
     * Ask device picker to show BT devices that support Object Transfer
     */
    int FILTER_TYPE_TRANSFER = 2;
    /**
     * Ask device picker to show BT devices that support
     * Personal Area Networking User (PANU) profile
     */
    int FILTER_TYPE_PANU = 3;
    /**
     * Ask device picker to show BT devices that support Network Access Point (NAP) profile
     */
    int FILTER_TYPE_NAP = 4;
}