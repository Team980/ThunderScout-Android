package com.team980.thunderscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.object.RankedDefense;

import java.util.ArrayList;
import java.util.List;

public class RankedDefenseAdapter extends RecyclerView.Adapter<RankedDefenseAdapter.RankedDefenseViewHolder> {

    private ArrayList<RankedDefense> defenseList;

    public RankedDefenseAdapter() {
        defenseList = new ArrayList<>();
    }

    public RankedDefenseAdapter(List<RankedDefense> list) {
        defenseList = (ArrayList<RankedDefense>) list;
    }


    @Override
    public RankedDefenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.data_view, parent, false);

        return new RankedDefenseViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(RankedDefenseViewHolder holder, int position) {
        RankedDefense defense = defenseList.get(position);

        String def = defense.getDefense().toString();
        String rank = defense.getRank().getDescription();

        holder.bind(def, rank);
    }

    @Override
    public int getItemCount() {
        return defenseList.size();
    }

    public void add(RankedDefense def) {
        defenseList.add(def);
        notifyItemInserted(defenseList.size() - 1);
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
