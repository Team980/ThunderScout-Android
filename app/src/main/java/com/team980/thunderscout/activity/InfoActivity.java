package com.team980.thunderscout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.adapter.DefenseAdapter;
import com.team980.thunderscout.adapter.RankedDefenseAdapter;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.TeamWrapper;
import com.team980.thunderscout.data.enumeration.CrossingStats;
import com.team980.thunderscout.data.enumeration.ScoringStats;

import java.text.SimpleDateFormat;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launchIntent = getIntent();
        boolean isAggregate = launchIntent.getBooleanExtra("com.team980.thunderscout.IS_AGGREGATE_DATA", false); //TODO sensible default

        if (isAggregate) {
            TeamWrapper team = (TeamWrapper) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_TEAM");

            setTitle("Team Info: Team " + team.getTeamNumber());

            setContentView(R.layout.activity_info_team);

            TextView numberOfMatches = (TextView) findViewById(R.id.info_numberOfMatches);
            numberOfMatches.setText(team.getNumberOfMatches() + " matches");

            //TODO AverageCrossingStats
            //TODO AverageScoringStats

            RecyclerView autoDefensesCrossed = (RecyclerView) findViewById(R.id.info_autoListCumulativeDefensesCrossed);
            autoDefensesCrossed.setLayoutManager(new LinearLayoutManager(this));

            DefenseAdapter listDefensesAdapter = new DefenseAdapter(team.getAverageScoutData().getCumulativeAutoDefensesCrossed());
            autoDefensesCrossed.setAdapter(listDefensesAdapter);

            TextView avgDefensesBreached = (TextView) findViewById(R.id.info_teleopAverageDefensesBreached);
            avgDefensesBreached.setText("Breached an average of "
                    + team.getAverageScoutData().getAverageTeleopDefensesBreached() + " defenses each match");

            TextView totalDefensesBreached = (TextView) findViewById(R.id.info_teleopCumulativeDefensesBreached);
            totalDefensesBreached.setText("Breached a total of "
                    + team.getAverageScoutData().getCumulativeTeleopDefensesBreached() + " defenses");

            TextView avgGoalsScored = (TextView) findViewById(R.id.info_teleopAverageGoalsScored);
            avgGoalsScored.setText("Scored an average of "
                    + team.getAverageScoutData().getAverageTeleopGoalsScored() + " boulders each match");

            TextView totalGoalsScored = (TextView) findViewById(R.id.info_teleopCumulativeGoalsScored);
            totalGoalsScored.setText("Scored a total of "
                    + team.getAverageScoutData().getCumulativeTeleopGoalsScored() + " boulders");

            //RecyclerView listDefensesBreached = (RecyclerView) findViewById(R.id.info_teleopListCumulativeDefensesBreached);
            //listDefensesBreached.setLayoutManager(new LinearLayoutManager(this));

            //TODO CumulativeListDefensesBreached
            //RankedDefenseAdapter listDefensesAdapter =
                    //new RankedDefenseAdapter(team.getAverageScoutData().getCumulativeTeleopDefensesCrossed());
            //listDefensesBreached.setAdapter(listDefensesAdapter);

            if (team.getAverageScoutData().getTeleopLowGoals()) {
                LinearLayout lowGoalContainer = (LinearLayout) findViewById(R.id.info_teleopLowGoalContainer);
                lowGoalContainer.setVisibility(View.VISIBLE);

                //TODO AverageRank
            }

            if (team.getAverageScoutData().getTeleopHighGoals()) {
                LinearLayout highGoalContainer = (LinearLayout) findViewById(R.id.info_teleopHighGoalContainer);
                highGoalContainer.setVisibility(View.VISIBLE);

                //TODO AverageRank

            }

            //TODO DriverSkillRank

            //TODO CommentsList
        } else {
            ScoutData data = (ScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_SCOUT");

            setTitle("Match Info: " + SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));

            setContentView(R.layout.activity_info_scout);

            TextView teamNumber = (TextView) findViewById(R.id.info_teamNumber);
            teamNumber.setText(data.getTeamNumber());

            TextView dateAdded = (TextView) findViewById(R.id.info_dateAdded);
            dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));

            TextView autoDefenseStats = (TextView) findViewById(R.id.info_autoDefenseStats);
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
            comments.setText(data.getTeleopComments());
        }
    }
}
