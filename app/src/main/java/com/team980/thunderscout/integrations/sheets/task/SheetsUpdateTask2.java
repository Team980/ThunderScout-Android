package com.team980.thunderscout.integrations.sheets.task;

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
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.data.ScoutData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
/**
 * use SheetsUpdateTask instead, this is just for reference
 */
public class SheetsUpdateTask2 extends AsyncTask<ScoutData, Void, Void> {

    private Context context;

    private Sheets sheetsService = null;

    private static final String[] ACCOUNT_SCOPES = {SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE};

    String spreadsheetId;

    public SheetsUpdateTask2(Context context) {
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
            spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute(); //spreadsheet which contains workbooks

            for (ScoutData data : dataList) { //loop for each data object... there should only be 1 but who cares

                List<Sheet> sheets = spreadsheet.getSheets();
                Sheet teamSheet = null; //workbook for team

                for (Sheet s : sheets) {
                    if (s.getProperties().getTitle() == data.getTeamNumber()) {
                        teamSheet = s;
                        break;
                    } else if (!ThunderScout.isInteger(s.getProperties().getTitle())) {
                        sheets.remove(s); //removes that pesky default sheet
                    }
                }

                if (teamSheet == null) {
                    teamSheet = new Sheet();
                    teamSheet.getProperties().setTitle(data.getTeamNumber());
                    spreadsheet.getSheets().add(teamSheet);
                    insertInitData(teamSheet);
                }

                insertIntoSheet(teamSheet, data);

            }

            //TODO push changes / build proper request format
            //sheetsService.spreadsheets().values().batchUpdate(spreadsheetId, )
            //OR sheetsService.spreadsheets().values().update()

            //TODO is it this?
            //DimensionRange range1 = new DimensionRange();
            //range1.setSheetId()
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Deprecated
    private void insertInitData(Sheet sheet) {

    }

    @Deprecated
    private BatchUpdateValuesRequest initUpdateRequest(ScoutData data) {
        BatchUpdateValuesRequest updateRequest = new BatchUpdateValuesRequest();

        ArrayList<ValueRange> valueRanges = new ArrayList<>();
        ValueRange range = new ValueRange();
        range.setMajorDimension("COLUMNS");
        valueRanges.add(range);

        updateRequest.setData(valueRanges);
        return updateRequest;
    }

    @Deprecated
    private void insertIntoSheet(Sheet sheet, ScoutData scoutData) {
        ArrayList<RowData> rows = new ArrayList<>();
        ArrayList<CellData> cells = new ArrayList<>();

        // Init a CellData object for each data cell
        CellData name = new CellData();
        name.setUserEnteredValue(
                new ExtendedValue().setStringValue(scoutData.getComments()));
        cells.add(name);

        CellData email = new CellData();
        email.setUserEnteredValue(
                new ExtendedValue().setStringValue(scoutData.getComments()));
        cells.add(email);

        CellData phoneNumber = new CellData();
        phoneNumber.setUserEnteredValue(
                new ExtendedValue().setStringValue(scoutData.getComments()));
        cells.add(phoneNumber);

        CellData grade = new CellData();
        grade.setUserEnteredValue(
                new ExtendedValue().setNumberValue((double) scoutData.getDateAdded()));
        cells.add(grade);


        // Add data column to sheet (looks hacky, ik)
        rows.add(new RowData().setValues(cells));

        GridData gridData = new GridData();
        gridData.setRowData(rows);

        ArrayList<GridData> gridList = new ArrayList<>();
        gridList.add(gridData);

        sheet.setData(gridList);
    }
}
