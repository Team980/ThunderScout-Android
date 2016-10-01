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
import com.google.api.services.sheets.v4.model.ValueRange;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.Defense;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SheetsUpdateTask extends AsyncTask<ScoutData, Void, Void> {

    private Context context;

    private Sheets sheetsService = null;

    private static final String[] ACCOUNT_SCOPES = {SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE};

    private String spreadsheetId;

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

        try {

            for (ScoutData data : dataList) { //loop for each data object... there should only be 1 but who cares

                String range = data.getTeamNumber() + "!A1"; //SheetName!A1 - A1 notation...

                ValueRange content = new ValueRange();
                content.setMajorDimension("COLUMNS");
                content.setRange(range); //this feels redundant

                ArrayList<Object> columnData = new ArrayList<>(); //stores data of a column

                initColumnData(columnData, data);

                ArrayList<List<Object>> wrappedData = new ArrayList<>(); //stores columns

                wrappedData.add(columnData);

                content.setValues(wrappedData);

                sheetsService.spreadsheets().values().append(spreadsheetId, range, content)
                        //.setValueInputOption("RAW").setInsertDataOption("INSERT_ROWS") //you can never be too careful with flags
                        .execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initColumnData(ArrayList<Object> columnDataList, ScoutData data) {

        //columnDataList.add(data.getDateAdded()); //TODO format date correctly

        columnDataList.add("Autonomous");

        columnDataList.add("Defense Crossed: " + data.getAutoDefenseCrossed());

        columnDataList.add("High Goals: " + data.getAutoHighGoals());
        columnDataList.add("Low Goals: " + data.getAutoLowGoals());
        columnDataList.add("Missed: " + data.getAutoMissedGoals());

        columnDataList.add("Teleop");

        for (Defense defense : Defense.values()) {
            if (defense == Defense.NONE) {
                continue;
            }

            int count = data.getTeleopDefenseCrossings().get(defense);

            columnDataList.add(defense.name() + ": " + count);
        }

        columnDataList.add("High Goals: " + data.getTeleopHighGoals());
        columnDataList.add("Low Goals: " + data.getTeleopLowGoals());
        columnDataList.add("Missed: " + data.getTeleopMissedGoals());

        columnDataList.add("Summary");

        columnDataList.add("Scaling Stats: " + data.getScalingStats().name());
        columnDataList.add("Challenged Tower: " + data.hasChallengedTower());

        columnDataList.add("Trouble With");
        columnDataList.add(data.getTroubleWith());
        columnDataList.add("Comments");
        columnDataList.add(data.getComments());

    }
}
