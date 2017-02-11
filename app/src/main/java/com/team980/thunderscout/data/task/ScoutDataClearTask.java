package com.team980.thunderscout.data.task;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.data.ScoutDataContract.ScoutDataTable;
import com.team980.thunderscout.data.ScoutDataDbHelper;
import com.team980.thunderscout.info.LocalDataAdapter;

public class ScoutDataClearTask extends AsyncTask<Void, Void, Void> {

    private LocalDataAdapter viewAdapter;
    private Context context;

    public ScoutDataClearTask(LocalDataAdapter adapter, Context context) {
        viewAdapter = adapter;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        viewAdapter.clearData();
    }

    @Override
    public Void doInBackground(Void... params) {

        SQLiteDatabase db = new ScoutDataDbHelper(context).getWritableDatabase();

        int rowsDeleted;

        try {
            rowsDeleted = db.delete(ScoutDataTable.TABLE_NAME, null, null);
        } catch (SQLiteException e) {
            FirebaseCrash.report(e);
            return null;
        }

        FirebaseCrash.logcat(Log.INFO, this.getClass().getName(), rowsDeleted + " rows deleted from DB");

        db.close();
        return null;
    }
}
