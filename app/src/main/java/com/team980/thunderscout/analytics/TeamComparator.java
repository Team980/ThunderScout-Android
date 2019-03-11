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
import com.team980.thunderscout.schema.enumeration.HabLevel;

import java.util.Comparator;

//TODO break out logic - Average/Min/Max and the statistic should be selectable separately
public enum TeamComparator implements Comparator<TeamWrapper> {
    //TODO The order isn't consistent when the data is equal-
    //TODO it appears to revert to the order fetched from the database when refreshed

    SORT_TEAM_NUMBER("Team number") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return -(Integer.valueOf(o1.getTeam())
                    .compareTo(Integer.valueOf(o2.getTeam()))); //For this one smaller is better
        }
    },

    SORT_LAST_UPDATED("Time updated") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return ScoutDataStatistics.getLastUpdated(o1.getDataList())
                    .compareTo(ScoutDataStatistics.getLastUpdated(o2.getDataList()));
        }
    },

    SORT_LEVEL_2_START_FREQUENCY("Level 2 Start Frequency") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getStartingLevel() == HabLevel.LEVEL_2),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getStartingLevel() == HabLevel.LEVEL_2));
        }
    },

    SORT_HAB_LINE_CROSS_SUCCESS("Hab Line Cross Success") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), ScoutData::crossedHabLine),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), ScoutData::crossedHabLine));
        }
    },

    SORT_STORM_ROCKET_HATCH_AVERAGE("Avg. Storm Rocket Hatches") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormHighRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormMidRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormLowRocketHatchCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormHighRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormMidRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormLowRocketHatchCount));
        }
    },

    SORT_STORM_CARGO_SHIP_HATCH_AVERAGE("Avg. Storm Ship Hatches") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormCargoShipHatchCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormCargoShipHatchCount));
        }
    },

    SORT_STORM_ROCKET_CARGO_AVERAGE("Avg. Storm Rocket Cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormHighRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormLowRocketCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormHighRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormLowRocketCargoCount));
        }
    },

    SORT_STORM_CARGO_SHIP_CARGO_AVERAGE("Avg. Storm Ship Cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormCargoShipCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormCargoShipCargoCount));
        }
    },

    SORT_TELEOP_ROCKET_HATCH_AVERAGE("Avg. Teleop Rocket Hatches") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopHighRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopMidRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopLowRocketHatchCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopHighRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopMidRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopLowRocketHatchCount));
        }
    },

    SORT_TELEOP_CARGO_SHIP_HATCH_AVERAGE("Avg. Teleop Ship Hatches") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopCargoShipHatchCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopCargoShipHatchCount));
        }
    },

    SORT_TELEOP_ROCKET_CARGO_AVERAGE("Avg. Teleop Rocket Cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopHighRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopLowRocketCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopHighRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopLowRocketCargoCount));
        }
    },

    SORT_TELEOP_CARGO_SHIP_CARGO_AVERAGE("Avg. Teleop Ship Cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopCargoShipCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopCargoShipCargoCount));
        }
    },

    SORT_LEVEL_2_CLIMB_FREQUENCY("Level 2 Climb Frequency") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getEndgameClimbLevel() == HabLevel.LEVEL_2),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getEndgameClimbLevel() == HabLevel.LEVEL_2));
        }
    },

    SORT_LEVEL_3_CLIMB_FREQUENCY("Level 3 Climb Frequency") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getEndgameClimbLevel() == HabLevel.LEVEL_2),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getEndgameClimbLevel() == HabLevel.LEVEL_2));
        }
    },

    SORT_AVERAGE_CLIMB_TIME("Average Climb Time") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), data -> data.getEndgameClimbTime().ordinal()),
                    ScoutDataStatistics.getAverage(o2.getDataList(), data -> data.getEndgameClimbTime().ordinal()));
        }
    },

    SORT_SUPPORTED_OTHER_ROBOTS("Supported Another Robot") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), ScoutData::supportedOtherRobots),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), ScoutData::supportedOtherRobots));
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