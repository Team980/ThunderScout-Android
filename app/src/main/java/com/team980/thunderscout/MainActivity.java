/*
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.team980.thunderscout.analytics.matches.MatchesFragment;
import com.team980.thunderscout.analytics.rankings.RankingsFragment;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.home.HomeFragment;
import com.team980.thunderscout.preferences.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String INTENT_FLAG_SHOWN_FRAGMENT = "SHOWN_FRAGMENT";
    public static final int INTENT_FLAGS_HOME = 0;
    public static final int INTENT_FLAGS_MATCHES = 1;
    public static final int INTENT_FLAGS_RANKINGS = 2;

    public static final String ACTION_REFRESH_DATA_VIEW = "com.team80.thunderscout.ACTION_REFRESH_DATA_VIEW"; //TODO

    boolean accountMenuExpanded = false; //runtime state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThunderScout_BaseTheme_NavigationPane); //Disable splash screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getHeaderView(0).setOnClickListener(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        AccountScope currentScope = AccountScope.valueOf(sharedPrefs.getString(getResources().getString(R.string.pref_current_account_scope),
                AccountScope.LOCAL.name()));

        ImageView image = navigationView.getHeaderView(0).findViewById(R.id.account_image);
        switch (currentScope) {
            case LOCAL:
                image.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_white_72dp));

                ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_name)).setText("My " + Build.MODEL);
                ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_id)).setText("Local storage");

                //TODO populate
                break;
            /*case CLOUD:
                image.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_circle_white_72dp));

                ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_name)).setText("Team 980 (ThunderCloud)"); //TODO tweak based on account data
                ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_id)).setText("account@team980.com");

                //TODO populate
                break;*/
        }

        int shownFragment = getIntent().getIntExtra(INTENT_FLAG_SHOWN_FRAGMENT, INTENT_FLAGS_HOME);

        Fragment fragment;

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        } else {
            switch (shownFragment) {
                case 0: //INTENT_FLAGS_HOME
                    navigationView.setCheckedItem(R.id.nav_home);
                    fragment = new HomeFragment();
                    break;
                case 1: //INTENT_FLAGS_MATCHES
                    navigationView.setCheckedItem(R.id.nav_matches);
                    fragment = new MatchesFragment();
                    break;
                case 2: //INTENT_FLAGS_RANKINGS
                    navigationView.setCheckedItem(R.id.nav_rankings);
                    fragment = new RankingsFragment();
                    break;
                default: //default to INTENT_FLAGS_HOME
                    navigationView.setCheckedItem(R.id.nav_home);
                    fragment = new HomeFragment();
                    break;
            }
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();

        if (sharedPrefs.getInt(getResources().getString(R.string.pref_last_presented_update_dialog), 1) < BuildConfig.VERSION_CODE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("New in version " + BuildConfig.VERSION_NAME);
            builder.setMessage(R.string.update_notes);
            builder.setPositiveButton("OK", null);

            builder.create().show();

            sharedPrefs.edit().putInt(getResources().getString(R.string.pref_last_presented_update_dialog), BuildConfig.VERSION_CODE).apply();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment); //and we do what here, exactly?
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (fragment instanceof BackPressListener) { //Custom interface that supporting fragments use to intercept the back button
                if (!((BackPressListener) fragment).onBackPressed()) {
                    super.onBackPressed(); //If they return false, continue normal back behavior
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Main navigation menu
        if (id == R.id.nav_home) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new HomeFragment());
            ft.commit();
        } else if (id == R.id.nav_matches) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new MatchesFragment());
            ft.commit();
        } else if (id == R.id.nav_rankings) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new RankingsFragment());
            ft.commit();
        }

        //Secondary navigation menu
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_debug) {
            Intent intent = new Intent(this, FirebaseDebugActivity.class);
            startActivity(intent);
        }*/

        //AccountScope navigation menu
        else if (id == R.id.nav_account_local) {
            NavigationView view = findViewById(R.id.nav_view);

            ImageView image = view.getHeaderView(0).findViewById(R.id.account_image);
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_white_72dp));

            ((TextView) view.findViewById(R.id.account_name)).setText("My " + Build.MODEL);

            ((TextView) view.findViewById(R.id.account_id)).setText("Local storage");

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putString(getResources().getString(R.string.pref_current_account_scope), AccountScope.LOCAL.name()).apply();

            contractAccountMenu();

            //TODO repopulate current fragment
        } /*else if (id == R.id.nav_account_cloud) {
            NavigationView view = findViewById(R.id.nav_view);

            ImageView image = view.getHeaderView(0).findViewById(R.id.account_image);
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_circle_white_72dp));

            ((TextView) view.findViewById(R.id.account_name)).setText("Team 980 (ThunderCloud)"); //TODO tweak based on account data
            ((TextView) view.findViewById(R.id.account_id)).setText("account@team980.com");

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putString(getResources().getString(R.string.pref_current_account_scope), AccountScope.CLOUD.name()).apply();

            contractAccountMenu();

            //TODO repopulate current fragment
        } else if (id == R.id.nav_account_settings) {
            Toast.makeText(this, "Open settings", Toast.LENGTH_SHORT).show(); //TODO

            contractAccountMenu();
        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (accountMenuExpanded) {
            contractAccountMenu();
        } else {
            //expandAccountMenu(); TODO reenable
        }
    }

    private void expandAccountMenu() {
        NavigationView view = findViewById(R.id.nav_view);
        ImageView dropdown = view.getHeaderView(0).findViewById(R.id.account_dropdown);

        view.getMenu().clear();
        view.inflateMenu(R.menu.drawer_account_menu);

        view.getMenu().findItem(R.id.nav_account_local).setTitle("My " + Build.MODEL);

        //TODO do some UI tweaks depending on account values
        //view.getMenu().findItem(R.id.nav_account_cloud).setIcon(R.drawable.ic_account_circle_white_72dp);

        dropdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_white_24dp));
        accountMenuExpanded = true;
    }

    private void contractAccountMenu() {
        NavigationView view = findViewById(R.id.nav_view);
        ImageView dropdown = view.getHeaderView(0).findViewById(R.id.account_dropdown);

        view.getMenu().clear();
        view.inflateMenu(R.menu.drawer_menu);

        view.getMenu().findItem(R.id.nav_home).setChecked(true); //TODO set current item instead of HOME

        dropdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_white_24dp));
        accountMenuExpanded = false;
    }

    public interface BackPressListener {

        /**
         * @return whether to stop processing the input further
         */
        boolean onBackPressed();
    }

}
