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

package com.team980.thunderscout.iexport.task;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.opencsv.CSVWriter;
import com.team980.thunderscout.R;
import com.team980.thunderscout.iexport.ExportActivity;
import com.team980.thunderscout.schema.ScoutData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class CSVExportTask extends AsyncTask<ScoutData, Integer, File> {

    private ExportActivity activity;

    public CSVExportTask(ExportActivity activity) {
        this.activity = activity;
    }

    @Override
    public File doInBackground(ScoutData... params) {

        CSVWriter writer;

        File dir = new File(Environment.getExternalStorageDirectory(), "ThunderScout");
        dir.mkdir();
        dir.setReadable(true, false);

        String deviceName = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(activity.getResources().getString(R.string.pref_device_name), Build.MANUFACTURER + " " + Build.MODEL);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        File csv = new File(dir, deviceName + "_exported_" + formatter.format(System.currentTimeMillis()) + ".csv");
        csv.setReadable(true, false);

        try {
            writer = new CSVWriter(new FileWriter(csv));
        } catch (IOException e) {
            Crashlytics.logException(e);
            return null;
        }

        int i = 0;
        for (ScoutData data : params) {
            writer.writeNext(data.toStringArray());
            publishProgress(i++);
        }

        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Crashlytics.logException(e);
            //ignore
        }

        return csv;
    }

    @Override
    protected void onProgressUpdate(Integer... matchesWritten) {
        super.onProgressUpdate(matchesWritten);

        activity.onExportProgressUpdate(matchesWritten[0]);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);

        Crashlytics.log(Log.INFO, this.getClass().getName(), "CSV export complete: " + file.getAbsolutePath());
        Toast.makeText(activity, "CSV export complete: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        activity.onExportCompletion(file);
    }

}