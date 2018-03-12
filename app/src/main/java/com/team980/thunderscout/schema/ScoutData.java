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

package com.team980.thunderscout.schema;

import android.support.annotation.NonNull;

import com.team980.thunderscout.schema.enumeration.AllianceStation;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implements data for one team from one match.
 */
public class ScoutData implements Comparable<ScoutData>, Serializable {

    /**
     * ScoutData Version 2018-1
     * <p>
     * [7] 2018-1: First 2018 spec. Makes a few changes to how the 2017 spec was formatted.
     */
    private static final long serialVersionUID = 7;

    /**
     * The unique ID used internally by the chosen storage provider
     * Used ONLY for internal tracking / data deletion
     */
    private int id;

    private String team; //trust me, this is better as a string
    private int matchNumber;
    private AllianceStation allianceStation;

    private Date date;
    private String source;

    private Autonomous autonomous;
    private Teleop teleop;

    private String strategies;
    private String difficulties;
    private String comments;

    public ScoutData() {
        //default values
        allianceStation = AllianceStation.RED_1;
        date = new Date(0);

        autonomous = new Autonomous();
        teleop = new Teleop();
    }

    public ScoutData(int primaryKey) { //ID can only be set at object creation
        id = primaryKey;

        //default values
        allianceStation = AllianceStation.RED_1;
        date = new Date(0);

        autonomous = new Autonomous();
        teleop = new Teleop();
    }

    public static ScoutData fromStringArray(String[] array) { //used by CSV
        ScoutData data = new ScoutData();

        //Init
        data.setTeam(array[0]);
        data.setMatchNumber(Integer.parseInt(array[1]));
        data.setAllianceStation(AllianceStation.valueOf(array[2]));
        data.setDate(new Date(Long.parseLong(array[3])));
        data.setSource(array[4]);

        //Auto
        data.getAutonomous().setCrossedAutoLine(Boolean.parseBoolean(array[5]));
        data.getAutonomous().setPowerCubeAllianceSwitchCount(Integer.parseInt(array[6]));
        data.getAutonomous().setPowerCubeScaleCount(Integer.parseInt(array[7]));
        data.getAutonomous().setPowerCubePlayerStationCount(Integer.parseInt(array[8]));

        //Teleop
        data.getTeleop().setPowerCubeAllianceSwitchCount(Integer.parseInt(array[9]));
        data.getTeleop().setPowerCubeScaleCount(Integer.parseInt(array[10]));
        data.getTeleop().setPowerCubeOpposingSwitchCount(Integer.parseInt(array[11]));
        data.getTeleop().setPowerCubePlayerStationCount(Integer.parseInt(array[12]));
        data.getTeleop().setClimbingStats(ClimbingStats.valueOf(array[13]));
        data.getTeleop().setSupportedOtherRobots(Boolean.parseBoolean(array[14]));

        //Summary
        data.setStrategies(array[15]);
        data.setDifficulties(array[16]);
        data.setComments(array[17]);

        return data;
    }

    // --- INIT ---

