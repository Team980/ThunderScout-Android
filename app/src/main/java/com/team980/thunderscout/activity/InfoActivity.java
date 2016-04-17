package com.team980.thunderscout.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.object.TeamWrapper;

import java.text.SimpleDateFormat;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launchIntent = getIntent();
        boolean isAggregate = launchIntent.getBooleanExtra("com.team980.thunderscout.IS_AGGREGATE_DATA", false); //TODO sensible default

        if (isAggregate) {
            TeamWrapper team = (TeamWrapper) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_TEAM");

            setContentView(R.layout.activity_info_team);

            TextView teamNumber = (TextView) findViewById(R.id.info_teamNumber);
            teamNumber.setText(team.getTeamNumber());
        } else {
            ScoutData data = (ScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_SCOUT");

            setContentView(R.layout.activity_info_scout);

            TextView teamNumber = (TextView) findViewById(R.id.info_teamNumber);
            teamNumber.setText(data.getTeamNumber());

            TextView dateAdded = (TextView) findViewById(R.id.info_dateAdded);
            dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDateAdded()));
        }
    }
}
