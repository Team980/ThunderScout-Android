package com.team980.thunderscout.data;

import com.team980.thunderscout.data.enumeration.CrossingStats;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.enumeration.Rank;
import com.team980.thunderscout.data.enumeration.ScoringStats;
import com.team980.thunderscout.data.object.AverageCrossingStats;
import com.team980.thunderscout.data.object.AverageRank;
import com.team980.thunderscout.data.object.AverageScoringStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Class to manage averaging of values and objects.
 * Created by TeamWrapper.
 */
public class AverageScoutData implements Serializable {

    private List<ScoutData> data;

    public AverageScoutData(List<ScoutData> d) {
        data = d;
    }

    public AverageCrossingStats getAverageAutoCrossingStats() {
        List<CrossingStats> cs = new ArrayList<>();

        for (ScoutData d : data) {
            cs.add(d.getAutoCrossingStats());
        }

        return new AverageCrossingStats(cs);
    }

    public List<Defense> getCumulativeAutoDefensesCrossed() {
        List<Defense> defenses = new ArrayList<>();

        for (ScoutData d : data) {

            if (d.getAutoCrossingStats() == CrossingStats.CROSSED) {
                defenses.add(d.getAutoDefenseCrossed());
            }
        }

        return defenses;
    }

    public AverageScoringStats getAverageAutoScoringStats() {
        List<ScoringStats> ss = new ArrayList<>();

        for (ScoutData d : data) {
            ss.add(d.getAutoScoringStats());
        }

        return new AverageScoringStats(ss);
    }

    public float getAverageTeleopDefensesBreached() {
        return getCumulativeTeleopDefensesBreached() / data.size();
    }

    public float getCumulativeTeleopDefensesBreached() {
        float totalBreached = 0.0f;

        for (ScoutData d : data) {
            totalBreached += d.getTeleopDefensesBreached();
        }

        return totalBreached;
    }

    /**
     * Will always average the statistics of multiple crossings of the same defense
     */
    public EnumMap<Defense, AverageRank> getCumulativeTeleopDefensesCrossed() {
        EnumMap<Defense, AverageRank> defenses = new EnumMap<>(Defense.class);

        for (Defense d : Defense.values()) {
            ArrayList<Rank> ranks = new ArrayList<>();

            for (ScoutData scoutData : data) {
                EnumMap<Defense, Rank> potentials = scoutData.getTeleopMapDefensesBreached();

                if (!potentials.containsKey(d) || potentials.get(d) == Rank.NOT_ATTEMPTED) {
                    //The defense was either never put into the map or was initialized as NOT_ATTEMPTED;
                    //we don't care about that value here
                    continue;
                }

                ranks.add(potentials.get(d));
            }

            defenses.put(d, new AverageRank(ranks));
        }

        return defenses;
    }

    public int getAverageTeleopGoalsScored() {
        return getCumulativeTeleopGoalsScored() / data.size();
    }

    public int getCumulativeTeleopGoalsScored() {
        int goalsScored = 0;

        for (ScoutData d : data) {
            goalsScored += d.getTeleopGoalsScored();
        }

        return goalsScored;
    }

    public boolean getTeleopLowGoals() {
        for (ScoutData scoutData : data) {
            if (scoutData.getTeleopLowGoals()) {
                return true;
            }
        }

        return false;
    }

    public boolean getTeleopHighGoals() {
        for (ScoutData scoutData : data) {
            if (scoutData.getTeleopHighGoals()) {
                return true;
            }
        }

        return false;
    }

    public AverageRank getAverageTeleopLowGoalRank() {
        ArrayList<Rank> ranks = new ArrayList<>();

        for (ScoutData d : data) {
            ranks.add(d.getTeleopLowGoalRank());
        }

        return new AverageRank(ranks);
    }

    public AverageRank getAverageTeleopHighGoalRank() {
        ArrayList<Rank> ranks = new ArrayList<>();

        for (ScoutData d : data) {
            ranks.add(d.getTeleopHighGoalRank());
        }

        return new AverageRank(ranks);
    }

    public AverageRank getAverageDriverSkill() {
        ArrayList<Rank> ranks = new ArrayList<>();

        for (ScoutData d : data) {
            ranks.add(d.getTeleopDriverSkill());
        }

        return new AverageRank(ranks);
    }

    public List<String> getComments() {
        ArrayList<String> comments = new ArrayList<>();

        for (ScoutData d : data) {
            comments.add(d.getTeleopComments());
        }

        return comments;
    }
}
