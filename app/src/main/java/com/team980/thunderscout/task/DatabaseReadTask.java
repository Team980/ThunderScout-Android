package com.team980.thunderscout.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.team980.thunderscout.adapter.DataViewAdapter;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.ServerDataContract.ScoutDataTable;
import com.team980.thunderscout.data.ServerDataDbHelper;

/**
 * TODO Rewrite this class
 */
@Deprecated()
public class DatabaseReadTask extends AsyncTask<Void, ScoutData, Void> {

    private DataViewAdapter viewAdapter;
    private Context context;

    private SwipeRefreshLayout swipeLayout;

    public DatabaseReadTask(DataViewAdapter adapter, Context context) {
        viewAdapter = adapter;
        this.context = context;
    }

    public DatabaseReadTask(DataViewAdapter adapter, Context context, SwipeRefreshLayout refresh) {
        viewAdapter = adapter;
        this.context = context;

        swipeLayout = refresh;
    }

    @Override
    protected void onPreExecute() {
        //viewAdapter.clearData();

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

        SQLiteDatabase db = new ServerDataDbHelper(context).getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ScoutDataTable._ID,
                ScoutDataTable.COLUMN_NAME_TEAM_NUMBER,
                ScoutDataTable.COLUMN_NAME_DATE_ADDED
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ScoutDataTable._ID + " DESC";

        Cursor cursor;

        try {
            cursor = db.query(
                    ScoutDataTable.TABLE_NAME,  // The table to query
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
            ScoutData data = new ScoutData();

            String teamNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TEAM_NUMBER));

            data.setTeamNumber(teamNumber);

            String dateAdded = cursor.getString(
                    cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DATE_ADDED));

            try {
                data.setDateAdded(Long.valueOf(dateAdded));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //TODO do the other fields

            publishProgress(data);
        }

        while (cursor.moveToNext()) {
            ScoutData data = new ScoutData();

            String teamNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_TEAM_NUMBER));

            data.setTeamNumber(teamNumber);

            String dateAdded = cursor.getString(
                    cursor.getColumnIndexOrThrow(ScoutDataTable.COLUMN_NAME_DATE_ADDED));

            try {
                data.setDateAdded(Long.valueOf(dateAdded));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //TODO do the other fields

            publishProgress(data);
        }

        cursor.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(ScoutData[] values) {
        //Runs on UI thread when publishProgress() is called
        viewAdapter.addScoutData(values[0]);

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
