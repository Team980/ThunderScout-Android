package com.team980.thunderscout.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.task.ScoutDataWriteTask;
import com.team980.thunderscout.feed.EntryOperationWrapper;
import com.team980.thunderscout.feed.EntryOperationWrapper.EntryOperationStatus;
import com.team980.thunderscout.feed.EntryOperationWrapper.EntryOperationType;
import com.team980.thunderscout.feed.FeedEntry;
import com.team980.thunderscout.feed.task.FeedDataWriteTask;
import com.team980.thunderscout.util.TSNotificationBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerConnectionTask extends AsyncTask<Void, Integer, ScoutData> {

    private final BluetoothSocket mmSocket;

    private Context context;

    private TSNotificationBuilder notificationManager;

    public ServerConnectionTask(BluetoothSocket socket, Context context) {
        mmSocket = socket;

        this.context = context;

        notificationManager = TSNotificationBuilder.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected ScoutData doInBackground(Void[] params) {
        int notificationId = notificationManager.showBtTransferInProgress(mmSocket.getRemoteDevice().getName(), true);

        ObjectInputStream fromScoutStream;
        ObjectOutputStream toScoutStream;

        try {
            toScoutStream = new ObjectOutputStream(mmSocket.getOutputStream());
            toScoutStream.flush();
            fromScoutStream = new ObjectInputStream(mmSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            notificationManager.showBtTransferError(mmSocket.getRemoteDevice().getName(),
                    notificationId);
            return null;
        }

        //TODO version check
        ScoutData data = null;
        try {
            data = (ScoutData) fromScoutStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            notificationManager.showBtTransferError(mmSocket.getRemoteDevice().getName(),
                    notificationId);
            return null;
        }

        try {
            fromScoutStream.close();
            toScoutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        notificationManager.showBtTransferFinished(notificationId);
        return data;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        //Runs on UI thread when publishProgress() is called
        super.onProgressUpdate(values);

        Toast.makeText(context, "Inserted into DB: Row " + values[0], Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(ScoutData o) {
        super.onPostExecute(o);

        if (o != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            FeedEntry feedEntry = new FeedEntry(FeedEntry.EntryType.SERVER_RECEIVED_MATCH, System.currentTimeMillis());

            if (prefs.getBoolean("bt_send_to_local_storage", true)) {
                //Put the fetched ScoutData in the local database
                ScoutDataWriteTask writeTask = new ScoutDataWriteTask(o, context);
                writeTask.execute();

                feedEntry.addOperation(new EntryOperationWrapper(EntryOperationType.SAVED_TO_LOCAL_STORAGE,
                        EntryOperationStatus.OPERATION_SUCCESSFUL)); //TODO determine this based on callback?
            }

            if (prefs.getBoolean("bt_send_to_bt_server", false)) {
                //TODO is this a really good idea??
            }

            /*if (prefs.getBoolean("bt_send_to_linked_sheet", false)) {
                SheetsUpdateTask task = new SheetsUpdateTask(context);
                task.execute(o);
            }*/

            FeedDataWriteTask feedDataWriteTask = new FeedDataWriteTask(feedEntry, context);
            feedDataWriteTask.execute();
        } else {
            Log.d("ServerConnectionTask", "Failed to start FeedDataWriteTask!");
        }
    }

}
