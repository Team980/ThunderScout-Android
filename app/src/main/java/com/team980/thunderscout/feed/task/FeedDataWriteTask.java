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

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.feed.FeedDataContract;
import com.team980.thunderscout.feed.FeedDataDbHelper;
import com.team980.thunderscout.feed.FeedEntry;
import com.team980.thunderscout.feed.HomeFragment;

public class FeedDataWriteTask extends AsyncTask<Void, Integer, Void> {

    private final FeedEntry entry;
    private Context context;

    private LocalBroadcastManager localBroadcastManager;

    public FeedDataWriteTask(FeedEntry entry, Context context) {
        this.entry = entry;

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

        // Gets the data repository in write mode
        SQLiteDatabase db = new FeedDataDbHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(FeedDataContract.FeedDataTable.COLUMN_NAME_ENTRY_TYPE, entry.getType().name());
        values.put(FeedDataContract.FeedDataTable.COLUMN_NAME_ENTRY_DATE, entry.getTimestamp().getTime());

        byte[] operations = ThunderScout.serializeObject(entry.getChildItemList());
        values.put(FeedDataContract.FeedDataTable.COLUMN_NAME_ENTRY_OPERATIONS, operations);

        try {
            // Insert the new row
            db.insertOrThrow(
                    FeedDataContract.FeedDataTable.TABLE_NAME,
                    null,
                    values);
        } catch (final Exception e) {
            FirebaseCrash.report(e);
        }

        db.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution
        super.onPostExecute(o);

        Intent intent = new Intent(HomeFragment.ACTION_REFRESH_VIEW_PAGER);
        localBroadcastManager.sendBroadcast(intent); //notify the UI thread so we can refresh the ViewPager automatically :D
    }
}
