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

package com.team980.thunderscout.analytics.rankings.legacy_breakdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.rankings.TeamWrapper;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

@Deprecated
public class TeamInfoActivity extends AppCompatActivity {

    private AverageScoutData scoutData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_team);

        Intent launchIntent = getIntent();
        scoutData = (AverageScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_AVERAGE_SCOUT");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Team Info: Team " + scoutData.getTeamNumber());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // --- Init ---
        TextView lastUpdated = findViewById(R.id.info_average_lastUpdated);
        lastUpdated.setText("Last updated: " + SimpleDateFormat.getDateTimeInstance().format(scoutData.getLastUpdated()));

        // --- Auto ---
        TextView autoGearsDelivered = findViewById(R.id.info_average_autoGearsDelivered);
        autoGearsDelivered.setText(formatter.format(scoutData.getAverageAutoGearsDelivered()) + "");

        TextView autoFuelDumpValue = findViewById(R.id.info_average_autoLowGoalDumpAmount);
        TextView autoFuelDumpNumericalValue = findViewById(R.id.info_average_autoLowGoalNumericalDumpAmount);
        autoFuelDumpValue.setText(scoutData.getAverageAutoLowGoalDumpAmount().toString());
        autoFuelDumpNumericalValue.setText("(" + scoutData.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + " - " + scoutData.getAverageAutoLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView autoHighGoals = findViewById(R.id.info_average_autoHighGoals);
        autoHighGoals.setText(formatter.format(scoutData.getAverageAutoHighGoals()));

        TextView autoMissedGoals = findViewById(R.id.info_average_autoMissedHighGoals);
        autoMissedGoals.setText(formatter.format(scoutData.getAverageAutoMissedHighGoals()));

        TextView crossedBaselinePercentage = findViewById(R.id.info_average_autoCrossedBaselinePercentage);
        crossedBaselinePercentage.setText(formatter.format(scoutData.getCrossedBaselinePercentage()) + "%");

        ProgressBar crossedBaselineProgressBar = findViewById(R.id.info_average_autoCrossedBaselineProgressBar);
        crossedBaselineProgressBar.setProgress((int) scoutData.getCrossedBaselinePercentage());

        // --- Teleop ---
        TextView teleopGearsDelivered = findViewById(R.id.info_average_teleopGearsDelivered);
        teleopGearsDelivered.setText(formatter.format(scoutData.getAverageTeleopGearsDelivered()) + "");

        TextView teleopDumpFrequency = findViewById(R.id.info_average_teleopFuelDumps);
        teleopDumpFrequency.setText(formatter.format(scoutData.getAverageTeleopDumpFrequency()) + "");

        TextView teleopFuelDumpValue = findViewById(R.id.info_average_teleopLowGoalDumpAmount);
        TextView teleopFuelDumpNumericalValue = findViewById(R.id.info_average_teleopLowGoalNumericalDumpAmount);
        teleopFuelDumpValue.setText(scoutData.getAverageTeleopLowGoalDumpAmount().toString());
        teleopFuelDumpNumericalValue.setText("(" + scoutData.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + " - " + scoutData.getAverageTeleopLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView teleopHighGoals = findViewById(R.id.info_average_teleopHighGoals);
        teleopHighGoals.setText(formatter.format(scoutData.getAverageTeleopHighGoals()));

        TextView teleopMissedGoals = findViewById(R.id.info_average_teleopMissedHighGoals);
        teleopMissedGoals.setText(formatter.format(scoutData.getAverageTeleopMissedHighGoals()));

        TextView attemptedClimbPercentage = findViewById(R.id.info_average_teleopAttemptedClimbPercentage);
        attemptedClimbPercentage.setText(formatter.format(scoutData.getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB)) + "%");

        ProgressBar attemptedClimbProgressBar = findViewById(R.id.info_average_teleopAttemptedClimbProgressBar);
        attemptedClimbProgressBar.setProgress((int) scoutData.getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB));

        TextView pressedTouchpadPercentage = findViewById(R.id.info_average_teleopPressedTouchpadPercentage);
        pressedTouchpadPercentage.setText(formatter.format(scoutData.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");

        ProgressBar pressedTouchpadProgressBar = findViewById(R.id.info_average_teleopPressedTouchpadProgressBar);
        pressedTouchpadProgressBar.setProgress((int) scoutData.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD));

        // --- Summary ---

        RecyclerView troubleWith = findViewById(R.id.info_average_summaryTroubleWith);

        troubleWith.setLayoutManager(new LinearLayoutManager(this));
        troubleWith.setAdapter(new CommentsAdapter(scoutData.getTroublesList()));

        RecyclerView comments = findViewById(R.id.info_average_summaryComments);

        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(new CommentsAdapter(scoutData.getCommentsList()));

        TeamWrapper tw = new TeamWrapper(scoutData.getTeamNumber());
        tw.getDataList().addAll(scoutData.getDataList());

        Snackbar.make(findViewById(R.id.info_average_lastUpdated), "Expected point average:: " + tw.getExpectedPointContribution(), Snackbar.LENGTH_INDEFINITE).show();
    }

}

