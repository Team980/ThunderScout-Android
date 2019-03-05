/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.team980.thunderscout.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.schema.Card;
import com.team980.thunderscout.home.schema.CardAction;
import com.team980.thunderscout.home.schema.CardSection;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CardViewHolder> {

    private Context context;

    private LayoutInflater mInflator;

    private ArrayList<Card> cards;

    public FeedAdapter(Context context) {
        this.context = context;

        mInflator = LayoutInflater.from(context);

        cards = new ArrayList<>();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(mInflator.inflate(R.layout.card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.bind(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public int indexOf(Card card) {
        return cards.indexOf(card);
    }

    public void addCard(Card card) {
        cards.add(card);
        notifyItemInserted(cards.size() - 1);
    }

    public void addCard(Card card, int position) {
        cards.add(position, card);
        notifyItemInserted(position);
    }

    public void dismissCard(int index) {
        cards.remove(index);
        notifyItemRemoved(index);
    }

    public void dismissCard(Card card) {
        dismissCard(cards.indexOf(card));
    }

    public void clearCards() {
        cards.clear();
        notifyDataSetChanged();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private Card card;

        private TextView title;
        private TextView text;
        private ImageView icon;

        private LinearLayout actions;

        private LinearLayout sections;

        public CardViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_title);
            text = itemView.findViewById(R.id.card_text);
            icon = itemView.findViewById(R.id.card_icon);

            actions = itemView.findViewById(R.id.card_actions);

            sections = itemView.findViewById(R.id.card_sections);
        }

        public void bind(Card card) {
            this.card = card;

            title.setText(card.getTitle());
            text.setText(card.getText());
            icon.setImageDrawable(card.getIcon());

            actions.removeAllViews();
            for (CardAction action : card.getActions()) {
                View actionView = mInflator.inflate(R.layout.card_action_view, actions, false);

                Button actionButton = actionView.findViewById(R.id.card_action_button);
                actionButton.setText(action.getName());
                actionButton.setOnClickListener(v -> action.onClick(card));

                actions.addView(actionView);
            }

            sections.removeAllViews();
            if (card.getSections().size() > 0) {
                sections.setVisibility(View.VISIBLE);
                for (CardSection section : card.getSections()) {
                    View sectionView = mInflator.inflate(section.getLayoutId(), sections, false);

                    section.bind(sectionView);

                    sections.addView(sectionView);
                }
            } else {
                sections.setVisibility(View.GONE);
            }
        }

        public Card getCard() {
            return card;
        }
    }
}
