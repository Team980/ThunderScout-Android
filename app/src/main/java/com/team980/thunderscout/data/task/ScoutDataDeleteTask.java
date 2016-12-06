package com.team980.thunderscout.data.task;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.ScoutDataContract.ScoutDataTable;
import com.team980.thunderscout.data.ScoutDataDbHelper;
import com.team980.thunderscout.info.LocalDataAdapter;
import com.team980.thunderscout.info.ThisDeviceFragment;

import java.util.List;

public class ScoutDataDeleteTask extends AsyncTask<Void, Integer, Void> {

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
            where.append(data.getDateAdded()).append(",");
        }
        where.setLength(where.length() - 1); //no more comma
        where.append(")");

        int rowsDeleted;

        try {
            rowsDeleted = db.delete(ScoutDataTable.TABLE_NAME, where.toString(), null);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }

        publishProgress(rowsDeleted);

        db.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        //Runs on UI thread when publishProgress() is called
        Toast.makeText(context, values[0] + " rows deleted from DB", Toast.LENGTH_LONG).show();

        super.onProgressUpdate(values);
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
