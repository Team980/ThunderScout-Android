package com.team980.thunderscout.home.card_views;

import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.Card;
import com.team980.thunderscout.home.CardViewHolder;

public class RecentMatchesViewHolder extends CardViewHolder {

    private TextView cardText;

    public RecentMatchesViewHolder(View itemView) {
        super(itemView);

        cardText = itemView.findViewById(R.id.card_text);
    }

    @Override
    public void bind(Card card) {
        cardText.setText("Recent matches show up here");
    }
}
