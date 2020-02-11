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

    SORT_LEVEL_2_START_FREQUENCY("Started in front of Power Port?") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getStartingLevel() == HabLevel.LEVEL_1),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getStartingLevel() == HabLevel.LEVEL_1));
        }
    },

    SORT_HAB_LINE_CROSS_SUCCESS("Moved off initiation line?") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), ScoutData::crossedHabLine),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), ScoutData::crossedHabLine));
        }
    },
//adapt new check boxes
    SORT_CONTROL_PANEL_ROATION_SUCCESS("Rotated control panel") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), ScoutData::controlPanelRotation),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), ScoutData::controlPanelRotation));
        }
    },
    SORT_CONTROL_PANEL_POSITION_SUCCESS("Rotated control panel") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), ScoutData::controlPanelPosition),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), ScoutData::controlPanelPosition));
        }
    },
//end adapt
    SORT_STORM_ROCKET_HATCH_AVERAGE("Avg. Auto Power Cells") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormHighRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormHighRocketCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormHighRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormHighRocketCargoCount));
        }
    },

/*    SORT_STORM_CARGO_SHIP_HATCH_AVERAGE("Avg. Storm Cargo Ship hatches") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormCargoShipHatchCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormCargoShipHatchCount));
        }
    },

    SORT_STORM_ROCKET_CARGO_AVERAGE("Avg. Storm Rocket cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormLowRocketCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormHighRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormLowRocketCargoCount));
        }
    },

    SORT_STORM_CARGO_SHIP_CARGO_AVERAGE("Avg. Storm Cargo Ship cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getStormCargoShipCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getStormCargoShipCargoCount));
        }
    },*/

    SORT_FLOOR_PICKUP_FREQUENCY("Frequency of picking up off the floor"){
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getStartingLevel().equals("Floor Pickup")),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getStartingLevel().equals("Floor Pickup")));
        }
    },

    SORT_LOADING_STATION_PICKUP_FREQUENCY("Frequency of picking up from loading station"){
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getStartingLevel().equals("Loading Station pickup")),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getStartingLevel().equals("Loading Station pickup")));
        }
    },

    SORT_TELEOP_ROCKET_HATCH_AVERAGE("Avg. Teleop Power Cells") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopMidRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopLowRocketHatchCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopMidRocketHatchCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopLowRocketHatchCount));
        }
    },

/*    SORT_TELEOP_CARGO_SHIP_HATCH_AVERAGE("Avg. Teleop Cargo Ship hatches") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopCargoShipHatchCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopCargoShipHatchCount));
        }
    },

    SORT_TELEOP_ROCKET_CARGO_AVERAGE("Avg. Teleop Rocket cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopHighRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopLowRocketCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopHighRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopMidRocketCargoCount)
                            + ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopLowRocketCargoCount));
        }
    },

    SORT_TELEOP_CARGO_SHIP_CARGO_AVERAGE("Avg. Teleop Cargo Ship cargo") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), ScoutData::getTeleopCargoShipCargoCount),
                    ScoutDataStatistics.getAverage(o2.getDataList(), ScoutData::getTeleopCargoShipCargoCount));
        }
    },*/

    SORT_LEVEL_2_CLIMB_FREQUENCY("Climbed in the Middle") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getEndgameClimbLevel().equals("Center Climb")),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getEndgameClimbLevel().equals("Center Climb")));
        }
    },

    SORT_LEVEL_3_CLIMB_FREQUENCY("Climbed at the ends") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getPercentage(o1.getDataList(), data -> data.getEndgameClimbLevel().equals("End Climb")),
                    ScoutDataStatistics.getPercentage(o2.getDataList(), data -> data.getEndgameClimbLevel().equals("End Climb")));
        }
    },

    SORT_AVERAGE_CLIMB_TIME("Average climbing time") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.compare(ScoutDataStatistics.getAverage(o1.getDataList(), data -> data.getEndgameClimbTime().ordinal()),
                    ScoutDataStatistics.getAverage(o2.getDataList(), data -> data.getEndgameClimbTime().ordinal()));
        }
    },

    SORT_SUPPORTED_OTHER_ROBOTS("Supported another robot?") {
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