/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.analytics.rankings.breakdown;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.ScoutDataStatistics;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TeamInfoActivity extends AppCompatActivity {

    public static final String EXTRA_SCOUT_DATA_LIST = "com.team980.thunderscout.SCOUT_DATA_LIST";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_team);

        Intent launchIntent = getIntent();
        ArrayList<ScoutData> dataList = (ArrayList<ScoutData>) launchIntent.getSerializableExtra(EXTRA_SCOUT_DATA_LIST);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Team Info: Team " + dataList.get(0).getTeam());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Team Info: Team " + dataList.get(0).getTeam(),
                    current.getIcon());

            setTaskDescription(taskDesc);
        }

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // Init
        TextView lastUpdated = findViewById(R.id.info_team_lastUpdated);
        lastUpdated.setText(SimpleDateFormat.getDateTimeInstance().format(ScoutDataStatistics.getLastUpdated(dataList)));

        // Auto
        TextView crossPercent = findViewById(R.id.info_team_autoCrossPercentage);
        crossPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getAutonomous().crossedAutoLine())) + "%");

        TextView auto_powerCubeAllianceSwitchAverage = findViewById(R.id.info_team_autoPowerCubeAllianceSwitchAverage);
        auto_powerCubeAllianceSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getAutonomous().getPowerCubeAllianceSwitchCount())));

        TextView auto_powerCubeScaleAverage = findViewById(R.id.info_team_autoPowerCubeScaleAverage);
        auto_powerCubeScaleAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getAutonomous().getPowerCubeScaleCount())));

        TextView auto_powerCubePlayerStationAverage = findViewById(R.id.info_team_autoPowerCubePlayerStationAverage);
        auto_powerCubePlayerStationAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getAutonomous().getPowerCubePlayerStationCount())));

        // Teleop
        TextView teleop_powerCubeAllianceSwitchAverage = findViewById(R.id.info_team_teleopPowerCubeAllianceSwitchAverage);
        teleop_powerCubeAllianceSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubeAllianceSwitchCount())));

        TextView teleop_powerCubeScaleAverage = findViewById(R.id.info_team_teleopPowerCubeScaleAverage);
        teleop_powerCubeScaleAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubeScaleCount())));

        TextView teleop_powerCubeOpposingSwitchAverage = findViewById(R.id.info_team_teleopPowerCubeOpposingSwitchAverage);
        teleop_powerCubeOpposingSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubeOpposingSwitchCount())));

        TextView teleop_powerCubePlayerStationAverage = findViewById(R.id.info_team_teleopPowerCubePlayerStationAverage);
        teleop_powerCubePlayerStationAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubePlayerStationCount())));

        TextView climbPercent = findViewById(R.id.info_team_teleopClimbPercentage);
        climbPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getTeleop().getClimbingStats() == ClimbingStats.CLIMBED)) + "%");

        TextView supportedRobotPercent = findViewById(R.id.info_team_teleopSupportedRobotPercent);
        supportedRobotPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getTeleop().supportedOtherRobotWhenClimbing())) + "%");

        // Summary
        RecyclerView strategies = findViewById(R.id.info_team_strategies);
        TextView strategiesPlaceholder = findViewById(R.id.info_team_strategiesPlaceholder);

        List<String> strategiesList = ScoutDataStatistics.getStringList(dataList,
                data -> "[" + data.getMatchNumber() + "] " + data.getStrategies());
        if (strategiesList == null || strategiesList.isEmpty() || listContentsAreEmpty(strategiesList)) {
            strategies.setVisibility(View.GONE);
            strategiesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            strategies.setVisibility(View.VISIBLE);
            strategiesPlaceholder.setVisibility(View.GONE);

            strategies.setLayoutManager(new LinearLayoutManager(this));
            strategies.setAdapter(new CommentsAdapter(strategiesList));
        }

        RecyclerView difficulties = findViewById(R.id.info_team_difficulties);
        TextView difficultiesPlaceholder = findViewById(R.id.info_team_difficultiesPlaceholder);

        List<String> difficultiesList = ScoutDataStatistics.getStringList(dataList,
                data -> "[" + data.getMatchNumber() + "] " + data.getDifficulties());
        if (difficultiesList == null || difficultiesList.isEmpty() || listContentsAreEmpty(difficultiesList)) {
            difficulties.setVisibility(View.GONE);
            difficultiesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            difficulties.setVisibility(View.VISIBLE);
            difficultiesPlaceholder.setVisibility(View.GONE);

            difficulties.setLayoutManager(new LinearLayoutManager(this));
            difficulties.setAdapter(new CommentsAdapter(difficultiesList));
        }

        RecyclerView comments = findViewById(R.id.info_team_comments);
        TextView commentsPlaceholder = findViewById(R.id.info_team_commentsPlaceholder);

        List<String> commentsList = ScoutDataStatistics.getStringList(dataList,
                data -> "[" + data.getMatchNumber() + "] " + data.getComments());
        if (commentsList == null || commentsList.isEmpty() || listContentsAreEmpty(commentsList)) {
            comments.setVisibility(View.GONE);
            commentsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.VISIBLE);
            commentsPlaceholder.setVisibility(View.GONE);

            comments.setLayoutManager(new LinearLayoutManager(this));
            comments.setAdapter(new CommentsAdapter(commentsList));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean listContentsAreEmpty(List<String> list) {
        for (String s : list) {
            if (s != null && !s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}