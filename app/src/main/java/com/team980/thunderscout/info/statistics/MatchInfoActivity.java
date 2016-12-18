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
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.enumeration.ScalingStats;

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
            autoDefenseImage.setImageResource(R.drawable.portcullis); //TODO yes, I know it's hardcoded
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
        TextView teleopTotalDefenses = (TextView) findViewById(R.id.info_match_teleopTotalDefenses);
        teleopTotalDefenses.setText(data.getTeleopDefenseCrossings().size() + "");

        RecyclerView listDefensesCrossed = (RecyclerView) findViewById(R.id.info_match_teleopListDefenseCrossings);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listDefensesCrossed.setLayoutManager(mLayoutManager);

        DefenseAdapter listDefensesAdapter = new DefenseAdapter(data.getTeleopDefenseCrossings());
        listDefensesCrossed.setAdapter(listDefensesAdapter);

        TextView teleopTotalGoals = (TextView) findViewById(R.id.info_match_teleopTotalGoals);
        totalGoals = data.getTeleopLowGoals() + data.getTeleopHighGoals();
        teleopTotalGoals.setText(totalGoals + "");

        TextView teleopLowGoals = (TextView) findViewById(R.id.info_match_teleopLowGoals);
        teleopLowGoals.setText(data.getTeleopLowGoals() + "");

        TextView teleopHighGoals = (TextView) findViewById(R.id.info_match_teleopHighGoals);
        teleopHighGoals.setText(data.getTeleopHighGoals() + "");

        TextView teleopMissedGoals = (TextView) findViewById(R.id.info_match_teleopMissedGoals);
        teleopMissedGoals.setText(data.getTeleopMissedGoals() + "");

        // --- Summary ---
        TextView scalingStats = (TextView) findViewById(R.id.info_match_summaryScalingStats);
        TextView scalingStatsAction = (TextView) findViewById(R.id.info_match_summaryScalingAction);

        if (data.getScalingStats() == ScalingStats.NONE) {
            scalingStats.setVisibility(View.GONE);
            scalingStatsAction.setText("Did not scale the tower");
        } else if (data.getScalingStats() == ScalingStats.PARTIAL) {
            scalingStats.setText("PARTIALLY SCALED");
        } else if (data.getScalingStats() == ScalingStats.FULL) {
            scalingStats.setText("FULLY SCALED");
        }

        TextView challengedTower = (TextView) findViewById(R.id.info_match_summaryChallengedTower);
        TextView challengeAction = (TextView) findViewById(R.id.info_match_summaryChallengeAction);

        if (data.hasChallengedTower()) {
            challengedTower.setText("CHALLENGED");
        } else {
            challengedTower.setVisibility(View.GONE);
            challengeAction.setText("Did not challenge the tower");
        }

        TextView troubleWith = (TextView) findViewById(R.id.info_match_summaryTroubleWith);
        if (data.getTroubleWith() != null) {
            troubleWith.setText(data.getTroubleWith());
        } else {
            troubleWith.setText("None");
        }

        TextView comments = (TextView) findViewById(R.id.info_match_summaryComments);
        if (data.getComments() != null) {
            comments.setText(data.getComments());
        } else {
            comments.setText("None");
        }
    }
}

