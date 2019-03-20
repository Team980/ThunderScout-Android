/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.team980.thunderscout.analytics.matches.MatchesFragment;
import com.team980.thunderscout.analytics.rankings.RankingsFragment;
import com.team980.thunderscout.home.HomeFragment;
import com.team980.thunderscout.preferences.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String INTENT_FLAG_SHOWN_FRAGMENT = "SHOWN_FRAGMENT";
    public static final int INTENT_FLAGS_HOME = 0;
    public static final int INTENT_FLAGS_MATCHES = 1;
    public static final int INTENT_FLAGS_RANKINGS = 2;

    public static final String ACTION_REFRESH_DATA_VIEW = "com.team80.thunderscout.ACTION_REFRESH_DATA_VIEW";

    public static final int REQUEST_CODE_AUTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThunderScout_BaseTheme_NavigationPane); //Disable splash screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateAccountHeader();

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAccountHeader();
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
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_AUTH) { //yes this is correct
            recreate();
        }
    }

    private void updateAccountHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        ImageView image = navigationView.getHeaderView(0).findViewById(R.id.account_image);

        image.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_app_badge_48dp));

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_name)).setText(sharedPrefs
                .getString(getResources().getString(R.string.pref_device_name), Build.MANUFACTURER + " " + Build.MODEL));
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_id)).setText(BuildConfig.VERSION_NAME);
    }

    public interface BackPressListener {

        /**
         * @return whether to stop processing the input further
         */
        boolean onBackPressed();
    }

}
