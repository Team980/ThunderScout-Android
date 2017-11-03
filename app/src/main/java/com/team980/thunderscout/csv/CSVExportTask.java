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

package com.team980.thunderscout.csv;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.opencsv.CSVWriter;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.backend.local.ScoutDataContract;
import com.team980.thunderscout.backend.local.ScoutDataDbHelper;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.AllianceStation;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;
import com.team980.thunderscout.schema.enumeration.FuelDumpAmount;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVExportTask extends AsyncTask<Void, String, File> {

    private Activity activity;

    private ExportActivity.ExportAction action;

    public CSVExportTask(Activity activity, ExportActivity.ExportAction action) {
        this.activity = activity;
        this.action = action;
    }

    @Override
    public File doInBackground(Void... params) {

        SQLiteDatabase db = new ScoutDataDbHelper(activity).getWritableDatabase(); //TODO use the DatabaseReadTask internally - shouldn't be that hard

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_TEAM_NUMBER,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_MATCH_NUMBER,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_ALLIANCE_STATION,

                ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATE_ADDED,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATA_SOURCE,

                ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DELIVERED,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DROPPED,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE,

                ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DROPPED,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_CLIMBING_STATS,

                ScoutDataContract.ScoutDataTable.COLUMN_NAME_TROUBLE_WITH,
                ScoutDataContract.ScoutDataTable.COLUMN_NAME_COMMENTS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ScoutDataContract.ScoutDataTable._ID + " DESC";

        Cursor cursor;

        try {
            cursor = db.query(
                    ScoutDataContract.ScoutDataTable.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        } catch (SQLiteException e) {
            Crashlytics.logException(e);
            return null;
        }

        CSVWriter writer;

        File dir = new File(Environment.getExternalStorageDirectory(), "ThunderScout");
        dir.mkdir();
        dir.setReadable(true, false);

        String deviceName = Settings.Secure.getString(activity.getContentResolver(), "bluetooth_name").replace(' ', '_');
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        File csv = new File(dir, deviceName + "_exported_" + formatter.format(System.currentTimeMillis()) + ".csv");
        csv.setReadable(true, false);

        try {
            writer = new CSVWriter(new FileWriter(csv), ',');
        } catch (IOException e) {
            Crashlytics.logException(e);
            return null;
        }

        writer.writeNext(projection); //This adds headers to the file! ;)

        if (cursor.moveToFirst()) {
            addDataToFile(writer, cursor);
        }

        while (cursor.moveToNext()) {
            addDataToFile(writer, cursor);
        }

        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Crashlytics.logException(e);
            //ignore
        }

        publishProgress(csv.getAbsolutePath());

        cursor.close();
        db.close();
        return csv;
    }

    private void addDataToFile(CSVWriter writer, Cursor cursor) {
        ScoutData data = initScoutData(cursor);
        writer.writeNext(data.toStringArray());
    }

    private ScoutData initScoutData(Cursor cursor) {
        ScoutData data = new ScoutData();

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

        // Auto
        int autoGearsDelivered = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DELIVERED));

        data.getAutonomous().setGearsDelivered(autoGearsDelivered);

        int autoGearsDropped = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DROPPED));

        data.getAutonomous().setGearsDropped(autoGearsDropped);

        String autoLowGoalDumpAmount = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT));

        data.getAutonomous().setLowGoalDumpAmount(FuelDumpAmount.valueOf(autoLowGoalDumpAmount));

        int autoHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS));

        data.getAutonomous().setHighGoals(autoHighGoals);

        int autoMissedHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS));

        data.getAutonomous().setMissedHighGoals(autoMissedHighGoals);

        int crossedBaseline = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE));

        data.getAutonomous().setCrossedBaseline(crossedBaseline != 0); //I2B conversion

        // Teleop
        int teleopGearsDelivered = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED));

        data.getTeleop().setGearsDelivered(teleopGearsDelivered);

        int teleopGearsDropped = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DROPPED));

        data.getTeleop().setGearsDropped(teleopGearsDropped);

        byte[] teleopLowGoalDumps = cursor.getBlob(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS));

        data.getTeleop().getLowGoalDumps().addAll((ArrayList<FuelDumpAmount>) ThunderScout.deserializeObject(teleopLowGoalDumps));

        int teleopHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS));

        data.getTeleop().setHighGoals(teleopHighGoals);

        int teleopMissedHighGoals = cursor.getInt(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS));

        data.getTeleop().setMissedHighGoals(teleopMissedHighGoals);

        String climbingStats = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_CLIMBING_STATS));

        data.getTeleop().setClimbingStats(ClimbingStats.valueOf(climbingStats));

        // Summary
        String troubleWith = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_TROUBLE_WITH));

        data.setTroubleWith(troubleWith);

        String comments = cursor.getString(
                cursor.getColumnIndexOrThrow(ScoutDataContract.ScoutDataTable.COLUMN_NAME_COMMENTS));

        data.setComments(comments);

        return data;
    }

    @Override
    protected void onProgressUpdate(String[] values) {
        //Runs on UI thread when publishProgress() is called
        Crashlytics.log(Log.INFO, this.getClass().getName(), "CSV export complete: " + values[0]);
        Toast.makeText(activity, "CSV export complete: " + values[0], Toast.LENGTH_SHORT).show();

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(File file) {
        //Runs on UI thread after execution

        if (action == ExportActivity.ExportAction.OPEN_FILE) {
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".CSV");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(activity, "com.team980.thunderscout.provider", file), mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intent, 0);
            } else {
                Toast.makeText(activity, "No activities found to handle request", Toast.LENGTH_LONG).show();
                Crashlytics.log(Log.INFO, this.getClass().getName(), "No activities found to handle request");
            }

        } else if (action == ExportActivity.ExportAction.SHARE_TO_SYSTEM) {
            //String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".CSV");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain"); //This is needed to force Bluetooth to show in the list
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(activity, "com.team980.thunderscout.provider", file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(Intent.createChooser(intent, "Share exported data using"));
            } else {
                Toast.makeText(activity, "No activities found to handle request", Toast.LENGTH_LONG).show();
                Crashlytics.log(Log.INFO, this.getClass().getName(), "No activities found to handle request");
            }
        }

        super.onPostExecute(file);
    }

}
