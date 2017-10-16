package com.team980.thunderscout.home;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * A single card on the homepage's feed.
 */
public class Card implements Comparable<Card> {

    private CardType type;

    private Context context;

    public Card(CardType type, Context context) {
        this.type = type;
        this.context = context;
    }

    public CardType getCardType() {
        return type;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int compareTo(@NonNull Card other) {
        return type.compareTo(other.getCardType());
    }
}
