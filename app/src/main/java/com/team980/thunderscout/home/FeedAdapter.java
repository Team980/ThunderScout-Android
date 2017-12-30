package com.team980.thunderscout.home;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        cards.add(new Card("Card 1")
                .setIcon(context.getResources().getDrawable(R.drawable.ic_info_white_24dp))
                .setText("Hello, world! This is a card, and it can show up to provide you useful information. " +
                        "Perhaps there's a setting you forgot to configure, or a new feature introduced in the latest app version.")
                .addAction(new CardAction("View", (card, action) -> Toast.makeText(context, "View", Toast.LENGTH_LONG).show()))
                .addAction(new CardAction("Delete", (card, action) -> Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show())));

        cards.add(new Card("Card 2")
                .setIcon(context.getResources().getDrawable(R.drawable.ic_bluetooth_transfer_white_24dp))
                .setText("In the future, cards will also be able to notify you of currently running processes, such as the Bluetooth server." +
                        "\nThese persistent cards will display recently scouted matches with quick entry points to view them in full, while also keeping track of statistics such as the success rate of Bluetooth transmissions."));

        cards.add(new Card("Card 3")
                .setIcon(context.getResources().getDrawable(R.drawable.ic_delete_white_24dp))
                .setText("Many contextual cards will appear as you use the app. Once acted upon, you can dismiss one of these cards by swiping to the right or clicking the Dismiss button.")
                .setDismissable(true)
                .addAction(new CardAction("Dismiss", (card, action) -> dismissCard(card))));

        cards.add(new Card("Card 4")
                .setIcon(context.getResources().getDrawable(R.drawable.ic_assignment_white_24dp))
                .setText("Some cards will even present custom layouts with special information, like shown above.")
                .addSection(new CardSection(R.layout.match_view, sectionView -> {
                    sectionView.setBackground(new ColorDrawable(context.getResources().getColor(R.color.background_floating_light)));

                    TextView matchNumber = sectionView.findViewById(R.id.match_number);
                    matchNumber.setText("13");
                })));

        cards.add(new Card("Card 5")
                .setIcon(context.getResources().getDrawable(R.drawable.ic_flag_white_24dp))
                .setText("Here's an example of how multiple custom views can be added to a card.")
                .setDismissable(true)
                .addSection(new CardSection(R.layout.match_view, sectionView -> sectionView.setBackground(new ColorDrawable(context.getResources().getColor(R.color.background_floating_light)))))
                .addSection(new CardSection(R.layout.match_view, sectionView -> sectionView.setBackground(new ColorDrawable(context.getResources().getColor(R.color.background_floating_light)))))
                .addAction(new CardAction("Dismiss", (card, action) -> dismissCard(card))));

        notifyItemRangeInserted(0, 5);
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

    public void addCard(Card card) {
        cards.add(card);
        notifyItemInserted(cards.size() - 1);
    }

    public void dismissCard(int index) {
        cards.remove(index);
        notifyItemRemoved(index);
    }

    public void dismissCard(Card card) {
        dismissCard(cards.indexOf(card));
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
