package com.team980.thunderscout.home.schema;

import android.support.annotation.NonNull;

/**
 * An action that sits at the bottom of a card.
 */
public class CardAction {

    private String name;
    private ActionCallback callback;

    public CardAction(@NonNull String name, @NonNull ActionCallback callback) {
        this.name = name;
        this.callback = callback;
    }

    public String getName() {
        return name;
    }

    public void onClick(Card card) {
        callback.onActionClick(card, this);
    }

    /**
     * A callback to execute click events.
     */
    public interface ActionCallback {
        void onActionClick(Card card, CardAction action);
    }
}
