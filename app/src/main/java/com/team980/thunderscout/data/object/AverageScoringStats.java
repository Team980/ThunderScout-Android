package com.team980.thunderscout.data.object;

import com.team980.thunderscout.data.enumeration.ScoringStats;

import java.util.List;

/**
 * Simple wrapper class
 */
public class AverageScoringStats {

    private List<ScoringStats> stats;

    public AverageScoringStats(List<ScoringStats> ss) {
        stats = ss;
    }

    public int getNumberOfAveragedItems() {
        return stats.size();
    }

    public float getOccurrenceFloat(ScoringStats ss) {
        float occurrences = 0.0f;

        for (ScoringStats s : stats) {
            if (s.equals(ss)) {
                occurrences++;
            }
        }

        return occurrences / stats.size();
    }

    public float getOccurrencePercent(ScoringStats ss) {
        return getOccurrenceFloat(ss) * 100;
    }
}
