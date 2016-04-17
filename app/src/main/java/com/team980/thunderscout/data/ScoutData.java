package com.team980.thunderscout.data;

import com.team980.thunderscout.data.object.CrossingStats;
import com.team980.thunderscout.data.object.Defense;
import com.team980.thunderscout.data.object.Rank;
import com.team980.thunderscout.data.object.RankedDefense;
import com.team980.thunderscout.data.object.ScoringStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoutData implements Serializable {

    public float teleopDefensesBreached;
    public List<RankedDefense> teleopListDefensesBreached;
    public float teleopGoalsScored;
    public boolean teleopLowGoals;
    public boolean teleopHighGoals;
    public Rank teleopDriverSkill;
    public String teleopComments;
    private String teamNumber;
    private long dateAdded;
    private CrossingStats autoCrossingStats;
    private Defense autoDefenseCrossed;
    private ScoringStats autoScoringStats;

    public ScoutData() {
        autoCrossingStats = CrossingStats.NONE;
        autoScoringStats = ScoringStats.NONE;

        teleopListDefensesBreached = new ArrayList<>();
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

    public List<RankedDefense> getTeleopListDefensesBreached() {
        return teleopListDefensesBreached;
    }

    //todo phase out set(List) for add(RankedDefense)?
    @Deprecated
    public void setTeleopListDefensesBreached(List<RankedDefense> teleopListDefensesBreached) {
        this.teleopListDefensesBreached = teleopListDefensesBreached;
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
