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
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.opencsv.CSVReader;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.task.ScoutDataWriteTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CSVImportTask extends AsyncTask<Void, ScoutData, Void> {

    private Activity activity;

    private Uri fileUri;

    public CSVImportTask(Activity activity, Uri uri) {
        this.activity = activity;
        fileUri = uri;
    }

    @Override
    public Void doInBackground(Void... params) {

        CSVReader reader;
        try {
            reader = new CSVReader(new InputStreamReader(activity.getContentResolver().openInputStream(fileUri)));
        } catch (FileNotFoundException e) {
            FirebaseCrash.report(e);
            return null;
        }

        List<String[]> rows;
        try {
            rows = reader.readAll();
        } catch (IOException e) {
            FirebaseCrash.report(e);
            return null;
        }

        for (String[] row : rows) {

            if(!ThunderScout.isInteger(row[0])) {
                continue;
            }

            publishProgress(ScoutData.fromStringArray(row));
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(ScoutData[] values) {
        //Runs on UI thread when publishProgress() is called

        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), "Posting ScoutData from CSV " + fileUri.getLastPathSegment() + " to database");

        ScoutDataWriteTask writeTask = new ScoutDataWriteTask(values[0], activity);
        writeTask.execute();

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void v) {
        //Runs on UI thread after execution
        super.onPostExecute(v);

        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), "CSV import complete");
        Toast.makeText(activity, "CSV import complete", Toast.LENGTH_SHORT).show();
        activity.finish();
    }

}