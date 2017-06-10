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

package com.team980.thunderscout.analytics.matches.legacy_breakdown;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.rankings.TeamWrapper;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.SimpleDateFormat;

@Deprecated //TODO recreate, use new analytic methods
public class MatchInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_match);

        Intent launchIntent = getIntent();
        ScoutData data = (ScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_SCOUT");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Match Info: Team " + data.getTeam());
        getSupportActionBar().setSubtitle("Qualification Match " + data.getMatchNumber());

        toolbar.setBackground(new ColorDrawable(getResources().getColor(data.getAllianceStation().getColor().getColorPrimary())));
        findViewById(R.id.app_bar_layout).setBackground(new ColorDrawable(getResources().getColor(data.getAllianceStation().getColor().getColorPrimary())));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(data.getAllianceStation().getColor().getColorPrimaryDark()));
        }

        // --- Init ---
        TextView dateAdded = findViewById(R.id.info_match_dateAdded);
        dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDate()));

        TextView dataSource = findViewById(R.id.info_match_dataSource);
        dataSource.setText("Source: " + data.getSource());

        // --- Auto ---

        //TODO use @strings with inputs as Spannables for in-view styling

        TextView autoGearsDelivered = findViewById(R.id.info_match_autoGearsDelivered);
        autoGearsDelivered.setText(data.getAutonomous().getGearsDelivered() + "");

        TextView autoFuelDumpAmount = findViewById(R.id.info_match_autoLowGoalDumpAmount);
        autoFuelDumpAmount.setText(data.getAutonomous().getLowGoalDumpAmount().toString());

        TextView autoFuelNumericalDumpAmount = findViewById(R.id.info_match_autoLowGoalNumericalDumpAmount);
        autoFuelNumericalDumpAmount.setText("(" + data.getAutonomous().getLowGoalDumpAmount().getMinimumAmount() + " - " +
                data.getAutonomous().getLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView autoHighGoals = findViewById(R.id.info_match_autoHighGoals);
        autoHighGoals.setText(data.getAutonomous().getHighGoals() + "");

        TextView autoMissedGoals = findViewById(R.id.info_match_autoMissedHighGoals);
        autoMissedGoals.setText(data.getAutonomous().getMissedHighGoals() + "");

        TextView crossedBaseline = findViewById(R.id.info_match_autoCrossedBaseline);
        TextView crossedBaselineAction = findViewById(R.id.info_match_autoCrossedBaselineAction);

        if (data.getAutonomous().getCrossedBaseline()) {
            crossedBaseline.setText("CROSSED ");
            crossedBaselineAction.setText("the baseline");
        } else {
            crossedBaseline.setVisibility(View.GONE);
            crossedBaselineAction.setText("Did not cross the baseline");
        }

        // --- Teleop ---
        TextView teleopGearsDelivered = findViewById(R.id.info_match_teleopGearsDelivered);
        teleopGearsDelivered.setText(data.getTeleop().getGearsDelivered() + "");

        TextView teleopFuelDumpAmount = findViewById(R.id.info_match_teleopNumberOfLowGoalFuelDumps);
        teleopFuelDumpAmount.setText(data.getTeleop().getLowGoalDumps().size() + "");

        RecyclerView listFuelDumps = findViewById(R.id.info_match_teleopListFuelDumps);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listFuelDumps.setLayoutManager(mLayoutManager);

        FuelDumpAdapter listDumpsAdapter = new FuelDumpAdapter(data.getTeleop().getLowGoalDumps());
        listFuelDumps.setAdapter(listDumpsAdapter);

        TextView teleopHighGoals = findViewById(R.id.info_match_teleopHighGoals);
        teleopHighGoals.setText(data.getTeleop().getHighGoals() + "");

        TextView teleopMissedGoals = findViewById(R.id.info_match_teleopMissedHighGoals);
        teleopMissedGoals.setText(data.getTeleop().getMissedHighGoals() + "");

        TextView climbingStats = findViewById(R.id.info_match_teleopClimbingStats);
        TextView climbingStatsAction = findViewById(R.id.info_match_teleopClimbingStatsAction);

        if (data.getTeleop().getClimbingStats() == ClimbingStats.PRESSED_TOUCHPAD) {
            climbingStats.setText("PRESSED ");
            climbingStatsAction.setText("the touchpad");
        } else if (data.getTeleop().getClimbingStats() == ClimbingStats.ATTEMPTED_CLIMB) {
            climbingStats.setText("ATTEMPTED TO CLIMB ");
            climbingStatsAction.setText("the airship");
        } else {
            climbingStats.setVisibility(View.GONE);
            climbingStatsAction.setText("Did not climb the airship");
        }

        // --- Summary ---
        TextView troubleWith = findViewById(R.id.info_match_summaryTroubleWith);
        if (data.getTroubleWith() != null && !data.getTroubleWith().equals("")) {
            troubleWith.setText(data.getTroubleWith());
        } else {
            troubleWith.setText("None");
        }

        TextView comments = findViewById(R.id.info_match_summaryComments);
        if (data.getComments() != null && !data.getComments().equals("")) {
            comments.setText(data.getComments());
        } else {
            comments.setText("None");
        }

        TeamWrapper tw = new TeamWrapper(data.getTeam());
        tw.getDataList().add(data);

        Snackbar.make(findViewById(R.id.info_match_dateAdded), "Expected points: " + tw.getExpectedPointContribution(), Snackbar.LENGTH_INDEFINITE).show();
    }
}

