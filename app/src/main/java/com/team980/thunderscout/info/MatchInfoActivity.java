package com.team980.thunderscout.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;

public class MatchInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_match);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent launchIntent = getIntent();
        ScoutData data = (ScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_SCOUT");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle("Match Info: Team " + data.getTeamNumber());

        // --- Init ---
        /*TextView dateAdded = (TextView) findViewById(R.id.info_dateAdded);
        dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));

        TextView dataSource = (TextView) findViewById(R.id.info_dataSource);
        dataSource.setText("Source: " + data.getDataSource());

        // --- Auto ---
        TextView autoDefenseStats = (TextView) findViewById(R.id.info_autoDefenseCrossed);
        if (data.getAutoDefenseCrossed() == Defense.NONE) {
            autoDefenseStats.setText("Did not cross a defense");
        } else {
            autoDefenseStats.setText("Crossed the " + data.getAutoDefenseCrossed().name());
        }

        TextView autoLowGoals = (TextView) findViewById(R.id.info_autoScoringLow);
        autoLowGoals.setText("Scored " + data.getAutoLowGoals() + " low goals");

        TextView autoHighGoals = (TextView) findViewById(R.id.info_autoScoringHigh);
        autoHighGoals.setText("Scored " + data.getAutoHighGoals() + " high goals");

        TextView autoMissedGoals = (TextView) findViewById(R.id.info_autoScoringMissed);
        autoMissedGoals.setText("Missed " + data.getAutoMissedGoals() + " goals");

        // --- Teleop ---
        TextView defensesBreached = (TextView) findViewById(R.id.info_teleopDefensesCrossed);
        defensesBreached.setText("Crossed " + data.getTeleopDefenseCrossings().size() + " defenses");

        RecyclerView listDefensesCrossed = (RecyclerView) findViewById(R.id.info_teleopListDefenseCrossings);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listDefensesCrossed.setLayoutManager(mLayoutManager);

        DefenseAdapter listDefensesAdapter = new DefenseAdapter(data.getTeleopDefenseCrossings());
        listDefensesCrossed.setAdapter(listDefensesAdapter);

        TextView teleopLowGoals = (TextView) findViewById(R.id.info_teleopScoringLow);
        teleopLowGoals.setText("Scored " + data.getTeleopLowGoals() + " low goals");

        TextView teleopHighGoals = (TextView) findViewById(R.id.info_teleopScoringHigh);
        teleopHighGoals.setText("Scored " + data.getTeleopHighGoals() + " high goals");

        TextView teleopMissedGoals = (TextView) findViewById(R.id.info_teleopScoringMissed);
        teleopMissedGoals.setText("Missed " + data.getTeleopMissedGoals() + " goals");

        // --- Summary ---
        TextView scalingStats = (TextView) findViewById(R.id.info_scalingStats);
        if (data.getScalingStats() == ScalingStats.NONE) {
            scalingStats.setText("Did not scale the tower");
        } else if (data.getScalingStats() == ScalingStats.PARTIAL) {
            scalingStats.setText("Partially scaled the tower");
        } else if (data.getScalingStats() == ScalingStats.FULL) {
            scalingStats.setText("Fully scaled the tower");
        }

        TextView challengedTower = (TextView) findViewById(R.id.info_challengedTower);
        if (data.hasChallengedTower()) {
            challengedTower.setText("Challenged the tower");
        } else {
            challengedTower.setText("Did not challenge the tower");
        }

        TextView troubleWith = (TextView) findViewById(R.id.info_troubleWith);
        troubleWith.setText(data.getTroubleWith());

        TextView comments = (TextView) findViewById(R.id.info_comments);
        comments.setText(data.getComments());*/
    }
}

