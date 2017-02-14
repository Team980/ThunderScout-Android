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

package com.team980.thunderscout.info.statistics;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.ClimbingStats;

import java.text.SimpleDateFormat;

public class MatchInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_match);

        Intent launchIntent = getIntent();
        ScoutData data = (ScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_SCOUT");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Match Info: Team " + data.getTeamNumber());
        getSupportActionBar().setSubtitle("Qualification Match " + data.getMatchNumber());

        toolbar.setBackground(new ColorDrawable(getResources().getColor(data.getAllianceColor().getColorPrimary())));
        findViewById(R.id.app_bar_layout).setBackground(new ColorDrawable(getResources().getColor(data.getAllianceColor().getColorPrimary())));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(data.getAllianceColor().getColorPrimaryDark()));
        }

        // --- Init ---
        TextView dateAdded = (TextView) findViewById(R.id.info_match_dateAdded);
        dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));

        TextView dataSource = (TextView) findViewById(R.id.info_match_dataSource);
        dataSource.setText("Source: " + data.getDataSource());

        // --- Auto ---

        //TODO use @strings with inputs as Spannables for in-view styling

        TextView autoGearsDelivered = (TextView) findViewById(R.id.info_match_autoGearsDelivered);
        autoGearsDelivered.setText(data.getAutoGearsDelivered() + "");

        TextView autoFuelDumpAmount = (TextView) findViewById(R.id.info_match_autoLowGoalDumpAmount);
        autoFuelDumpAmount.setText(data.getAutoLowGoalDumpAmount().toString());

        TextView autoFuelNumericalDumpAmount = (TextView) findViewById(R.id.info_match_autoLowGoalNumericalDumpAmount);
        autoFuelNumericalDumpAmount.setText("(" + data.getAutoLowGoalDumpAmount().getMinimumAmount() + " - " +
                data.getAutoLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView autoHighGoals = (TextView) findViewById(R.id.info_match_autoHighGoals);
        autoHighGoals.setText(data.getAutoHighGoals() + "");

        TextView autoMissedGoals = (TextView) findViewById(R.id.info_match_autoMissedHighGoals);
        autoMissedGoals.setText(data.getAutoMissedHighGoals() + "");

        TextView crossedBaseline = (TextView) findViewById(R.id.info_match_autoCrossedBaseline);
        TextView crossedBaselineAction = (TextView) findViewById(R.id.info_match_autoCrossedBaselineAction);

        if (data.hasCrossedBaseline()) {
            crossedBaseline.setText("CROSSED ");
            crossedBaselineAction.setText("the baseline");
        } else {
            crossedBaseline.setVisibility(View.GONE);
            crossedBaselineAction.setText("Did not cross the baseline");
        }

        // --- Teleop ---
        TextView teleopGearsDelivered = (TextView) findViewById(R.id.info_match_teleopGearsDelivered);
        teleopGearsDelivered.setText(data.getTeleopGearsDelivered() + "");

        TextView teleopFuelDumpAmount = (TextView) findViewById(R.id.info_match_teleopNumberOfLowGoalFuelDumps);
        teleopFuelDumpAmount.setText(data.getTeleopLowGoalDumps().size() + "");

        RecyclerView listFuelDumps = (RecyclerView) findViewById(R.id.info_match_teleopListFuelDumps);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listFuelDumps.setLayoutManager(mLayoutManager);

        FuelDumpAdapter listDumpsAdapter = new FuelDumpAdapter(data.getTeleopLowGoalDumps());
        listFuelDumps.setAdapter(listDumpsAdapter);

        TextView teleopHighGoals = (TextView) findViewById(R.id.info_match_teleopHighGoals);
        teleopHighGoals.setText(data.getAutoHighGoals() + "");

        TextView teleopMissedGoals = (TextView) findViewById(R.id.info_match_teleopMissedHighGoals);
        teleopMissedGoals.setText(data.getAutoMissedHighGoals() + "");

        TextView climbingStats = (TextView) findViewById(R.id.info_match_teleopClimbingStats);
        TextView climbingStatsAction = (TextView) findViewById(R.id.info_match_teleopClimbingStatsAction);

        if (data.getClimbingStats() == ClimbingStats.PRESSED_TOUCHPAD) {
            climbingStats.setText("PRESSED ");
            climbingStatsAction.setText("the touchpad");
        } else if (data.getClimbingStats() == ClimbingStats.ATTEMPTED_CLIMB) {
            climbingStats.setText("ATTEMPTED TO CLIMB ");
            climbingStatsAction.setText("the airship");
        } else {
            climbingStats.setVisibility(View.GONE);
            climbingStatsAction.setText("Did not climb the airship");
        }

        // --- Summary ---
        TextView troubleWith = (TextView) findViewById(R.id.info_match_summaryTroubleWith);
        if (data.getTroubleWith() != null && !data.getTroubleWith().equals("")) {
            troubleWith.setText(data.getTroubleWith());
        } else {
            troubleWith.setText("None");
        }

        TextView comments = (TextView) findViewById(R.id.info_match_summaryComments);
        if (data.getComments() != null  && !data.getComments().equals("")) {
            comments.setText(data.getComments());
        } else {
            comments.setText("None");
        }
    }
}

