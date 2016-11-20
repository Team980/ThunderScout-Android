package com.team980.thunderscout.info;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.TeamWrapper;
import com.team980.thunderscout.info.statistics.MatchInfoActivity;
import com.team980.thunderscout.info.statistics.TeamInfoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.team980.thunderscout.data.TeamWrapper.TeamComparator.SORT_TEAM_NUMBER;
import static com.team980.thunderscout.data.TeamWrapper.TeamComparator.getComparator;

public class LocalDataAdapter extends ExpandableRecyclerAdapter<LocalDataAdapter.TeamViewHolder, LocalDataAdapter.ScoutViewHolder> {

    private LayoutInflater mInflator;

    private ArrayList<TeamWrapper> teams;

    private Context context;

    private TeamWrapper.TeamComparator sortMode = SORT_TEAM_NUMBER;

    public LocalDataAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);

        mInflator = LayoutInflater.from(context); //TODO move to ViewGroup.getContext()

        teams = (ArrayList<TeamWrapper>) getParentItemList();

        this.context = context;
    }

    // onCreate ...
    @Override
    public TeamViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View teamView = mInflator.inflate(R.layout.team_view, parentViewGroup, false);
        return new TeamViewHolder(teamView);
    }

    @Override
    public ScoutViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View scoutView = mInflator.inflate(R.layout.scout_view, childViewGroup, false);
        return new ScoutViewHolder(scoutView);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(TeamViewHolder teamViewHolder, int position, ParentListItem parentListItem) {
        TeamWrapper team = (TeamWrapper) parentListItem;
        teamViewHolder.bind(team);
    }

    @Override
    public void onBindChildViewHolder(ScoutViewHolder scoutViewHolder, int position, Object childListItem) {
        ScoutData scoutData = (ScoutData) childListItem;
        scoutViewHolder.bind(scoutData);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * Adds an entry to the view. Called by the database reader.
     *
     * @param data ScoutData to insert
     */
    public void addScoutData(ScoutData data) {
        Log.d("Adding Data", "Fetching parent item list");

        for (int i = 0; i < teams.size(); i++) {
            TeamWrapper tw = teams.get(i);
            Log.d("Adding Data", "Looping: " + i);

            if (tw.getTeamNumber().equals(data.getTeamNumber())) {
                //Pre-existing team
                Log.d("Adding Data", "Pre existing team: " + data.getTeamNumber());

                ArrayList<ScoutData> childList = (ArrayList<ScoutData>) tw.getChildItemList();

                Log.d("Adding Data", "Fetching child item list");

                for (ScoutData child : childList) {
                    Log.d("Adding Data", "Looping child: " + child.getTeamNumber());
                    if (child.getDateAdded() == (data.getDateAdded())) { //TODO verify this works
                        //This child has already been added to the database
                        Log.d("Adding Data", "Child already in DB");
                        return;
                    }
                }

                childList.add(data);
                Log.d("Adding Data", "Adding new child to parent");
                notifyChildItemInserted(i, childList.size() - 1); //TODO verify this
                notifyParentItemChanged(i); //This forces the parent to update

                sort(sortMode);
                return;
            }
        }
        //New team
        Log.d("Adding Data", "Adding new parent to list");
        teams.add(new TeamWrapper(data.getTeamNumber(), data));
        notifyParentItemInserted(teams.size() - 1); //TODO verify this

        sort(sortMode);
    }

    /**
     * Removes all the data from the list.
     * Called by the database emptier.
     */
    public void clearData() {
        if (teams.size() == 0) {
            //list is empty
            return;
        }

        notifyParentItemRangeRemoved(0, teams.size());
        getParentItemList().removeAll(teams);
    }

    public void sort(TeamWrapper.TeamComparator mode) {
        sortMode = mode;

        Collections.sort(teams, getComparator(sortMode));
        notifyParentItemRangeChanged(0, teams.size());
    }



    public class TeamViewHolder extends ParentViewHolder {

        private TextView teamNumber;
        private TextView descriptor;

        private ImageButton expandButton;

        public TeamViewHolder(View itemView) {
            super(itemView);

            teamNumber = (TextView) itemView.findViewById(R.id.team_teamNumber);
            descriptor = (TextView) itemView.findViewById(R.id.team_descriptor);

            expandButton = (ImageButton) itemView.findViewById(R.id.team_expandButton);
        }

        public void bind(final TeamWrapper tw) {
            teamNumber.setText("Team " + String.valueOf(tw.getTeamNumber()));
            descriptor.setText(tw.getDescriptor(sortMode));

            if (sortMode == TeamWrapper.TeamComparator.SORT_TEAM_NUMBER) {
                descriptor.setText(tw.getNumberOfMatches() + " matches");
            } else {
                descriptor.setText(tw.getDescriptor(sortMode));
            }

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        collapseView();
                        expandButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
                    } else {
                        expandView();
                        expandButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchInfoActivity = new Intent(context, TeamInfoActivity.class);
                    launchInfoActivity.putExtra("com.team980.thunderscout.INFO_AVERAGE_SCOUT", tw.getAverageScoutData());
                    context.startActivity(launchInfoActivity);
                }
            });
        }
    }

    public class ScoutViewHolder extends ChildViewHolder {

        private TextView dateAdded;

        private ImageButton infoButton;

        public ScoutViewHolder(View itemView) {
            super(itemView);

            dateAdded = (TextView) itemView.findViewById(R.id.scout_dateAdded);

            infoButton = (ImageButton) itemView.findViewById(R.id.scout_infoButton);
        }

        public void bind(final ScoutData scoutData) {
            dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(scoutData.getDateAdded()));

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent launchInfoActivity = new Intent(context, MatchInfoActivity.class);
                    launchInfoActivity.putExtra("com.team980.thunderscout.INFO_SCOUT", scoutData);
                    context.startActivity(launchInfoActivity);
                }
            });

            infoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent launchInfoActivity = new Intent(context, MatchInfoActivity.class);
                    launchInfoActivity.putExtra("com.team980.thunderscout.INFO_SCOUT", scoutData);
                    context.startActivity(launchInfoActivity);
                }
            });
        }
    }


}
