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

package com.team980.thunderscout.scouting_flow;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.bluetooth.ClientConnectionThread;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.util.CounterCompoundView;
import com.team980.thunderscout.util.TransitionUtils;

import java.util.Date;
import java.util.List;

public class ScoutingFlowActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, ScoutingFlowDialogFragment.ScoutingFlowDialogFragmentListener, StorageWrapper.StorageListener {

    // IDs for callback
    public static final String OPERATION_SAVE_THIS_DEVICE = "SAVE_THIS_DEVICE";
    public static final String OPERATION_SEND_BLUETOOTH = "SEND_BLUETOOTH";
    private ScoutingFlowViewPagerAdapter viewPagerAdapter;
    private ScoutData scoutData;
    private FloatingActionButton fab;
    private Bundle operationStates; //used for task loop

    private ProgressDialog operationStateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            scoutData = (ScoutData) savedInstanceState.getSerializable("ScoutData");
        } else {
            scoutData = new ScoutData();

            ScoutingFlowDialogFragment dialogFragment = new ScoutingFlowDialogFragment();
            dialogFragment.setCancelable(false);
            dialogFragment.show(getSupportFragmentManager(), "ScoutingFlowDialogFragment");
        }

        setContentView(R.layout.activity_scouting_flow);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPagerAdapter = new ScoutingFlowViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (scoutData.getTeam() != null) { //Generate header based on presence of team number
            getSupportActionBar().setTitle("Scout: Team " + scoutData.getTeam());
            getSupportActionBar().setSubtitle("Qualification Match " + scoutData.getMatchNumber());

            toolbar.setBackground(new ColorDrawable(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary())));
            tabLayout.setBackground(new ColorDrawable(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary())));
            findViewById(R.id.app_bar_layout).setBackground(new ColorDrawable(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimary())));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(scoutData.getAllianceStation().getColor().getColorPrimaryDark()));
            }
        } else {
            getSupportActionBar().setTitle("Scout a match...");
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

        if (id == R.id.action_add_to_home_screen) {
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
        new AlertDialog.Builder(this)
                .setTitle("Discard draft?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Discard", (arg0, arg1) -> ScoutingFlowActivity.super.onBackPressed()).create().show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            initScoutData();

            Crashlytics.log(Log.INFO, this.getClass().getName(), "Starting scouting loop");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            boolean saveToThisDevice = prefs.getBoolean(getResources().getString(R.string.pref_ms_save_to_local_device), true);
            boolean sendToBluetoothServer = prefs.getBoolean(getResources().getString(R.string.pref_ms_send_to_bluetooth_server), false);

            operationStates = new Bundle();
            operationStates.putBoolean(OPERATION_SAVE_THIS_DEVICE, saveToThisDevice);
            operationStates.putBoolean(OPERATION_SEND_BLUETOOTH, sendToBluetoothServer);

            operationStateDialog = new ProgressDialog(this);
            operationStateDialog.setIndeterminate(true); //TODO can we use values too?
            operationStateDialog.setCancelable(false);
            operationStateDialog.setCanceledOnTouchOutside(false);
            operationStateDialog.setTitle("Storing data...");

            dataOutputLoop();

            /*if (prefs.getBoolean("ms_send_to_linked_sheet", false)) { //Saving to Sheets
                SheetsUpdateTask task = new SheetsUpdateTask(getApplicationContext()); //MEMORY LEAK PREVENTION
                task.execute(scoutData);

                Toast info = Toast.makeText(this, "Sending to Google Sheets...", Toast.LENGTH_LONG);
                info.show();
            }*/
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

    private void dataOutputLoop() { //TODO Due to changes in backend this no longer works the same
        //TODO I would advocate running this in the background again and posting notifications
        //TODO because this method doesn't work great with the current data structure
        //TODO it also assumes LOCAL mode
        Crashlytics.log(Log.INFO, this.getClass().getName(), "Looping through data output loop");
        if (!operationStateDialog.isShowing()) {
            operationStateDialog.show(); //Show it if it isn't already visible
        }

        if (operationStates.getBoolean(OPERATION_SAVE_THIS_DEVICE)) {
            operationStateDialog.setMessage("Saving scout data to this device");

            AccountScope.getStorageWrapper(AccountScope.LOCAL, this).writeData(getData(), this);


        } else if (operationStates.getBoolean(OPERATION_SEND_BLUETOOTH)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            String address = prefs.getString(getResources().getString(R.string.pref_ms_bluetooth_server_device), null);

            BluetoothDevice device;
            try {
                device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
            } catch (IllegalArgumentException e) {
                dataOutputCallbackFail(ScoutingFlowActivity.OPERATION_SEND_BLUETOOTH, e);
                return;
            }

            operationStateDialog.setMessage("Sending scout data to " + device.getName());

            if (device.getName() == null) { //This should catch the bluetooth off error
                dataOutputCallbackFail(ScoutingFlowActivity.OPERATION_SEND_BLUETOOTH, new NullPointerException("Error initializing Bluetooth!"));
                return;
            }

            ClientConnectionThread connectThread = new ClientConnectionThread(device, scoutData, getApplicationContext(), this);
            connectThread.start();

        } else {
            operationStateDialog.dismiss();
            operationStateDialog = null;

            finish();

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putInt(getResources().getString(R.string.pref_last_used_match_number), scoutData.getMatchNumber())
                    .putString(getResources().getString(R.string.pref_last_used_alliance_station), scoutData.getAllianceStation().name())
                    .apply();
        }
    }

    public void dataOutputCallbackSuccess(final String operationId) {
        operationStates.putBoolean(operationId, false); //we're done with that!

        operationStateDialog.setMessage("");

        Crashlytics.log(Log.INFO, this.getClass().getName(), "Operation " +
                operationId + " SUCCESSFUL");

        dataOutputLoop();
    }

    //TODO broadcast receiver?
    public void dataOutputCallbackFail(final String operationId, Exception ex) {

        operationStateDialog.hide();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error: " + ex.getClass().getName())
                .setIcon(R.drawable.ic_warning_white_24dp)
                .setMessage(ex.getLocalizedMessage() + "\n" + "\n" + "Would you like to reattempt the operation?")
                .setCancelable(false)
                .setPositiveButton("Retry", (dialog, id) -> {
                    operationStates.putBoolean(operationId, true); //retry

                    dataOutputLoop();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    operationStates.putBoolean(operationId, false); //do not retry

                    dataOutputLoop();
                });

        Crashlytics.log(Log.INFO, this.getClass().getName(), "Operation " +
                operationId + " FAILED");
        //Crashlytics.logException(ex); This would create duplicate reports.

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initScoutData() {
        // Init
        scoutData.setDate(new Date(System.currentTimeMillis()));

        scoutData.setSource(Settings.Secure.getString(getContentResolver(), "bluetooth_name"));

        // Auto
        View autoView = viewPagerAdapter.getItem(0).getView();

        CounterCompoundView autoGearsDelivered = autoView.findViewById(R.id.auto_counterGearsDelivered); //TODO sometimes this fails... is it another activity state bug?

        scoutData.getAutonomous().setGearsDelivered((int) autoGearsDelivered.getValue());

        CounterCompoundView autoGearsDropped = autoView.findViewById(R.id.auto_counterGearsDropped);

        scoutData.getAutonomous().setGearsDropped((int) autoGearsDropped.getValue());

        CounterCompoundView autoHighGoals = autoView.findViewById(R.id.auto_counterHighGoals);

        scoutData.getAutonomous().setHighGoals((int) autoHighGoals.getValue());

        CounterCompoundView autoMissedHighGoals = autoView.findViewById(R.id.auto_counterMissedHighGoals);

        scoutData.getAutonomous().setMissedHighGoals((int) autoMissedHighGoals.getValue());

        // Teleop
        View teleopView = viewPagerAdapter.getItem(1).getView();

        CounterCompoundView teleopGearsDelivered = teleopView.findViewById(R.id.teleop_counterGearsDelivered);

        scoutData.getTeleop().setGearsDelivered((int) teleopGearsDelivered.getValue());

        CounterCompoundView teleopGearsDropped = teleopView.findViewById(R.id.teleop_counterGearsDropped);

        scoutData.getTeleop().setGearsDropped((int) teleopGearsDropped.getValue());

        scoutData.getTeleop().getLowGoalDumps().addAll(((TeleopFragment) viewPagerAdapter.getItem(1)).getFuelDumpAdapter().get());

        CounterCompoundView teleopHighGoals = teleopView.findViewById(R.id.teleop_counterHighGoals);

        scoutData.getTeleop().setHighGoals((int) teleopHighGoals.getValue());

        CounterCompoundView teleopMissedHighGoals = teleopView.findViewById(R.id.teleop_counterMissedHighGoals);

        scoutData.getTeleop().setMissedHighGoals((int) teleopMissedHighGoals.getValue());

        // Summary
        View summaryView = viewPagerAdapter.getItem(2).getView();

        EditText troubleWith = summaryView.findViewById(R.id.summary_edittextTroubleWith);

        scoutData.setTroubleWith(troubleWith.getText().toString());

        EditText comments = summaryView.findViewById(R.id.summary_edittextComments);

        scoutData.setComments(comments.getText().toString());
    }

    @Override
    public void onDataQuery(List<ScoutData> dataList) {
        //do nothing
    }

    @Override
    public void onDataWrite(List<ScoutData> dataWritten) {
        if (dataWritten != null) {
            dataOutputCallbackSuccess(OPERATION_SAVE_THIS_DEVICE);
        } else {
            dataOutputCallbackFail(OPERATION_SAVE_THIS_DEVICE, null);
        }

        //TODO figure out how to send a refresh intent to both fragments
        //Intent intent = new Intent(HomeFragment.ACTION_REFRESH_VIEW_PAGER);
        //localBroadcastManager.sendBroadcast(intent); //notify the UI thread so we can refresh the ViewPager automatically :D
    }

    @Override
    public void onDataRemove(List<ScoutData> dataRemoved) {
        //do nothing
    }

    @Override
    public void onDataClear(boolean success) {
        //do nothing
    }
}
