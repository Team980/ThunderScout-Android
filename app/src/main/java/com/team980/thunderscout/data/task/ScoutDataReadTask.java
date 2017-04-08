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

package com.team980.thunderscout.data.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.ScoutDataContract.ScoutDataTable;
import com.team980.thunderscout.data.ScoutDataDbHelper;
import com.team980.thunderscout.data.enumeration.AllianceColor;
import com.team980.thunderscout.data.enumeration.ClimbingStats;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;
import com.team980.thunderscout.info.LocalDataAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * TODO Rewrite this class to add sorting/filtering parameters
 */
public class ScoutDataReadTask extends AsyncTask<Void, ScoutData, Void> {

    private LocalDataAdapter viewAdapter;
    private Context context;

    private SwipeRefreshLayout swipeLayout;

    public ScoutDataReadTask(LocalDataAdapter adapter, Context context) {
        viewAdapter = adapter;
        this.context = context;
    }

    public ScoutDataReadTask(LocalDataAdapter adapter, Context context, SwipeRefreshLayout refresh) {
        viewAdapter = adapter;
        this.context = context;

        swipeLayout = refresh;
    }

    @Override
    protected void onPreExecute() {
        //viewAdapter.clearData();

        if (swipeLayout != null) {

            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(true);
                }
            });
        }

        super.onPreExecute();
    }

    @Override
    public Void doInBackground(Void... params) {

        SQLiteDatabase db = new ScoutDataDbHelper(context).getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScoutDataTable._ID,
                ScoutDataTable.COLUMN_NAME_TEAM_NUMBER,
                ScoutDataTable.COLUMN_NAME_MATCH_NUMBER,
                ScoutDataTable.COLUMN_NAME_ALLIANCE_COLOR,

                ScoutDataTable.COLUMN_NAME_DATE_ADDED,
                ScoutDataTable.COLUMN_NAME_DATA_SOURCE,

                ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DELIVERED,
                ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DROPPED,
                ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT,
                ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS,
                ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS,
                ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE,

                ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED,
                ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DROPPED,
                ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS,
                ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS,
                ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS,
                ScoutDataTable.COLUMN_NAME_CLIMBING_STATS,

                ScoutDataTable.COLUMN_NAME_TROUBLE_WITH,
                ScoutDataTable.COLUMN_NAME_COMMENTS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ScoutDataTable._ID + " DESC";

        Cursor cursor;

        try {
            cursor = db.query(
                    ScoutDataTable.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        } catch (SQLiteException e) {
            FirebaseCrash.report(e);
            return null;
        }

        if (cursor.moveToFirst()) {
            initScoutData(cursor);
        }

        while (cursor.moveToNext()) {
            initScoutData(cursor);
        }

        cursor.close();
        db.close();
        return null;
    }

    private void initScoutData(Cursor cursor) {
        ScoutData data = new ScoutData();

        // Init
        String teamNumber = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TEAM_NUMBER));

        data.setTeam(teamNumber);

        int matchNumber = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_MATCH_NUMBER));

        data.setMatch(matchNumber);

        String allianceColor = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_ALLIANCE_COLOR));

        data.setAlliance(AllianceColor.valueOfCompat(allianceColor));

        byte[] dateAdded = cursor.getBlob(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DATE_ADDED));

        data.setDate((Date) ThunderScout.deserializeObject(dateAdded));

        String dataSource = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DATA_SOURCE));

        data.setSource(dataSource);

        // Auto
        int autoGearsDelivered = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DELIVERED));

        data.getAutonomous().setGearsDelivered(autoGearsDelivered);

        int autoGearsDropped = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DROPPED));

        data.getAutonomous().setGearsDropped(autoGearsDropped);

        String autoLowGoalDumpAmount = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT));

        data.getAutonomous().setLowGoalDumpAmount(FuelDumpAmount.valueOf(autoLowGoalDumpAmount));

        int autoHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS));

        data.getAutonomous().setHighGoals(autoHighGoals);

        int autoMissedHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS));

        data.getAutonomous().setMissedHighGoals(autoMissedHighGoals);

        int crossedBaseline = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE));

        data.getAutonomous().setCrossedBaseline(crossedBaseline != 0); //I2B conversion

        // Teleop
        int teleopGearsDelivered = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED));

        data.getTeleop().setGearsDelivered(teleopGearsDelivered);

        int teleopGearsDropped = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DROPPED));

        data.getTeleop().setGearsDropped(teleopGearsDelivered);

        byte[] teleopLowGoalDumps = cursor.getBlob(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS));

        data.getTeleop().getLowGoalDumps().addAll((ArrayList<FuelDumpAmount>) ThunderScout.deserializeObject(teleopLowGoalDumps));

        int teleopHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS));

        data.getTeleop().setHighGoals(teleopHighGoals);

        int teleopMissedHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS));

        data.getTeleop().setMissedHighGoals(teleopMissedHighGoals);

        String climbingStats = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_CLIMBING_STATS));

        data.getTeleop().setClimbingStats(ClimbingStats.valueOf(climbingStats));

        // Summary
        String troubleWith = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TROUBLE_WITH));

        data.setTroubleWith(troubleWith);

        String comments = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_COMMENTS));

        data.setComments(comments);

        publishProgress(data);
    }

    @Override
    protected void onProgressUpdate(ScoutData[] values) {
        //Runs on UI thread when publishProgress() is called
        viewAdapter.addScoutData(values[0]);

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution

        if (swipeLayout != null) {
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
        }

        super.onPostExecute(o);
    }

}
