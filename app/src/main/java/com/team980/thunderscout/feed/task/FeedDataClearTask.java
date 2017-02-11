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
