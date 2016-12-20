package com.team980.thunderscout.integrations.sheets;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.Arrays;

public class GoogleAuthActivity extends AppCompatActivity {

    private GoogleAccountCredential credential;
    private static final String[] ACCOUNT_SCOPES = {SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        credential = GoogleAccountCredential.usingOAuth2(
                this, Arrays.asList(ACCOUNT_SCOPES))
                .setBackOff(new ExponentialBackOff());

        selectAccount();
    }

    protected void selectAccount() {
        // Start a dialog from which the user can choose an account
        startActivityForResult(
                credential.newChooseAccountIntent(),
                1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1000: //REQUEST_ACCOUNT_PICKER
                if (resultCode == Activity.RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    SharedPreferences settings =
                            PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("google_account_name", accountName);
                    editor.apply();
                    credential.setSelectedAccountName(accountName);
                }
                finish();
            case 1001: //REQUEST_AUTH
                if (resultCode == Activity.RESULT_OK) {
                    SharedPreferences settings =
                            PreferenceManager.getDefaultSharedPreferences(this);

                    Toast.makeText(this, "1001: Auth complete for " + settings.getString("google_account_name", null), Toast.LENGTH_LONG).show();
                } else {
                    startActivityForResult(credential.newChooseAccountIntent(), 1000);
                }
                break;
        }


    }
}
