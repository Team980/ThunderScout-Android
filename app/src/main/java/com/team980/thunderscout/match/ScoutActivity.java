package com.team980.thunderscout.match;

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
import android.widget.CheckBox;
import android.widget.EditText;
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

public class ScoutActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPagerAdapter viewPagerAdapter;

    private ScoutData scoutData;

    private FloatingActionButton fab;

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
        toolbar.addView(View.inflate(this, R.layout.team_number, null));
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Scout: Team");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
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

            default: //Other tabs

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
                .setIcon(R.drawable.ic_warning_white_24dp)
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
            initScoutData();

            EditText teamNumber = (EditText) findViewById(R.id.scout_teamNumber);

            if (teamNumber.getText().toString().isEmpty()) {
                teamNumber.setError("This field is required"); //Not AppCompat or definitively Material, but still ok
                return;
            }

            if (!ThunderScout.isInteger(teamNumber.getText().toString())) {
                teamNumber.setError("This must be an integer!");
                return;
            }

            //TODO: modular
            if (v.getId() == R.id.buttonSave) { //Saving locally

                scoutData.setDataSource(ScoutData.SOURCE_LOCAL_DEVICE);

                DatabaseWriteTask task = new DatabaseWriteTask(new ScoutData(scoutData), this);
                task.execute();

                Toast info = Toast.makeText(this, "Storing data...", Toast.LENGTH_LONG);
                info.show();

                //TODO notification

            }

            if (v.getId() == R.id.buttonSendBluetooth) { //Sending via BT

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

                String address = prefs.getString("bt_server_device", null);

                for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
                    if (device.getAddress().equals(address)) {
                        scoutData.setDataSource(device.getName());

                        ClientConnectionThread connectThread = new ClientConnectionThread(device, scoutData, this);
                        connectThread.start();

                        Toast info = Toast.makeText(this, "Sending data to " + device.getName() + "...", Toast.LENGTH_LONG);
                        info.show();

                    }
                }
            }

            if (v.getId() == R.id.buttonSendSheets) { //Saving to Sheets
                //TODO send to Google Sheets
            }

            finish();
        }
    }

    public ScoutData getData() {
        return scoutData;
    }

    private void initScoutData() {
        // Init
        EditText teamNumber = (EditText) findViewById(R.id.scout_teamNumber);

        scoutData.setTeamNumber(teamNumber.getText().toString());

        scoutData.setDateAdded(System.currentTimeMillis());

        // Auto
        View autoView = viewPagerAdapter.getItem(0).getView();

        CounterCompoundView autoLowGoals = (CounterCompoundView) autoView.findViewById(R.id.auto_counterLowGoals);

        scoutData.setAutoLowGoals((int) autoLowGoals.getValue());

        CounterCompoundView autoHighGoals = (CounterCompoundView) autoView.findViewById(R.id.auto_counterHighGoals);

        scoutData.setAutoHighGoals((int) autoHighGoals.getValue());

        CounterCompoundView autoMissedGoals = (CounterCompoundView) autoView.findViewById(R.id.auto_counterMissedGoals);

        scoutData.setAutoMissedGoals((int) autoMissedGoals.getValue());

        // Teleop
        View teleopView = viewPagerAdapter.getItem(1).getView();

        EnumMap<Defense, Integer> mapDefenseCrossings = new EnumMap<>(Defense.class);

        for (Defense defense : Defense.values()) {
            if (defense == Defense.NONE) {
                continue;
            }

            CounterCompoundView defenseCounter = (CounterCompoundView) teleopView.findViewById(defense.getCounterId());
            mapDefenseCrossings.put(defense, (int) defenseCounter.getValue());
        }

        scoutData.getTeleopDefenseCrossings().putAll(mapDefenseCrossings);

        CounterCompoundView teleopLowGoals = (CounterCompoundView) teleopView.findViewById(R.id.teleop_counterLowGoals);

        scoutData.setTeleopLowGoals((int) teleopLowGoals.getValue());

        CounterCompoundView teleopHighGoals = (CounterCompoundView) teleopView.findViewById(R.id.teleop_counterHighGoals);

        scoutData.setTeleopHighGoals((int) teleopHighGoals.getValue());

        CounterCompoundView teleopMissedGoals = (CounterCompoundView) teleopView.findViewById(R.id.teleop_counterMissedGoals);

        scoutData.setTeleopMissedGoals((int) teleopMissedGoals.getValue());

        // Summary
        View summaryView = viewPagerAdapter.getItem(2).getView();

        CheckBox challengedTower = (CheckBox) summaryView.findViewById(R.id.summary_checkboxHasChallenged);

        scoutData.setChallengedTower(challengedTower.isChecked());

        EditText troubleWith = (EditText) summaryView.findViewById(R.id.summary_edittextTroubleWith);

        scoutData.setTroubleWith(troubleWith.getText().toString());

        EditText comments = (EditText) summaryView.findViewById(R.id.summary_edittextComments);

        scoutData.setComments(comments.getText().toString());
    }
}
