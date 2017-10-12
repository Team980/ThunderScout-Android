package com.team980.thunderscout.home.card_views;

import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.Card;
import com.team980.thunderscout.home.CardViewHolder;

public class BluetoothServerViewHolder extends CardViewHolder {

    private TextView cardText;

    public BluetoothServerViewHolder(View itemView) {
        super(itemView);

        cardText = itemView.findViewById(R.id.card_text);
    }

    @Override
    public void bind(Card card) {
        cardText.setText("Bluetooth server stats show up here");
    }
}
