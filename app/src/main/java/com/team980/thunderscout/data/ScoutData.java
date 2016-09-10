package com.team980.thunderscout.data;

import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.enumeration.ScalingStats;

import java.io.Serializable;
import java.util.EnumMap;

/**
 * Implements data for one team from one match.
 */
public class ScoutData implements Serializable {

    /**
     * ScoutData Version 2
     */
    private static final long serialVersionUID = 2;

    // INIT
    private String teamNumber;

    private long dateAdded;
    private String dataSource; //TODO add when scouting
    //TODO sharing/sync status

    // AUTO
    private Defense autoDefenseCrossed;

    private int autoHighGoals;
    private int autoLowGoals;
    private int autoMissedGoals;

    // TELEOP
    private EnumMap<Defense, Integer> teleopDefenseCrossings;

    private int teleopHighGoals;
    private int teleopLowGoals;
    private int teleopMissedGoals;

    // SUMMARY
    private ScalingStats scalingStats;
    private boolean challengedTower;

    private String troubleWith;
    private String comments;

    public ScoutData() {
        teleopDefenseCrossings = new EnumMap<>(Defense.class);

        //default values
        scalingStats = ScalingStats.NONE;
    }

    // --- INIT ---

    public String getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber;
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

    public Defense getAutoDefenseCrossed() {
        return autoDefenseCrossed;
    }

    public void setAutoDefenseCrossed(Defense autoDefenseCrossed) {
        this.autoDefenseCrossed = autoDefenseCrossed;
    }

    public int getAutoLowGoals() {
        return autoLowGoals;
    }

    public void setAutoLowGoals(int autoLowGoals) {
        this.autoLowGoals = autoLowGoals;
    }

    public int getAutoHighGoals() {
        return autoHighGoals;
    }

    public void setAutoHighGoals(int autoHighGoals) {
        this.autoHighGoals = autoHighGoals;
    }

    public int getAutoMissedGoals() {
        return autoMissedGoals;
    }

    public void setAutoMissedGoals(int autoMissedGoals) {
        this.autoMissedGoals = autoMissedGoals;
    }

    // --- TELEOP ---

    public EnumMap<Defense, Integer> getTeleopDefenseCrossings() {
        return teleopDefenseCrossings;
    }

    public int getTeleopLowGoals() {
        return teleopLowGoals;
    }

    public void setTeleopLowGoals(int teleopLowGoals) {
        this.teleopLowGoals = teleopLowGoals;
    }

    public int getTeleopHighGoals() {
        return teleopHighGoals;
    }

    public void setTeleopHighGoals(int teleopHighGoals) {
        this.teleopHighGoals = teleopHighGoals;
    }

    public int getTeleopMissedGoals() {
        return teleopMissedGoals;
    }

    public void setTeleopMissedGoals(int teleopMissedGoals) {
        this.teleopMissedGoals = teleopMissedGoals;
    }

    // --- SUMMARY ---

    public ScalingStats getScalingStats() {
        return scalingStats;
    }

    public void setScalingStats(ScalingStats scalingStats) {
        this.scalingStats = scalingStats;
    }

    public boolean hasChallengedTower() {
        return challengedTower;
    }

    public void setChallengedTower(boolean challengedTower) {
        this.challengedTower = challengedTower;
    }

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
}
