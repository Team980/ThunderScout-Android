package com.team980.thunderscout.info.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.Defense;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class DefenseAdapter extends RecyclerView.Adapter<DefenseAdapter.DefenseViewHolder> {

    private List<Defense> defenseList;
    private EnumMap<Defense, Integer> defenseMap;

    private float numberOfMatches;

    private NumberFormat formatter;

    public DefenseAdapter(EnumMap<Defense, Integer> l) {
        defenseMap = l;

        defenseList = new ArrayList<>();
        defenseList.addAll(defenseMap.keySet());

        numberOfMatches = 1;

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);
    }

    public DefenseAdapter(EnumMap<Defense, Integer> l, int i) {
        defenseMap = l;
        numberOfMatches = i;

        defenseList = new ArrayList<>();
        defenseList.addAll(defenseMap.keySet());

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);
    }


    @Override
    public DefenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.defense_view, parent, false);

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

        private ProgressBar crossingBar;

        public DefenseViewHolder(View itemView) {
            super(itemView);

            defense = (TextView) itemView.findViewById(R.id.comment);
            count = (TextView) itemView.findViewById(R.id.times_crossed);

            crossingBar = (ProgressBar) itemView.findViewById(R.id.crossing_bar);
        }

        public void bind(String defText, int countNum) {
            defense.setText(defText);

            float displayNum = (countNum / numberOfMatches);

            if (numberOfMatches == 1) {
                count.setText(formatter.format(displayNum) + " crossings");
            } else {
                count.setText(formatter.format(displayNum) + " crossings per match");
            }

            int maxCount = 0;
            for (Defense defense : Defense.values()) {
                if (defenseMap.get(defense) != null && defenseMap.get(defense) > maxCount) {
                    maxCount = defenseMap.get(defense);
                    //this iterates and gets the largest count
                }
            }

            crossingBar.setMax(maxCount);
            crossingBar.setProgress(countNum);
        }
    }
}
