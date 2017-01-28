package com.team980.thunderscout.info.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

import java.util.List;

public class FuelDumpAdapter extends RecyclerView.Adapter<FuelDumpAdapter.FuelDumpViewHolder> {

    private List<FuelDumpAmount> dumpList;

    public FuelDumpAdapter(List<FuelDumpAmount> l) {
        dumpList = l;
    }

    @Override
    public FuelDumpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.fuel_dump_view, parent, false);

        return new FuelDumpViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(FuelDumpViewHolder holder, int i) {
        FuelDumpAmount amount = dumpList.get(i);

        holder.bind(amount);
    }

    @Override
    public int getItemCount() {
        return dumpList.size();
    }

    public void add(FuelDumpAmount s) {
        dumpList.add(s);
        notifyItemInserted(dumpList.size() - 1);
    }

    public class FuelDumpViewHolder extends RecyclerView.ViewHolder {

        private TextView value;
        private TextView numericalValue;

        public FuelDumpViewHolder(View itemView) {
            super(itemView);

            value = (TextView) itemView.findViewById(R.id.value);
            numericalValue = (TextView) itemView.findViewById(R.id.numericalValue);

        }

        public void bind(FuelDumpAmount amount) {
            value.setText(amount.toString());
            numericalValue.setText("(" + amount.getMinimumAmount() + " - " + amount.getMaximumAmount() + ")");
        }
    }
}
