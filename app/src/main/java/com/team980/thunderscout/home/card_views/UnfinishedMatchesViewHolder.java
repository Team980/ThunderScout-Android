package com.team980.thunderscout.home.card_views;

import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.CardViewHolder;

public class UnfinishedMatchesViewHolder extends CardViewHolder {

    private TextView cardText;

    public UnfinishedMatchesViewHolder(View itemView) {
        super(itemView);

        cardText = itemView.findViewById(R.id.card_text);
    }

    @Override
    public void bind() {
        cardText.setText("Unfinished matches show up here");
    }

    @Override
    public void update() {

    }
}
