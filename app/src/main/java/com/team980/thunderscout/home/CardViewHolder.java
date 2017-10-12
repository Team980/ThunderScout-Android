package com.team980.thunderscout.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract public class CardViewHolder extends RecyclerView.ViewHolder {

    public CardViewHolder(View itemView) {
        super(itemView);
    }

    abstract public void bind(Card card);
}
