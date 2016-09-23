package com.team980.thunderscout.info;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.Defense;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class DefenseAdapter extends RecyclerView.Adapter<DefenseAdapter.DefenseViewHolder> {

    private List<Defense> defenseList;
    private EnumMap<Defense, Integer> defenseMap;

    public DefenseAdapter() {
        defenseList = new ArrayList<>();
        defenseMap = new EnumMap<>(Defense.class);
    }

    public DefenseAdapter(EnumMap<Defense, Integer> l) {
        defenseMap = l;

        defenseList = new ArrayList<>();
        defenseList.addAll(defenseMap.keySet());
    }


    @Override
    public DefenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.two_item_view, parent, false);

        return new DefenseViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(DefenseViewHolder holder, int i) {
        Defense defense = defenseList.get(i);
        int count = defenseMap.get(defense);

        holder.bind(defense.name(), count);
    }

    @Override
    public int getItemCount() {
        return defenseList.size();
    }

    public void add(Defense def, int count) {
        defenseList.add(def);
        defenseMap.put(def, count);
        notifyItemInserted(defenseMap.size() - 1);
    }

    public class DefenseViewHolder extends RecyclerView.ViewHolder {

        private TextView defense;
        private TextView count;

        public DefenseViewHolder(View itemView) {
            super(itemView);

            defense = (TextView) itemView.findViewById(R.id.data_key);
            count = (TextView) itemView.findViewById(R.id.data_value);
        }

        public void bind(String defText, int countNum) {
            defense.setText(defText);
            count.setText(String.valueOf(countNum));
        }
    }
}
