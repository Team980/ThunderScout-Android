package com.team980.thunderscout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.Defense;

import java.util.ArrayList;
import java.util.List;

public class DefenseAdapter extends RecyclerView.Adapter<DefenseAdapter.DefenseViewHolder> {

    private List<Defense> defenseList;

    public DefenseAdapter() {
        defenseList = new ArrayList<>();
    }

    public DefenseAdapter(List<Defense> l) {
        defenseList = l;
    }


    @Override
    public DefenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.one_item_view, parent, false);

        return new DefenseViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(DefenseViewHolder holder, int i) {
        Defense defense = defenseList.get(i);

        holder.bind(defense.toString());
    }

    @Override
    public int getItemCount() {
        return defenseList.size();
    }

    public void add(Defense def) {
        defenseList.add(def);
        notifyItemInserted(defenseList.size() - 1);
    }

    public class DefenseViewHolder extends RecyclerView.ViewHolder {

        private TextView defense;

        public DefenseViewHolder(View itemView) {
            super(itemView);

            defense = (TextView) itemView.findViewById(R.id.data_key);
        }

        public void bind(String defText) {
            defense.setText(defText);
        }
    }
}
