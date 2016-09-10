package com.team980.thunderscout.info;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.team980.thunderscout.match.AutoFragment;
import com.team980.thunderscout.match.SummaryFragment;
import com.team980.thunderscout.match.TeleopFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private String tabTitles[] = new String[]{"Auto", "Teleop", "Summary"};
    private Fragment tabs[];

    public ViewPagerAdapter(final FragmentManager fm) {
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

