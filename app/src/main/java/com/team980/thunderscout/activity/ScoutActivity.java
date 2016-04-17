package com.team980.thunderscout.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.team980.thunderscout.R;
import com.team980.thunderscout.adapter.ViewPagerAdapter;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.object.Defense;
import com.team980.thunderscout.data.object.RankedDefense;
import com.team980.thunderscout.task.DatabaseWriteTask;
import com.team980.thunderscout.thread.ClientConnectionThread;
import com.team980.thunderscout.view.CounterCompoundView;
import com.team980.thunderscout.view.RankedSliderCompoundView;

import java.util.ArrayList;
import java.util.List;

public class ScoutActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ScoutData scoutData;

    private FloatingActionButton fab, fabSave, fabSend;

    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scoutData = new ScoutData(); //TODO cache this if the user wishes to

        setContentView(R.layout.activity_scout);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Scout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        fabSave.setOnClickListener(this);

        fabSend = (FloatingActionButton) findViewById(R.id.fab_send);
        fabSend.setOnClickListener(this);

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //do nothing
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 1: //TELEOP tab
                fab.show();
                fabSend.hide();
                break;

            default: //AUTO tab

                if (isFabOpen) {
                    fab.startAnimation(rotate_backward);
                    fabSave.startAnimation(fab_close);
                    fabSend.startAnimation(fab_close);
                    fabSave.setClickable(false);
                    fabSend.setClickable(false);
                    isFabOpen = false;
                }

                fab.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //do nothing
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.AlertDialog)
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

            if (isFabOpen) {

                fab.startAnimation(rotate_backward);
                fabSave.startAnimation(fab_close);
                fabSend.startAnimation(fab_close);
                fabSave.setClickable(false);
                fabSend.setClickable(false);
                isFabOpen = false;

            } else {

                fab.startAnimation(rotate_forward);
                fabSave.startAnimation(fab_open);
                fabSend.startAnimation(fab_open);
                fabSave.setClickable(true);
                fabSend.setClickable(true);
                isFabOpen = true;

            }
        } else {

            //TODO make sure it's all filled out beforehand

            EditText teamNumberField = (EditText) findViewById(R.id.teleop_editTextTeamNumber);
            scoutData.setTeamNumber(teamNumberField.getText().toString());

            CounterCompoundView defensesBreached = (CounterCompoundView) findViewById(R.id.teleop_counterBreach);
            scoutData.setTeleopDefensesBreached(defensesBreached.getValue());

            List<RankedDefense> list = new ArrayList<>();
            for (Defense d : Defense.values()) {
                CheckBox checkBox = (CheckBox) findViewById(d.getTeleopID());

                if (checkBox.isChecked()) {
                    RankedSliderCompoundView slider = (RankedSliderCompoundView) findViewById(d.getTeleopSliderID());

                    RankedDefense rd = new RankedDefense(d, slider.getRankValue());
                    list.add(rd);
                }
            }
            scoutData.setTeleopListDefensesBreached(list);

            CounterCompoundView goalsScored = (CounterCompoundView) findViewById(R.id.teleop_counterScore);
            scoutData.setTeleopGoalsScored(goalsScored.getValue());

            CheckBox lowGoals = (CheckBox) findViewById(R.id.teleop_goalLow);
            scoutData.setTeleopLowGoals(lowGoals.isChecked());

            CheckBox highGoals = (CheckBox) findViewById(R.id.teleop_goalHigh);
            scoutData.setTeleopHighGoals(highGoals.isChecked());

            RankedSliderCompoundView driverSkill = (RankedSliderCompoundView) findViewById(R.id.teleop_sliderSkill);
            scoutData.setTeleopDriverSkill(driverSkill.getRankValue());

            EditText comments = (EditText) findViewById(R.id.teleop_comments);
            scoutData.setTeleopComments(comments.getText().toString());

            //TODO run code based on ID again

            if (v.getId() == R.id.fab_save) {

                scoutData.setDateAdded(System.currentTimeMillis());

                DatabaseWriteTask task = new DatabaseWriteTask(scoutData, this);
                task.execute();

                Toast info = Toast.makeText(this, "Storing data...", Toast.LENGTH_LONG);
                info.show();

                //TODO notification

                finish();

            } else if (v.getId() == R.id.fab_send) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

                String address = prefs.getString("pref_serverDevice", null);
                //TODO prompt for device?

                for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
                    if (device.getAddress().equals(address)) {
                        ClientConnectionThread connectThread = new ClientConnectionThread(device, scoutData, this,
                                findViewById(R.id.teleop_coordinatorLayout));
                        connectThread.start();

                        Toast info = Toast.makeText(this, "Sending data to " + device.getName() + "...", Toast.LENGTH_LONG);
                        info.show();

                        //TODO notification

                        finish();
                    }
                }
            }
        }
    }


    public ScoutData getData() {
        return scoutData;
    }

}
