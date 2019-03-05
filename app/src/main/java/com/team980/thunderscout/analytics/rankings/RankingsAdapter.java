/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.analytics.rankings;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.TeamComparator;
import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.analytics.rankings.breakdown.TeamInfoActivity;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.schema.ScoutData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RankingsAdapter extends RecyclerView.Adapter<RankingsAdapter.TeamViewHolder> implements StorageWrapper.StorageListener {

    //Instance state parameters
    private static final String KEY_CONTENTS = "adapter_contents";
    private static final String KEY_TEAM_FILTER = "adapter_team_filter";
    private static final String KEY_SELECTED_ITEMS = "adapter_selected_items";

    private LayoutInflater mInflator;
    private RankingsFragment fragment;
    private ArrayList<TeamWrapper> teamList; //The list that represents all the loaded data
    private TeamComparator sortMode = TeamComparator.SORT_TEAM_NUMBER;
    private String teamFilter = "";
    private ArrayList<TeamWrapper> selectedItems;
    private NumberFormat formatter;

    public RankingsAdapter(RankingsFragment fragment) {
        mInflator = LayoutInflater.from(fragment.getContext());

        this.fragment = fragment;

        teamList = new ArrayList<>();

        selectedItems = new ArrayList<>();

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
    }

    @Override
    public RankingsAdapter.TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View teamView = mInflator.inflate(R.layout.team_view, parent, false);
        return new TeamViewHolder(teamView);
    }

    @Override
    public void onBindViewHolder(RankingsAdapter.TeamViewHolder holder, int position) {
        TeamWrapper team = teamList.get(position);
        holder.bind(team);
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_CONTENTS, teamList);
        outState.putString(KEY_TEAM_FILTER, teamFilter);
        outState.putSerializable(KEY_SELECTED_ITEMS, selectedItems);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        teamList = (ArrayList<TeamWrapper>) savedInstanceState.getSerializable(KEY_CONTENTS);
        teamFilter = savedInstanceState.getString(KEY_TEAM_FILTER);
        selectedItems = ((ArrayList<TeamWrapper>) savedInstanceState.getSerializable(KEY_SELECTED_ITEMS));

        if (fragment.isInSelectionMode()) {
            //update UI with selections
            fragment.updateSelectionModeContext(selectedItems.size());
        }

        notifyDataSetChanged();
    }

    public TeamComparator getCurrentSortMode() {
        return sortMode;
    }

    public void sort(TeamComparator mode) {
        sortMode = mode;

        Collections.sort(teamList, TeamComparator.getComparator(sortMode));

        notifyDataSetChanged();
    }

    public void filterByTeam(String query) {
        teamFilter = query;

        AccountScope.getStorageWrapper(fragment.getContext()).queryData(this);
        notifyDataSetChanged();
    }

    public void select(TeamWrapper team) {
        selectedItems.add(team);

        if (!fragment.isInSelectionMode()) {
            //enter selection mode
            fragment.setSelectionMode(true);
        } else {
            fragment.updateSelectionModeContext(getSelectedItemCount());
        }
    }

    public void deselect(TeamWrapper team) {
        selectedItems.remove(team);

        if (getSelectedItemCount() == 0 && fragment.isInSelectionMode()) {
            //exit selection mode
            fragment.setSelectionMode(false);
        } else {
            fragment.updateSelectionModeContext(getSelectedItemCount());
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

    public List<TeamWrapper> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public void onDataQuery(List<ScoutData> dataList) {
        int listSize = teamList.size();
        teamList.clear();
        //notifyItemRangeRemoved(0, listSize);

        data:
        for (ScoutData data : dataList) {
            for (int i = 0; i < teamList.size(); i++) { //I wish there was an easier way, but there isn't
                TeamWrapper wrapper = teamList.get(i);

                if (wrapper.getTeam().equals(data.getTeam())) {
                    //Pre-existing team

                    wrapper.getDataList().add(data);
                    //notifyItemChanged(i);
                    continue data; //continues the loop labeled 'DATA'
                }
            }

            if (data.getTeam().toLowerCase().startsWith(teamFilter.toLowerCase())) { //Inline filtering in query - TODO add a setting to use contains?
                //New team
                TeamWrapper wrapper = new TeamWrapper(data.getTeam());
                wrapper.getDataList().add(data);
                teamList.add(wrapper);
                //notifyItemInserted(teamList.size() - 1);
            }
        }

        Collections.sort(teamList, TeamComparator.getComparator(sortMode));
        notifyDataSetChanged();

        fragment.getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void onDataClear(boolean success) {
        int listSize = teamList.size();
        teamList.clear();
        notifyItemRangeRemoved(0, listSize);
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private TextView teamNumber;
        private TextView descriptor;
        //private TextView rank;

        public TeamViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            teamNumber = itemView.findViewById(R.id.team_number);
            descriptor = itemView.findViewById(R.id.team_descriptor);
            //rank = itemView.findViewById(R.id.team_rank);
        }

        public void bind(final TeamWrapper wrapper) {
            teamNumber.setText(wrapper.getTeam());
            descriptor.setText(wrapper.getDescriptor(sortMode));

            //rank.setText(formatter.format(wrapper.getExpectedPointContribution()) + " points");

            if (selectedItems.contains(wrapper)) { //if selected
                itemView.setBackgroundColor(mInflator.getContext()
                        .getResources().getColor(R.color.background_floating));
                itemView.setSelected(true);
                itemView.setActivated(true);
            } else {
                itemView.setBackgroundColor(mInflator.getContext()
                        .getResources().getColor(android.R.color.transparent));
                itemView.setSelected(false);
                itemView.setActivated(false);
            }

            itemView.setOnClickListener(v -> {
                if (fragment.isInSelectionMode()) {
                    if (selectedItems.contains(wrapper)) { //if selected
                        deselect(wrapper);
                        itemView.setBackgroundColor(mInflator.getContext()
                                .getResources().getColor(android.R.color.transparent));
                        itemView.setSelected(false);
                        itemView.setActivated(false);
                    } else {
                        select(wrapper);
                        itemView.setBackgroundColor(mInflator.getContext()
                                .getResources().getColor(R.color.background_floating));
                        itemView.setSelected(true);
                        itemView.setActivated(true);
                    }
                } else {
                    Intent launchInfoActivity = new Intent(fragment.getContext(), TeamInfoActivity.class);
                    launchInfoActivity.putExtra(TeamInfoActivity.EXTRA_SCOUT_DATA_LIST, wrapper.getDataList());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions
                                .makeSceneTransitionAnimation(fragment.getActivity(), itemView, "team");
                        itemView.setTransitionName("team");
                        fragment.getContext().startActivity(launchInfoActivity, options.toBundle());
                    } else {
                        fragment.getContext().startActivity(launchInfoActivity);
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (selectedItems.contains(wrapper)) { //if selected
                    deselect(wrapper);
                    itemView.setBackgroundColor(mInflator.getContext()
                            .getResources().getColor(android.R.color.transparent));
                    itemView.setSelected(false);
                    itemView.setActivated(false);
                } else {
                    select(wrapper);
                    itemView.setBackgroundColor(mInflator.getContext()
                            .getResources().getColor(R.color.background_floating));
                    itemView.setSelected(true);
                    itemView.setActivated(true);
                }
                return true;
            });
        }
    }
}
