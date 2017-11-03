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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.backend.local.ScoutDataContract;
import com.team980.thunderscout.backend.local.ScoutDataDbHelper;
import com.team980.thunderscout.schema.ScoutData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoutDataRemoveTask extends AsyncTask<ScoutData, Void, List<ScoutData>> {

    @Nullable
    private StorageWrapper.StorageListener listener;
    private Context context;

    public ScoutDataRemoveTask(@Nullable StorageWrapper.StorageListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public List<ScoutData> doInBackground(ScoutData... dataToDelete) {

        SQLiteDatabase db = new ScoutDataDbHelper(context).getWritableDatabase();

        List<ScoutData> dataRemoved = new ArrayList<>();

        StringBuilder where = new StringBuilder(ScoutDataContract.ScoutDataTable._ID + " IN (");
        for (ScoutData data : dataToDelete) {
            where.append(data.getId()).append(",");
        }
        where.setLength(where.length() - 1); //no more comma
        where.append(")");

        int rowsDeleted;

        try {
            rowsDeleted = db.delete(ScoutDataContract.ScoutDataTable.TABLE_NAME, where.toString(), null);
        } catch (SQLiteException e) {
            Crashlytics.logException(e);
            return dataRemoved;
        }

        if (rowsDeleted > 0) {
            dataRemoved.addAll(Arrays.asList(dataToDelete));
        }

        db.close();
        return dataRemoved;
    }

    @Override
    protected void onPostExecute(List<ScoutData> dataRemoved) {
        //Runs on UI thread after execution

        if (listener != null) {
            listener.onDataRemove(dataRemoved);
        }

        super.onPostExecute(dataRemoved);
    }

}
