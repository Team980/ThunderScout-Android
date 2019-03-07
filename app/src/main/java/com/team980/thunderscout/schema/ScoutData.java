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

package com.team980.thunderscout.schema;

import android.support.annotation.NonNull;

import com.team980.thunderscout.schema.enumeration.AllianceStation;
import com.team980.thunderscout.schema.enumeration.ClimbTime;
import com.team980.thunderscout.schema.enumeration.HabLevel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implements data for one team from one match.
 */
public class ScoutData implements Comparable<ScoutData>, Serializable {

    /**
     * ScoutData Version 2019-1
     * <p>
     * [9] 2019-1: First 2019 spec. It's categorized differently from 2018.
     */
    private static final long serialVersionUID = 9;

    /**
     * The unique ID used internally by the chosen storage provider
     * Used ONLY for internal tracking / data deletion
     */
    private String id;

    private String team; //trust me, this is better as a string
    private int matchNumber;
    private AllianceStation allianceStation;

    private Date date;
    private String source;

    private HabLevel startingLevel;
    private boolean crossedHabLine;

    private int stormHighRocketHatchCount;
    private int stormMiddleRocketHatchCount;
    private int stormLowRocketHatchCount;
    private int stormCargoShipHatchCount;

    private int stormHighRocketCargoCount;
    private int stormMiddleRocketCargoCount;
    private int stormLowRocketCargoCount;
    private int stormCargoShipCargoCount;

    private int teleopHighRocketHatchCount;
    private int teleopMiddleRocketHatchCount;
    private int teleopLowRocketHatchCount;
    private int teleopCargoShipHatchCount;

    private int teleopHighRocketCargoCount;
    private int teleopMiddleRocketCargoCount;
    private int teleopLowRocketCargoCount;
    private int teleopCargoShipCargoCount;

    private HabLevel endgameClimbLevel;
    private ClimbTime endgameClimbTime;
    private boolean supportedOtherRobots;
    private String climbDescription;

    private String notes;

    public ScoutData() {
        //default values
        allianceStation = AllianceStation.RED_1;
        date = new Date(0);

        startingLevel = HabLevel.LEVEL_1;
        crossedHabLine = false;

        endgameClimbLevel = HabLevel.NONE;
        endgameClimbTime = ClimbTime.GREATER_THAN_FIFTEEN_SECONDS;
        supportedOtherRobots = false;
    }

    public ScoutData(String primaryKey) {
        id = primaryKey;

        //default values
        startingLevel = HabLevel.LEVEL_1;
        crossedHabLine = false;

        endgameClimbLevel = HabLevel.NONE;
        endgameClimbTime = ClimbTime.GREATER_THAN_FIFTEEN_SECONDS;
        supportedOtherRobots = false;
    }

    // --- INIT ---

    /**
     * The unique ID used internally by the chosen storage provider
     * Used ONLY for internal tracking / data deletion
     */
    public String getId() {
        return id;
    }