    /**
     * The unique ID used internally by the chosen storage provider
     * Used ONLY for internal tracking / data deletion
     */
    public int getId() {
        return id;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public AllianceStation getAllianceStation() {
        return allianceStation;
    }

    public void setAllianceStation(AllianceStation alliance) {
        this.allianceStation = alliance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date d) {
        date = d;
    }

    public String getSource() {
        return source;
    }

    // --- AUTO ---

    public void setSource(String d) {
        source = d;
    }

    // --- TELEOP ---

    public Autonomous getAutonomous() {
        return autonomous;
    }

    // --- SUMMARY ---

    public Teleop getTeleop() {
        return teleop;
    }

    public String getStrategies() {
        return strategies;
    }

    public void setStrategies(String strategies) {
        this.strategies = strategies;
    }

    public String getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(String difficulties) {
        this.difficulties = difficulties;
    }

    public String getComments() {
        return comments;
    }

    // --- OTHER METHODS ---

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String[] toStringArray() {
        ArrayList<String> fieldList = new ArrayList<>();

        //Init
        fieldList.add(getTeam());
        fieldList.add(String.valueOf(getMatchNumber()));
        fieldList.add(getAllianceStation().name());
        fieldList.add(String.valueOf(getDate().getTime()));
        fieldList.add(getSource());

        //Auto
        fieldList.add(String.valueOf(getAutonomous().crossedAutoLine()));
        fieldList.add(String.valueOf(getAutonomous().getPowerCubeAllianceSwitchCount()));
        fieldList.add(String.valueOf(getAutonomous().getPowerCubeScaleCount()));
        fieldList.add(String.valueOf(getAutonomous().getPowerCubePlayerStationCount()));

        //Teleop
        fieldList.add(String.valueOf(getTeleop().getPowerCubeAllianceSwitchCount()));
        fieldList.add(String.valueOf(getTeleop().getPowerCubeScaleCount()));
        fieldList.add(String.valueOf(getTeleop().getPowerCubeOpposingSwitchCount()));
        fieldList.add(String.valueOf(getTeleop().getPowerCubePlayerStationCount()));
        fieldList.add(getTeleop().getClimbingStats().name());
        fieldList.add(String.valueOf(getTeleop().supportedOtherRobots()));

        //Summary
        fieldList.add(getStrategies());
        fieldList.add(getDifficulties());
        fieldList.add(getComments());

        return fieldList.toArray(new String[fieldList.size()]);
    }

    @Override
    public int compareTo(@NonNull ScoutData other) {
        if (team.compareTo(other.getTeam()) == 0) {
            return Integer.compare(matchNumber, other.getMatchNumber());
        } else {
            return team.compareTo(other.getTeam());
        }
    }

    public class Autonomous implements Serializable {

        private boolean crossedAutoLine;
        private int powerCubeAllianceSwitchCount;
        private int powerCubeScaleCount;
        private int powerCubePlayerStationCount;

        private Autonomous() {
            crossedAutoLine = false;
        }

        public boolean crossedAutoLine() {
            return crossedAutoLine;
        }

        public void setCrossedAutoLine(boolean crossedAutoLine) {
            this.crossedAutoLine = crossedAutoLine;
        }

        public int getPowerCubeAllianceSwitchCount() {
            return powerCubeAllianceSwitchCount;
        }

        public void setPowerCubeAllianceSwitchCount(int powerCubeAllianceSwitchCount) {
            this.powerCubeAllianceSwitchCount = powerCubeAllianceSwitchCount;
        }

        public int getPowerCubeScaleCount() {
            return powerCubeScaleCount;
        }

        public void setPowerCubeScaleCount(int powerCubeScaleCount) {
            this.powerCubeScaleCount = powerCubeScaleCount;
        }

        public int getPowerCubePlayerStationCount() {
            return powerCubePlayerStationCount;
        }

        public void setPowerCubePlayerStationCount(int powerCubePlayerStationCount) {
            this.powerCubePlayerStationCount = powerCubePlayerStationCount;
        }
    }

    public class Teleop implements Serializable {

        private int powerCubeAllianceSwitchCount;
        private int powerCubeScaleCount;
        private int powerCubeOpposingSwitchCount;
        private int powerCubePlayerStationCount;
        private ClimbingStats climbingStats;
        private boolean supportedOtherRobots;

        private Teleop() {
            climbingStats = ClimbingStats.DID_NOT_CLIMB;
            supportedOtherRobots = false;
        }

        public int getPowerCubeAllianceSwitchCount() {
            return powerCubeAllianceSwitchCount;
        }

        public void setPowerCubeAllianceSwitchCount(int powerCubeAllianceSwitchCount) {
            this.powerCubeAllianceSwitchCount = powerCubeAllianceSwitchCount;
        }

        public int getPowerCubeScaleCount() {
            return powerCubeScaleCount;
        }

        public void setPowerCubeScaleCount(int powerCubeScaleCount) {
            this.powerCubeScaleCount = powerCubeScaleCount;
        }

        public int getPowerCubeOpposingSwitchCount() {
            return powerCubeOpposingSwitchCount;
        }

        public void setPowerCubeOpposingSwitchCount(int powerCubeOpposingSwitchCount) {
            this.powerCubeOpposingSwitchCount = powerCubeOpposingSwitchCount;
        }

        public int getPowerCubePlayerStationCount() {
            return powerCubePlayerStationCount;
        }

        public void setPowerCubePlayerStationCount(int powerCubePlayerStationCount) {
            this.powerCubePlayerStationCount = powerCubePlayerStationCount;
        }

        public ClimbingStats getClimbingStats() {
            return climbingStats;
        }

        public void setClimbingStats(ClimbingStats climbingStats) {
            this.climbingStats = climbingStats;
        }

        public boolean supportedOtherRobots() {
            return supportedOtherRobots;
        }

        public void setSupportedOtherRobots(boolean supportedOtherRobots) {
            this.supportedOtherRobots = supportedOtherRobots;
        }
    }
}
