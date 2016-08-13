package com.team980.thunderscout.task;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import java.io.IOException;

public class SheetsSendTask extends AsyncTask<Void, Void, Void> {

    private Sheets sheetsService = null;

    public SheetsSendTask(GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        sheetsService = new Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("ThunderScout")
                .build();
    }

    @Override
    protected Void doInBackground(Void... voids) { //TODO discern proper data model
        Spreadsheet spreadsheet = new Spreadsheet();

        try {
            sheetsService.spreadsheets().create(spreadsheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
