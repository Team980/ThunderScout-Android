package com.team980.thunderscout.data.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.ScoutDataContract.ScoutDataTable;
import com.team980.thunderscout.data.ScoutDataDbHelper;
import com.team980.thunderscout.data.enumeration.AllianceColor;
import com.team980.thunderscout.data.enumeration.ClimbingStats;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;
import com.team980.thunderscout.info.LocalDataAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

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
                ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT,
                ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS,
                ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS,
                ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE,

                ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED,
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
            e.printStackTrace();
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

        data.setTeamNumber(teamNumber);

        int matchNumber = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_MATCH_NUMBER));

        data.setMatchNumber(matchNumber);

        String allianceColor = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_ALLIANCE_COLOR));

        data.setAllianceColor(AllianceColor.valueOf(allianceColor));

        String dateAdded = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DATE_ADDED));

        try {
            data.setDateAdded(Long.valueOf(dateAdded));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String dataSource = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DATA_SOURCE));

        data.setDataSource(dataSource);

        // Auto
        int autoGearsDelivered = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DELIVERED));

        data.setAutoGearsDelivered(autoGearsDelivered);

        String autoLowGoalDumpAmount = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT));

        data.setAutoLowGoalDumpAmount(FuelDumpAmount.valueOf(autoLowGoalDumpAmount));

        int autoHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS));

        data.setAutoHighGoals(autoHighGoals);

        int autoMissedHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS));

        data.setAutoMissedHighGoals(autoMissedHighGoals);

        int crossedBaseline = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE));

        data.setCrossedBaseline(crossedBaseline != 0); //I2B conversion

        // Teleop
        int teleopGearsDelivered = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED));

        data.setTeleopGearsDelivered(teleopGearsDelivered);

        byte[] teleopLowGoalDumps = cursor.getBlob(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS));

        data.getTeleopLowGoalDumps().addAll((ArrayList<FuelDumpAmount>) ThunderScout.deserializeObject(teleopLowGoalDumps));

        int teleopHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS));

        data.setTeleopHighGoals(teleopHighGoals);

        int teleopMissedHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS));

        data.setTeleopMissedHighGoals(teleopMissedHighGoals);

        String climbingStats = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_CLIMBING_STATS));

        data.setClimbingStats(ClimbingStats.valueOf(climbingStats));

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
