package com.team980.thunderscout.home.card_views;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.CardViewHolder;

public class PendingMatchesViewHolder extends CardViewHolder {

    private ProgressBar cardProgress;
    private ImageButton dismissButton;
    private TextView cardText;

    public PendingMatchesViewHolder(View itemView) {
        super(itemView);

        cardProgress = itemView.findViewById(R.id.card_progress);
        dismissButton = itemView.findViewById(R.id.card_button_dismiss);
        cardText = itemView.findViewById(R.id.card_text);
    }

    @Override
    public void bind() {
        cardProgress.setIndeterminate(true);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO dismiss
            }
        });
        cardText.setText("Sending to UNKNOWN...");
    }

    @Override
    public void update() {

    }
}
