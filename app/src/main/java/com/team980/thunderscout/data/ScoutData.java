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

package com.team980.thunderscout.data;

import android.util.Log;

import com.team980.thunderscout.data.enumeration.AllianceColor;
import com.team980.thunderscout.data.enumeration.ClimbingStats;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implements data for one team from one match.
 */
public class ScoutData implements Serializable {

    /**
     * ScoutData Version 2017-2
     * <p>
     * 2017-2: Serializable ArrayList for dumps in teleop
     * 2017-1: First 2017 spec
     */
    private static final long serialVersionUID = 4;

    // INIT
    private String teamNumber;
    private int matchNumber;
    private AllianceColor allianceColor;

    private long dateAdded;
    private String dataSource;

    // AUTO
    private int autoGearsDelivered;
    private FuelDumpAmount autoLowGoalDumpAmount;
    private int autoHighGoals;
    private int autoMissedHighGoals;
    private boolean crossedBaseline;

    // TELEOP
    private int teleopGearsDelivered;
    private ArrayList<FuelDumpAmount> teleopLowGoalDumps; //average # of fuel
    private int teleopHighGoals;
    private int teleopMissedHighGoals;
    private ClimbingStats climbingStats;

    // SUMMARY
    private String troubleWith;
    private String comments;

    public ScoutData() {
        //default values
        autoLowGoalDumpAmount = FuelDumpAmount.NONE;
        teleopLowGoalDumps = new ArrayList<>();
        climbingStats = ClimbingStats.DID_NOT_CLIMB;
    }

    /**
     * Copy constructor
     */
    public ScoutData(ScoutData other) {
        //Init
        setTeamNumber(other.getTeamNumber());
        setMatchNumber(other.getMatchNumber());
        setAllianceColor(other.getAllianceColor());
        setDateAdded(other.getDateAdded());
        setDataSource(other.getDataSource());

        //Auto
        setAutoGearsDelivered(other.getAutoGearsDelivered());
        setAutoLowGoalDumpAmount(other.getAutoLowGoalDumpAmount());
        setAutoHighGoals(other.getAutoHighGoals());
        setAutoMissedHighGoals(other.getAutoMissedHighGoals());
        setCrossedBaseline(other.hasCrossedBaseline());

        //Teleop
        setTeleopGearsDelivered(other.getTeleopGearsDelivered());
        teleopLowGoalDumps = other.getTeleopLowGoalDumps();
        setTeleopHighGoals(other.getTeleopHighGoals());
        setTeleopMissedHighGoals(other.getTeleopMissedHighGoals());
        setClimbingStats(other.getClimbingStats());

        //Summary
        setTroubleWith(other.getTroubleWith());
        setComments(other.getComments());
    }

    // --- INIT ---

    public String getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public AllianceColor getAllianceColor() {
        return allianceColor;
    }

    public void setAllianceColor(AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long d) {
        dateAdded = d;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String d) {
        dataSource = d;
    }

    // --- AUTO ---

    public int getAutoGearsDelivered() {
        return autoGearsDelivered;
    }

    public void setAutoGearsDelivered(int autoGearsDelivered) {
        this.autoGearsDelivered = autoGearsDelivered;
    }

    public FuelDumpAmount getAutoLowGoalDumpAmount() {
        return autoLowGoalDumpAmount;
    }

    public void setAutoLowGoalDumpAmount(FuelDumpAmount autoLowGoalDumpAmount) {
        this.autoLowGoalDumpAmount = autoLowGoalDumpAmount;
    }

    public int getAutoHighGoals() {
        return autoHighGoals;
    }

    public void setAutoHighGoals(int autoHighGoals) {
        this.autoHighGoals = autoHighGoals;
    }

    public int getAutoMissedHighGoals() {
        return autoMissedHighGoals;
    }

    public void setAutoMissedHighGoals(int autoMissedHighGoals) {
        this.autoMissedHighGoals = autoMissedHighGoals;
    }

    public boolean hasCrossedBaseline() {
        return crossedBaseline;
    }

    public void setCrossedBaseline(boolean crossedBaseline) {
        this.crossedBaseline = crossedBaseline;
    }

