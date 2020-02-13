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
import com.team980.thunderscout.schema.enumeration.ClimbTime;
import com.team980.thunderscout.schema.enumeration.HabLevel;

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
            case SORT_LEVEL_2_START_FREQUENCY:
                return "Started in front of Power Port in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getStartingLevel() == HabLevel.LEVEL_1)) + "% of matches";
            case SORT_HAB_LINE_CROSS_SUCCESS:
                return "Moved off initiation line in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        ScoutData::crossedHabLine)) + "% of matches";
            case SORT_CONTROL_PANEL_ROATION_SUCCESS:
                return "Control Panel successfully rotated in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        ScoutData::controlPanelRotation)) + "% of matches";
            case SORT_CONTROL_PANEL_POSITION_SUCCESS:
                return "Control Panel successfully positioned in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        ScoutData::controlPanelPosition)) + "% of matches";
            case SORT_STORM_ROCKET_HATCH_AVERAGE://actually auto average scored power cells
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getStormHighRocketHatchCount) + ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getStormHighRocketCargoCount) ) + " Power Cells";
/*            case SORT_STORM_CARGO_SHIP_HATCH_AVERAGE:
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getStormCargoShipHatchCount)) + " hatches";
            case SORT_STORM_ROCKET_CARGO_AVERAGE:
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getStormHighRocketCargoCount) + ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getStormMidRocketCargoCount) + ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getStormLowRocketCargoCount)) + " cargo";
            case SORT_STORM_CARGO_SHIP_CARGO_AVERAGE:
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getStormCargoShipCargoCount)) + " cargo";

 */
            case SORT_FLOOR_PICKUP_FREQUENCY://ADDED
                return "Started in front of Power Port in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getStartingLevel() == HabLevel.LEVEL_1)) + "% of matches";

            case SORT_LOADING_STATION_PICKUP_FREQUENCY://ADDED
                return "Started in front of Power Port in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getStartingLevel() == HabLevel.LEVEL_1)) + "% of matches";

            case SORT_TELEOP_ROCKET_HATCH_AVERAGE://power cells scored
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopMidRocketHatchCount) + ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopLowRocketHatchCount)) + " power cells";

 /*           case SORT_TELEOP_CARGO_SHIP_HATCH_AVERAGE:
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopCargoShipHatchCount)) + " hatches";
            case SORT_TELEOP_ROCKET_CARGO_AVERAGE:
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopHighRocketCargoCount) + ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopMidRocketCargoCount) + ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopLowRocketCargoCount)) + " cargo";
            case SORT_TELEOP_CARGO_SHIP_CARGO_AVERAGE:
                return "Scored " + formatter.format(ScoutDataStatistics.getAverage(dataList,
                        ScoutData::getTeleopCargoShipCargoCount)) + " cargo";*/

            case SORT_LEVEL_2_CLIMB_FREQUENCY:
                return "Climbed in the center of the bar in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getEndgameClimbLevel().equals("Center Climb"))) + "% of matches";
            case SORT_LEVEL_3_CLIMB_FREQUENCY:
                return "Climbed at the end of the bar in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getEndgameClimbLevel().equals("End Climb"))) + "% of matches";
            case SORT_AVERAGE_CLIMB_TIME:
                return "Climbed on neutral position bar in  " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getEndgameClimbTime().equals("Neutral Height"))) + "% of matches";
            case SORT_HIGH_BAR_CLIMB_FREQUENCY:
                return "Climbed on high position bar in  " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getEndgameClimbTime().equals("High Position"))) + "% of matches";
            case SORT_LOW_BAR_CLIMB_FREQUENCY:
                return "Climbed on low position bar in  " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        data -> data.getEndgameClimbTime().equals("Low Position"))) + "% of matches";
            case SORT_SUPPORTED_OTHER_ROBOTS:
                return "Supported another robot in " + formatter.format(ScoutDataStatistics.getPercentage(dataList,
                        ScoutData::supportedOtherRobots)) + "% of matches";
            default: //Fallback - shouldn't trigger
                return dataList.size() + " matches";
        }
    }
}
