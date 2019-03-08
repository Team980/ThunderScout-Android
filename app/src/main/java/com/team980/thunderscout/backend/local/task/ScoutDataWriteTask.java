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

package com.team980.thunderscout.backend.local.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.backend.local.ScoutDataContract;
import com.team980.thunderscout.backend.local.ScoutDataDbHelper;
import com.team980.thunderscout.schema.ScoutData;

import java.util.ArrayList;
import java.util.List;

public class ScoutDataWriteTask extends AsyncTask<ScoutData, Void, List<ScoutData>> {

    @Nullable
    private StorageWrapper.StorageListener listener;
    private Context context;

    public ScoutDataWriteTask(@Nullable StorageWrapper.StorageListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected List<ScoutData> doInBackground(ScoutData... dataList) {

        // Gets the data repository in write mode
        SQLiteDatabase db = new ScoutDataDbHelper(context).getWritableDatabase();

        List<ScoutData> dataWritten = new ArrayList<>();

        for (ScoutData data : dataList) {

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();

            // Init
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TEAM_NUMBER, data.getTeam());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_MATCH_NUMBER, data.getMatchNumber());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ALLIANCE_STATION, data.getAllianceStation().name());

            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATE_ADDED, data.getDate().getTime());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATA_SOURCE, data.getSource());

            // Sandstorm
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_STARTING_LEVEL, data.getStartingLevel().name());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_CROSSED_HAB_LINE, data.crossedHabLine());

            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_HIGH_ROCKET_HATCH_COUNT, data.getStormHighRocketHatchCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_MIDDLE_ROCKET_HATCH_COUNT, data.getStormMidRocketHatchCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_LOW_ROCKET_HATCH_COUNT, data.getStormLowRocketHatchCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_CARGO_SHIP_HATCH_COUNT, data.getStormCargoShipHatchCount());

            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_HIGH_ROCKET_CARGO_COUNT, data.getStormHighRocketCargoCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_MIDDLE_ROCKET_CARGO_COUNT, data.getStormMidRocketCargoCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_LOW_ROCKET_CARGO_COUNT, data.getStormLowRocketCargoCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_CARGO_SHIP_CARGO_COUNT, data.getStormCargoShipCargoCount());

            // Teleoperated
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_ROCKET_HATCH_COUNT, data.getTeleopHighRocketHatchCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MIDDLE_ROCKET_HATCH_COUNT, data.getTeleopMidRocketHatchCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_ROCKET_HATCH_COUNT, data.getTeleopLowRocketHatchCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_CARGO_SHIP_HATCH_COUNT, data.getTeleopCargoShipHatchCount());

            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_ROCKET_CARGO_COUNT, data.getTeleopHighRocketCargoCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MIDDLE_ROCKET_CARGO_COUNT, data.getTeleopMidRocketCargoCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_ROCKET_CARGO_COUNT, data.getTeleopLowRocketCargoCount());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_CARGO_SHIP_CARGO_COUNT, data.getTeleopCargoShipCargoCount());

            // Endgame
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_CLIMB_LEVEL, data.getEndgameClimbLevel().name());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_CLIMB_TIME, data.getEndgameClimbTime().name());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_SUPPORTED_OTHER_ROBOT_WHEN_CLIMBING, data.supportedOtherRobots());
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_CLIMB_DESCRIPTION, data.getClimbDescription());

            // Notes
            values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_NOTES, data.getNotes());

            long rowId;
            try {
                // Insert the new row
                rowId = db.insertOrThrow(
                        ScoutDataContract.ScoutDataTable.TABLE_NAME,
                        null,
                        values);
            } catch (final Exception e) {
                e.printStackTrace();
                return dataWritten;
            }

            if (rowId > 0) {
                dataWritten.add(data);
            }
        }

        db.close();
        return dataWritten;
    }

    @Override
    protected void onPostExecute(List<ScoutData> dataWritten) {
        //Runs on UI thread after execution

        if (listener != null) {
            listener.onDataWrite(dataWritten);
        }

        super.onPostExecute(dataWritten);
    }
}
