package com.team980.thunderscout.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.task.DatabaseWriteTask;
import com.team980.thunderscout.util.TSNotificationManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerConnectionTask extends AsyncTask<Void, Integer, ScoutData> {

    private final BluetoothSocket mmSocket;

    private Context context;

    private TSNotificationManager notificationManager;

    public ServerConnectionTask(BluetoothSocket socket, Context context) {
        mmSocket = socket;

        this.context = context;

        notificationManager = TSNotificationManager.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected ScoutData doInBackground(Void[] params) {
        int notificationId = notificationManager.showBtTransferInProgress(mmSocket.getRemoteDevice().getName());

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

        notificationManager.showBtTransferSuccessful(mmSocket.getRemoteDevice().getName(),
                notificationId);
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
            if (prefs.getString("pref_serverStorageTask", "SAVE").equals("SAVE")) { //TODO modularize the saving mechanism
                //Put the fetched ScoutData in the local database
                DatabaseWriteTask writeTask = new DatabaseWriteTask(o, context);
                writeTask.execute();
            } else if (prefs.getString("pref_serverStorageTask", "SAVE").equals("SEND_SHEETS")) {
                //TODO sync with Google Sheets
            }
        } else {
            Log.d("ServerConnectionTask", "Failed to start DatabaseWriteTask!");
        }
    }

}
