/*package com.team980.thunderscout.sheets;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.task.DatabaseReadTask;

import java.util.Arrays;

public class LinkedSheetManager {

    private BroadcastReceiver receiver;

    public static final String ACTION_INIT_SHEET = "com.team980.thunderscout.INIT_SHEET";
    public static final String ACTION_SEND_TO_SHEET = "com.team980.thunderscout.SEND_TO_SHEET";

    public static final String EXTRA_SCOUT_DATA = "com.team980.thunderscout.EXTRA_SCOUT_DATA";

    private GoogleAccountCredential credential;
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE};

    public LinkedSheetManager() {
        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        String accountName = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("google_account_name", null);
        if (accountName != null) {
            credential.setSelectedAccountName(accountName);
        } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    credential.newChooseAccountIntent(),
                    1);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SEND_TO_SHEET);
        filter.addAction(ACTION_INIT_SHEET);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ACTION_INIT_SHEET:
                        break;
                    case ACTION_SEND_TO_SHEET:
                        ScoutData data = (ScoutData) intent.getSerializableExtra(EXTRA_SCOUT_DATA);
                        break;
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(filter));
    }
}
*/