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

package com.team980.thunderscout.scouting_flow;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.bluetooth.ClientConnectionTask;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;
import com.team980.thunderscout.scouting_flow.view.CounterCompoundView;
import com.team980.thunderscout.util.TransitionUtils;

import java.util.Date;
import java.util.List;

public class ScoutingFlowActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, ScoutingFlowDialogFragment.ScoutingFlowDialogFragmentListener, StorageWrapper.StorageListener {

    public static final String EXTRA_SCOUT_DATA = "EXTRA_SCOUT_DATA";

    private ScoutingFlowViewPagerAdapter viewPagerAdapter;
    private ScoutData scoutData;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ThunderScout_BaseTheme);
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            scoutData = (ScoutData) savedInstanceState.getSerializable("ScoutData");
        } else {
            if (getIntent().hasExtra(EXTRA_SCOUT_DATA)) {
                scoutData = (ScoutData) getIntent().getSerializableExtra(EXTRA_SCOUT_DATA);
                //Fields are filled in by the fragments
            } else {
                scoutData = new ScoutData();

                ScoutingFlowDialogFragment dialogFragment = new ScoutingFlowDialogFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getSupportFragmentManager(), "ScoutingFlowDialogFragment");
            }
        }

        setContentView(R.layout.activity_scouting_flow);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);

        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPagerAdapter = new ScoutingFlowViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab_finish);
        fab.setOnClickListener(this);

        if (scoutData.getTeam() != null) { //Generate header based on presence of team number
            getSupportActionBar().setTitle("Scout: Team " + scoutData.getTeam());
            getSupportActionBar().setSubtitle("Qualification Match " + scoutData.getMatchNumber());

            toolbar.setBackground(new ColorDrawable(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary())));
            tabLayout.setBackground(new ColorDrawable(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary())));
            findViewById(R.id.app_bar_layout).setBackground(new ColorDrawable(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary())));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimaryDark()));

                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
                ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Scout: Team " + scoutData.getTeam(),
                        current.getIcon(), getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary()));
                setTaskDescription(taskDesc);
            }
        } else {
            getSupportActionBar().setTitle("Scout a match...");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
                ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Scout a match...",
                        current.getIcon(), getResources().getColor(R.color.primary));
                setTaskDescription(taskDesc);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scouting_flow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.action_edit_details) {
            ScoutingFlowDialogFragment dialogFragment = new ScoutingFlowDialogFragment();

            Bundle args = new Bundle();
            args.putSerializable(ScoutingFlowDialogFragment.EXTRA_DEFAULT_DATA, scoutData);
            dialogFragment.setArguments(args);

            dialogFragment.show(getSupportFragmentManager(), "ScoutingFlowDialogFragment");
        }

        /*if (id == R.id.action_add_to_home_screen) {
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {

                ShortcutInfoCompat pinShortcutInfo =
                        new ShortcutInfoCompat.Builder(this, "match_scout")
                                .setShortLabel("Match scout")
                                .setLongLabel("Scout a match")
                                .setIcon(IconCompat.createWithResource(this, R.drawable.ic_shortcut_send))
                                .setIntent(new Intent(this, ScoutingFlowActivity.class)
                                        .setAction(Intent.ACTION_VIEW))
                                .build();

                // Create the PendingIntent object only if your app needs to be notified
                // that the user allowed the shortcut to be pinned. Note that, if the
                // pinning operation fails, your app isn't notified. We assume here that the
                // app has implemented a method called createShortcutResultIntent() that
                // returns a broadcast intent.
                Intent pinnedShortcutCallbackIntent =
                        ShortcutManagerCompat.createShortcutResultIntent(this, pinShortcutInfo);

                // Configure the intent so that your app's broadcast receiver gets
                // the callback successfully.
                PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                        pinnedShortcutCallbackIntent, 0);

                ShortcutManagerCompat.requestPinShortcut(this, pinShortcutInfo,
                        successCallback.getIntentSender());
            } else {
                Toast.makeText(this, "Not supported by your launcher or OS", Toast.LENGTH_SHORT).show();
            }
        }*/

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
        new AlertDialog.Builder(this)
                .setTitle("Discard draft?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Discard", (arg0, arg1) -> ScoutingFlowActivity.super.onBackPressed()).create().show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_finish) { //Send button - the only button that matters
            initScoutData();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            AlertDialog suspendDialog = null;

            // Local device
            if (prefs.getBoolean(getResources().getString(R.string.pref_ms_save_to_local_device), true)) {
                AccountScope.getStorageWrapper(AccountScope.LOCAL, this).writeData(getData(), new StorageWrapper.StorageListener() {
                    @Override
                    public void onDataWrite(@Nullable List<ScoutData> dataWritten) {
                        Intent refreshIntent = new Intent().setAction(MainActivity.ACTION_REFRESH_DATA_VIEW);
                        LocalBroadcastManager.getInstance(ScoutingFlowActivity.this).sendBroadcast(refreshIntent);
                    }
                });
                //If this errors, we'll catch it internally
            }

            // Bluetooth server
            if (prefs.getBoolean(getResources().getString(R.string.pref_ms_send_to_bluetooth_server), false)) {
                String address = prefs.getString(getResources().getString(R.string.pref_ms_bluetooth_server_device), null);

                try {
                    if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        suspendDialog = new AlertDialog.Builder(this)
                                .setTitle("Bluetooth is disabled")
                                .setIcon(R.drawable.ic_warning_24dp)
                                .setMessage("Please enable Bluetooth and try again")
                                .setPositiveButton("OK", (dialog, which) -> finish())
                                .setOnDismissListener((dialog) -> finish())
                                .create();
                    }

                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);

                    ClientConnectionTask connectTask = new ClientConnectionTask(device, scoutData, getApplicationContext());
                    connectTask.execute();
                } catch (IllegalArgumentException e) {
                    suspendDialog = new AlertDialog.Builder(this)
                            .setTitle("Bluetooth server device not set")
                            .setIcon(R.drawable.ic_warning_24dp)
                            .setMessage("Please configure your scouting settings and try again")
                            .setPositiveButton("OK", (dialog, which) -> finish())
                            .setOnDismissListener((dialog) -> finish())
                            .create();
                }
            }

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putInt(getResources().getString(R.string.pref_last_used_match_number), scoutData.getMatchNumber())
                    .putString(getResources().getString(R.string.pref_last_used_alliance_station), scoutData.getAllianceStation().name())
                    .apply();

            if (suspendDialog == null) {
                finish();
            } else {
                suspendDialog.show();
            }
        }

    }

    @Override
    public void onDialogPositiveClick(ScoutingFlowDialogFragment dialog) {
        if (dialog.allFieldsComplete()) {
            dialog.initScoutData(scoutData);

            getSupportActionBar().setTitle("Scout: Team " + scoutData.getTeam());
            getSupportActionBar().setSubtitle("Qualification Match " + scoutData.getMatchNumber()); //TODO match types?

            int toolbarColor = ((ColorDrawable) findViewById(R.id.toolbar).getBackground()).getColor();

            int statusBarColor;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = getWindow().getStatusBarColor();
            } else {
                statusBarColor = getResources().getColor(R.color.primary_dark);
            }

            TransitionUtils.toolbarAndStatusBarTransition(toolbarColor, statusBarColor,
                    getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary()),
                    getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimaryDark()), this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
                ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Scout: Team " + scoutData.getTeam(),
                        current.getIcon(), getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary()));
                setTaskDescription(taskDesc);
            }

            dialog.dismiss();
        } else {
            //do not dismiss - TODO show error
        }
    }

    @Override
    public void onDialogNegativeClick(ScoutingFlowDialogFragment dialog) {
        dialog.dismiss();

        if (scoutData.getTeam() == null) {
            finish();
        }
    }

    public ScoutData getData() {
        return scoutData;
    }

    private void initScoutData() { //TODO this SHOULD NOT BE IN THE ACTIVITY CODE
        // Init
        scoutData.setDate(new Date(System.currentTimeMillis()));

        scoutData.setSource(PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getResources().getString(R.string.pref_device_name), Build.MANUFACTURER + " " + Build.MODEL));

        // Auto
        View autoView = viewPagerAdapter.getItem(0).getView();

        CheckBox crossedAutoLine = autoView.findViewById(R.id.auto_checkBoxCrossedAutoLine);
        scoutData.setCrossedAutoLine(crossedAutoLine.isChecked());

        CounterCompoundView auto_powerCubeAllianceSwitchCount = autoView.findViewById(R.id.auto_counterPowerCubeAllianceSwitchCount);
        scoutData.setAutoPowerCubeAllianceSwitchCount((int) auto_powerCubeAllianceSwitchCount.getValue());

        CounterCompoundView auto_powerCubeScaleCount = autoView.findViewById(R.id.auto_counterPowerCubeScaleCount);
        scoutData.setAutoPowerCubeScaleCount((int) auto_powerCubeScaleCount.getValue());

        CounterCompoundView auto_powerCubePlayerStationCount = autoView.findViewById(R.id.auto_counterPowerCubePlayerStationCount);
        scoutData.setAutoPowerCubePlayerStationCount((int) auto_powerCubePlayerStationCount.getValue());

        // Teleop
        View teleopView = viewPagerAdapter.getItem(1).getView();

        CounterCompoundView teleop_powerCubeAllianceSwitchCount = teleopView.findViewById(R.id.teleop_counterPowerCubeAllianceSwitch);
        scoutData.setTeleopPowerCubeAllianceSwitchCount((int) teleop_powerCubeAllianceSwitchCount.getValue());

        CounterCompoundView teleop_powerCubeScaleCount = teleopView.findViewById(R.id.teleop_counterPowerCubeScaleCount);
        scoutData.setTeleopPowerCubeScaleCount((int) teleop_powerCubeScaleCount.getValue());

        CounterCompoundView teleop_powerCubeOpposingSwitchCount = teleopView.findViewById(R.id.teleop_counterPowerCubeOpposingSwitchCount);
        scoutData.setTeleopPowerCubeOpposingSwitchCount((int) teleop_powerCubeOpposingSwitchCount.getValue());

        CounterCompoundView teleop_powerCubePlayerStationCount = teleopView.findViewById(R.id.teleop_counterPowerCubePlayerStationCount);
        scoutData.setTeleopPowerCubePlayerStationCount((int) teleop_powerCubePlayerStationCount.getValue());

        Spinner climbingStats = teleopView.findViewById(R.id.teleop_spinnerClimbingStats);
        scoutData.setClimbingStats(ClimbingStats.values()[climbingStats.getSelectedItemPosition()]);

        CheckBox supportedOtherRobotsWhenClimbing = teleopView.findViewById(R.id.teleop_checkBoxSupportedOtherRobotsWhenClimbing);
        scoutData.setSupportedOtherRobots(supportedOtherRobotsWhenClimbing.isChecked());

        // Summary
        View summaryView = viewPagerAdapter.getItem(2).getView();

        EditText strategies = summaryView.findViewById(R.id.summary_edittextStrategies);
        scoutData.setStrategies(strategies.getText().toString());

        EditText difficulties = summaryView.findViewById(R.id.summary_edittextDifficulties);
        scoutData.setDifficulties(difficulties.getText().toString());

        EditText comments = summaryView.findViewById(R.id.summary_edittextComments);
        scoutData.setComments(comments.getText().toString());
    }
}
