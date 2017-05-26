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

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.backend.local.ScoutDataContract;
import com.team980.thunderscout.backend.local.ScoutDataDbHelper;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.legacy.info.LocalDataAdapter;
import com.team980.thunderscout.legacy.info.ThisDeviceFragment;

import java.util.List;

@Deprecated
public class ScoutDataDeleteTask extends AsyncTask<Void, Void, Void> {

    private LocalDataAdapter viewAdapter;
    private Context context;

    private LocalBroadcastManager localBroadcastManager;

    private List<ScoutData> dataToDelete;


    public ScoutDataDeleteTask(LocalDataAdapter adapter, Context context, List<ScoutData> datas) {
        viewAdapter = adapter;
        this.context = context;

        dataToDelete = datas;

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        viewAdapter.clearSelections();
    }

    @Override
    public Void doInBackground(Void... params) {

        SQLiteDatabase db = new ScoutDataDbHelper(context).getWritableDatabase();

        StringBuilder where = new StringBuilder("date_added IN (");
        for (ScoutData data : dataToDelete) {
            where.append(data.getDate()).append(",");
        }
        where.setLength(where.length() - 1); //no more comma
        where.append(")");

        int rowsDeleted;

        try {
            rowsDeleted = db.delete(ScoutDataContract.ScoutDataTable.TABLE_NAME, where.toString(), null);
        } catch (SQLiteException e) {
            FirebaseCrash.report(e);
            return null;
        }

        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), rowsDeleted + " rows deleted from DB");

        db.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution
        viewAdapter.clearData();

        Intent intent = new Intent(ThisDeviceFragment.ACTION_REFRESH_VIEW_PAGER);
        localBroadcastManager.sendBroadcast(intent); //notify the UI thread so we can refresh the ViewPager automatically :D

        super.onPostExecute(o);
    }

}
