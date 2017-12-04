package com.team980.thunderscout.analytics.alliances;

import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.analytics.rankings.breakdown.AverageScoutData;
import com.team980.thunderscout.schema.ScoutData;

import java.util.ArrayList;
import java.util.List;

public class AverageAllianceData { //This is starting to strain my data model a bit

    private AverageScoutData station1;
    private AverageScoutData station2;
    private AverageScoutData station3;

    public AverageAllianceData(TeamWrapper station1Team, TeamWrapper station2Team, TeamWrapper station3Team) {
        this.station1 = new AverageScoutData(station1Team.getDataList());
        this.station2 = new AverageScoutData(station2Team.getDataList());
        this.station3 = new AverageScoutData(station3Team.getDataList());
    }

    public AverageScoutData getStation1() {
        return station1;
    }

    public AverageScoutData getStation2() {
        return station2;
    }

    public AverageScoutData getStation3() {
        return station3;
    }

    //AUTO

    public float getAverageAutoGearsDelivered() {
        return station1.getAverageAutoGearsDelivered()
                + station2.getAverageAutoGearsDelivered()
                + station3.getAverageAutoGearsDelivered();
    }

    public float getAverageAutoLowGoals() { // ugh I hate FuelDumpAmount soooo much
        float low1 = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size

        float low2 = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size

        float low3 = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size

        return low1 + low2 + low3;
    }

    public float getAverageAutoHighGoals() {
        return station1.getAverageAutoHighGoals()
                + station2.getAverageAutoHighGoals()
                + station3.getAverageAutoHighGoals();
    }

    //TELEOP

    public float getAverageTeleopGearsDelivered() {
        return station1.getAverageTeleopGearsDelivered()
                + station2.getAverageTeleopGearsDelivered()
                + station3.getAverageTeleopGearsDelivered();
    }

    public float getAverageTeleopLowGoals() { // ugh I hate FuelDumpAmount soooo much
        float low1 = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low1 *= station1.getAverageTeleopDumpFrequency(); /// times average dump amount

        float low2 = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low2 *= station1.getAverageTeleopDumpFrequency(); /// times average dump amount

        float low3 = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low3 *= station1.getAverageTeleopDumpFrequency(); /// times average dump amount

        return low1 + low2 + low3;
    }

    public float getAverageTeleopHighGoals() {
        return station1.getAverageTeleopHighGoals()
                + station2.getAverageTeleopHighGoals()
                + station3.getAverageTeleopHighGoals();
    }

    //SUMMARY

    public List<String> getTroublesList() {
        List<String> troublesList = new ArrayList<>();

        for (ScoutData data : station1.getDataList()) {
            if (data.getTroubleWith() != null && !data.getTroubleWith().isEmpty()) {
                troublesList.add("[" + data.getTeam() + " - " + data.getMatchNumber() + "] " + data.getTroubleWith()); //Prefix team and match number on comment
            }
        }

        for (ScoutData data : station2.getDataList()) {
            if (data.getTroubleWith() != null && !data.getTroubleWith().isEmpty()) {
                troublesList.add("[" + data.getTeam() + " - " + data.getMatchNumber() + "] " + data.getTroubleWith()); //Prefix team and match number on comment
            }
        }

        for (ScoutData data : station3.getDataList()) {
            if (data.getTroubleWith() != null && !data.getTroubleWith().isEmpty()) {
                troublesList.add("[" + data.getTeam() + " - " + data.getMatchNumber() + "] " + data.getTroubleWith()); //Prefix team and match number on comment
            }
        }

        return troublesList;
    }

    public List<String> getCommentsList() {
        List<String> commentsList = new ArrayList<>();

        for (ScoutData data : station1.getDataList()) {
            if (data.getComments() != null && !data.getComments().isEmpty()) {
                commentsList.add("[" + data.getTeam() + " - " + data.getMatchNumber() + "] " + data.getComments()); //Prefix team and match number on comment
            }
        }

        for (ScoutData data : station2.getDataList()) {
            if (data.getComments() != null && !data.getComments().isEmpty()) {
                commentsList.add("[" + data.getTeam() + " - " + data.getMatchNumber() + "] " + data.getComments()); //Prefix team and match number on comment
            }
        }

        for (ScoutData data : station3.getDataList()) {
            if (data.getComments() != null && !data.getComments().isEmpty()) {
                commentsList.add("[" + data.getTeam() + " - " + data.getMatchNumber() + "] " + data.getComments()); //Prefix team and match number on comment
            }
        }

        return commentsList;
    }
}
