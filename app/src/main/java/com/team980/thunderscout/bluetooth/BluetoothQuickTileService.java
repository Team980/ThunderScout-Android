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

package com.team980.thunderscout.bluetooth;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.team980.thunderscout.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class BluetoothQuickTileService extends TileService {
    @Override
    public void onTileAdded() {
        TileService.requestListeningState(getApplicationContext(), new ComponentName(this, this.getClass()));
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();

        //tile removed from menu - stop everything
    }

    @Override
    public void onStartListening() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Tile tile = getQsTile();

        if (prefs.getBoolean("enable_bt_server", false)) { //TODO this isn't data from the server itself... it's just of the preference
            //TODO this should reflect error states better...
            tile.setIcon(Icon.createWithResource(this,
                    R.drawable.ic_bluetooth_searching_white_24dp));
            tile.setState(Tile.STATE_ACTIVE);
        } else {
            tile.setIcon(Icon.createWithResource(this,
                    R.drawable.ic_bluetooth_disabled_white_24dp));
            tile.setState(Tile.STATE_INACTIVE);
        }

        tile.updateTile();
    }

    @Override
    public void onClick() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("enable_bt_server", false)) { //Toggles the bluetooth server state
            prefs.edit().putBoolean("enable_bt_server", false).apply();

        } else {
            prefs.edit().putBoolean("enable_bt_server", true).apply();
        }
    }
}
