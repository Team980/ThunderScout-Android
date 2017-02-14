/*
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.match;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

import java.util.ArrayList;

public class DumpCounterAdapter extends RecyclerView.Adapter<DumpCounterAdapter.FuelDumpViewHolder> {

    private ArrayList<FuelDumpAmount> fuelDumps;

    public DumpCounterAdapter() {
        fuelDumps = new ArrayList<>();
    }

    @Override
    public FuelDumpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.dump_counter_view, parent, false);

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

    public ArrayList<FuelDumpAmount> get() {
        return fuelDumps;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("FuelDumps", fuelDumps);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null
                || !savedInstanceState.containsKey("FuelDumps")) {
            return;
        }

        ArrayList<FuelDumpAmount> itemList = (ArrayList<FuelDumpAmount>) savedInstanceState.getSerializable("FuelDumps");
        if (itemList == null) {
            return;
        }

        fuelDumps = itemList;
        notifyDataSetChanged();
    }

    public class FuelDumpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button plus;
        private Button minus;
        private ImageButton remove;

        private TextView text;
        private TextView numericValue;

        public FuelDumpViewHolder(View itemView) {
            super(itemView);

            plus = (Button) itemView.findViewById(R.id.plus);
            minus = (Button) itemView.findViewById(R.id.minus);
            remove = (ImageButton) itemView.findViewById(R.id.remove);

            text = (TextView) itemView.findViewById(R.id.value);
            numericValue = (TextView) itemView.findViewById(R.id.numericalValue);
        }

        public void bind() {
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
            remove.setOnClickListener(this);

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
            } else if (v.getId() == R.id.remove) {
                fuelDumps.remove(getLayoutPosition());
                notifyItemRemoved(getLayoutPosition());
                return;
            }

            fuelDumps.set(getLayoutPosition(), value); // :D

            text.setText(value.toString());
            numericValue.setText(value.getMinimumAmount() + " - " + value.getMaximumAmount());
        }
    }
}
