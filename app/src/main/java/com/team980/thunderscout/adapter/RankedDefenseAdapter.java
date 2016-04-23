package com.team980.thunderscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.enumeration.Rank;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;

public class RankedDefenseAdapter extends RecyclerView.Adapter<RankedDefenseAdapter.RankedDefenseViewHolder> {

    private EnumMap<Defense, Rank> defenseMap;

    public RankedDefenseAdapter() {
        defenseMap = new EnumMap<>(Defense.class);
    }

    public RankedDefenseAdapter(EnumMap<Defense, Rank> map) {
        defenseMap = map;
    }


    @Override
    public RankedDefenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.two_item_view, parent, false);

        return new RankedDefenseViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(RankedDefenseViewHolder holder, int i) {
        Set<Defense> entries = defenseMap.keySet();
        Defense def = (Defense) entries.toArray()[i];

        Collection<Rank> values = defenseMap.values();
        Rank rank = (Rank) values.toArray()[i];

        holder.bind(def.toString(), rank.getDescription());
    }

    @Override
    public int getItemCount() {
        return defenseMap.size();
    }

    public void add(Defense def, Rank rank) {
        defenseMap.put(def, rank);
        notifyItemInserted(defenseMap.size() - 1);
    }

    public class RankedDefenseViewHolder extends RecyclerView.ViewHolder {

        private TextView defense;
        private TextView rank;

        public RankedDefenseViewHolder(View itemView) {
            super(itemView);

            defense = (TextView) itemView.findViewById(R.id.data_defense);

            rank = (TextView) itemView.findViewById(R.id.data_rank);
        }

        public void bind(String defText, String rankText) {
            defense.setText(defText);
            rank.setText(rankText);
        }
    }
}
