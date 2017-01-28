package com.team980.thunderscout.info.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.AverageScoutData;
import com.team980.thunderscout.data.enumeration.ClimbingStats;

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
        TextView autoGearsDelivered = (TextView) view.findViewById(R.id.info_average_autoGearsDelivered);
        autoGearsDelivered.setText(formatter.format(data.getAverageAutoGearsDelivered()) + "");

        TextView autoFuelDumpValue = (TextView) view.findViewById(R.id.info_average_autoLowGoalDumpAmount);
        TextView autoFuelDumpNumericalValue = (TextView) view.findViewById(R.id.info_average_autoLowGoalNumericalDumpAmount);
        autoFuelDumpValue.setText(data.getAverageAutoLowGoalDumpAmount().toString());
        autoFuelDumpNumericalValue.setText("(" + data.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + " - " + data.getAverageAutoLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView autoHighGoals = (TextView) view.findViewById(R.id.info_average_autoHighGoals);
        autoHighGoals.setText(formatter.format(data.getAverageAutoHighGoals()));

        TextView autoMissedGoals = (TextView) view.findViewById(R.id.info_average_autoMissedHighGoals);
        autoMissedGoals.setText(formatter.format(data.getAverageAutoMissedHighGoals()));

        TextView crossedBaselinePercentage = (TextView) view.findViewById(R.id.info_average_autoCrossedBaselinePercentage);
        crossedBaselinePercentage.setText(formatter.format(data.getCrossedBaselinePercentage()) + "%");

        ProgressBar crossedBaselineProgressBar = (ProgressBar) view.findViewById(R.id.info_average_autoCrossedBaselineProgressBar);
        crossedBaselineProgressBar.setProgress((int) data.getCrossedBaselinePercentage());

        // --- Teleop ---
        TextView teleopGearsDelivered = (TextView) view.findViewById(R.id.info_average_teleopGearsDelivered);
        teleopGearsDelivered.setText(formatter.format(data.getAverageTeleopGearsDelivered()) + "");

        TextView teleopDumpFrequency = (TextView) view.findViewById(R.id.info_average_teleopFuelDumps);
        teleopDumpFrequency.setText(formatter.format(data.getAverageTeleopDumpFrequency()) + "");

        TextView teleopFuelDumpValue = (TextView) view.findViewById(R.id.info_average_teleopLowGoalDumpAmount);
        TextView teleopFuelDumpNumericalValue = (TextView) view.findViewById(R.id.info_average_teleopLowGoalNumericalDumpAmount);
        teleopFuelDumpValue.setText(data.getAverageTeleopLowGoalDumpAmount().toString());
        teleopFuelDumpNumericalValue.setText("(" + data.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + " - " + data.getAverageTeleopLowGoalDumpAmount().getMaximumAmount() + ")");

        TextView teleopHighGoals = (TextView) view.findViewById(R.id.info_average_teleopHighGoals);
        teleopHighGoals.setText(formatter.format(data.getAverageTeleopHighGoals()));

        TextView teleopMissedGoals = (TextView) view.findViewById(R.id.info_average_teleopMissedHighGoals);
        teleopMissedGoals.setText(formatter.format(data.getAverageTeleopMissedHighGoals()));

        TextView attemptedClimbPercentage = (TextView) view.findViewById(R.id.info_average_teleopAttemptedClimbPercentage);
        attemptedClimbPercentage.setText(formatter.format(data.getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB)) + "%");

        ProgressBar attemptedClimbProgressBar = (ProgressBar) view.findViewById(R.id.info_average_teleopAttemptedClimbProgressBar);
        attemptedClimbProgressBar.setProgress((int) data.getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB));

        TextView pressedTouchpadPercentage = (TextView) view.findViewById(R.id.info_average_teleopPressedTouchpadPercentage);
        pressedTouchpadPercentage.setText(formatter.format(data.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");

        ProgressBar pressedTouchpadProgressBar = (ProgressBar) view.findViewById(R.id.info_average_teleopPressedTouchpadProgressBar);
        pressedTouchpadProgressBar.setProgress((int) data.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD));

        // --- Summary ---

        RecyclerView troubleWith = (RecyclerView) view.findViewById(R.id.info_average_summaryTroubleWith);

        troubleWith.setLayoutManager(new LinearLayoutManager(getContext()));
        troubleWith.setAdapter(new CommentsAdapter(data.getTroublesList()));

        RecyclerView comments = (RecyclerView) view.findViewById(R.id.info_average_summaryComments);

        comments.setLayoutManager(new LinearLayoutManager(getContext()));
        comments.setAdapter(new CommentsAdapter(data.getCommentsList()));
    }
}
