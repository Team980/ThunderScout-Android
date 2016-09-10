package com.team980.thunderscout.match;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.team980.thunderscout.R;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.bluetooth.ClientConnectionThread;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.task.DatabaseWriteTask;
import com.team980.thunderscout.info.ViewPagerAdapter;
import com.team980.thunderscout.util.CounterCompoundView;

import java.util.EnumMap;

public class ScoutActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, NestedScrollView.OnScrollChangeListener {

    private ScoutData scoutData;

    private FloatingActionButton fab;
    private boolean isToolbarAnimating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            scoutData = (ScoutData) savedInstanceState.getSerializable("ScoutData");
        } else {
            scoutData = new ScoutData(); //TODO cache this if the user wishes to
        }

        setContentView(R.layout.activity_scout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Scout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        ImageButton saveButton = (ImageButton) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(this);

        ImageButton sendButton = (ImageButton) findViewById(R.id.buttonSendBluetooth);
        sendButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("ScoutData", scoutData);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //do nothing
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 2: //SUMMARY tab
                fab.show();
                fab.setClickable(true);
                break;

            default: //AUTO tab
                hideToolbar();

                fab.hide();
                fab.setClickable(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Do nothing
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this) //TODO specify newer icon
                .setTitle("Are you sure you want to exit?")
                .setMessage("The data currently in the scouting form will be lost!")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        ScoutActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.teleop_scrollView);

            //Check errors

            showToolbar();
        } else {

            //Init scoutData

            //TODO: modular
            if (v.getId() == R.id.buttonSave) {

                DatabaseWriteTask task = new DatabaseWriteTask(scoutData, this);
                task.execute();

                Toast info = Toast.makeText(this, "Storing data...", Toast.LENGTH_LONG);
                info.show();

                //TODO notification

                finish();

            }

            if (v.getId() == R.id.buttonSendBluetooth) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

                String address = prefs.getString("bt_server_device", null);

                for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
                    if (device.getAddress().equals(address)) {
                        scoutData.setDataSource(device.getName());

                        ClientConnectionThread connectThread = new ClientConnectionThread(device, scoutData, this);
                        connectThread.start();

                        Toast info = Toast.makeText(this, "Sending data to " + device.getName() + "...", Toast.LENGTH_LONG);
                        info.show();

                        finish();
                    }
                }
            }

            if (v.getId() == R.id.buttonSendSheets) {
                //TODO send to Google Sheets
            }
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        hideToolbar();
    }

    @SuppressLint("NewApi")
    private void showToolbar() {
        // previously invisible view
        Toolbar toolbar = (Toolbar) findViewById(R.id.sendToolbar);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) { //No lollipop, no animation
            toolbar.setVisibility(View.VISIBLE);

            fab.hide();
            fab.setClickable(false);
            return;
        }

        // get the center for the clipping circle
        int cx = toolbar.getWidth() / 2;
        int cy = toolbar.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(toolbar, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        toolbar.setVisibility(View.VISIBLE);
        anim.start();

        fab.hide();
        fab.setClickable(false);
    }

    @SuppressLint("NewApi")
    private void hideToolbar() {
        // previously visible view
        final Toolbar toolbar = (Toolbar) findViewById(R.id.sendToolbar);

        if (isToolbarAnimating) {
            return;
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) { //No lollipop, no animation
            toolbar.setVisibility(View.GONE);

            fab.show();
            fab.setClickable(true);
            return;
        }

        // get the center for the clipping circle
        int cx = toolbar.getWidth() / 2;
        int cy = toolbar.getHeight() / 2;

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

        // create the animation (the final radius is zero)
        Animator anim =
                null;
        anim = ViewAnimationUtils.createCircularReveal(toolbar, cx, cy, initialRadius, 0);


        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toolbar.setVisibility(View.GONE);

                fab.show();
                fab.setClickable(true);

                isToolbarAnimating = false;
            }
        });

        // start the animation
        anim.start();
        isToolbarAnimating = true;
    }

    public ScoutData getData() {
        return scoutData;
    }
}
