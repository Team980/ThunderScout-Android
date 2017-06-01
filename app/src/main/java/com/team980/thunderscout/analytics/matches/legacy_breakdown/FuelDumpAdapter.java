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

package com.team980.thunderscout.analytics.matches.legacy_breakdown;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.schema.enumeration.FuelDumpAmount;

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
