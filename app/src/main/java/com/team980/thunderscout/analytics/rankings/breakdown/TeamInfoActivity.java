/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.team980.thunderscout.R;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.util.TransitionUtils;

import java.util.List;

public class TeamInfoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String EXTRA_SCOUT_DATA_LIST = "com.team980.thunderscout.SCOUT_DATA_LIST";

    private Toolbar toolbar;
    private List<ScoutData> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_team);

        Intent launchIntent = getIntent();
        dataList = (List<ScoutData>) launchIntent.getSerializableExtra(EXTRA_SCOUT_DATA_LIST);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Team Info: Team " + dataList.get(0).getTeam());

        ViewPager viewPager = findViewById(R.id.view_pager);

        TeamInfoViewPagerAdapter viewPagerAdapter = new TeamInfoViewPagerAdapter(getSupportFragmentManager(), dataList);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Team Info: Team " + dataList.get(0).getTeam(),
                    current.getIcon());

            setTaskDescription(taskDesc);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Do nothing
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            int toolbarColor = ((ColorDrawable) findViewById(R.id.toolbar).getBackground()).getColor();

            int statusBarColor;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = getWindow().getStatusBarColor();
            } else {
                statusBarColor = getResources().getColor(R.color.primary_dark);
            }

            TransitionUtils.toolbarAndStatusBarTransition(toolbarColor, statusBarColor,
                    getResources().getColor(R.color.primary),
                    getResources().getColor(R.color.primary_dark), this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
                ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Team Info: Team " + dataList.get(0).getTeam(),
                        current.getIcon(), getResources().getColor(R.color.primary));
                setTaskDescription(taskDesc);
            }
        } else {
            int toolbarColor = ((ColorDrawable) findViewById(R.id.toolbar).getBackground()).getColor();

            int statusBarColor;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = getWindow().getStatusBarColor();
            } else {
                statusBarColor = getResources().getColor(R.color.primary_dark);
            }

            TransitionUtils.toolbarAndStatusBarTransition(toolbarColor, statusBarColor,
                    getResources().getColor(dataList.get(position - 1).getAllianceStation().getColor().getColorPrimary()),
                    getResources().getColor(dataList.get(position - 1).getAllianceStation().getColor().getColorPrimaryDark()), this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
                ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription("Team Info: Team " + dataList.get(0).getTeam(),
                        current.getIcon(), getResources().getColor(dataList.get(position - 1).getAllianceStation().getColor().getColorPrimary()));
                setTaskDescription(taskDesc);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Do nothing
    }
}