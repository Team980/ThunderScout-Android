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
import android.content.Intent;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.matches.legacy_statistics.MatchInfoActivity;
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
    }

    @Override
    public MatchesAdapter.MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View matchView = mInflator.inflate(R.layout.match_view, parent, false);
        return new MatchViewHolder(matchView);
    }

    @Override
    public void onBindViewHolder(MatchesAdapter.MatchViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        MatchWrapper match = matchArray.get(matchArray.keyAt(position)); //Treat it like a list for this code
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matchArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onDataQuery(List<ScoutData> dataList) {
        int arraySize = matchArray.size();
        matchArray.clear();
        notifyItemRangeRemoved(0, arraySize);

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

        private TextView matchNumber;
        private GridLayout matchGrid;

        public MatchViewHolder(View itemView) {
            super(itemView);

            matchNumber = (TextView) itemView.findViewById(R.id.match_number);
            matchGrid = (GridLayout) itemView.findViewById(R.id.match_grid);
        }

        public void bind(final MatchWrapper wrapper) {
            if (wrapper != null) { //Why would this ever be null!?
                matchNumber.setText(wrapper.getMatchNumber() + "");
            }

            for (final AllianceStation station : AllianceStation.values()) {
                TextView matchView = (TextView) matchGrid.findViewById(station.getMatchCellViewID());

                if (wrapper.getData(station) != null) {
                    matchView.setText(wrapper.getData(station).getTeam());

                    matchView.setBackgroundColor(mInflator.getContext()
                            .getResources().getColor(station.getColorStratified()));

                    matchView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent launchInfoActivity = new Intent(fragment.getContext(), MatchInfoActivity.class);
                            launchInfoActivity.putExtra("com.team980.thunderscout.INFO_SCOUT", wrapper.getData(station));
                            fragment.getContext().startActivity(launchInfoActivity);
                        }
                    });

                    matchView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(fragment.getContext(), "Selection NYI", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                } else {
                    matchView.setText("");

                    matchView.setBackgroundColor(mInflator.getContext()
                            .getResources().getColor(android.R.color.transparent));

                    matchView.setOnClickListener(null);
                    matchView.setClickable(false);

                    matchView.setOnLongClickListener(null);
                    matchView.setLongClickable(false);
                }
            }
        }
    }
}