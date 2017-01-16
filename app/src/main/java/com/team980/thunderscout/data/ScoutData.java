package com.team980.thunderscout.data;

import com.team980.thunderscout.data.enumeration.AllianceColor;
import com.team980.thunderscout.data.enumeration.ClimbingStats;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

import java.io.Serializable;

/**
 * Implements data for one team from one match.
 */
public class ScoutData implements Serializable {

    /**
     * ScoutData Version 4 - 2017
     */
    private static final long serialVersionUID = 4;

    public static final String SOURCE_LOCAL_DEVICE = "This device";

    // INIT
    private String teamNumber;
    private int matchNumber;
    private AllianceColor allianceColor;

    private long dateAdded;
    private String dataSource;
    //TODO sharing/sync status

    // AUTO
    private int autoGearsDelivered;
    private FuelDumpAmount autoLowGoalDumpAmount;
    private int autoHighGoals;
    private boolean crossedBaseline;

    // TELEOP
    private int teleopGearsDelivered;
    private FuelDumpAmount averageTeleopLowGoalDumpAmount; //average # of fuel
    private int teleopDumpFrequency; //number of dumps made
    private int teleopHighGoals;
    private ClimbingStats climbingStats;

    // SUMMARY
    private String troubleWith;
    private String comments;


    public ScoutData() {
        //default values
        autoLowGoalDumpAmount = FuelDumpAmount.NONE;
        averageTeleopLowGoalDumpAmount = FuelDumpAmount.NONE;
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
        setCrossedBaseline(other.hasCrossedBaseline());

        //Teleop
        setTeleopGearsDelivered(other.getTeleopGearsDelivered());
        setAverageTeleopLowGoalDumpAmount(other.getAverageTeleopLowGoalDumpAmount());
        setTeleopDumpFrequency(other.getTeleopDumpFrequency());
        setTeleopHighGoals(other.getTeleopHighGoals());
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

    public FuelDumpAmount getAverageTeleopLowGoalDumpAmount() {
        return averageTeleopLowGoalDumpAmount;
    }

    public void setAverageTeleopLowGoalDumpAmount(FuelDumpAmount averageTeleopLowGoalDumpAmount) {
        this.averageTeleopLowGoalDumpAmount = averageTeleopLowGoalDumpAmount;
    }

    public int getTeleopDumpFrequency() {
        return teleopDumpFrequency;
    }

    public void setTeleopDumpFrequency(int teleopDumpFrequency) {
        this.teleopDumpFrequency = teleopDumpFrequency;
    }

    public int getTeleopHighGoals() {
        return teleopHighGoals;
    }

    public void setTeleopHighGoals(int teleopHighGoals) {
        this.teleopHighGoals = teleopHighGoals;
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
}
