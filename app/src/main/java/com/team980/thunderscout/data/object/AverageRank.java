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

    public Rank getAverageRank() {
        int id = 0;

        for (Rank rank : ranks) {
            id += rank.getId();
        }

        if (ranks.isEmpty()) {
            return Rank.NOT_ATTEMPTED;
        }

        return Rank.fromId(id / ranks.size());
    }
}