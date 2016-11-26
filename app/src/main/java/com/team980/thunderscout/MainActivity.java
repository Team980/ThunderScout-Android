package com.team980.thunderscout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.team980.thunderscout.feed.HomeFragment;
import com.team980.thunderscout.info.ThisDeviceFragment;
import com.team980.thunderscout.preferences.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String INTENT_FLAG_SHOWN_FRAGMENT = "SHOWN_FRAGMENT";
    public static int INTENT_FLAGS_HOME = 0;
    public static int INTENT_FLAGS_THIS_DEVICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FRAGSTATE", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int shownFragment = getIntent().getIntExtra(INTENT_FLAG_SHOWN_FRAGMENT, INTENT_FLAGS_HOME);

        Fragment fragment;

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            Log.d("FRAGSTATE", "restoring");
        } else {
            Log.d("FRAGSTATE", "new frag");
            switch (shownFragment) {
                case 0: //INTENT_FLAGS_HOME
                    navigationView.setCheckedItem(R.id.nav_match_scout);
                    fragment = new HomeFragment();
                    break;
                case 1: //INTENT_FLAGS_THIS_DEVICE
                    navigationView.setCheckedItem(R.id.nav_local_storage);
                    fragment = new ThisDeviceFragment();
                    break;
                default: //default to INTENT_FLAGS_HOME
                    navigationView.setCheckedItem(R.id.nav_match_scout);
                    fragment = new HomeFragment();
                    break;
            }
        }

        Log.d("FRAGSTATE", "frag class: " + fragment.getClass().getName());

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

        Log.d("FRAGSTATE", "saving");
        Log.d("FRAGSTATE", "class: " + fragment.getClass());

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

        if (id == R.id.nav_match_scout) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new HomeFragment());
            ft.commit();
        } else if (id == R.id.nav_local_storage) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new ThisDeviceFragment());
            ft.commit();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
