package com.team980.thunderscout.match;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ScoutingFlowViewPagerAdapter extends FragmentPagerAdapter {


    private String tabTitles[] = new String[]{"Auto", "Teleop", "Summary"};
    private Fragment tabs[];

    public ScoutingFlowViewPagerAdapter(final FragmentManager fm) {
        super(fm);

        tabs = new Fragment[]{new AutoFragment(), new TeleopFragment(), new SummaryFragment()};
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

