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

package com.team980.thunderscout.analytics.matches.breakdown;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.schema.ScoutData;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MatchInfoActivity extends AppCompatActivity {

    public static final String EXTRA_SCOUT_DATA = "com.team980.thunderscout.SCOUT_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_match);

        Intent launchIntent = getIntent();
        ScoutData data = (ScoutData) launchIntent.getSerializableExtra(EXTRA_SCOUT_DATA);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Match Info: Team " + data.getTeam());
        getSupportActionBar().setSubtitle("Qualification Match " + data.getMatchNumber());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Match Info: Team " + data.getTeam(),
                    current.getIcon(), getResources().getColor(data.getAllianceStation().getColor().getColorPrimary()));
            setTaskDescription(taskDesc);
        }

        toolbar.setBackground(new ColorDrawable(getResources().getColor(data.getAllianceStation().getColor().getColorPrimary())));
        findViewById(R.id.app_bar_layout).setBackground(new ColorDrawable(getResources().getColor(data.getAllianceStation().getColor().getColorPrimary())));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(data.getAllianceStation().getColor().getColorPrimaryDark()));
        }

        // Init
        TextView date = findViewById(R.id.info_match_date);
        date.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDate()));

        TextView source = findViewById(R.id.info_match_source);
        source.setText(data.getSource());

        // Auto
        TextView crossedAutoLine = findViewById(R.id.info_match_autoCrossedAutoLine);
        crossedAutoLine.setText(String.valueOf(data.getAutonomous().crossedAutoLine()).toUpperCase(Locale.ROOT));

        TextView auto_powerCubeAllianceSwitchCount = findViewById(R.id.info_match_autoPowerCubeAllianceSwitchCount);
        auto_powerCubeAllianceSwitchCount.setText(data.getAutonomous().getPowerCubeAllianceSwitchCount() + "");

        TextView auto_powerCubeScaleCount = findViewById(R.id.info_match_autoPowerCubeScaleCount);
        auto_powerCubeScaleCount.setText(data.getAutonomous().getPowerCubeScaleCount() + "");

        TextView auto_powerCubePlayerStationCount = findViewById(R.id.info_match_autoPowerCubePlayerStationCount);
        auto_powerCubePlayerStationCount.setText(data.getAutonomous().getPowerCubePlayerStationCount() + "");

        // Teleop
        TextView teleop_powerCubeAllianceSwitchCount = findViewById(R.id.info_match_teleopPowerCubeAllianceSwitchCount);
        teleop_powerCubeAllianceSwitchCount.setText(data.getTeleop().getPowerCubeAllianceSwitchCount() + "");

        TextView teleop_powerCubeScaleCount = findViewById(R.id.info_match_teleopPowerCubeScaleCount);
        teleop_powerCubeScaleCount.setText(data.getTeleop().getPowerCubeScaleCount() + "");

        TextView teleop_powerCubeOpposingSwitchCount = findViewById(R.id.info_match_teleopPowerCubeOpposingSwitchCount);
        teleop_powerCubeOpposingSwitchCount.setText(data.getTeleop().getPowerCubeOpposingSwitchCount() + "");

        TextView teleop_powerCubePlayerStationCount = findViewById(R.id.info_match_teleopPowerCubePlayerStationCount);
        teleop_powerCubePlayerStationCount.setText(data.getTeleop().getPowerCubePlayerStationCount() + "");

        TextView climbingStats = findViewById(R.id.info_match_teleopClimbingStats);
        climbingStats.setText(data.getTeleop().getClimbingStats().toString().toUpperCase());

        TextView supportedOtherRobotsWhenClimbing = findViewById(R.id.info_match_teleopSupportedOtherRobotsWhenClimbing);
        supportedOtherRobotsWhenClimbing.setText(String.valueOf(data.getTeleop().supportedOtherRobotWhenClimbing()).toUpperCase(Locale.ROOT));

        // Summary
        TextView strategies = findViewById(R.id.info_match_strategies);
        if (data.getStrategies() != null && !data.getStrategies().isEmpty()) {
            strategies.setText(data.getStrategies());
        } else {
            strategies.setText("N/A");
        }

        TextView difficulties = findViewById(R.id.info_match_difficulties);
        if (data.getDifficulties() != null && !data.getDifficulties().isEmpty()) {
            difficulties.setText(data.getDifficulties());
        } else {
            difficulties.setText("N/A");
        }

        TextView comments = findViewById(R.id.info_match_comments);
        if (data.getComments() != null && !data.getComments().isEmpty()) {
            comments.setText(data.getComments());
        } else {
            comments.setText("N/A");
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
}
