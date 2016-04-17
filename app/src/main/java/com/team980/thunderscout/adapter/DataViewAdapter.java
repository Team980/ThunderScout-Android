package com.team980.thunderscout.adapter;

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
import com.team980.thunderscout.activity.InfoActivity;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.object.TeamWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DataViewAdapter extends ExpandableRecyclerAdapter<DataViewAdapter.TeamViewHolder, DataViewAdapter.ScoutViewHolder> {

    private LayoutInflater mInflator;

    private Context context;

    public DataViewAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);

        mInflator = LayoutInflater.from(context);

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

    /**
     * Adds an entry to the view. Called by the database reader.
     *
     * @param data ScoutData to insert
     */
    public void addScoutData(ScoutData data) {
        ArrayList<TeamWrapper> teamList = (ArrayList) getParentItemList();

        Log.d("Adding Data", "Fetching parent item list");

        for (int i = 0; i < teamList.size(); i++) {
            TeamWrapper tw = teamList.get(i);
            Log.d("Adding Data", "Looping: " + i);

            if (tw.getTeamNumber().equals(data.getTeamNumber())) {
                //Pre-existing team
                Log.d("Adding Data", "Pre existing team: " + data.getTeamNumber());

                ArrayList<ScoutData> childList = (ArrayList) tw.getChildItemList();

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
                return;
            }
        }
        //New team
        Log.d("Adding Data", "Adding new parent to list");
        teamList.add(new TeamWrapper(data.getTeamNumber(), data));
        notifyParentItemInserted(teamList.size() - 1); //TODO verify this
    }

    public class TeamViewHolder extends ParentViewHolder {

        private TextView teamNumber;
        private TextView descriptor;

        private ImageButton infoButton;

        public TeamViewHolder(View itemView) {
            super(itemView);

            teamNumber = (TextView) itemView.findViewById(R.id.team_teamNumber);
            descriptor = (TextView) itemView.findViewById(R.id.team_descriptor);

            infoButton = (ImageButton) itemView.findViewById(R.id.team_infoButton);
        }

        public void bind(final TeamWrapper tw) {
            teamNumber.setText(String.valueOf(tw.getTeamNumber()));
            descriptor.setText(tw.getDescriptor());

            infoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent launchInfoActivity = new Intent(context, InfoActivity.class);
                    launchInfoActivity.putExtra("com.team980.thunderscout.IS_AGGREGATE_DATA", true);
                    launchInfoActivity.putExtra("com.team980.thunderscout.INFO_TEAM", tw);
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

            infoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent launchInfoActivity = new Intent(context, InfoActivity.class);
                    launchInfoActivity.putExtra("com.team980.thunderscout.IS_AGGREGATE_DATA", false);
                    launchInfoActivity.putExtra("com.team980.thunderscout.INFO_SCOUT", scoutData);
                    context.startActivity(launchInfoActivity);
                }
            });
        }
    }


}
