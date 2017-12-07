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

package com.team980.thunderscout.analytics.rankings.compare;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.analytics.rankings.TeamPointEstimator;
import com.team980.thunderscout.analytics.rankings.breakdown.AverageScoutData;
import com.team980.thunderscout.analytics.rankings.breakdown.CommentsAdapter;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.util.List;

public class CompareBottomSheetFragment extends BottomSheetDialogFragment {

    private AverageScoutData[] alliance = new AverageScoutData[3];

    private TeamListAdapter adapter;

    public static CompareBottomSheetFragment newInstance(TeamWrapper t1, TeamWrapper t2, TeamWrapper t3) {
        CompareBottomSheetFragment f = new CompareBottomSheetFragment();

        Bundle args = new Bundle();
        args.putSerializable("station1", t1.getDataList());
        args.putSerializable("station2", t2.getDataList());
        args.putSerializable("station3", t3.getDataList());
        f.setArguments(args);

        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.Theme_Design_BottomSheetDialog);

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet_compare, null);
        dialog.setContentView(dialogView);

        Toolbar toolbar = dialogView.findViewById(R.id.toolbar);
        toolbar.setTitle("Compare teams...");

        AverageScoutData station1 = new AverageScoutData((List<ScoutData>) getArguments().getSerializable("station1"));
        AverageScoutData station2 = new AverageScoutData((List<ScoutData>) getArguments().getSerializable("station2"));
        AverageScoutData station3 = new AverageScoutData((List<ScoutData>) getArguments().getSerializable("station3"));

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // Auto
        TextView crossPercent = dialogView.findViewById(R.id.info_alliance_station1_autoCrossPercentage);
        crossPercent.setText(formatter.format(station1.getCrossedBaselinePercentage()) + "%");
        TextView crossPercent2 = dialogView.findViewById(R.id.info_alliance_station2_autoCrossPercentage);
        crossPercent2.setText(formatter.format(station2.getCrossedBaselinePercentage()) + "%");
        TextView crossPercent3 = dialogView.findViewById(R.id.info_alliance_station3_autoCrossPercentage);
        crossPercent3.setText(formatter.format(station3.getCrossedBaselinePercentage()) + "%");

        TextView mobility = dialogView.findViewById(R.id.info_alliance_station1_autoMobilityPoints);
        mobility.setText(formatter.format(TeamPointEstimator.getBaselinePoints(station1)) + " pts");
        TextView mobility2 = dialogView.findViewById(R.id.info_alliance_station2_autoMobilityPoints);
        mobility2.setText(formatter.format(TeamPointEstimator.getBaselinePoints(station2)) + " pts");
        TextView mobility3 = dialogView.findViewById(R.id.info_alliance_station3_autoMobilityPoints);
        mobility3.setText(formatter.format(TeamPointEstimator.getBaselinePoints(station3)) + " pts");

        TextView autoLowGoalCount = dialogView.findViewById(R.id.info_alliance_station1_autoLowGoalCount);
        autoLowGoalCount.setText(formatter.format((station1.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageAutoLowGoalDumpAmount().getMaximumAmount()) / 2));
        TextView autoLowGoalCount2 = dialogView.findViewById(R.id.info_alliance_station2_autoLowGoalCount);
        autoLowGoalCount2.setText(formatter.format((station2.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + station2.getAverageAutoLowGoalDumpAmount().getMaximumAmount()) / 2));
        TextView autoLowGoalCount3 = dialogView.findViewById(R.id.info_alliance_station3_autoLowGoalCount);
        autoLowGoalCount3.setText(formatter.format((station3.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + station3.getAverageAutoLowGoalDumpAmount().getMaximumAmount()) / 2));

        TextView autoHighGoalCount = dialogView.findViewById(R.id.info_alliance_station1_autoHighGoalCount);
        autoHighGoalCount.setText(formatter.format(station1.getAverageAutoHighGoals()));
        TextView autoHighGoalCount2 = dialogView.findViewById(R.id.info_alliance_station2_autoHighGoalCount);
        autoHighGoalCount2.setText(formatter.format(station2.getAverageAutoHighGoals()));
        TextView autoHighGoalCount3 = dialogView.findViewById(R.id.info_alliance_station3_autoHighGoalCount);
        autoHighGoalCount3.setText(formatter.format(station3.getAverageAutoHighGoals()));

        TextView autoMissedGoalCount = dialogView.findViewById(R.id.info_alliance_station1_autoMissedGoalCount);
        autoMissedGoalCount.setText(formatter.format(station1.getAverageAutoMissedHighGoals()));
        TextView autoMissedGoalCount2 = dialogView.findViewById(R.id.info_alliance_station2_autoMissedGoalCount);
        autoMissedGoalCount2.setText(formatter.format(station2.getAverageAutoMissedHighGoals()));
        TextView autoMissedGoalCount3 = dialogView.findViewById(R.id.info_alliance_station3_autoMissedGoalCount);
        autoMissedGoalCount3.setText(formatter.format(station3.getAverageAutoMissedHighGoals()));

        TextView autoFuelPoints = dialogView.findViewById(R.id.info_alliance_station1_autoFuelPoints);
        autoFuelPoints.setText(formatter.format(TeamPointEstimator.getAutoFuelPoints(station1)) + " pts");
        TextView autoFuelPoints2 = dialogView.findViewById(R.id.info_alliance_station2_autoFuelPoints);
        autoFuelPoints2.setText(formatter.format(TeamPointEstimator.getAutoFuelPoints(station2)) + " pts");
        TextView autoFuelPoints3 = dialogView.findViewById(R.id.info_alliance_station3_autoFuelPoints);
        autoFuelPoints3.setText(formatter.format(TeamPointEstimator.getAutoFuelPoints(station3)) + " pts");

        TextView autoGearDeliveryCount = dialogView.findViewById(R.id.info_alliance_station1_autoGearDeliveryCount);
        autoGearDeliveryCount.setText(formatter.format(station1.getAverageAutoGearsDelivered()));
        TextView autoGearDeliveryCount2 = dialogView.findViewById(R.id.info_alliance_station2_autoGearDeliveryCount);
        autoGearDeliveryCount2.setText(formatter.format(station2.getAverageAutoGearsDelivered()));
        TextView autoGearDeliveryCount3 = dialogView.findViewById(R.id.info_alliance_station3_autoGearDeliveryCount);
        autoGearDeliveryCount3.setText(formatter.format(station3.getAverageAutoGearsDelivered()));

        TextView autoGearDropCount = dialogView.findViewById(R.id.info_alliance_station1_autoGearDropCount);
        autoGearDropCount.setText(formatter.format(station1.getAverageAutoGearsDropped()));
        TextView autoGearDropCount2 = dialogView.findViewById(R.id.info_alliance_station2_autoGearDropCount);
        autoGearDropCount2.setText(formatter.format(station2.getAverageAutoGearsDropped()));
        TextView autoGearDropCount3 = dialogView.findViewById(R.id.info_alliance_station3_autoGearDropCount);
        autoGearDropCount3.setText(formatter.format(station3.getAverageAutoGearsDropped()));

        TextView autoRotorPoints = dialogView.findViewById(R.id.info_alliance_station1_autoRotorPoints);
        autoRotorPoints.setText(formatter.format(TeamPointEstimator.getAutoRotorPoints(station1)) + " pts");
        TextView autoRotorPoints2 = dialogView.findViewById(R.id.info_alliance_station2_autoRotorPoints);
        autoRotorPoints2.setText(formatter.format(TeamPointEstimator.getAutoRotorPoints(station2)) + " pts");
        TextView autoRotorPoints3 = dialogView.findViewById(R.id.info_alliance_station3_autoRotorPoints);
        autoRotorPoints3.setText(formatter.format(TeamPointEstimator.getAutoRotorPoints(station3)) + " pts");

        // Teleop
        TextView teleopLowGoalCount = dialogView.findViewById(R.id.info_alliance_station1_teleopLowGoalCount);
        // ugh I hate FuelDumpAmount soooo much
        float low = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low *= station1.getAverageTeleopDumpFrequency(); /// times average dump amount
        teleopLowGoalCount.setText(formatter.format(low));
        TextView teleopLowGoalCount2 = dialogView.findViewById(R.id.info_alliance_station2_teleopLowGoalCount);
        // ugh I hate FuelDumpAmount soooo much
        float low2 = (station2.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station2.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low2 *= station2.getAverageTeleopDumpFrequency(); /// times average dump amount
        teleopLowGoalCount2.setText(formatter.format(low2));
        TextView teleopLowGoalCount3 = dialogView.findViewById(R.id.info_alliance_station3_teleopLowGoalCount);
        // ugh I hate FuelDumpAmount soooo much
        float low3 = (station3.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station3.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low3 *= station3.getAverageTeleopDumpFrequency(); /// times average dump amount
        teleopLowGoalCount3.setText(formatter.format(low3));

        TextView teleopHighGoalCount = dialogView.findViewById(R.id.info_alliance_station1_teleopHighGoalCount);
        teleopHighGoalCount.setText(formatter.format(station1.getAverageTeleopHighGoals()));
        TextView teleopHighGoalCount2 = dialogView.findViewById(R.id.info_alliance_station2_teleopHighGoalCount);
        teleopHighGoalCount2.setText(formatter.format(station2.getAverageTeleopHighGoals()));
        TextView teleopHighGoalCount3 = dialogView.findViewById(R.id.info_alliance_station3_teleopHighGoalCount);
        teleopHighGoalCount3.setText(formatter.format(station3.getAverageTeleopHighGoals()));

        TextView teleopMissedGoalCount = dialogView.findViewById(R.id.info_alliance_station1_teleopMissedGoalCount);
        teleopMissedGoalCount.setText(formatter.format(station1.getAverageTeleopMissedHighGoals()));
        TextView teleopMissedGoalCount2 = dialogView.findViewById(R.id.info_alliance_station2_teleopMissedGoalCount);
        teleopMissedGoalCount2.setText(formatter.format(station2.getAverageTeleopMissedHighGoals()));
        TextView teleopMissedGoalCount3 = dialogView.findViewById(R.id.info_alliance_station3_teleopMissedGoalCount);
        teleopMissedGoalCount3.setText(formatter.format(station3.getAverageTeleopMissedHighGoals()));

        TextView teleopFuelPoints = dialogView.findViewById(R.id.info_alliance_station1_teleopFuelPoints);
        teleopFuelPoints.setText(formatter.format(TeamPointEstimator.getTeleopFuelPoints(station1)) + " pts");
        TextView teleopFuelPoints2 = dialogView.findViewById(R.id.info_alliance_station2_teleopFuelPoints);
        teleopFuelPoints2.setText(formatter.format(TeamPointEstimator.getTeleopFuelPoints(station2)) + " pts");
        TextView teleopFuelPoints3 = dialogView.findViewById(R.id.info_alliance_station3_teleopFuelPoints);
        teleopFuelPoints3.setText(formatter.format(TeamPointEstimator.getTeleopFuelPoints(station3)) + " pts");

        TextView teleopGearDeliveryCount = dialogView.findViewById(R.id.info_alliance_station1_teleopGearDeliveryCount);
        teleopGearDeliveryCount.setText(formatter.format(station1.getAverageTeleopGearsDelivered()));
        TextView teleopGearDeliveryCount2 = dialogView.findViewById(R.id.info_alliance_station2_teleopGearDeliveryCount);
        teleopGearDeliveryCount2.setText(formatter.format(station2.getAverageTeleopGearsDelivered()));
        TextView teleopGearDeliveryCount3 = dialogView.findViewById(R.id.info_alliance_station3_teleopGearDeliveryCount);
        teleopGearDeliveryCount3.setText(formatter.format(station3.getAverageTeleopGearsDelivered()));

        TextView teleopGearDropCount = dialogView.findViewById(R.id.info_alliance_station1_teleopGearDropCount);
        teleopGearDropCount.setText(formatter.format(station1.getAverageTeleopGearsDropped()));
        TextView teleopGearDropCount2 = dialogView.findViewById(R.id.info_alliance_station2_teleopGearDropCount);
        teleopGearDropCount2.setText(formatter.format(station2.getAverageTeleopGearsDropped()));
        TextView teleopGearDropCount3 = dialogView.findViewById(R.id.info_alliance_station3_teleopGearDropCount);
        teleopGearDropCount3.setText(formatter.format(station3.getAverageTeleopGearsDropped()));

        TextView teleopRotorPoints = dialogView.findViewById(R.id.info_alliance_station1_teleopRotorPoints);
        teleopRotorPoints.setText(formatter.format(TeamPointEstimator.getTeleopRotorPoints(station1)) + " pts");
        TextView teleopRotorPoints2 = dialogView.findViewById(R.id.info_alliance_station2_teleopRotorPoints);
        teleopRotorPoints2.setText(formatter.format(TeamPointEstimator.getTeleopRotorPoints(station2)) + " pts");
        TextView teleopRotorPoints3 = dialogView.findViewById(R.id.info_alliance_station3_teleopRotorPoints);
        teleopRotorPoints3.setText(formatter.format(TeamPointEstimator.getTeleopRotorPoints(station3)) + " pts");

        TextView climbPercent = dialogView.findViewById(R.id.info_alliance_station1_teleopClimbPercentage);
        climbPercent.setText(formatter.format(station1.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");
        TextView climbPercent2 = dialogView.findViewById(R.id.info_alliance_station2_teleopClimbPercentage);
        climbPercent2.setText(formatter.format(station2.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");
        TextView climbPercent3 = dialogView.findViewById(R.id.info_alliance_station3_teleopClimbPercentage);
        climbPercent3.setText(formatter.format(station3.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");

        TextView climbPts = dialogView.findViewById(R.id.info_alliance_station1_teleopClimbPoints);
        climbPts.setText(formatter.format(TeamPointEstimator.getClimbingPoints(station1)) + " pts");
        TextView climbPts2 = dialogView.findViewById(R.id.info_alliance_station2_teleopClimbPoints);
        climbPts2.setText(formatter.format(TeamPointEstimator.getClimbingPoints(station2)) + " pts");
        TextView climbPts3 = dialogView.findViewById(R.id.info_alliance_station3_teleopClimbPoints);
        climbPts3.setText(formatter.format(TeamPointEstimator.getClimbingPoints(station3)) + " pts");

        // Summary
        TextView rankingPoints = dialogView.findViewById(R.id.info_alliance_station1_rankingPoints);
        rankingPoints.setText(formatter.format(TeamPointEstimator.getRankingPoints(station1)) + " pts");
        TextView rankingPoints2 = dialogView.findViewById(R.id.info_alliance_station2_rankingPoints);
        rankingPoints2.setText(formatter.format(TeamPointEstimator.getRankingPoints(station2)) + " pts");
        TextView rankingPoints3 = dialogView.findViewById(R.id.info_alliance_station3_rankingPoints);
        rankingPoints3.setText(formatter.format(TeamPointEstimator.getRankingPoints(station3)) + " pts");

        TextView total = dialogView.findViewById(R.id.info_alliance_station1_totalPoints);
        total.setText(formatter.format(TeamPointEstimator.getPointContribution(station1)) + " pts");
        TextView total2 = dialogView.findViewById(R.id.info_alliance_station2_totalPoints);
        total2.setText(formatter.format(TeamPointEstimator.getPointContribution(station2)) + " pts");
        TextView total3 = dialogView.findViewById(R.id.info_alliance_station3_totalPoints);
        total3.setText(formatter.format(TeamPointEstimator.getPointContribution(station3)) + " pts");

        RecyclerView troubleWith = dialogView.findViewById(R.id.info_alliance_troubleWith);
        TextView troubleWithPlaceholder = dialogView.findViewById(R.id.info_alliance_troubleWithPlaceholder);

        if (station1.getTroublesList() == null || station1.getTroublesList().isEmpty() || listIsEmpty(station1.getTroublesList())) {
            troubleWith.setVisibility(View.GONE);
            troubleWithPlaceholder.setVisibility(View.VISIBLE);
        } else {
            troubleWith.setVisibility(View.VISIBLE);
            troubleWithPlaceholder.setVisibility(View.GONE);

            troubleWith.setLayoutManager(new LinearLayoutManager(getContext()));
            troubleWith.setAdapter(new CommentsAdapter(station1.getTroublesList()));
        }

        RecyclerView comments = dialogView.findViewById(R.id.info_alliance_comments);
        TextView commentsPlaceholder = dialogView.findViewById(R.id.info_alliance_commentsPlaceholder);

        if (station1.getCommentsList() == null || station1.getCommentsList().isEmpty() || listIsEmpty(station1.getCommentsList())) {
            comments.setVisibility(View.GONE);
            commentsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.VISIBLE);
            commentsPlaceholder.setVisibility(View.GONE);

            comments.setLayoutManager(new LinearLayoutManager(getContext()));
            comments.setAdapter(new CommentsAdapter(station1.getCommentsList()));
        }

        return dialog;
    }

    private boolean listIsEmpty(List<String> list) {
        for (String s : list) {
            if (s != null && !s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
