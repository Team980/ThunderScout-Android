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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.matches.breakdown.MatchInfoActivity;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.AllianceStation;

import java.util.ArrayList;
import java.util.List;


public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> implements StorageWrapper.StorageListener {

    private LayoutInflater mInflator;

    private MatchesFragment fragment;

    private SparseArray<MatchWrapper> matchArray;

    private ArrayList<ScoutData> selectedItems;

    public MatchesAdapter(MatchesFragment fragment) {
        mInflator = LayoutInflater.from(fragment.getContext());

        this.fragment = fragment;

        matchArray = new SparseArray<>();

        selectedItems = new ArrayList<>();
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

    public void select(ScoutData data) {
        selectedItems.add(data);

        if (!fragment.isInSelectionMode()) {
            //enter selection mode
            fragment.setSelectionMode(true);
        } else {
            fragment.updateSelectionModeTitle(getSelectedItemCount());
        }
    }

    public void deselect(ScoutData data) {
        selectedItems.remove(data);

        if (getSelectedItemCount() == 0 && fragment.isInSelectionMode()) {
            //exit selection mode
            fragment.setSelectionMode(false);
        } else {
            fragment.updateSelectionModeTitle(getSelectedItemCount());
        }
    }

    public void clearSelections() {
        selectedItems.clear();

        if (fragment.isInSelectionMode()) {
            //exit selection mode
            fragment.setSelectionMode(false);
        }

        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<ScoutData> getSelectedItems() {
        return selectedItems;
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
    public void onDataWrite(List<ScoutData> dataWritten) {
        //do nothing for now
    }

    @Override
    public void onDataRemove(List<ScoutData> dataRemoved) {
        for (ScoutData data : dataRemoved) {
            matchArray.get(data.getMatchNumber()).removeData(data.getAllianceStation(), data);
            notifyItemChanged(data.getMatchNumber() - 1); //This makes very little sense

            if (matchArray.get(data.getMatchNumber()).isEmpty()) {
                matchArray.delete(data.getMatchNumber());
                notifyItemRemoved(data.getMatchNumber()); //Again, why are these different
            }
        }

        //notifyDataSetChanged(); //only one that works :(
    }

    @Override
    public void onDataClear(boolean success) {
        int arraySize = matchArray.size();
        matchArray.clear();
        notifyItemRangeRemoved(0, arraySize);
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        //TODO needs a way to handle overlapping data (same matchNumber and AllianceStation)

        private LinearLayout itemView;

        private TextView matchNumber;
        private GridLayout matchGrid;

        public MatchViewHolder(View itemView) {
            super(itemView);

            this.itemView = (LinearLayout) itemView;

            matchNumber = itemView.findViewById(R.id.match_number);
            matchGrid = itemView.findViewById(R.id.match_grid);
        }

        public void bind(final MatchWrapper wrapper) {
            if (wrapper != null) { //Why would this ever be null!?
                matchNumber.setText(wrapper.getMatchNumber() + "");

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fragment.isInSelectionMode()) {
                            for (AllianceStation station : AllianceStation.values()) {
                                if (wrapper.getData(station) == null) {
                                    continue;
                                }

                                if (selectedItems.contains(wrapper.getData(station))) { //if selected
                                    deselect(wrapper.getData(station));
                                    itemView.findViewById(station.getMatchCellViewID())
                                            .setBackgroundColor(mInflator.getContext()
                                                    .getResources().getColor(station.getColorStratified()));
                                } else {
                                    select(wrapper.getData(station));
                                    itemView.findViewById(station.getMatchCellViewID())
                                            .setBackgroundColor(mInflator.getContext()
                                                    .getResources().getColor(R.color.accent));
                                }
                            }
                        } else {
                            //Nothing yet
                        }
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        for (AllianceStation station : AllianceStation.values()) {
                            if (wrapper.getData(station) == null) {
                                continue;
                            }

                            if (selectedItems.contains(wrapper.getData(station))) { //if selected
                                deselect(wrapper.getData(station));
                                itemView.findViewById(station.getMatchCellViewID())
                                        .setBackgroundColor(mInflator.getContext()
                                                .getResources().getColor(station.getColorStratified()));
                            } else {
                                select(wrapper.getData(station));
                                itemView.findViewById(station.getMatchCellViewID())
                                        .setBackgroundColor(mInflator.getContext()
                                                .getResources().getColor(R.color.accent));
                            }
                        }
                        return true;
                    }
                });
            } else {
                matchNumber.setText(""); //is this really necessary?
            }

            for (final AllianceStation station : AllianceStation.values()) {
                final TextView matchView = matchGrid.findViewById(station.getMatchCellViewID());

                if (wrapper.getData(station) != null) {

                    if (wrapper.getDataList(station).size() == 1) { //Single match, treat as normal
                        matchView.setText(wrapper.getData(station).getTeam());

                        matchView.setVisibility(View.VISIBLE);

                        if (selectedItems.contains(wrapper.getData(station))) { //if selected
                            matchView.setBackgroundColor(mInflator.getContext()
                                    .getResources().getColor(R.color.accent));
                        } else {
                            matchView.setBackgroundColor(mInflator.getContext()
                                    .getResources().getColor(station.getColorStratified()));
                        }

                        matchView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fragment.isInSelectionMode()) {
                                    if (selectedItems.contains(wrapper.getData(station))) { //if selected
                                        deselect(wrapper.getData(station));
                                        matchView.setBackgroundColor(mInflator.getContext()
                                                .getResources().getColor(station.getColorStratified()));
                                    } else {
                                        select(wrapper.getData(station));
                                        matchView.setBackgroundColor(mInflator.getContext()
                                                .getResources().getColor(R.color.accent));
                                    }
                                } else {
                                    Intent launchInfoActivity = new Intent(fragment.getContext(), MatchInfoActivity.class);
                                    launchInfoActivity.putExtra("com.team980.thunderscout.INFO_SCOUT", wrapper.getData(station));
                                    fragment.getContext().startActivity(launchInfoActivity);
                                }
                            }
                        });

                        matchView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if (selectedItems.contains(wrapper.getData(station))) { //if selected
                                    deselect(wrapper.getData(station));
                                    matchView.setBackgroundColor(mInflator.getContext()
                                            .getResources().getColor(station.getColorStratified()));
                                } else {
                                    select(wrapper.getData(station));
                                    matchView.setBackgroundColor(mInflator.getContext()
                                            .getResources().getColor(R.color.accent));
                                }
                                return true;
                            }
                        });

                    } else { //MULTIPLE matches in this slot - display error!
                        matchView.setText("!");

                        matchView.setVisibility(View.VISIBLE);

                        matchView.setBackgroundColor(mInflator.getContext()
                                .getResources().getColor(R.color.accent_dark));


                        matchView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fragment.isInSelectionMode()) {
                                    return;
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                                View dialogView = mInflator.inflate(R.layout.dialog_match_conflict, null);

                                builder.setView(dialogView)
                                        .setTitle("Resolve conflict...")
                                        .setNegativeButton("Cancel", null);

                                Dialog dialog = builder.create();

                                RecyclerView conflictView = dialogView.findViewById(R.id.dialog_matchConflictAdapter);
                                conflictView.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
                                conflictView.setAdapter(new MatchConflictAdapter(wrapper.getDataList(station),
                                        MatchesAdapter.this, dialog, fragment.getContext()));

                                dialog.show();
                            }
                        });
                    }
                } else {
                    matchView.setText("");

                    matchView.setVisibility(View.INVISIBLE);

                    matchView.setOnClickListener(null);
                    matchView.setClickable(false);

                    matchView.setOnLongClickListener(null);
                    matchView.setLongClickable(false);
                }
            }
        }
    }
}
