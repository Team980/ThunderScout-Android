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

package com.team980.thunderscout.backend.cloud;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;

import java.util.Arrays;

@Deprecated //TODO recreate as an actually good settings page
/**
 * https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md
 */
public class FirebaseDebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storm_debug);

        reloadContent();
    }

    public void reloadContent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String name = "Not signed in";
        String email = "Not signed in";
        Uri photoUrl = null;
        boolean emailVerified = false;
        String uid = "null";

        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();

            System.out.println(name);
            System.out.println(email);
            System.out.println(photoUrl);
            System.out.println(emailVerified);
            System.out.println(uid);
        }

        TextView nameView = findViewById(R.id.name);
        nameView.setText(name);

        TextView emailView = findViewById(R.id.email);
        emailView.setText(email);

        TextView verifiedView = findViewById(R.id.emailVerified);
        verifiedView.setText(emailVerified + "");

        TextView uidView = findViewById(R.id.uid);
        uidView.setText(uid);

        ImageView photoView = findViewById(R.id.photo);
        Glide.with(this).load(photoUrl).into(photoView);
    }

    public void authButton(View v) {
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
    }

    public void signOut(View v) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                            .putString(getResources().getString(R.string.pref_current_account_scope), AccountScope.LOCAL.name()).apply();
                    reloadContent();
                });
    }

    public void deleteAccount(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Unlink account from ThunderCloud?")
                .setIcon(R.drawable.ic_warning_white_24dp)
                .setMessage("You will lose all data stored in your account! This cannot be reversed!")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> AuthUI.getInstance()
                        .delete(FirebaseDebugActivity.this)
                        .addOnCompleteListener(task -> {
                            PreferenceManager.getDefaultSharedPreferences(this).edit()
                                    .putString(getResources().getString(R.string.pref_current_account_scope), AccountScope.LOCAL.name()).apply();
                            reloadContent();
                        })).show();
    }
}
