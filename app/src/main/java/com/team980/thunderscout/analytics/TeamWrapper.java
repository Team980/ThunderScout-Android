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

package com.team980.thunderscout.analytics;

import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Represents data for one team, for all the matches they played in.
 */
public class TeamWrapper implements Serializable {

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
            case SORT_TEAM_NUMBER: //fall through
            case SORT_LAST_UPDATED:
                return "Last updated " + SimpleDateFormat.getDateTimeInstance().format(ScoutDataStatistics.getLastUpdated(dataList));
            case SORT_AUTO_LINE_CROSS_SUCCESS:
                return "Crossed the auto line in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        ScoutData::getCrossedAutoLine)) + "% of matches";
            case SORT_AUTO_POWER_CUBE_ALLIANCE_SWITCH_AVERAGE:
                return "Delivered " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getAutoPowerCubeAllianceSwitchCount)) + " cubes";
            case SORT_AUTO_POWER_CUBE_SCALE_AVERAGE:
                return "Delivered " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getAutoPowerCubeScaleCount)) + " cubes";
            case SORT_AUTO_POWER_CUBE_PLAYER_STATION_AVERAGE:
                return "Delivered " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getAutoPowerCubePlayerStationCount)) + " cubes";
            case SORT_TELEOP_POWER_CUBE_ALLIANCE_SWITCH_AVERAGE:
                return "Delivered " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopPowerCubeAllianceSwitchCount)) + " cubes";
            case SORT_TELEOP_POWER_CUBE_SCALE_AVERAGE:
                return "Delivered " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopPowerCubeScaleCount)) + " cubes";
            case SORT_TELEOP_POWER_CUBE_OPPOSING_SWITCH_AVERAGE:
                return "Delivered " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopPowerCubeOpposingSwitchCount)) + " cubes";
            case SORT_TELEOP_POWER_CUBE_PLAYER_STATION_AVERAGE:
                return "Delivered " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopPowerCubePlayerStationCount)) + " cubes";
            case SORT_CLIMBING_STATS_PERCENTAGE:
                return "Climbed in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getClimbingStats() == ClimbingStats.CLIMBED)) + "% of matches";
            case SORT_SUPPORTED_OTHER_ROBOTS:
                return "Supported other robots in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        ScoutData::getSupportedOtherRobots)) + "% of matches";
            default: //Fallback - shouldn't trigger
                return dataList.size() + " matches";
        }
    }
}
