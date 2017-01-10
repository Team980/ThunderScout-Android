package com.team980.thunderscout.info.statistics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TeamInfoViewPagerAdapter extends FragmentPagerAdapter {


    private String tabTitles[] = new String[]{"Average", "Variability", "Trend"};
    private Fragment tabs[];

    public TeamInfoViewPagerAdapter(final FragmentManager fm) {
        super(fm);

        tabs = new Fragment[]{new AverageTeamInfoFragment(), new VariabilityTeamInfoFragment(), new TrendTeamInfoFragment()};
    }

    @Override
    public Fragment getItem(int position) {
        return tabs[position];
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

