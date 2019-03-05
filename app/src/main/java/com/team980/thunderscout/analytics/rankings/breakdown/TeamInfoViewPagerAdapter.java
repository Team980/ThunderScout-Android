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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.team980.thunderscout.analytics.matches.breakdown.MatchInfoFragment;
import com.team980.thunderscout.schema.ScoutData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamInfoViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> tabs;
    private List<ScoutData> matches;

    public TeamInfoViewPagerAdapter(final FragmentManager fm, List<ScoutData> dataList) {
        super(fm);

        tabs = new ArrayList<>();
        matches = dataList;

        Collections.sort(matches, (o1, o2) -> Integer.compare(o1.getMatchNumber(), o2.getMatchNumber()));

        TeamInfoFragment teamInfo = new TeamInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable(TeamInfoFragment.EXTRA_SCOUT_DATA_LIST, (Serializable) dataList);
        teamInfo.setArguments(args);

        tabs.add(teamInfo);

        for (ScoutData data : dataList) {
            MatchInfoFragment matchInfo = new MatchInfoFragment();

            args = new Bundle();
            args.putSerializable(MatchInfoFragment.EXTRA_SCOUT_DATA, data);
            matchInfo.setArguments(args);

            tabs.add(matchInfo);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }


    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = tabs.get(position);

        if (fragment instanceof TeamInfoFragment) {
            return "Overall Summary";
        } else if (fragment instanceof MatchInfoFragment) {
            return "Match " + matches.get(position - 1).getMatchNumber();
        } else {
            return "Error";
        }
    }
}
