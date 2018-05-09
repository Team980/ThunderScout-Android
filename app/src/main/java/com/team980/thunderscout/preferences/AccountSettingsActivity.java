/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.team980.thunderscout.preferences;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;

import java.io.IOException;
import java.util.Arrays;

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            super.onCreate(savedInstanceState);
            //TODO show first run dialog?

            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.ThunderScout_BaseTheme_ActionBar)
                            .setIsSmartLockEnabled(false)
                            .setPrivacyPolicyUrl("http://team980.com/thunderscout/privacy-policy/")
                            .setLogo(R.mipmap.ic_launcher_splash)
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    0);
            return;
        }

        setTheme(R.style.ThunderScout_BaseTheme_ActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView nameView = findViewById(R.id.name);
        nameView.setText(user.getDisplayName());

        TextView emailView = findViewById(R.id.email);
        emailView.setText(user.getEmail());

        ImageView photoView = findViewById(R.id.photo);
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl().toString().replace("s96-c/photo.jpg", "s400-c/photo.jpg")).apply(new RequestOptions().circleCrop()).into(photoView);
        } else {
            Glide.with(this).load(R.drawable.ic_cloud_circle_72dp).into(photoView);
        }

        AccountPreferenceFragment fragment = new AccountPreferenceFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(this, "Signed in as " + user.getDisplayName(), Toast.LENGTH_LONG).show();

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putString(getResources().getString(R.string.pref_current_account_scope), AccountScope.CLOUD.name()).apply();

            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getResources().getString(R.string.pref_enable_push_notifications), true)) {
                FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                FirebaseInstanceId.getInstance().getToken();
            }

            setResult(MainActivity.REQUEST_CODE_AUTH);

            finish();
        } else {
            if (response == null) {
                // User pressed back button
                finish();
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                new AlertDialog.Builder(this)
                        .setTitle("No Internet connection")
                        .setIcon(R.drawable.ic_warning_24dp)
                        .setMessage("Please connect to the Internet and try again")
                        .setPositiveButton("OK", (dialog, which) -> finish())
                        .setOnDismissListener((dialog) -> finish())
                        .create().show();
                return;
            }

            Toast.makeText(this, "An unknown error occurred", Toast.LENGTH_LONG).show();
            Log.e(getClass().getName(), "Sign-in error: ", response.getError());
            Crashlytics.logException(response.getError());
        }
    }

    public void signOut(View v) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    Toast.makeText(this, "Signed out", Toast.LENGTH_LONG).show();

                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                            .putBoolean(getResources().getString(R.string.pref_ms_save_to_thundercloud), false)
                            .putBoolean(getResources().getString(R.string.pref_bt_save_to_thundercloud), false)
                            .putString(getResources().getString(R.string.pref_current_account_scope), AccountScope.LOCAL.name())
                            .apply();

                    FirebaseMessaging.getInstance().setAutoInitEnabled(false);
                    AsyncTask.execute(() -> {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException ignored) {
                        }
                    });

                    setResult(MainActivity.REQUEST_CODE_AUTH);

                    finish();
                });
    }

    public void deleteAccount(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Delete account?")
                .setIcon(R.drawable.ic_warning_24dp)
                .setMessage("You will lose all data stored in your account! This cannot be reversed!")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> AuthUI.getInstance()
                        .delete(AccountSettingsActivity.this)
                        .addOnCompleteListener(task -> {
                            Toast.makeText(this, "Account deleted", Toast.LENGTH_LONG).show();

                            PreferenceManager.getDefaultSharedPreferences(this).edit()
                                    .putBoolean(getResources().getString(R.string.pref_ms_save_to_thundercloud), false)
                                    .putBoolean(getResources().getString(R.string.pref_bt_save_to_thundercloud), false)
                                    .putString(getResources().getString(R.string.pref_current_account_scope), AccountScope.LOCAL.name())
                                    .apply();

                            FirebaseMessaging.getInstance().setAutoInitEnabled(false);
                            AsyncTask.execute(() -> {
                                try {
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                } catch (IOException ignored) {
                                }
                            });

                            setResult(MainActivity.REQUEST_CODE_AUTH);

                            finish();
                        })).show();
    }

    public static class AccountPreferenceFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_account);

            Preference privacyPolicy = findPreference(getResources().getString(R.string.pref_privacy_policy));
            privacyPolicy.setOnPreferenceClickListener(preference1 -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://team980.com/thunderscout/privacy-policy/")); //TODO test with redesigned site
                startActivity(browserIntent);
                return true;
            });

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                findPreference(getResources().getString(R.string.pref_enable_push_notifications))
                        .setEnabled(false);
            } else {
                findPreference(getResources().getString(R.string.pref_enable_push_notifications))
                        .setOnPreferenceChangeListener((preference, newValue) -> {
                            if ((boolean) newValue) {
                                FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                                FirebaseInstanceId.getInstance().getToken();
                            } else {
                                FirebaseMessaging.getInstance().setAutoInitEnabled(false);
                                AsyncTask.execute(() -> {
                                    try {
                                        FirebaseInstanceId.getInstance().deleteInstanceId();
                                    } catch (IOException ignored) {
                                    }
                                });
                            }
                            return true;
                        });
            }
        }
    }
}
