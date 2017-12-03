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
import com.team980.thunderscout.analytics.rankings.TeamPointEstimator;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TeamInfoActivity extends AppCompatActivity {

    public static final String EXTRA_AVERAGE_SCOUT_DATA = "com.team980.thunderscout.AVERAGE_SCOUT_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_team);

        Intent launchIntent = getIntent();
        AverageScoutData data = (AverageScoutData) launchIntent.getSerializableExtra(EXTRA_AVERAGE_SCOUT_DATA);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Team Info: Team " + data.getTeam());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Team Info: Team " + data.getTeam(),
                    current.getIcon());

            setTaskDescription(taskDesc);
        }

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // Init
        TextView lastUpdated = findViewById(R.id.info_team_lastUpdated);
        lastUpdated.setText(SimpleDateFormat.getDateTimeInstance().format(data.getLastUpdated()));

        // Auto
        TextView crossPercent = findViewById(R.id.info_team_autoCrossPercentage);
        crossPercent.setText(formatter.format(data.getCrossedBaselinePercentage()) + "%");

        TextView mobility = findViewById(R.id.info_team_autoMobilityPoints);
        mobility.setText(formatter.format(TeamPointEstimator.getBaselinePoints(data)) + " pts");

        TextView autoLowGoalCount = findViewById(R.id.info_team_autoLowGoalCount);
        autoLowGoalCount.setText(formatter.format((data.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + data.getAverageAutoLowGoalDumpAmount().getMaximumAmount()) / 2));

        TextView autoHighGoalCount = findViewById(R.id.info_team_autoHighGoalCount);
        autoHighGoalCount.setText(formatter.format(data.getAverageAutoHighGoals()));

        TextView autoMissedGoalCount = findViewById(R.id.info_team_autoMissedGoalCount);
        autoMissedGoalCount.setText(formatter.format(data.getAverageAutoMissedHighGoals()));

        TextView autoFuelPoints = findViewById(R.id.info_team_autoFuelPoints);
        autoFuelPoints.setText(formatter.format(TeamPointEstimator.getAutoFuelPoints(data)) + " pts");

        TextView autoGearDeliveryCount = findViewById(R.id.info_team_autoGearDeliveryCount);
        autoGearDeliveryCount.setText(formatter.format(data.getAverageAutoGearsDelivered()));

        TextView autoGearDropCount = findViewById(R.id.info_team_autoGearDropCount);
        autoGearDropCount.setText(formatter.format(data.getAverageAutoGearsDropped()));

        TextView autoRotorPoints = findViewById(R.id.info_team_autoRotorPoints);
        autoRotorPoints.setText(formatter.format(TeamPointEstimator.getAutoRotorPoints(data)) + " pts");

        // Teleop
        TextView teleopLowGoalCount = findViewById(R.id.info_team_teleopLowGoalCount);
        // ugh I hate FuelDumpAmount soooo much
        float low = (data.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + data.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low *= data.getAverageTeleopDumpFrequency(); /// times average dump amount
        teleopLowGoalCount.setText(formatter.format(low));

        TextView teleopHighGoalCount = findViewById(R.id.info_team_teleopHighGoalCount);
        teleopHighGoalCount.setText(formatter.format(data.getAverageTeleopHighGoals()));

        TextView teleopMissedGoalCount = findViewById(R.id.info_team_teleopMissedGoalCount);
        teleopMissedGoalCount.setText(formatter.format(data.getAverageTeleopMissedHighGoals()));

        TextView teleopFuelPoints = findViewById(R.id.info_team_teleopFuelPoints);
        teleopFuelPoints.setText(formatter.format(TeamPointEstimator.getTeleopFuelPoints(data)) + " pts");

        TextView teleopGearDeliveryCount = findViewById(R.id.info_team_teleopGearDeliveryCount);
        teleopGearDeliveryCount.setText(formatter.format(data.getAverageTeleopGearsDelivered()));

        TextView teleopGearDropCount = findViewById(R.id.info_team_teleopGearDropCount);
        teleopGearDropCount.setText(formatter.format(data.getAverageTeleopGearsDropped()));

        TextView teleopRotorPoints = findViewById(R.id.info_team_teleopRotorPoints);
        teleopRotorPoints.setText(formatter.format(TeamPointEstimator.getTeleopRotorPoints(data)) + " pts");

        TextView climbPercent = findViewById(R.id.info_team_teleopClimbPercentage);
        climbPercent.setText(formatter.format(data.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");

        TextView climbPts = findViewById(R.id.info_team_teleopClimbPoints);
        climbPts.setText(formatter.format(TeamPointEstimator.getClimbingPoints(data)) + " pts");

        // Summary
        TextView rankingPoints = findViewById(R.id.info_team_rankingPoints);
        rankingPoints.setText(formatter.format(TeamPointEstimator.getRankingPoints(data)) + " pts");

        TextView total = findViewById(R.id.info_team_totalPoints);
        total.setText(formatter.format(TeamPointEstimator.getPointContribution(data)) + " pts");

        RecyclerView troubleWith = findViewById(R.id.info_team_troubleWith);
        TextView troubleWithPlaceholder = findViewById(R.id.info_team_troubleWithPlaceholder);

        if (data.getTroublesList() == null || data.getTroublesList().isEmpty() || listIsEmpty(data.getTroublesList())) {
            troubleWith.setVisibility(View.GONE);
            troubleWithPlaceholder.setVisibility(View.VISIBLE);
        } else {
            troubleWith.setVisibility(View.VISIBLE);
            troubleWithPlaceholder.setVisibility(View.GONE);

            troubleWith.setLayoutManager(new LinearLayoutManager(this));
            troubleWith.setAdapter(new CommentsAdapter(data.getTroublesList()));
        }

        RecyclerView comments = findViewById(R.id.info_team_comments);
        TextView commentsPlaceholder = findViewById(R.id.info_team_commentsPlaceholder);

        if (data.getCommentsList() == null || data.getCommentsList().isEmpty() || listIsEmpty(data.getCommentsList())) {
            comments.setVisibility(View.GONE);
            commentsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.VISIBLE);
            commentsPlaceholder.setVisibility(View.GONE);

            comments.setLayoutManager(new LinearLayoutManager(this));
            comments.setAdapter(new CommentsAdapter(data.getCommentsList()));
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

    private boolean listIsEmpty(List<String> list) {
        for (String s : list) {
            if (s != null && !s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}