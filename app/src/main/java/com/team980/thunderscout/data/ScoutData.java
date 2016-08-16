package com.team980.thunderscout.data;

import com.team980.thunderscout.data.enumeration.CrossingStats;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.enumeration.Rank;
import com.team980.thunderscout.data.enumeration.ScoringStats;

import java.io.Serializable;
import java.util.EnumMap;

/**
 * Implements data for one team from one match.
 */
public class ScoutData implements Serializable { //TODO why do driverSkill, comments have teleop in the name

    private String teamNumber;
    private long dateAdded;
    private String dataSource;
    //TODO sharing/sync status

    private CrossingStats autoCrossingStats;
    private Defense autoDefenseCrossed;
    private ScoringStats autoScoringStats;

    private float teleopDefensesBreached;
    private EnumMap<Defense, Rank> teleopMapDefensesBreached;
    private float teleopGoalsScored;

    private boolean teleopLowGoals;
    private boolean teleopHighGoals;
    private Rank teleopLowGoalRank;
    private Rank teleopHighGoalRank;

    private Rank teleopDriverSkill;
    private String teleopComments;

    public ScoutData() {
        autoCrossingStats = CrossingStats.NONE;
        autoScoringStats = ScoringStats.NONE;

        teleopMapDefensesBreached = new EnumMap<>(Defense.class);

        //default values
        teleopLowGoalRank = Rank.NOT_ATTEMPTED;
        teleopHighGoalRank = Rank.NOT_ATTEMPTED;
    }

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

    public CrossingStats getAutoCrossingStats() {
        return autoCrossingStats;
    }

    public void setAutoCrossingStats(CrossingStats autoCrossingStats) {
        this.autoCrossingStats = autoCrossingStats;
    }

    public Defense getAutoDefenseCrossed() {
        return autoDefenseCrossed;
    }

    public void setAutoDefenseCrossed(Defense autoDefenseCrossed) {
        this.autoDefenseCrossed = autoDefenseCrossed;
    }

    public ScoringStats getAutoScoringStats() {
        return autoScoringStats;
    }

    public void setAutoScoringStats(ScoringStats autoScoringStats) {
        this.autoScoringStats = autoScoringStats;
    }

    public float getTeleopDefensesBreached() {
        return teleopDefensesBreached;
    }

    public void setTeleopDefensesBreached(float teleopDefensesBreached) {
        this.teleopDefensesBreached = teleopDefensesBreached;
    }

    public EnumMap<Defense, Rank> getTeleopMapDefensesBreached() {
        return teleopMapDefensesBreached;
    }

    public float getTeleopGoalsScored() {
        return teleopGoalsScored;
    }

    public void setTeleopGoalsScored(float teleopGoalsScored) {
        this.teleopGoalsScored = teleopGoalsScored;
    }

    public boolean getTeleopLowGoals() {
        return teleopLowGoals;
    }

    public void setTeleopLowGoals(boolean teleopLowGoals) {
        this.teleopLowGoals = teleopLowGoals;
    }

    public boolean getTeleopHighGoals() {
        return teleopHighGoals;
    }

    public void setTeleopHighGoals(boolean teleopHighGoals) {
        this.teleopHighGoals = teleopHighGoals;
    }

    public Rank getTeleopLowGoalRank() {
        return teleopLowGoalRank;
    }

    public void setTeleopLowGoalRank(Rank teleopLowGoalRank) {
        this.teleopLowGoalRank = teleopLowGoalRank;
    }

    public Rank getTeleopHighGoalRank() {
        return teleopHighGoalRank;
    }

    public void setTeleopHighGoalRank(Rank teleopHighGoalRank) {
        this.teleopHighGoalRank = teleopHighGoalRank;
    }

    public Rank getTeleopDriverSkill() {
        return teleopDriverSkill;
    }

    public void setTeleopDriverSkill(Rank teleopDriverSkill) {
        this.teleopDriverSkill = teleopDriverSkill;
    }

    public String getTeleopComments() {
        return teleopComments;
    }

    public void setTeleopComments(String teleopComments) {
        this.teleopComments = teleopComments;
    }
}
