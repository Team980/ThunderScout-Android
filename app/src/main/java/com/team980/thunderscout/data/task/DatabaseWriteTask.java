package com.team980.thunderscout.data.task;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.ServerDataContract;
import com.team980.thunderscout.data.ServerDataDbHelper;
import com.team980.thunderscout.data.enumeration.ScalingStats;
import com.team980.thunderscout.info.ThisDeviceFragment;

public class DatabaseWriteTask extends AsyncTask<Void, Integer, Void> {

    private final ScoutData data;
    private Context context;

    private LocalBroadcastManager localBroadcastManager;

    public DatabaseWriteTask(ScoutData data, Context context) {
        this.data = data;

        this.context = context;

        localBroadcastManager = LocalBroadcastManager.getInstance(context);

    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void[] params) {

        //Put data into Database! :)
        ServerDataDbHelper mDbHelper = new ServerDataDbHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Init
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TEAM_NUMBER, data.getTeamNumber());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_DATE_ADDED, data.getDateAdded());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_DATA_SOURCE, data.getDataSource());

        // Auto
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_AUTO_DEFENSE_CROSSED, data.getAutoDefenseCrossed().toString());

        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOALS, data.getAutoLowGoals());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS, data.getAutoHighGoals());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_AUTO_MISSED_GOALS, data.getAutoMissedGoals());

        // Teleop
        byte[] listDefenseCrossings = ThunderScout.serializeObject(data.getTeleopDefenseCrossings());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_DEFENSE_CROSSINGS, listDefenseCrossings);

        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOALS, data.getTeleopLowGoals());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS, data.getTeleopHighGoals());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_GOALS, data.getTeleopMissedGoals());

        // Summary
        ScalingStats scalingStats = data.getScalingStats();
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_SCALING_STATS, scalingStats.toString());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_CHALLENGED_TOWER, data.hasChallengedTower());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TROUBLE_WITH, data.getTroubleWith());
        values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_COMMENTS, data.getComments());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ServerDataContract.ScoutDataTable.TABLE_NAME,
                null,
                values);

        publishProgress((int) newRowId);

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        //Runs on UI thread when publishProgress() is called
        super.onProgressUpdate(values);

        Toast.makeText(context, "Inserted into DB: Row " + values[0], Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution
        super.onPostExecute(o);

        Intent intent = new Intent(ThisDeviceFragment.ACTION_REFRESH_VIEW_PAGER);
        localBroadcastManager.sendBroadcast(intent); //notify the UI thread so we can refresh the ViewPager automatically :D
    }
}
