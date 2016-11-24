package com.team980.thunderscout.info.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.AverageScoutData;

public class TeamInfoActivity extends AppCompatActivity {

    private AverageScoutData scoutData;

    private TeamInfoViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_team);

        Intent launchIntent = getIntent();
        scoutData = (AverageScoutData) launchIntent.getSerializableExtra("com.team980.thunderscout.INFO_AVERAGE_SCOUT");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Team Info: Team " + scoutData.getTeamNumber());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPagerAdapter = new TeamInfoViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public AverageScoutData getAverageScoutData() {
        return scoutData;
    }
}

