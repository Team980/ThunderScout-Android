package com.team980.thunderscout.integrations.sheets.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class SheetsCreateTask extends AsyncTask<Void, Void, String> {

    private Context context;

    private Sheets sheetsService = null;

    private static final String[] ACCOUNT_SCOPES = {SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE};

    public SheetsCreateTask(Context context) {
        this.context = context;

        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(context);

        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(ACCOUNT_SCOPES))
                .setBackOff(new ExponentialBackOff());

        String accountName = settings.getString("google_account_name", null);

        credential.setSelectedAccountName(accountName);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        sheetsService = new Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("ThunderScout")
                .build();
    }

    @Override
    protected String doInBackground(Void... voids) {
        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setProperties(new SpreadsheetProperties().setTitle("ThunderScout Data: " + SimpleDateFormat.getDateInstance().format(System.currentTimeMillis())));
        spreadsheet.setSheets(null);
        try {
            spreadsheet = sheetsService.spreadsheets().create(spreadsheet).execute();

        } catch (UserRecoverableAuthIOException e) {
            ((Activity) context).startActivityForResult(e.getIntent(), 1001); //TODO This REALLY should be called somewhere else!
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return spreadsheet.getSpreadsheetId();
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        preferences.edit().putString("linked_spreadsheet_id", result).apply();

        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
