package com.team980.thunderscout.task;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team980.thunderscout.data.ScoutData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerConnectionTask extends AsyncTask<Void, Integer, ScoutData> {

    private final BluetoothSocket mmSocket;

    private Context context;

    public ServerConnectionTask(BluetoothSocket socket, Context context) {
        mmSocket = socket;

        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //Runs on UI thread before execution
        super.onPreExecute();
    }

    @Override
    protected ScoutData doInBackground(Void[] params) {

        ObjectInputStream fromScoutStream;
        ObjectOutputStream toScoutStream;

        try {
            toScoutStream = new ObjectOutputStream(mmSocket.getOutputStream());
            toScoutStream.flush();
            fromScoutStream = new ObjectInputStream(mmSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //TODO version check
        ScoutData data = null;
        try {
            data = (ScoutData) fromScoutStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        try {
            fromScoutStream.close();
            toScoutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            //Put the fetched ScoutData in the local database
            DatabaseWriteTask writeTask = new DatabaseWriteTask(o, context);
            writeTask.execute();
        } else {
            Log.d("ServerConnectionTask", "Failed to start DatabaseWriteTask!");
        }
    }

}
