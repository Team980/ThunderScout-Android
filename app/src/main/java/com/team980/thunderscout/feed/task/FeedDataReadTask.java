package com.team980.thunderscout.feed.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.feed.ActivityFeedAdapter;
import com.team980.thunderscout.feed.EntryOperationWrapper;
import com.team980.thunderscout.feed.FeedDataContract.FeedDataTable;
import com.team980.thunderscout.feed.FeedDataDbHelper;
import com.team980.thunderscout.feed.FeedEntry;

import java.util.List;

public class FeedDataReadTask extends AsyncTask<Void, FeedEntry, Void> {

    private ActivityFeedAdapter viewAdapter;
    private Context context;

    private SwipeRefreshLayout swipeLayout;

    public FeedDataReadTask(ActivityFeedAdapter adapter, Context context) {
        viewAdapter = adapter;
        this.context = context;
    }

    public FeedDataReadTask(ActivityFeedAdapter adapter, Context context, SwipeRefreshLayout refresh) {
        viewAdapter = adapter;
        this.context = context;

        swipeLayout = refresh;
    }

    @Override
    protected void onPreExecute() {
        viewAdapter.clearEntries();

        if (swipeLayout != null) {

            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(true);
                }
            });
        }

        super.onPreExecute();
    }

    @Override
    public Void doInBackground(Void... params) {

        SQLiteDatabase db = new FeedDataDbHelper(context).getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedDataTable._ID,
                FeedDataTable.COLUMN_NAME_ENTRY_TYPE,
                FeedDataTable.COLUMN_NAME_ENTRY_DATE,
                FeedDataTable.COLUMN_NAME_ENTRY_OPERATIONS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedDataTable._ID + " DESC";

        Cursor cursor;

        try {
            cursor = db.query(
                    FeedDataTable.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }

        if (cursor.moveToFirst()) {
            initFeedEntry(cursor);
        }

        while (cursor.moveToNext()) {
            initFeedEntry(cursor);
        }

        cursor.close();
        db.close();
        return null;
    }

    private void initFeedEntry(Cursor cursor) {
        FeedEntry.EntryType entryType = FeedEntry.EntryType.valueOf(cursor.getString(
                cursor.getColumnIndexOrThrow(FeedDataTable.COLUMN_NAME_ENTRY_TYPE)));

        long timestamp = cursor.getLong(
                cursor.getColumnIndexOrThrow(FeedDataTable.COLUMN_NAME_ENTRY_DATE));


        FeedEntry entry = new FeedEntry(entryType, timestamp);

        byte[] serializedList = cursor.getBlob(
                cursor.getColumnIndexOrThrow(FeedDataTable.COLUMN_NAME_ENTRY_OPERATIONS));

        List<EntryOperationWrapper> operations = (List<EntryOperationWrapper>) ThunderScout.deserializeObject(serializedList);

        for (EntryOperationWrapper operation : operations) {
            entry.addOperation(operation);
        }

        publishProgress(entry);
    }

    @Override
    protected void onProgressUpdate(FeedEntry[] values) {
        //Runs on UI thread when publishProgress() is called
        viewAdapter.addFeedEntry(values[0]);

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void o) {
        //Runs on UI thread after execution

        if (swipeLayout != null) {
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
        }

        super.onPostExecute(o);
    }

}
