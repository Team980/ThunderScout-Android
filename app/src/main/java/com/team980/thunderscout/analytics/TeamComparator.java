/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
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

import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.util.Comparator;

//TODO break out logic - Average/Min/Max and the statistic should be selectable separately
public enum TeamComparator implements Comparator<TeamWrapper> {
    //TODO The order isn't consistent when the data is equal-
    //TODO it appears to revert to the order fetched from the database when refreshed

    SORT_TEAM_NUMBER("Team Number") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return -(Integer.valueOf(o1.getTeam())
                    .compareTo(Integer.valueOf(o2.getTeam()))); //For this one smaller is better
        }
    },

    SORT_LAST_UPDATED("Time Updated") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return ScoutDataStatistics.getLastUpdated(o1.getDataList())
                    .compareTo(ScoutDataStatistics.getLastUpdated(o2.getDataList()));
        }
    },

    SORT_AUTO_LINE_CROSS_SUCCESS("Auto Line Cross Success") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getPercentage(o1.getDataList(),
                    data -> data.crossedAutoLine()))
                    .compareTo(ScoutDataStatistics.getPercentage(o2.getDataList(),
                            data -> data.crossedAutoLine()));
        }
    },

    SORT_AUTO_POWER_CUBE_ALLIANCE_SWITCH_AVERAGE("Avg. Auto Alliance Switch Cubes") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getAverage(o1.getDataList(),
                    data -> data.getAutoPowerCubeAllianceSwitchCount()))
                    .compareTo(ScoutDataStatistics.getAverage(o2.getDataList(),
                            data -> data.getAutoPowerCubeAllianceSwitchCount()));
        }
    },

    SORT_AUTO_POWER_CUBE_SCALE_AVERAGE("Avg. Auto Central Scale Cubes") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getAverage(o1.getDataList(),
                    data -> data.getAutoPowerCubeScaleCount()))
                    .compareTo(ScoutDataStatistics.getAverage(o2.getDataList(),
                            data -> data.getAutoPowerCubeScaleCount()));
        }
    },

    SORT_AUTO_POWER_CUBE_PLAYER_STATION_AVERAGE("Avg. Auto Player Station Cubes") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getAverage(o1.getDataList(),
                    data -> data.getAutoPowerCubePlayerStationCount()))
                    .compareTo(ScoutDataStatistics.getAverage(o2.getDataList(),
                            data -> data.getAutoPowerCubePlayerStationCount()));
        }
    },

    SORT_TELEOP_POWER_CUBE_ALLIANCE_SWITCH_AVERAGE("Avg. Teleop Alliance Switch Cubes") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getAverage(o1.getDataList(),
                    data -> data.getTeleopPowerCubeAllianceSwitchCount()))
                    .compareTo(ScoutDataStatistics.getAverage(o2.getDataList(),
                            data -> data.getTeleopPowerCubeAllianceSwitchCount()));
        }
    },

    SORT_TELEOP_POWER_CUBE_SCALE_AVERAGE("Avg. Teleop Central Scale Cubes") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getAverage(o1.getDataList(),
                    data -> data.getTeleopPowerCubeScaleCount()))
                    .compareTo(ScoutDataStatistics.getAverage(o2.getDataList(),
                            data -> data.getTeleopPowerCubeScaleCount()));
        }
    },

    SORT_TELEOP_POWER_CUBE_OPPOSING_SWITCH_AVERAGE("Avg. Teleop Opposing Switch Cubes") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getAverage(o1.getDataList(),
                    data -> data.getTeleopPowerCubeOpposingSwitchCount()))
                    .compareTo(ScoutDataStatistics.getAverage(o2.getDataList(),
                            data -> data.getTeleopPowerCubeOpposingSwitchCount()));
        }
    },

    SORT_TELEOP_POWER_CUBE_PLAYER_STATION_AVERAGE("Avg. Teleop Player Station Cubes") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getAverage(o1.getDataList(),
                    data -> data.getTeleopPowerCubePlayerStationCount()))
                    .compareTo(ScoutDataStatistics.getAverage(o2.getDataList(),
                            data -> data.getTeleopPowerCubePlayerStationCount()));
        }
    },

    SORT_CLIMBING_STATS_PERCENTAGE("Climbing Success") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            int climbed = Double.valueOf(ScoutDataStatistics.getPercentage(o1.getDataList(),
                    data -> data.getClimbingStats() == ClimbingStats.CLIMBED))
                    .compareTo(ScoutDataStatistics.getPercentage(o2.getDataList(),
                            data -> data.getClimbingStats() == ClimbingStats.CLIMBED));

            if (climbed == 0) { //If it's the same...
                return Double.valueOf(ScoutDataStatistics.getPercentage(o1.getDataList(),
                        data -> data.getClimbingStats() == ClimbingStats.ATTEMPTED_CLIMB))
                        .compareTo(ScoutDataStatistics.getPercentage(o2.getDataList(),
                                data -> data.getClimbingStats() == ClimbingStats.ATTEMPTED_CLIMB));
            } else {
                return climbed;
            }
        }
    },

    SORT_SUPPORTED_OTHER_ROBOTS("Supported Other Robots") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(ScoutDataStatistics.getPercentage(o1.getDataList(),
                    data -> data.supportedOtherRobots()))
                    .compareTo(ScoutDataStatistics.getPercentage(o2.getDataList(),
                            data -> data.supportedOtherRobots()));
        }
    };

    private String displayName;

    TeamComparator(String par1) {
        displayName = par1;
    }

    public static Comparator<TeamWrapper> getComparator(final TeamComparator option) {
        return (o1, o2) -> option.compare(o1, o2) == 0 ? TeamComparator.SORT_TEAM_NUMBER.compare(o1, o2) * -1 : option.compare(o1, o2) * -1;
    }

    public static String[] getFormattedList() {
        TeamComparator[] states = values();
        String[] names = new String[states.length];

        for (int i = 0; i < states.length; i++) {
            names[i] = states[i].toString();
        }

        return names;
    }

    @Override
    public String toString() {
        return displayName;
    }
}