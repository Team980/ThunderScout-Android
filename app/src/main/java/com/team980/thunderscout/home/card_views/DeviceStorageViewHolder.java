package com.team980.thunderscout.home.card_views;

import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.Card;
import com.team980.thunderscout.home.CardViewHolder;

public class DeviceStorageViewHolder extends CardViewHolder {

    private TextView cardText;

    public DeviceStorageViewHolder(View itemView) {
        super(itemView);

        cardText = itemView.findViewById(R.id.card_text);
    }

    @Override
    public void bind(Card card) {
        cardText.setText("Saved matches show up here");
    }
}
