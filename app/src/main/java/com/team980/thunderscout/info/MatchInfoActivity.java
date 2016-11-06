package com.team980.thunderscout.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.Defense;

import java.text.SimpleDateFormat;

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
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle("Match Info: Team " + data.getTeamNumber());

        // --- Init ---
        TextView dateAdded = (TextView) findViewById(R.id.info_match_dateAdded);
        dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));

        TextView dataSource = (TextView) findViewById(R.id.info_match_dataSource);
        dataSource.setText("Source: " + data.getDataSource());

        // --- Auto ---
        TextView autoDefenseCrossingAction = (TextView) findViewById(R.id.info_match_autoDefenseCrossingAction);
        TextView autoDefenseCrossed = (TextView) findViewById(R.id.info_match_autoDefenseCrossed);

        ImageView autoDefenseImage = (ImageView) findViewById(R.id.info_match_autoDefenseImage);
        FrameLayout autoDefenseImageContainer = (FrameLayout) findViewById(R.id.info_match_autoDefenseImageContainer);

        if (data.getAutoDefenseCrossed() == Defense.NONE) {
            autoDefenseCrossingAction.setText("Did not cross a defense");
            autoDefenseCrossed.setVisibility(View.GONE);
            autoDefenseImageContainer.setVisibility(View.GONE);
        } else {
            autoDefenseCrossingAction.setText("Crossed the");
            autoDefenseCrossed.setText(data.getAutoDefenseCrossed().toString().toUpperCase());
            autoDefenseImage.setImageResource(Defense.PORTCULLIS.getCounterId()); //TODO yes, I know it's hardcoded
        }

        //TODO use @strings with inputs as Spannables for in-view styling

        TextView autoTotalGoals = (TextView) findViewById(R.id.info_match_autoTotalGoals);
        int totalGoals = data.getAutoLowGoals() + data.getAutoHighGoals();
        autoTotalGoals.setText(totalGoals + "");

        TextView autoLowGoals = (TextView) findViewById(R.id.info_match_autoLowGoals);
        autoLowGoals.setText(data.getAutoLowGoals() + "");

        TextView autoHighGoals = (TextView) findViewById(R.id.info_match_autoHighGoals);
        autoHighGoals.setText(data.getAutoHighGoals() + "");

        TextView autoMissedGoals = (TextView) findViewById(R.id.info_match_autoMissedGoals);
        autoMissedGoals.setText(data.getAutoMissedGoals() + "");

        // --- Teleop ---
        /*TextView defensesBreached = (TextView) findViewById(R.id.info_teleopDefensesCrossed);
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

