package com.team980.thunderscout.info;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.object.AverageRank;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;

public class AverageRankedDefenseAdapter extends RecyclerView.Adapter<AverageRankedDefenseAdapter.AverageRankedDefenseViewHolder> {

    private EnumMap<Defense, AverageRank> defenseMap;

    public AverageRankedDefenseAdapter() {
        defenseMap = new EnumMap<>(Defense.class);
    }

    public AverageRankedDefenseAdapter(EnumMap<Defense, AverageRank> map) {
        defenseMap = map;
    }


    @Override
    public AverageRankedDefenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.two_item_view, parent, false);

        return new AverageRankedDefenseViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(AverageRankedDefenseViewHolder holder, int i) {
        Set<Defense> entries = defenseMap.keySet();
        Defense def = (Defense) entries.toArray()[i];

        Collection<AverageRank> values = defenseMap.values();
        AverageRank rank = (AverageRank) values.toArray()[i];

        holder.bind(def.toString(), rank.getAverageRank().getDescription());
    }

    @Override
    public int getItemCount() {
        return defenseMap.size();
    }

    public void add(Defense def, AverageRank rank) {
        defenseMap.put(def, rank);
        notifyItemInserted(defenseMap.size() - 1);
    }

    public class AverageRankedDefenseViewHolder extends RecyclerView.ViewHolder {

        private TextView defense;
        private TextView rank;

        public AverageRankedDefenseViewHolder(View itemView) {
            super(itemView);

            defense = (TextView) itemView.findViewById(R.id.data_key);

            rank = (TextView) itemView.findViewById(R.id.data_value);
        }

        public void bind(String defText, String rankText) {
            defense.setText(defText);
            rank.setText(rankText);
        }
    }
}
