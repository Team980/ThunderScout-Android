package com.team980.thunderscout.match;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

import java.util.ArrayList;

public class FuelDumpAdapter extends RecyclerView.Adapter<FuelDumpAdapter.FuelDumpViewHolder> {

    private ArrayList<FuelDumpAmount> fuelDumps;

    public FuelDumpAdapter() {
        fuelDumps = new ArrayList<>();
    }

    @Override
    public FuelDumpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.fuel_dump_view, parent, false);

        return new FuelDumpViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(FuelDumpViewHolder holder, int i) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return fuelDumps.size();
    }

    public void add(FuelDumpAmount s) {
        fuelDumps.add(s);
        notifyItemInserted(fuelDumps.size() - 1);
    }

    public class FuelDumpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button plus;
        private Button minus;

        private TextView text;
        private TextView numericValue;

        public FuelDumpViewHolder(View itemView) {
            super(itemView);

            plus = (Button) itemView.findViewById(R.id.plus);
            minus = (Button) itemView.findViewById(R.id.minus);

            text = (TextView) itemView.findViewById(R.id.value);
            numericValue = (TextView) itemView.findViewById(R.id.numericalValue);
        }

        public void bind() {
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);

            FuelDumpAmount value = fuelDumps.get(getLayoutPosition());

            text.setText(value.toString());
            numericValue.setText(value.getMinimumAmount() + " - " + value.getMaximumAmount());

        }

        public void onClick(View v) {
            FuelDumpAmount value = fuelDumps.get(getLayoutPosition());

            if (v.getId() == R.id.plus) {
                int newOrdinal = value.ordinal() + 1;

                if ((FuelDumpAmount.values().length - 1) < newOrdinal) {
                    value = FuelDumpAmount.values()[FuelDumpAmount.values().length - 1];
                } else {
                    value = FuelDumpAmount.values()[newOrdinal];
                }

            } else if (v.getId() == R.id.minus) {
                int newOrdinal = value.ordinal() - 1;

                if (newOrdinal < 0) {
                    value = FuelDumpAmount.values()[0];
                } else {
                    value = FuelDumpAmount.values()[newOrdinal];
                }
            }

            fuelDumps.set(getLayoutPosition(), value); // :D

            text.setText(value.toString());
            numericValue.setText(value.getMinimumAmount() + " - " + value.getMaximumAmount());
        }
    }
}
