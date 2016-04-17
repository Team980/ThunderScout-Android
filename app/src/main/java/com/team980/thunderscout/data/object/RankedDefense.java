package com.team980.thunderscout.data.object;

import java.io.Serializable;

/**
 * Wrapper class for ranking defense crossings
 */
public class RankedDefense implements Serializable {

    private Defense defense;
    private Rank rank;

    public RankedDefense(Defense d, Rank r) {
        defense = d;
        rank = r;
    }

    public Defense getDefense() {
        return defense;
    }

    public Rank getRank() {
        return rank;
    }
}
