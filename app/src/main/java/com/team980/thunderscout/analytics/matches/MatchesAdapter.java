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

package com.team980.thunderscout.analytics.matches;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.AllianceStation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> implements StorageWrapper.StorageListener {

    private LayoutInflater mInflator;

    private MatchesFragment fragment;

    private SparseArray<MatchWrapper> matchArray;

    public MatchesAdapter(MatchesFragment fragment) {
        mInflator = LayoutInflater.from(fragment.getContext());

        this.fragment = fragment;

        matchArray = new SparseArray<>();

        fragment.getDataView().getRecycledViewPool().setMaxRecycledViews(0, 0); //disable recyclerview caching - TODO find a way to fix the scroll bugs without doing this as this is bad for performance

    }

    @Override
    public MatchesAdapter.MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View matchView = mInflator.inflate(R.layout.match_view, parent, false);
        return new MatchViewHolder(matchView);
    }

    @Override
    public void onBindViewHolder(MatchesAdapter.MatchViewHolder holder, int position) {
        MatchWrapper match = matchArray.get(matchArray.keyAt(position)); //Treat it like a list for this code
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matchArray.size();
    }

    @Override
    public void onDataQuery(List<ScoutData> dataList) {
        int arraySize = matchArray.size();
        matchArray.clear();
        notifyItemRangeRemoved(0, arraySize);

        fragment.getDataView().getRecycledViewPool().clear(); //Fixes the GridLayout constraint bug by flushing the cache

        for (ScoutData data : dataList) {
            MatchWrapper wrapper = matchArray.get(data.getMatchNumber());

            if (wrapper == null) {
                wrapper = new MatchWrapper(data.getMatchNumber());
                matchArray.put(data.getMatchNumber(), wrapper);
                notifyItemInserted(data.getMatchNumber());
            }

            wrapper.setData(data.getAllianceStation(), data);
            notifyItemChanged(data.getMatchNumber());
        }

        fragment.getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void onDataWrite(boolean success) {
        //do nothing for now
    }

    @Override
    public void onDataRemove(boolean success) {
        //do nothing for now
    }

    @Override
    public void onDataClear(boolean success) {
        int arraySize = matchArray.size();
        matchArray.clear();
        notifyItemRangeRemoved(0, arraySize);
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        //TODO needs a way to handle overlapping data (same matchNumber and AllianceStation)
        //TODO fix the sizing issues when various cells are empty + when team numbers differ in length

        private TextView matchNumber;
        private GridLayout matchGrid;

        public MatchViewHolder(View itemView) {
            super(itemView);

            matchNumber = (TextView) itemView.findViewById(R.id.match_number);
            matchGrid = (GridLayout) itemView.findViewById(R.id.match_grid);
        }

        public void bind(MatchWrapper wrapper) {
            if (wrapper != null) {
                matchNumber.setText(wrapper.getMatchNumber() + "");
            }

            for (AllianceStation station : AllianceStation.values()) {
                TextView matchView = new TextView(mInflator.getContext());

                matchView.setGravity(Gravity.CENTER);
                matchView.setTextAppearance(mInflator.getContext(), R.style.TextAppearance_AppCompat_Body1);
                matchView.setTextSize(24.0f);

                if (wrapper.getData(station) != null) {
                    matchView.setText(wrapper.getData(station).getTeam());

                    matchView.setBackgroundColor(mInflator.getContext()
                            .getResources().getColor(station.getColorStratified()));

                    //TODO register onClick listener
                } else {
                    matchView.setBackgroundColor(mInflator.getContext()
                            .getResources().getColor(android.R.color.transparent));
                }

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f));
                matchView.setLayoutParams(params);

                matchGrid.addView(matchView);
            }
        }
    }
}
