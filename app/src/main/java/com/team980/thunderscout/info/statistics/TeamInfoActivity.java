package com.team980.thunderscout.info.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.AverageScoutData;
import com.team980.thunderscout.data.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class TeamInfoActivity extends AppCompatActivity {

    private AverageScoutData scoutData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_team);

        Intent launchIntent = getIntent();
        scoutData = (AverageScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_AVERAGE_SCOUT");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Team Info: Team " + scoutData.getTeamNumber());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // --- Init ---
        TextView lastUpdated = (TextView) findViewById(R.id.info_average_lastUpdated);
        lastUpdated.setText("Last updated: " + SimpleDateFormat.getDateTimeInstance().format(scoutData.getLastUpdated()));

        // --- Auto ---
        TextView autoGearsDelivered = (TextView) findViewById(R.id.info_average_autoGearsDelivered);
        autoGearsDelivered.setText(formatter.format(scoutData.getAverageAutoGearsDelivered()) + "");

        TextView autoFuelDumpValue = (TextView) findViewById(R.id.info_average_autoLowGoalDumpAmount);
        TextView autoFuelDumpNumericalValue = (TextView) findViewById(R.id.info_average_autoLowGoalNumericalDumpAmount);
        autoFuelDumpValue.setText(scoutData.getAverageAutoLowGoalDumpAmount().toString());
        autoFuelDumpNumericalValue.setText("(" + scoutData.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + " - " + scoutData.getAverageAutoLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView autoHighGoals = (TextView) findViewById(R.id.info_average_autoHighGoals);
        autoHighGoals.setText(formatter.format(scoutData.getAverageAutoHighGoals()));

        TextView autoMissedGoals = (TextView) findViewById(R.id.info_average_autoMissedHighGoals);
        autoMissedGoals.setText(formatter.format(scoutData.getAverageAutoMissedHighGoals()));

        TextView crossedBaselinePercentage = (TextView) findViewById(R.id.info_average_autoCrossedBaselinePercentage);
        crossedBaselinePercentage.setText(formatter.format(scoutData.getCrossedBaselinePercentage()) + "%");

        ProgressBar crossedBaselineProgressBar = (ProgressBar) findViewById(R.id.info_average_autoCrossedBaselineProgressBar);
        crossedBaselineProgressBar.setProgress((int) scoutData.getCrossedBaselinePercentage());

        // --- Teleop ---
        TextView teleopGearsDelivered = (TextView) findViewById(R.id.info_average_teleopGearsDelivered);
        teleopGearsDelivered.setText(formatter.format(scoutData.getAverageTeleopGearsDelivered()) + "");

        TextView teleopDumpFrequency = (TextView) findViewById(R.id.info_average_teleopFuelDumps);
        teleopDumpFrequency.setText(formatter.format(scoutData.getAverageTeleopDumpFrequency()) + "");

        TextView teleopFuelDumpValue = (TextView) findViewById(R.id.info_average_teleopLowGoalDumpAmount);
        TextView teleopFuelDumpNumericalValue = (TextView) findViewById(R.id.info_average_teleopLowGoalNumericalDumpAmount);
        teleopFuelDumpValue.setText(scoutData.getAverageTeleopLowGoalDumpAmount().toString());
        teleopFuelDumpNumericalValue.setText("(" + scoutData.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + " - " + scoutData.getAverageTeleopLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView teleopHighGoals = (TextView) findViewById(R.id.info_average_teleopHighGoals);
        teleopHighGoals.setText(formatter.format(scoutData.getAverageTeleopHighGoals()));

        TextView teleopMissedGoals = (TextView) findViewById(R.id.info_average_teleopMissedHighGoals);
        teleopMissedGoals.setText(formatter.format(scoutData.getAverageTeleopMissedHighGoals()));

        TextView attemptedClimbPercentage = (TextView) findViewById(R.id.info_average_teleopAttemptedClimbPercentage);
        attemptedClimbPercentage.setText(formatter.format(scoutData.getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB)) + "%");

        ProgressBar attemptedClimbProgressBar = (ProgressBar) findViewById(R.id.info_average_teleopAttemptedClimbProgressBar);
        attemptedClimbProgressBar.setProgress((int) scoutData.getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB));

        TextView pressedTouchpadPercentage = (TextView) findViewById(R.id.info_average_teleopPressedTouchpadPercentage);
        pressedTouchpadPercentage.setText(formatter.format(scoutData.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");

        ProgressBar pressedTouchpadProgressBar = (ProgressBar) findViewById(R.id.info_average_teleopPressedTouchpadProgressBar);
        pressedTouchpadProgressBar.setProgress((int) scoutData.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD));

        // --- Summary ---

        RecyclerView troubleWith = (RecyclerView) findViewById(R.id.info_average_summaryTroubleWith);

        troubleWith.setLayoutManager(new LinearLayoutManager(this));
        troubleWith.setAdapter(new CommentsAdapter(scoutData.getTroublesList()));

        RecyclerView comments = (RecyclerView) findViewById(R.id.info_average_summaryComments);

        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new CommentsAdapter(scoutData.getCommentsList()));
    }

}

