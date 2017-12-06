package com.team980.thunderscout.analytics.rankings.compare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.TeamComparator;
import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.schema.ScoutData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom subclass that implements the required view binding to support TeamWrapper objects
 */
public class TeamListAdapter extends ArrayAdapter<TeamWrapper> implements StorageWrapper.StorageListener {

    private LayoutInflater inflater;

    public TeamListAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_team_header);
        setDropDownViewResource(R.layout.spinner_team_header_dropdown);

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.spinner_team_header, parent, false);
        }

        TextView viewText = view.findViewById(android.R.id.text1);
        viewText.setText(getItem(position).getTeam());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.spinner_team_header_dropdown, parent, false);
        }

        TextView viewText = view.findViewById(android.R.id.text1);
        viewText.setText("Team " + getItem(position).getTeam());
        return view;
    }

    @Override
    public void onDataQuery(List<ScoutData> dataList) {
        ArrayList<TeamWrapper> teamList = new ArrayList<>();

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

            //New team
            TeamWrapper wrapper = new TeamWrapper(data.getTeam());
            wrapper.getDataList().add(data);
            teamList.add(wrapper);
        }

        Collections.sort(teamList, TeamComparator.getComparator(TeamComparator.SORT_TEAM_NUMBER));

        addAll(teamList);
    }
}
