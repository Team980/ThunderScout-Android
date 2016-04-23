package com.team980.thunderscout.data.object;

import com.team980.thunderscout.data.enumeration.Rank;

import java.util.List;

/**
 * Simple wrapper class
 */
public class AverageRank {

    private List<Rank> ranks;

    public AverageRank(List<Rank> r) {
        ranks = r;
    }

    public int getNumberOfAveragedItems() {
        return ranks.size();
    }

    public float getOccurrenceFloat(Rank rank) {
        float occurrences = 0.0f;

        for (Rank r : ranks) {
            if (r.equals(rank)) {
                occurrences++;
            }
        }

        return occurrences / ranks.size();
    }

    public float getOccurrencePercent(Rank rank) {
        return getOccurrenceFloat(rank) * 100;
    }
}