    /**
     * This should ONLY be called by Firestore reads!
     * TODO - refactor CloudStorageWrapper to manually create and delete hierarchy, removing the need for this method
     */
    public void setId(String id) {
        this.id = id;
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

    public void setSource(String d) {
        source = d;
    }

    // --- SANDSTORM ---

    public HabLevel getStartingLevel() {
        return startingLevel;
    }

    public void setStartingLevel(HabLevel startingLevel) {
        this.startingLevel = startingLevel;
    }

    public boolean crossedHabLine() {
        return crossedHabLine;
    }

    public void setCrossedHabLine(boolean crossedHabLine) {
        this.crossedHabLine = crossedHabLine;
    }

    public int getStormHighRocketHatchCount() {
        return stormHighRocketHatchCount;
    }

    public void setStormHighRocketHatchCount(int stormHighRocketHatchCount) {
        this.stormHighRocketHatchCount = stormHighRocketHatchCount;
    }

    public int getStormMiddleRocketHatchCount() {
        return stormMiddleRocketHatchCount;
    }

    public void setStormMiddleRocketHatchCount(int stormMiddleRocketHatchCount) {
        this.stormMiddleRocketHatchCount = stormMiddleRocketHatchCount;
    }

    public int getStormLowRocketHatchCount() {
        return stormLowRocketHatchCount;
    }

    public void setStormLowRocketHatchCount(int stormLowRocketHatchCount) {
        this.stormLowRocketHatchCount = stormLowRocketHatchCount;
    }

    public int getStormCargoShipHatchCount() {
        return stormCargoShipHatchCount;
    }

    public void setStormCargoShipHatchCount(int stormCargoShipHatchCount) {
        this.stormCargoShipHatchCount = stormCargoShipHatchCount;
    }

    public int getStormHighRocketCargoCount() {
        return stormHighRocketCargoCount;
    }

    public void setStormHighRocketCargoCount(int stormHighRocketCargoCount) {
        this.stormHighRocketCargoCount = stormHighRocketCargoCount;
    }

    public int getStormMiddleRocketCargoCount() {
        return stormMiddleRocketCargoCount;
    }

    public void setStormMiddleRocketCargoCount(int stormMiddleRocketCargoCount) {
        this.stormMiddleRocketCargoCount = stormMiddleRocketCargoCount;
    }

    public int getStormLowRocketCargoCount() {
        return stormLowRocketCargoCount;
    }

    public void setStormLowRocketCargoCount(int stormLowRocketCargoCount) {
        this.stormLowRocketCargoCount = stormLowRocketCargoCount;
    }

    public int getStormCargoShipCargoCount() {
        return stormCargoShipCargoCount;
    }

    public void setStormCargoShipCargoCount(int stormCargoShipCargoCount) {
        this.stormCargoShipCargoCount = stormCargoShipCargoCount;
    }

    // --- TELEOPERATED ---

    public int getTeleopHighRocketHatchCount() {
        return teleopHighRocketHatchCount;
    }

    public void setTeleopHighRocketHatchCount(int teleopHighRocketHatchCount) {
        this.teleopHighRocketHatchCount = teleopHighRocketHatchCount;
    }

    public int getTeleopMiddleRocketHatchCount() {
        return teleopMiddleRocketHatchCount;
    }

    public void setTeleopMiddleRocketHatchCount(int teleopMiddleRocketHatchCount) {
        this.teleopMiddleRocketHatchCount = teleopMiddleRocketHatchCount;
    }

    public int getTeleopLowRocketHatchCount() {
        return teleopLowRocketHatchCount;
    }

    public void setTeleopLowRocketHatchCount(int teleopLowRocketHatchCount) {
        this.teleopLowRocketHatchCount = teleopLowRocketHatchCount;
    }

    public int getTeleopCargoShipHatchCount() {
        return teleopCargoShipHatchCount;
    }

    public void setTeleopCargoShipHatchCount(int teleopCargoShipHatchCount) {
        this.teleopCargoShipHatchCount = teleopCargoShipHatchCount;
    }

    public int getTeleopHighRocketCargoCount() {
        return teleopHighRocketCargoCount;
    }

    public void setTeleopHighRocketCargoCount(int teleopHighRocketCargoCount) {
        this.teleopHighRocketCargoCount = teleopHighRocketCargoCount;
    }

    public int getTeleopMiddleRocketCargoCount() {
        return teleopMiddleRocketCargoCount;
    }

    public void setTeleopMiddleRocketCargoCount(int teleopMiddleRocketCargoCount) {
        this.teleopMiddleRocketCargoCount = teleopMiddleRocketCargoCount;
    }

    public int getTeleopLowRocketCargoCount() {
        return teleopLowRocketCargoCount;
    }

    public void setTeleopLowRocketCargoCount(int teleopLowRocketCargoCount) {
        this.teleopLowRocketCargoCount = teleopLowRocketCargoCount;
    }

    public int getTeleopCargoShipCargoCount() {
        return teleopCargoShipCargoCount;
    }

    public void setTeleopCargoShipCargoCount(int teleopCargoShipCargoCount) {
        this.teleopCargoShipCargoCount = teleopCargoShipCargoCount;
    }

    // --- ENDGAME ---

    public HabLevel getEndgameClimbLevel() {
        return endgameClimbLevel;
    }

    public void setEndgameClimbLevel(HabLevel endgameClimbLevel) {
        this.endgameClimbLevel = endgameClimbLevel;
    }

    public ClimbTime getEndgameClimbTime() {
        return endgameClimbTime;
    }

    public void setEndgameClimbTime(ClimbTime endgameClimbTime) {
        this.endgameClimbTime = endgameClimbTime;
    }

    public boolean supportedOtherRobots() {
        return supportedOtherRobots;
    }

    public void setSupportedOtherRobots(boolean supportedOtherRobots) {
        this.supportedOtherRobots = supportedOtherRobots;
    }

    public String getClimbDescription() {
        return climbDescription;
    }

    public void setClimbDescription(String climbDescription) {
        this.climbDescription = climbDescription;
    }

    // --- NOTES ---

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // --- OTHER METHODS ---

    public static ScoutData fromStringArray(@NonNull String[] array) { //used by CSV
        ScoutData data = new ScoutData();

        //Init
        data.setTeam(array[0]);
        data.setMatchNumber(Integer.parseInt(array[1]));
        data.setAllianceStation(AllianceStation.valueOf(array[2]));
        data.setDate(new Date(Long.parseLong(array[3])));
        data.setSource(array[4]);

        //Sandstorm
        data.setStartingLevel(HabLevel.valueOf(array[5]));
        data.setCrossedHabLine(Boolean.parseBoolean(array[6]));

        data.setStormHighRocketHatchCount(Integer.parseInt(array[7]));
        data.setStormMiddleRocketHatchCount(Integer.parseInt(array[8]));
        data.setStormLowRocketHatchCount(Integer.parseInt(array[9]));
        data.setStormCargoShipHatchCount(Integer.parseInt(array[10]));

        data.setStormHighRocketCargoCount(Integer.parseInt(array[11]));
        data.setStormMiddleRocketCargoCount(Integer.parseInt(array[12]));
        data.setStormLowRocketCargoCount(Integer.parseInt(array[13]));
        data.setStormCargoShipCargoCount(Integer.parseInt(array[14]));

        //Teleoperated
        data.setTeleopHighRocketHatchCount(Integer.parseInt(array[15]));
        data.setTeleopMiddleRocketHatchCount(Integer.parseInt(array[16]));
        data.setTeleopLowRocketHatchCount(Integer.parseInt(array[17]));
        data.setTeleopCargoShipHatchCount(Integer.parseInt(array[18]));

        data.setTeleopHighRocketCargoCount(Integer.parseInt(array[19]));
        data.setTeleopMiddleRocketCargoCount(Integer.parseInt(array[20]));
        data.setTeleopLowRocketCargoCount(Integer.parseInt(array[21]));
        data.setTeleopCargoShipCargoCount(Integer.parseInt(array[22]));

        //Endgame
        data.setEndgameClimbLevel(HabLevel.valueOf(array[23]));
        data.setEndgameClimbTime(ClimbTime.valueOf(array[24]));
        data.setSupportedOtherRobots(Boolean.parseBoolean(array[25]));
        data.setClimbDescription(array[26]);

        //Notes
        data.setNotes(array[27]);

        return data;
    }

    public String[] toStringArray() {
        ArrayList<String> fieldList = new ArrayList<>();

        //Init
        fieldList.add(getTeam());
        fieldList.add(String.valueOf(getMatchNumber()));
        fieldList.add(getAllianceStation().name());
        fieldList.add(String.valueOf(getDate().getTime()));
        fieldList.add(getSource());

        //Sandstorm
        fieldList.add(String.valueOf(getStartingLevel()));
        fieldList.add(String.valueOf(crossedHabLine()));

        fieldList.add(String.valueOf(getStormHighRocketHatchCount()));
        fieldList.add(String.valueOf(getStormMiddleRocketHatchCount()));
        fieldList.add(String.valueOf(getStormLowRocketHatchCount()));
        fieldList.add(String.valueOf(getStormCargoShipHatchCount()));

        fieldList.add(String.valueOf(getStormHighRocketCargoCount()));
        fieldList.add(String.valueOf(getStormMiddleRocketCargoCount()));
        fieldList.add(String.valueOf(getStormLowRocketCargoCount()));
        fieldList.add(String.valueOf(getStormCargoShipCargoCount()));

        //Teleoperated
        fieldList.add(String.valueOf(getTeleopHighRocketHatchCount()));
        fieldList.add(String.valueOf(getTeleopMiddleRocketHatchCount()));
        fieldList.add(String.valueOf(getTeleopLowRocketHatchCount()));
        fieldList.add(String.valueOf(getTeleopCargoShipHatchCount()));

        fieldList.add(String.valueOf(getTeleopHighRocketCargoCount()));
        fieldList.add(String.valueOf(getTeleopMiddleRocketCargoCount()));
        fieldList.add(String.valueOf(getTeleopLowRocketCargoCount()));
        fieldList.add(String.valueOf(getTeleopCargoShipCargoCount()));

        //Endgame
        fieldList.add(String.valueOf(getEndgameClimbLevel()));
        fieldList.add(String.valueOf(getEndgameClimbTime()));
        fieldList.add(String.valueOf(supportedOtherRobots()));
        fieldList.add(getClimbDescription());

        //Notes
        fieldList.add(getNotes());

        return fieldList.toArray(new String[0]);
    }

    @Override
    public int compareTo(@NonNull ScoutData other) {
        if (team.compareTo(other.getTeam()) == 0) {
            return Integer.compare(matchNumber, other.getMatchNumber());
        } else {
            return team.compareTo(other.getTeam());
        }
    }
}
