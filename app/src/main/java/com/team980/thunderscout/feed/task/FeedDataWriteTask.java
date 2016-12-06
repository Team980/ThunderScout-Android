package com.team980.thunderscout.feed.task;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
            e.printStackTrace();
        }

        db.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution
        super.onPostExecute(o);

        Intent intent = new Intent(HomeFragment.ACTION_REFRESH_VIEW_PAGER);
        Log.d("PINGTEST", "intent");
        localBroadcastManager.sendBroadcast(intent); //notify the UI thread so we can refresh the ViewPager automatically :D
    }
}
