package com.team980.thunderscout.match;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.team980.thunderscout.R;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.bluetooth.ClientConnectionThread;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.task.ScoutDataWriteTask;
import com.team980.thunderscout.feed.EntryOperationWrapper;
import com.team980.thunderscout.feed.EntryOperationWrapper.EntryOperationStatus;
import com.team980.thunderscout.feed.EntryOperationWrapper.EntryOperationType;
import com.team980.thunderscout.feed.FeedEntry;
import com.team980.thunderscout.feed.task.FeedDataWriteTask;
import com.team980.thunderscout.util.CounterCompoundView;
import com.team980.thunderscout.util.ImagePreviewDialog;

import java.util.EnumMap;
import java.util.Random;

public class ScoutingFlowActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ScoutingFlowViewPagerAdapter viewPagerAdapter;

    private ScoutData scoutData;
    private FeedEntry feedEntry;

    private FloatingActionButton fab;

    // IDs for callback
    public static final String OPERATION_SAVE_THIS_DEVICE = "SAVE_THIS_DEVICE";
    public static final String OPERATION_SEND_BLUETOOTH = "SEND_BLUETOOTH";

    private Bundle operationStates; //used for task loop

    private ProgressDialog operationStateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            scoutData = (ScoutData) savedInstanceState.getSerializable("ScoutData");
        } else {
            scoutData = new ScoutData(); //TODO cache this if the user wishes to
        }

        setContentView(R.layout.activity_scouting_flow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.addView(View.inflate(this, R.layout.team_number, null));
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Scout: Team"); //TODO match number, Qualification
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        EditText teamNumber = (EditText) findViewById(R.id.scout_teamNumber);
        if (new Random().nextInt(10) == 0) {
            Log.d("Gremlin", "Scout Gremlin ACTIVE");
            teamNumber.setHint("086"); //code gremlin
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPagerAdapter = new ScoutingFlowViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                        ScoutingFlowActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    public void showImagePreview(View button) {

        Drawable image;

        switch (button.getId()) {
            case R.id.teleop_previewLowBar:
                image = getResources().getDrawable(R.drawable.low_bar);
                break;
            case R.id.teleop_previewPortcullis:
                image = getResources().getDrawable(R.drawable.portcullis);
                break;
            case R.id.teleop_previewChivalDeFrise:
                image = getResources().getDrawable(R.drawable.chival_de_frise);
                break;
            case R.id.teleop_previewMoat:
                image = getResources().getDrawable(R.drawable.moat);
                break;
            case R.id.teleop_previewRamparts:
                image = getResources().getDrawable(R.drawable.ramparts);
                break;
            case R.id.teleop_previewDrawbridge:
                image = getResources().getDrawable(R.drawable.drawbridge);
                break;
            case R.id.teleop_previewSallyport:
                image = getResources().getDrawable(R.drawable.sallyport);
                break;
            case R.id.teleop_previewRockWall:
                image = getResources().getDrawable(R.drawable.rock_wall);
                break;
            case R.id.teleop_previewRoughTerrain:
                image = getResources().getDrawable(R.drawable.rough_terrain);
                break;
            default:
                image = getResources().getDrawable(R.mipmap.ic_launcher);
                break;
        }

        ImagePreviewDialog dialog = new ImagePreviewDialog();
        dialog.setImagePreview(image);
        dialog.show(getSupportFragmentManager(), "ImagePreview");
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

            Log.d("SCOUTLOOP", "here we go again");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            boolean saveToThisDevice = prefs.getBoolean("ms_send_to_local_storage", true);
            boolean sendToBluetoothServer = prefs.getBoolean("ms_send_to_bt_server", false);

            operationStates = new Bundle();
            operationStates.putBoolean(OPERATION_SAVE_THIS_DEVICE, saveToThisDevice);
            operationStates.putBoolean(OPERATION_SEND_BLUETOOTH, sendToBluetoothServer);

            operationStateDialog = new ProgressDialog(this);
            operationStateDialog.setIndeterminate(true); //TODO can we use values too?
            operationStateDialog.setCancelable(false);
            operationStateDialog.setTitle("Storing data...");

            feedEntry = new FeedEntry(FeedEntry.EntryType.MATCH_SCOUTED, System.currentTimeMillis());

            dataOutputLoop();

            /*if (prefs.getBoolean("ms_send_to_linked_sheet", false)) { //Saving to Sheets
                SheetsUpdateTask task = new SheetsUpdateTask(getApplicationContext()); //MEMORY LEAK PREVENTION
                task.execute(scoutData);

                Toast info = Toast.makeText(this, "Sending to Google Sheets...", Toast.LENGTH_LONG);
                info.show();
            }*/
        }

    }

    public ScoutData getData() {
        return scoutData;
    }

    private void dataOutputLoop() {
        Log.d("SCOUTLOOP", "ever get that feeling of deja vu?");
        if (!operationStateDialog.isShowing()) {
            operationStateDialog.show(); //Show it if it isn't already visible
        }

        if (operationStates.getBoolean(OPERATION_SAVE_THIS_DEVICE)) {
            scoutData.setDataSource(ScoutData.SOURCE_LOCAL_DEVICE);

            operationStateDialog.setMessage("Saving scout data to this device");

            ScoutDataWriteTask task = new ScoutDataWriteTask(new ScoutData(scoutData), getApplicationContext(), this); //MEMORY LEAK PREVENTION
            task.execute();

        } else if (operationStates.getBoolean(OPERATION_SEND_BLUETOOTH)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            String address = prefs.getString("ms_bt_server_device", null);

            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address); //TODO THIS IS A NEW, BETTER SEND METHOD. NEEDS TESTING ;)
            scoutData.setDataSource(BluetoothAdapter.getDefaultAdapter().getName());

            operationStateDialog.setMessage("Sending scout data to " + device.getName());

            ClientConnectionThread connectThread = new ClientConnectionThread(device, scoutData, getApplicationContext(), this);
            connectThread.start();

        } else {
            operationStateDialog.dismiss();
            operationStateDialog = null;

            finish();

            FeedDataWriteTask feedDataWriteTask = new FeedDataWriteTask(feedEntry, this);
            feedDataWriteTask.execute();
        }
    }

    public void dataOutputCallbackSuccess(final String operationId) {
        Log.d("SCOUTLOOP", "back into the fray");
        operationStates.putBoolean(operationId, false); //we're done with that!

        operationStateDialog.setMessage("");

        EntryOperationWrapper operation = new EntryOperationWrapper(EntryOperationType.fromOperationId(operationId),
                EntryOperationStatus.OPERATION_SUCCESSFUL);
        feedEntry.addOperation(operation);

        dataOutputLoop();
    }

    //TODO broadcast receiver?
    public void dataOutputCallbackFail(final String operationId, Exception ex) {
        Log.d("SCOUTLOOP", "back into the fray");

        operationStateDialog.hide();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error: " + ex.getClass().getName())
                .setIcon(R.drawable.ic_warning_white_24dp)
                .setMessage(ex.getLocalizedMessage() + "\n" + "\n" + "Would you like to reattempt the operation?")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        operationStates.putBoolean(operationId, true); //retry

                        EntryOperationWrapper operation = new EntryOperationWrapper(EntryOperationType.fromOperationId(operationId),
                                EntryOperationStatus.OPERATION_FAILED);
                        feedEntry.addOperation(operation);

                        dataOutputLoop();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        operationStates.putBoolean(operationId, false); //do not retry

                        EntryOperationWrapper operation = new EntryOperationWrapper(EntryOperationType.fromOperationId(operationId),
                                EntryOperationStatus.OPERATION_ABORTED);
                        feedEntry.addOperation(operation);

                        dataOutputLoop();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initScoutData() {
        // Init
        EditText teamNumber = (EditText) findViewById(R.id.scout_teamNumber);

        scoutData.setTeamNumber(teamNumber.getText().toString());

        scoutData.setDateAdded(System.currentTimeMillis());

        // Auto
        View autoView = viewPagerAdapter.getItem(0).getView();

        CounterCompoundView autoLowGoals = (CounterCompoundView) autoView.findViewById(R.id.auto_counterLowGoals); //TODO sometimes this fails... is it another activity state bug?

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

            int count = (int) defenseCounter.getValue();
            if (count == 0) {
                continue;
            }

            mapDefenseCrossings.put(defense, count);
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
