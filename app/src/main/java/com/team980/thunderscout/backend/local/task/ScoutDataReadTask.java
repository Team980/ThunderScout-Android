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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.backend.local.ScoutDataContract;
import com.team980.thunderscout.backend.local.ScoutDataDbHelper;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.AllianceStation;
import com.team980.thunderscout.schema.enumeration.ClimbTime;
import com.team980.thunderscout.schema.enumeration.HabLevel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoutDataReadTask extends AsyncTask<Void, Void, List<ScoutData>> {

    @Nullable //no point in passing null, but safer code if I allow it
    private StorageWrapper.StorageListener listener;
    private Context context;

    public ScoutDataReadTask(@Nullable StorageWrapper.StorageListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public List<ScoutData> doInBackground(Void... params) {

        SQLiteDatabase db = new ScoutDataDbHelper(context).getReadableDatabase();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ScoutDataContract.ScoutDataTable._ID + " DESC";

        Cursor cursor;

        try {
            cursor = db.query(
                    ScoutDataContract.ScoutDataTable.TABLE_NAME,  // The table to query
                    null,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }

        List<ScoutData> dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            dataList.add(initScoutData(cursor));
        }

        while (cursor.moveToNext()) {
            dataList.add(initScoutData(cursor));
        }

        cursor.close();
        db.close();

        return dataList;
    }

    private ScoutData initScoutData(Cursor cursor) {
        ScoutData data = new ScoutData(cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable._ID)));

        // Init
        String teamNumber = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TEAM_NUMBER));

        data.setTeam(teamNumber);

        int matchNumber = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_MATCH_NUMBER));

        data.setMatchNumber(matchNumber);

        String allianceStation = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ALLIANCE_STATION));

        data.setAllianceStation(AllianceStation.valueOf(allianceStation));

        long dateAdded = cursor.getLong(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATE_ADDED));

        data.setDate(new Date(dateAdded));

        String dataSource = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATA_SOURCE));

        data.setSource(dataSource);

        // Sandstorm
        String startingLevel = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_STARTING_LEVEL));

        data.setStartingLevel(HabLevel.valueOf(startingLevel));

        int crossedHabLine = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_CROSSED_HAB_LINE));

        data.setCrossedHabLine(crossedHabLine != 0); //I2B conversion

        int stormHighRocketHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_HIGH_ROCKET_HATCH_COUNT));

        data.setStormHighRocketHatchCount(stormHighRocketHatchCount);

        int stormMiddleRocketHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_MIDDLE_ROCKET_HATCH_COUNT));

        data.setStormMiddleRocketHatchCount(stormMiddleRocketHatchCount);

        int stormLowRocketHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_LOW_ROCKET_HATCH_COUNT));

        data.setStormLowRocketHatchCount(stormLowRocketHatchCount);

        int stormCargoShipHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_CARGO_SHIP_HATCH_COUNT));

        data.setStormCargoShipHatchCount(stormCargoShipHatchCount);

        int stormHighRocketCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_HIGH_ROCKET_CARGO_COUNT));

        data.setStormHighRocketCargoCount(stormHighRocketCargoCount);

        int stormMiddleRocketCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_MIDDLE_ROCKET_CARGO_COUNT));

        data.setStormMiddleRocketCargoCount(stormMiddleRocketCargoCount);

        int stormLowRocketCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_LOW_ROCKET_CARGO_COUNT));

        data.setStormLowRocketCargoCount(stormLowRocketCargoCount);

        int stormCargoShipCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_STORM_CARGO_SHIP_CARGO_COUNT));

        data.setStormCargoShipCargoCount(stormCargoShipCargoCount);

        // Teleoperated
        int teleopHighRocketHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_ROCKET_HATCH_COUNT));

        data.setTeleopHighRocketHatchCount(teleopHighRocketHatchCount);

        int teleopMiddleRocketHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MIDDLE_ROCKET_HATCH_COUNT));

        data.setTeleopMiddleRocketHatchCount(teleopMiddleRocketHatchCount);

        int teleopLowRocketHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_ROCKET_HATCH_COUNT));

        data.setTeleopLowRocketHatchCount(teleopLowRocketHatchCount);

        int teleopCargoShipHatchCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_CARGO_SHIP_HATCH_COUNT));

        data.setTeleopCargoShipHatchCount(teleopCargoShipHatchCount);

        int teleopHighRocketCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_ROCKET_CARGO_COUNT));

        data.setTeleopHighRocketCargoCount(teleopHighRocketCargoCount);

        int teleopMiddleRocketCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MIDDLE_ROCKET_CARGO_COUNT));

        data.setTeleopMiddleRocketCargoCount(teleopMiddleRocketCargoCount);

        int teleopLowRocketCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_ROCKET_CARGO_COUNT));

        data.setTeleopLowRocketCargoCount(teleopLowRocketCargoCount);

        int teleopCargoShipCargoCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_CARGO_SHIP_CARGO_COUNT));

        data.setTeleopCargoShipCargoCount(teleopCargoShipCargoCount);

        // Endgame
        String endgameClimbLevel = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_CLIMB_LEVEL));

        data.setEndgameClimbLevel(HabLevel.valueOf(endgameClimbLevel));

        String endgameClimbTime = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_CLIMB_TIME));

        data.setEndgameClimbTime(ClimbTime.valueOf(endgameClimbTime));

        int supportedOtherRobotWhenClimbing = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_SUPPORTED_OTHER_ROBOT_WHEN_CLIMBING));

        data.setSupportedOtherRobots(supportedOtherRobotWhenClimbing != 0); //I2B conversion

        String climbDescription = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ENDGAME_CLIMB_DESCRIPTION));

        data.setClimbDescription(climbDescription);

        // Notes
        String notes = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_NOTES));

        data.setNotes(notes);

        return data;
    }

    @Override
    protected void onProgressUpdate(Void[] values) {
        //Runs on UI thread when publishProgress() is called

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<ScoutData> dataList) {
        //Runs on UI thread after execution

        if (listener != null) {
            listener.onDataQuery(dataList); //return the data to the listener
        }

        super.onPostExecute(dataList);
    }

}
