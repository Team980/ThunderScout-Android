/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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

import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.backend.local.ScoutDataContract;
import com.team980.thunderscout.backend.local.ScoutDataDbHelper;

public class ScoutDataClearTask extends AsyncTask<Void, Void, Boolean> {

    @Nullable
    private StorageWrapper.StorageListener listener;
    private Context context;

    public ScoutDataClearTask(@Nullable StorageWrapper.StorageListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public Boolean doInBackground(Void... params) {

        SQLiteDatabase db = new ScoutDataDbHelper(context).getWritableDatabase();

        int rowsDeleted;

        try {
            rowsDeleted = db.delete(ScoutDataContract.ScoutDataTable.TABLE_NAME, null, null);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println(this.getClass().getName() + " " + rowsDeleted + " rows deleted from DB");

        db.close();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean wasSuccessful) {

        if (listener != null) {
            listener.onDataClear(wasSuccessful);
        }

        super.onPostExecute(wasSuccessful);
    }
}
