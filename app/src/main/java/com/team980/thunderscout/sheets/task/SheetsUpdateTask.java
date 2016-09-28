package com.team980.thunderscout.sheets.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.team980.thunderscout.data.ScoutData;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SheetsUpdateTask extends AsyncTask<ScoutData, Void, Void> {

    private Context context;

    private Sheets sheetsService = null;

    private static final String[] ACCOUNT_SCOPES = {SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE};

    String spreadsheetId;

    public SheetsUpdateTask(Context context) {
        this.context = context;

        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(ACCOUNT_SCOPES))
                .setBackOff(new ExponentialBackOff());

        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(context);

        credential.setSelectedAccountName(settings.getString("google_account_name", null));

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        sheetsService = new Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("ThunderScout")
                .build();
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        spreadsheetId = preferences.getString("linked_spreadsheet_id", null);
    }

    @Override
    protected Void doInBackground(ScoutData... dataList) {
        Spreadsheet spreadsheet;

        try {
            spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();

            List<Sheet> sheets = spreadsheet.getSheets();
            //sheets.get(0).getProperties().getTitle()
            //sheets.get(0).getData().get(0).getRowData().get(0).getValues().get(0).getUserEnteredValue().getStringValue()
            //TODO look at StudentSignup to see how we did it

            //todo insert ScoutData into proper workbook
            //if workbook is not created, make it and add default headers (left column)
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
