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
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tslamic.dn.AndroidDeviceNames;
import com.team980.thunderscout.analytics.alliances.AlliancesFragment;
import com.team980.thunderscout.analytics.matches.MatchesFragment;
import com.team980.thunderscout.analytics.rankings.RankingsFragment;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.firebase_debug.FirebaseDebugActivity;
import com.team980.thunderscout.legacy.info.ThisDeviceFragment;
import com.team980.thunderscout.preferences.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static String INTENT_FLAG_SHOWN_FRAGMENT = "SHOWN_FRAGMENT";
    public static int INTENT_FLAGS_HOME = 0;
    public static int INTENT_FLAGS_MATCHES = 1;
    public static int INTENT_FLAGS_RANKINGS = 2;
    public static int INTENT_FLAGS_ALLIANCES = 3;

    boolean accountMenuExpanded = false; //runtime state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getHeaderView(0).setOnClickListener(this);

        AccountScope currentScope = AccountScope.valueOf(PreferenceManager.getDefaultSharedPreferences(this)
                .getString("current_account_scope", AccountScope.LOCAL.name()));

        ImageView image = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.account_image);
        switch (currentScope) {
            case LOCAL:
                image.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_white_72dp));

                String deviceName = AndroidDeviceNames.deviceNames(this).currentDeviceName();
                if (deviceName != null) {
                    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_name)).setText("My " + deviceName);
                } else {
                    ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_name)).setText("My device");
                }
                ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_id)).setText("Local storage");

                //TODO populate
                break;
            case CLOUD:
                image.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_circle_white_72dp));

                ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_name)).setText("Team 980 (ThunderCloud)"); //TODO tweak based on account data
                ((TextView) navigationView.getHeaderView(0).findViewById(R.id.account_id)).setText("account@team980.com");

                //TODO populate
                break;
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
                case 3: //INTENT_FLAGS_ALLIANCES
                    navigationView.setCheckedItem(R.id.nav_alliances);
                    fragment = new AlliancesFragment();
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

        if (fragment instanceof ThisDeviceFragment) {
            ((ThisDeviceFragment) fragment).saveAdapterState(outState);
        }

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

        if (fragment instanceof ThisDeviceFragment) {
            ((ThisDeviceFragment) fragment).restoreAdapterState(savedInstanceState);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        } else if (id == R.id.nav_alliances) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new AlliancesFragment());
            ft.commit();
        } else if (id == R.id.nav_local_storage) { //Legacy
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new ThisDeviceFragment());
            ft.commit();
        }

        //Secondary navigation menu
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_debug) {
            Intent intent = new Intent(this, FirebaseDebugActivity.class);
            startActivity(intent);
        }

        //AccountScope navigation menu
        else if (id == R.id.nav_account_local) {
            NavigationView view = (NavigationView) findViewById(R.id.nav_view);

            ImageView image = (ImageView) view.getHeaderView(0).findViewById(R.id.account_image);
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_white_72dp));

            String deviceName = AndroidDeviceNames.deviceNames(this).currentDeviceName();
            if (deviceName != null) {
                ((TextView) view.findViewById(R.id.account_name)).setText("My " + deviceName);
            } else {
                ((TextView) view.findViewById(R.id.account_name)).setText("My device");
            }
            ((TextView) view.findViewById(R.id.account_id)).setText("Local storage");

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putString("current_account_scope", AccountScope.LOCAL.name()).apply();

            contractAccountMenu();

            //TODO repopulate current fragment
        } else if (id == R.id.nav_account_cloud) {
            NavigationView view = (NavigationView) findViewById(R.id.nav_view);

            ImageView image = (ImageView) view.getHeaderView(0).findViewById(R.id.account_image);
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_circle_white_72dp));

            ((TextView) view.findViewById(R.id.account_name)).setText("Team 980 (ThunderCloud)"); //TODO tweak based on account data
            ((TextView) view.findViewById(R.id.account_id)).setText("account@team980.com");

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putString("current_account_scope", AccountScope.CLOUD.name()).apply();

            contractAccountMenu();

            //TODO repopulate current fragment
        } else if (id == R.id.nav_account_settings) {
            Toast.makeText(this, "Open settings", Toast.LENGTH_SHORT).show(); //TODO

            contractAccountMenu();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (accountMenuExpanded) {
            contractAccountMenu();
        } else {
            expandAccountMenu();
        }
    }

    private void expandAccountMenu() {
        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        ImageView dropdown = (ImageView) view.getHeaderView(0).findViewById(R.id.account_dropdown);

        view.getMenu().clear();
        view.inflateMenu(R.menu.drawer_account_menu);

        String deviceName = AndroidDeviceNames.deviceNames(this).currentDeviceName();
        if (deviceName != null) {
            view.getMenu().findItem(R.id.nav_account_local).setTitle("My " + deviceName);
        } else {
            view.getMenu().findItem(R.id.nav_account_local).setTitle("My device");
        }

        //TODO do some UI tweaks depending on account values
        //view.getMenu().findItem(R.id.nav_account_cloud).setIcon(R.drawable.ic_account_circle_white_72dp);

        dropdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_white_24dp));
        accountMenuExpanded = true;
    }

    private void contractAccountMenu() {
        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        ImageView dropdown = (ImageView) view.getHeaderView(0).findViewById(R.id.account_dropdown);

        view.getMenu().clear();
        view.inflateMenu(R.menu.drawer_menu);

        view.getMenu().findItem(R.id.nav_home).setChecked(true); //TODO set current item instead of HOME

        dropdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_white_24dp));
        accountMenuExpanded = false;
    }

}
