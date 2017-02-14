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

package com.team980.thunderscout.feed.task;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.feed.ActivityFeedAdapter;
import com.team980.thunderscout.feed.FeedDataContract.FeedDataTable;
import com.team980.thunderscout.feed.FeedDataDbHelper;

public class FeedDataClearTask extends AsyncTask<Void, Integer, Void> {

    private ActivityFeedAdapter viewAdapter;
    private Context context;

    public FeedDataClearTask(ActivityFeedAdapter adapter, Context context) {
        viewAdapter = adapter;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        viewAdapter.clearEntries();
    }

    @Override
    public Void doInBackground(Void... params) {

        SQLiteDatabase db = new FeedDataDbHelper(context).getWritableDatabase();

        int rowsDeleted;

        try {
            rowsDeleted = db.delete(FeedDataTable.TABLE_NAME, null, null);
        } catch (SQLiteException e) {
            FirebaseCrash.report(e);
            return null;
        }

        publishProgress(rowsDeleted);

        db.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        //Runs on UI thread when publishProgress() is called
        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), values[0] + " rows deleted from DB");

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution

        super.onPostExecute(o);
    }

}