    // --- TELEOP ---

    public int getTeleopGearsDelivered() {
        return teleopGearsDelivered;
    }

    public void setTeleopGearsDelivered(int teleopGearsDelivered) {
        this.teleopGearsDelivered = teleopGearsDelivered;
    }

    public ArrayList<FuelDumpAmount> getTeleopLowGoalDumps() {
        return teleopLowGoalDumps;
    }

    public int getTeleopHighGoals() {
        return teleopHighGoals;
    }

    public void setTeleopHighGoals(int teleopHighGoals) {
        this.teleopHighGoals = teleopHighGoals;
    }

    public int getTeleopMissedHighGoals() {
        return teleopMissedHighGoals;
    }

    public void setTeleopMissedHighGoals(int teleopMissedHighGoals) {
        this.teleopMissedHighGoals = teleopMissedHighGoals;
    }

    public ClimbingStats getClimbingStats() {
        return climbingStats;
    }

    public void setClimbingStats(ClimbingStats climbingStats) {
        this.climbingStats = climbingStats;
    }

    // --- SUMMARY ---

    public String getTroubleWith() {
        return troubleWith;
    }

    public void setTroubleWith(String troubleWith) {
        this.troubleWith = troubleWith;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // --- OTHER METHODS ---

    public String[] toStringArray() {
        ArrayList<String> fieldList = new ArrayList<>();

        //Init
        fieldList.add(getTeamNumber());
        fieldList.add(String.valueOf(getMatchNumber()));
        fieldList.add(getAllianceColor().name());
        fieldList.add(String.valueOf(getDateAdded()));
        fieldList.add(getDataSource());

        //Auto
        fieldList.add(String.valueOf(getAutoGearsDelivered()));
        fieldList.add(getAutoLowGoalDumpAmount().name());
        fieldList.add(String.valueOf(getAutoHighGoals()));
        fieldList.add(String.valueOf(getAutoMissedHighGoals()));
        fieldList.add(String.valueOf(hasCrossedBaseline()));

        //Teleop
        fieldList.add(String.valueOf(getTeleopGearsDelivered()));
        fieldList.add(getTeleopLowGoalDumps().toString());
        fieldList.add(String.valueOf(getTeleopHighGoals()));
        fieldList.add(String.valueOf(getTeleopMissedHighGoals()));
        fieldList.add(getClimbingStats().name());

        //Summary
        fieldList.add(getTroubleWith());
        fieldList.add(getComments());

        return fieldList.toArray(new String[fieldList.size()]);
    }

    public static ScoutData fromStringArray(String[] array) {
        ScoutData data = new ScoutData();

        //Init
        data.setTeamNumber(array[0]);
        data.setMatchNumber(Integer.parseInt(array[1]));
        data.setAllianceColor(AllianceColor.valueOfCompat(array[2]));
        data.setDateAdded(Long.parseLong(array[3]));
        data.setDataSource(array[4]);

        //Auto
        data.setAutoGearsDelivered(Integer.parseInt(array[5]));
        data.setAutoLowGoalDumpAmount(FuelDumpAmount.valueOf(array[6]));
        data.setAutoHighGoals(Integer.parseInt(array[7]));
        data.setAutoMissedHighGoals(Integer.parseInt(array[8]));
        data.setCrossedBaseline(Boolean.parseBoolean(array[9]));

        //Teleop
        data.setTeleopGearsDelivered(Integer.parseInt(array[10]));
        for (String amount : Arrays.asList(array[11].substring(1, array[11].length() - 1).split(", "))) {
            if (amount.isEmpty()) {
                break;
            }
            data.getTeleopLowGoalDumps().add(FuelDumpAmount.valueOf(amount.toUpperCase()));
        }
        data.setTeleopHighGoals(Integer.parseInt(array[12]));
        data.setTeleopMissedHighGoals(Integer.parseInt(array[13]));
        data.setClimbingStats(ClimbingStats.valueOf(array[14]));

        //Summary
        data.setTroubleWith(array[15]);
        data.setComments(array[16]);

        return data;
    }
}
