package com.team980.thunderscout.home.card_views;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.Card;
import com.team980.thunderscout.home.CardViewHolder;

public class PendingMatchesViewHolder extends CardViewHolder {

    private ProgressBar cardProgress;
    private TextView cardText;

    public PendingMatchesViewHolder(View itemView) {
        super(itemView);

        cardProgress = itemView.findViewById(R.id.card_progress);
        cardText = itemView.findViewById(R.id.card_text);
    }

    @Override
    public void bind(Card card) {
        cardProgress.setIndeterminate(true);
        cardText.setText("Sending to UNKNOWN...");
    }
}
