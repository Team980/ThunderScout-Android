package com.team980.thunderscout.home;

import android.support.annotation.NonNull;

/**
 * A single card on the homepage's feed.
 */
public class Card implements Comparable<Card> {

    private CardType type;

    public Card(CardType type) {
        this.type = type;
    }

    public CardType getCardType() {
        return type;
    }

    @Override
    public int compareTo(@NonNull Card other) {
        return type.compareTo(other.getCardType());
    }
}
