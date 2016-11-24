package com.team980.thunderscout.info.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.AverageScoutData;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class AverageTeamInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_average_team_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TeamInfoActivity activity = (TeamInfoActivity) getActivity();

        AverageScoutData data = activity.getAverageScoutData();

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // --- Init ---
        TextView lastUpdated = (TextView) view.findViewById(R.id.info_average_lastUpdated);
        lastUpdated.setText("Last updated: " + SimpleDateFormat.getDateTimeInstance().format(data.getLastUpdated()));

        // --- Auto ---
        TextView autoTotalDefenses = (TextView) view.findViewById(R.id.info_average_autoTotalDefenses);
        autoTotalDefenses.setText(data.getAutoDefenseCrossings().size() + "");

        RecyclerView listAutoDefensesCrossed = (RecyclerView) view.findViewById(R.id.info_average_autoListDefenseCrossings);

        listAutoDefensesCrossed.setLayoutManager(new LinearLayoutManager(getContext()));
        listAutoDefensesCrossed.setAdapter(new DefenseAdapter(data.getAutoDefenseCrossings()));

        TextView autoTotalGoals = (TextView) view.findViewById(R.id.info_average_autoTotalGoals);
        autoTotalGoals.setText(formatter.format(data.getAverageAutoTotalGoals()));

        TextView autoLowGoals = (TextView) view.findViewById(R.id.info_average_autoLowGoals);
        autoLowGoals.setText(formatter.format(data.getAverageAutoLowGoals()));

        TextView autoHighGoals = (TextView) view.findViewById(R.id.info_average_autoHighGoals);
        autoHighGoals.setText(formatter.format(data.getAverageAutoHighGoals()));

        TextView autoMissedGoals = (TextView) view.findViewById(R.id.info_average_autoMissedGoals);
        autoMissedGoals.setText(formatter.format(data.getAverageAutoMissedGoals()));

        // --- Teleop ---
        TextView teleopTotalDefenses = (TextView) view.findViewById(R.id.info_average_teleopTotalDefenses);
        teleopTotalDefenses.setText(data.getTeleopDefenseCrossings().size() + "");

        RecyclerView listTeleopDefensesCrossed = (RecyclerView) view.findViewById(R.id.info_average_teleopListDefenseCrossings);

        listTeleopDefensesCrossed.setLayoutManager(new LinearLayoutManager(getContext()));
        listTeleopDefensesCrossed.setAdapter(new DefenseAdapter(data.getTeleopDefenseCrossings(), data.getNumberOfMatches()));

        TextView teleopTotalGoals = (TextView) view.findViewById(R.id.info_average_teleopTotalGoals);
        teleopTotalGoals.setText(formatter.format(data.getAverageTeleopTotalGoals()));

        TextView teleopLowGoals = (TextView) view.findViewById(R.id.info_average_teleopLowGoals);
        teleopLowGoals.setText(formatter.format(data.getAverageTeleopLowGoals()));

        TextView teleopHighGoals = (TextView) view.findViewById(R.id.info_average_teleopHighGoals);
        teleopHighGoals.setText(formatter.format(data.getAverageTeleopHighGoals()));

        TextView teleopMissedGoals = (TextView) view.findViewById(R.id.info_average_teleopMissedGoals);
        teleopMissedGoals.setText(formatter.format(data.getAverageTeleopMissedGoals()));
    }
}
