/*package com.team980.thunderscout.integrations.sheets.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.team980.thunderscout.data.ScoutData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SheetsUpdateTask extends AsyncTask<ScoutData, Void, AppendValuesResponse> {

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
    protected AppendValuesResponse doInBackground(ScoutData... dataList) {
        try {
            ScoutData data = dataList[0];

            Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();

            boolean needsInit = true;
            for (Sheet sheet : spreadsheet.getSheets()) {
                if (sheet.getProperties().getTitle().equalsIgnoreCase(data.getTeamNumber())) {
                    needsInit = false;
                }
            }

            String range = data.getTeamNumber(); //SheetName!A1:B2 - A1 notation...

            if (needsInit) {
                AddSheetRequest addSheetRequest = new AddSheetRequest();
                addSheetRequest.setProperties(new SheetProperties().setTitle(data.getTeamNumber()));

                Request request =  new Request();
                request.setAddSheet(addSheetRequest);

                ArrayList<Request> requests = new ArrayList<>();
                requests.add(request);

                sheetsService.spreadsheets().batchUpdate(spreadsheetId, new BatchUpdateSpreadsheetRequest().setRequests(requests)).execute();

                ValueRange init = new ValueRange();
                init.setMajorDimension("ROWS");
                init.setRange(range);

                List<List<Object>> initWrapper = new ArrayList<>();
                initWrapper.add(initHeaderData());

                init.setValues(initWrapper);

                sheetsService.spreadsheets().values().append(spreadsheetId, range, init)
                        .setValueInputOption("RAW") //TODO determine proper value
                        .setInsertDataOption("INSERT_ROWS")
                        .execute();
            }

            ValueRange content = new ValueRange();
            content.setMajorDimension("ROWS");
            content.setRange(range);

            ArrayList<Object> columnData = new ArrayList<>(); //stores data of a column

            initRowData(columnData, data);

            ArrayList<List<Object>> wrappedData = new ArrayList<>(); //stores columns

            wrappedData.add(columnData);

            content.setValues(wrappedData);

            return sheetsService.spreadsheets().values().append(spreadsheetId, range, content)
                    .setValueInputOption("RAW") //TODO determine proper value
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(AppendValuesResponse response) {
        Toast.makeText(context, "Updated spreadsheet: " + response.getSpreadsheetId(), Toast.LENGTH_LONG).show();
    }

    private ArrayList<Object> initHeaderData() {
        ArrayList<Object> rowDataList = new ArrayList<>();

        //rowDataList.add("Date Added"); //TODO format date correctly

        rowDataList.add("Autonomous");

        rowDataList.add("Defense Crossed");

        rowDataList.add("High Goals");
        rowDataList.add("Low Goals");
        rowDataList.add("Missed");

        rowDataList.add("Teleop");

        for (Defense defense : Defense.values()) {
            if (defense == Defense.NONE) {
                continue;
            }

            rowDataList.add(defense.name());
        }

        rowDataList.add("High Goals");
        rowDataList.add("Low Goals");
        rowDataList.add("Missed");

        rowDataList.add("Summary");

        rowDataList.add("Scaling Stats");
        rowDataList.add("Challenged Tower");

        rowDataList.add("Trouble With");
        rowDataList.add("Comments");

        return rowDataList;
    }

    private void initRowData(ArrayList<Object> rowDataList, ScoutData data) {

        //rowDataList.add(SimpleDateFormat.getInstance().format(data.getDateAdded()));

        rowDataList.add(""); //Autonomous

        rowDataList.add(data.getAutoDefenseCrossed().name());

        rowDataList.add(data.getAutoHighGoals());
        rowDataList.add(data.getAutoLowGoals());
        rowDataList.add(data.getAutoMissedGoals());

        rowDataList.add(""); //Teleop

        for (Defense defense : Defense.values()) {
            if (defense == Defense.NONE) {
                continue;
            }

            int count;

            if (!data.getTeleopDefenseCrossings().containsKey(defense)) {
                count = 0;
            } else {
                count = data.getTeleopDefenseCrossings().get(defense);
            }

            rowDataList.add(count);
        }

        rowDataList.add(data.getTeleopHighGoals());
        rowDataList.add(data.getTeleopLowGoals());
        rowDataList.add(data.getTeleopMissedGoals());

        rowDataList.add(""); //Summary

        rowDataList.add(data.getScalingStats().name());
        rowDataList.add(data.hasChallengedTower());

        rowDataList.add(data.getTroubleWith());
        rowDataList.add(data.getComments());

    }
}*/
