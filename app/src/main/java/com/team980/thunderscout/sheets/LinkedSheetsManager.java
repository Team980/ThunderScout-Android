package com.team980.thunderscout.sheets;


import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.team980.thunderscout.data.ScoutData;

import java.util.Arrays;

public class LinkedSheetsManager {

    private static LinkedSheetsManager ourInstance;

    private Context context;

    private GoogleAccountCredential credential;
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE};


    public static LinkedSheetsManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new LinkedSheetsManager(context);
        }

        return ourInstance;
    }

    private LinkedSheetsManager(Context context) {
        this.context = context;

        credential = GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public boolean chooseAccount() {
        String accountName = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("google_account_name", null);
        if (accountName != null) {
            credential.setSelectedAccountName(accountName);
            return true;
        } else {
            //Activity should startActivityForResult
            return false;
        }
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void chooseAccountResponse(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1000: //REQUEST_ACCOUNT_PICKER
                if (resultCode == Activity.RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("google_account_name", accountName);
                        editor.apply();
                        credential.setSelectedAccountName(accountName);
                    }
                }
                break;
            case 1001: //REQUEST_AUTH
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(context, "1001: Auth complete", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void initSheet() {

    }

    public void addToSheet(ScoutData data) {

    }

    public void unlinkSheet() {

    }
}
