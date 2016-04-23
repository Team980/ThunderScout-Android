package com.team980.thunderscout.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.adapter.DataViewAdapter;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.ServerDataContract.ScoutDataTable;
import com.team980.thunderscout.data.ServerDataDbHelper;
import com.team980.thunderscout.data.enumeration.CrossingStats;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.enumeration.ScoringStats;
import com.team980.thunderscout.data.object.Rank;
import com.team980.thunderscout.data.object.RankedDefense;

import java.util.List;

/**
 * TODO Rewrite this class to remove redundancy, add sorting/filtering parameters
 */
@Deprecated()
public class DatabaseReadTask extends AsyncTask<Void, ScoutData, Void> {

    private DataViewAdapter viewAdapter;
    private Context context;

    private SwipeRefreshLayout swipeLayout;

    public DatabaseReadTask(DataViewAdapter adapter, Context context) {
        viewAdapter = adapter;
        this.context = context;
    }

    public DatabaseReadTask(DataViewAdapter adapter, Context context, SwipeRefreshLayout refresh) {
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

        SQLiteDatabase db = new ServerDataDbHelper(context).getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScoutDataTable._ID,
                ScoutDataTable.COLUMN_NAME_TEAM_NUMBER,
                ScoutDataTable.COLUMN_NAME_DATE_ADDED,
                ScoutDataTable.COLUMN_NAME_AUTO_CROSSING_STATS,
                ScoutDataTable.COLUMN_NAME_AUTO_DEFENSE_CROSSED,
                ScoutDataTable.COLUMN_NAME_AUTO_SCORING_STATS,
                ScoutDataTable.COLUMN_NAME_TELEOP_DEFENSES_BREACHED,
                ScoutDataTable.COLUMN_NAME_TELEOP_LIST_DEFENSES_BREACHED,
                ScoutDataTable.COLUMN_NAME_TELEOP_GOALS_SCORED,
                ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOALS,
                ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS,
                ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_RANK,
                ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOAL_RANK,
                ScoutDataTable.COLUMN_NAME_DRIVER_SKILL,
                ScoutDataTable.COLUMN_NAME_COMMENTS //TODO update with dataSource
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
        return null;
    }

    private void initScoutData(Cursor cursor) {
        ScoutData data = new ScoutData();

        String teamNumber = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TEAM_NUMBER));

        data.setTeamNumber(teamNumber);

        String dateAdded = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DATE_ADDED));

        try {
            data.setDateAdded(Long.valueOf(dateAdded));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        CrossingStats autoCrossingStats = CrossingStats.valueOf(cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_CROSSING_STATS)));

        data.setAutoCrossingStats(autoCrossingStats);

        Defense autoDefenseCrossed = Defense.valueOf(cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_DEFENSE_CROSSED)));

        data.setAutoDefenseCrossed(autoDefenseCrossed);

        ScoringStats autoScoringStats = ScoringStats.valueOf(cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_AUTO_SCORING_STATS)));

        data.setAutoScoringStats(autoScoringStats);

        float teleopDefensesBreached = cursor.getFloat(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_DEFENSES_BREACHED));

        data.setTeleopDefensesBreached(teleopDefensesBreached);

        byte[] serializedList = cursor.getBlob(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_LIST_DEFENSES_BREACHED));

        List<RankedDefense> teleopListDefensesBreached = (List<RankedDefense>) ThunderScout.deserializeObject(serializedList);

        data.setTeleopListDefensesBreached(teleopListDefensesBreached);

        float teleopGoalsScored = cursor.getFloat(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_GOALS_SCORED));

        data.setTeleopGoalsScored(teleopGoalsScored);

        int teleopLowGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOALS));

        data.setTeleopLowGoals(teleopLowGoals != 0); //This converts int to boolean surprisingly

        int teleopHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS));

        data.setTeleopHighGoals(teleopHighGoals != 0); //I2B conversion

        Rank teleopLowGoalRank = Rank.fromId(cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_RANK)));

        data.setTeleopLowGoalRank(teleopLowGoalRank);

        Rank teleopHighGoalRank = Rank.fromId(cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOAL_RANK)));

        data.setTeleopLowGoalRank(teleopHighGoalRank);

        int skill = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DRIVER_SKILL));

        data.setTeleopDriverSkill(Rank.fromId(skill));

        String comments = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_COMMENTS));

        data.setTeleopComments(comments);

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
