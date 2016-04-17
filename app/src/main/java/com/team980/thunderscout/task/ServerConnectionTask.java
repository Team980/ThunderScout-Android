package com.team980.thunderscout.task;

import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.ServerDataContract;
import com.team980.thunderscout.data.ServerDataDbHelper;
import com.team980.thunderscout.data.object.CrossingStats;
import com.team980.thunderscout.data.object.Defense;
import com.team980.thunderscout.data.object.RankedDefense;
import com.team980.thunderscout.data.object.ScoringStats;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerConnectionTask extends AsyncTask<Void, Integer, Void> {

    private final BluetoothSocket mmSocket;

    private Context context;

    private ScoutData data;

    public ServerConnectionTask(BluetoothSocket socket, Context context) {
        mmSocket = socket;

        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void[] params) {

        ObjectInputStream fromScoutStream;
        ObjectOutputStream toScoutStream;

        try {
            toScoutStream = new ObjectOutputStream(mmSocket.getOutputStream());
            toScoutStream.flush();
            fromScoutStream = new ObjectInputStream(mmSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (fromScoutStream != null) {

            //TODO version check

            ScoutData data = null;
            try {
                data = (ScoutData) fromScoutStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            try {
                fromScoutStream.close();
                toScoutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Put data into Database! :) TODO just call DatabaseWriteTask for consistency
            ServerDataDbHelper mDbHelper = new ServerDataDbHelper(context);

            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TEAM_NUMBER, data.getTeamNumber());

            long date = data.getDateAdded();
            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_DATE_ADDED, date);

            CrossingStats cs = data.getAutoCrossingStats();
            if (cs != null) {
                values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_AUTO_CROSSING_STATS, data.getAutoCrossingStats().toString());
            }

            Defense d = data.getAutoDefenseCrossed();
            if (d != null) {
                values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_AUTO_DEFENSE_CROSSED, data.getAutoDefenseCrossed().toString());
            }

            ScoringStats ss = data.getAutoScoringStats();
            if (ss != null) {
                values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_AUTO_SCORING_STATS, data.getAutoScoringStats().toString());
            }

            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_DEFENSES_BREACHED, data.getTeleopDefensesBreached());

            StringBuilder sb = new StringBuilder();
            for (RankedDefense de : data.getTeleopListDefensesBreached()) {
                sb.append(de.toString());
                sb.append("~"); //WOOT WOOT TILDE SEPARATOR TODO serialize
            }

            // Readable version
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }

            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LIST_DEFENSES_BREACHED, sb.toString()); //TODO sliders
            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GOALS_SCORED, data.getTeleopGoalsScored());
            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOALS, data.getTeleopLowGoals());
            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS, data.getTeleopHighGoals());
            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_DRIVER_SKILL, data.getTeleopDriverSkill().getId());
            values.put(ServerDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_COMMENTS, data.getTeleopComments());

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    ServerDataContract.ScoutDataTable.TABLE_NAME,
                    null,
                    values);

            publishProgress((int) newRowId);
        }

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
    }

}
