package com.team980.thunderscout.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;

import java.text.SimpleDateFormat;

@Deprecated
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launchIntent = getIntent();

        ScoutData data = (ScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_SCOUT");

        setTitle("Match Info: " + SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));

        setContentView(R.layout.activity_info_scout);

        TextView teamNumber = (TextView) findViewById(R.id.info_teamNumber);
        teamNumber.setText("Team " + data.getTeamNumber());

        TextView dateAdded = (TextView) findViewById(R.id.info_dateAdded);
        dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));

        TextView dataSource = (TextView) findViewById(R.id.info_dataSource);
        dataSource.setText("Source: " + data.getDataSource());

        /*TextView autoDefenseStats = (TextView) findViewById(R.id.info_autoDefenseStats);
        if (data.getAutoCrossingStats() == CrossingStats.NONE) {
            autoDefenseStats.setText("Did not cross a defense");
        } else {
            autoDefenseStats.setText(data.getAutoCrossingStats().toString() + " the " + data.getAutoDefenseCrossed().toString());
        }

        TextView autoScoringStats = (TextView) findViewById(R.id.info_autoScoringStats);
        if (data.getAutoScoringStats() == ScoringStats.NONE) {
            autoScoringStats.setText("Did not score a goal");
        } else {
            autoScoringStats.setText("Scored a " + data.getAutoScoringStats());
        }

        TextView defensesBreached = (TextView) findViewById(R.id.info_teleopDefensesBreached);
        defensesBreached.setText("Breached " + data.getTeleopDefensesBreached() + " defenses");

        RecyclerView listDefensesBreached = (RecyclerView) findViewById(R.id.info_teleopListDefensesBreached);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listDefensesBreached.setLayoutManager(mLayoutManager);

        RankedDefenseAdapter listDefensesAdapter = new RankedDefenseAdapter(data.getTeleopMapDefensesBreached());
        listDefensesBreached.setAdapter(listDefensesAdapter);

        TextView goalsScored = (TextView) findViewById(R.id.info_teleopGoalsScored);
        goalsScored.setText("Scored " + data.getTeleopGoalsScored() + " boulders");

        if (data.getTeleopLowGoals()) {
            LinearLayout lowGoalContainer = (LinearLayout) findViewById(R.id.info_teleopLowGoalContainer);
            lowGoalContainer.setVisibility(View.VISIBLE);

            TextView lowGoalRank = (TextView) findViewById(R.id.info_teleopLowGoalRank);
            lowGoalRank.setText(data.getTeleopLowGoalRank().getDescription());
        }

        if (data.getTeleopHighGoals()) {
            LinearLayout highGoalContainer = (LinearLayout) findViewById(R.id.info_teleopHighGoalContainer);
            highGoalContainer.setVisibility(View.VISIBLE);

            TextView highGoalRank = (TextView) findViewById(R.id.info_teleopHighGoalRank);
            highGoalRank.setText(data.getTeleopHighGoalRank().getDescription());
        }

        TextView driverSkill = (TextView) findViewById(R.id.info_driverSkill);
        driverSkill.setText(data.getTeleopDriverSkill().getDescription());

        TextView comments = (TextView) findViewById(R.id.info_comments);
        comments.setText(data.getTeleopComments());*/
    }
}

