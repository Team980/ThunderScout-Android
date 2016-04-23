package com.team980.thunderscout.data.object;

import com.team980.thunderscout.data.enumeration.CrossingStats;

import java.util.List;

/**
 * Simple wrapper class
 */
public class AverageCrossingStats {

    private List<CrossingStats> stats;

    public AverageCrossingStats(List<CrossingStats> cs) {
        stats = cs;
    }

    public int getNumberOfAveragedItems() {
        return stats.size();
    }

    public float getOccurrenceFloat(CrossingStats cs) {
        float occurrences = 0.0f;

        for (CrossingStats c : stats) {
            if (c.equals(cs)) {
                occurrences++;
            }
        }

        return occurrences / stats.size();
    }

    public float getOccurrencePercent(CrossingStats cs) {
        return getOccurrenceFloat(cs) * 100;
    }
}
