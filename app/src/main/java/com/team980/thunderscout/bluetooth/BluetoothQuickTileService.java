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
        Log.d("TILETRACE", "tile clicked");

        if (prefs.getBoolean("enable_bt_server", false)) { //Toggles the bluetooth server state
            Log.d("TILETRACE", "disabling BT server");
            prefs.edit().putBoolean("enable_bt_server", false).apply();

        } else {
            prefs.edit().putBoolean("enable_bt_server", true).apply();
            Log.d("TILETRACE", "enabling BT server");
        }
    }
}
