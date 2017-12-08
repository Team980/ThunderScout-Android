/*
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.analytics;

import com.team980.thunderscout.analytics.rankings.TeamPointEstimator;
import com.team980.thunderscout.analytics.rankings.breakdown.AverageScoutData;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Represents data for one team, for all the matches they played in.
 */
public class TeamWrapper implements Serializable { //TODO We shouldn't be creating this many AverageScoutData instances

    private String team;
    private ArrayList<ScoutData> dataList;

    private NumberFormat formatter; //TODO not Serializable?

    public TeamWrapper(String team) {
        this.team = team;
        dataList = new ArrayList<>();

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);
    }

    public String getTeam() {
        return team;
    }

    public ArrayList<ScoutData> getDataList() {
        return dataList;
    }

    public String getDescriptor(TeamComparator sortMode) { //TODO account for singular/plural?
        switch (sortMode) {
            case SORT_POINT_CONTRIBUTION:
                return formatter.format(TeamPointEstimator.getPointContribution(new AverageScoutData(dataList))) + " points";
            case SORT_TEAM_NUMBER:
                return "Last updated " + SimpleDateFormat.getDateTimeInstance().format(new AverageScoutData(dataList).getLastUpdated());
            case SORT_LAST_UPDATED:
                return "Last updated " + SimpleDateFormat.getDateTimeInstance().format(new AverageScoutData(dataList).getLastUpdated());
            case SORT_AUTO_GEARS_DELIVERED:
                return formatter.format(new AverageScoutData(dataList).getAverageAutoGearsDelivered()) + " gears delivered";
            //case SORT_AUTO_GEARS_DROPPED:
            //return formatter.format(new AverageScoutData(dataList).getAverageAutoGearsDropped()) + " gears dropped";
            //case SORT_AUTO_LOW_GOAL_DUMP_AMOUNT:
            //return new AverageScoutData(dataList).getAverageAutoLowGoalDumpAmount() + " amount of fuel dumped";
            case SORT_AUTO_HIGH_GOALS:
                return formatter.format(new AverageScoutData(dataList).getAverageAutoHighGoals()) + " high goals";
            //case SORT_AUTO_MISSED_HIGH_GOALS:
            //return formatter.format(new AverageScoutData(dataList).getAverageAutoHighGoals()) + " missed high goals";
            //case SORT_CROSSED_BASELINE_PERCENTAGE:
            //return "Crossed the baseline in " + formatter.format(new AverageScoutData(dataList).getCrossedBaselinePercentage()) + "% of matches";
            case SORT_TELEOP_GEARS_DELIVERED:
                return formatter.format(new AverageScoutData(dataList).getAverageTeleopGearsDelivered()) + " gears delivered";
            //case SORT_TELEOP_GEARS_DROPPED:
            //return formatter.format(new AverageScoutData(dataList).getAverageAutoGearsDropped()) + " gears dropped";
            //case SORT_TELEOP_DUMP_FREQUENCY:
            //return formatter.format(new AverageScoutData(dataList).getAverageTeleopDumpFrequency()) + " dumps per match";
            //case SORT_TELEOP_DUMP_AMOUNT:
            //return new AverageScoutData(dataList).getAverageTeleopLowGoalDumpAmount() + " amount of fuel dumped";
            case SORT_TELEOP_HIGH_GOALS:
                return formatter.format(new AverageScoutData(dataList).getAverageTeleopHighGoals()) + " high goals";
            //case SORT_TELEOP_MISSED_HIGH_GOALS:
            //return formatter.format(new AverageScoutData(dataList).getAverageTeleopHighGoals()) + " missed high goals";
            case SORT_CLIMBING_STATS_PERCENTAGE:
                return "Pressed the touchpad in " + formatter.format(new AverageScoutData(dataList).getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "% of matches";
            default: //Fallback - shouldn't trigger
                return dataList.size() + " matches";
        }
    }
}
