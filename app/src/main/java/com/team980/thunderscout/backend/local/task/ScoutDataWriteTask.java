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

package com.team980.thunderscout.backend.local.task;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.backend.local.ScoutDataContract;
import com.team980.thunderscout.backend.local.ScoutDataDbHelper;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.legacy.info.ThisDeviceFragment;
import com.team980.thunderscout.scouting_flow.ScoutingFlowActivity;

@Deprecated
public class ScoutDataWriteTask extends AsyncTask<Void, Integer, Void> {

    private final ScoutData data;
    private Context context;

    private LocalBroadcastManager localBroadcastManager;

    private ScoutingFlowActivity activity;

    public ScoutDataWriteTask(ScoutData data, Context context) {
        this.data = data;

        this.context = context;

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public ScoutDataWriteTask(ScoutData data, Context context, ScoutingFlowActivity activity) {
        this(data, context);

        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void[] params) {

        // Gets the data repository in write mode
        SQLiteDatabase db = new ScoutDataDbHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Init
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TEAM_NUMBER, data.getTeam());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_MATCH_NUMBER, data.getMatch());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_ALLIANCE_COLOR, data.getAlliance().name());

        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATE_ADDED, ThunderScout.serializeObject(data.getDate()));
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATA_SOURCE, data.getSource());

        // Auto
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DELIVERED, data.getAutonomous().getGearsDelivered());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DROPPED, data.getAutonomous().getGearsDropped());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT, data.getAutonomous().getLowGoalDumpAmount().name());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS, data.getAutonomous().getHighGoals());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS, data.getAutonomous().getMissedHighGoals());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE, data.getAutonomous().getCrossedBaseline());

        // Teleop
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED, data.getTeleop().getGearsDelivered());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DROPPED, data.getTeleop().getGearsDropped());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS, ThunderScout.serializeObject(data.getTeleop().getLowGoalDumps()));
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS, data.getTeleop().getHighGoals());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS, data.getTeleop().getMissedHighGoals());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_CLIMBING_STATS, data.getTeleop().getClimbingStats().name());

        // Summary
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TROUBLE_WITH, data.getTroubleWith());
        values.put(ScoutDataContract.ScoutDataTable.COLUMN_NAME_COMMENTS, data.getComments());

        try {
            // Insert the new row
            db.insertOrThrow(
                    ScoutDataContract.ScoutDataTable.TABLE_NAME,
                    null,
                    values);
        } catch (final Exception e) {
            FirebaseCrash.report(e);
            if (activity != null) {
                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {

                    @Override
                    public void run() {  //TODO broadcast reciever
                        activity.dataOutputCallbackFail(ScoutingFlowActivity.OPERATION_SAVE_THIS_DEVICE, e);
                    }
                });
            }
        }

        if (activity != null) {
            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {

                @Override
                public void run() {  //TODO broadcast reciever
                    activity.dataOutputCallbackSuccess(ScoutingFlowActivity.OPERATION_SAVE_THIS_DEVICE);
                }
            });
        }

        db.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution
        super.onPostExecute(o);

        Intent intent = new Intent(ThisDeviceFragment.ACTION_REFRESH_VIEW_PAGER);
        localBroadcastManager.sendBroadcast(intent); //notify the UI thread so we can refresh the ViewPager automatically :D
    }
}
