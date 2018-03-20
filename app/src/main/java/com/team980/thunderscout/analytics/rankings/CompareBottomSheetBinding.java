/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
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

import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.ScoutDataStatistics;
import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.analytics.rankings.breakdown.CommentsAdapter;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Deprecated
//TODO replace with Data Binding - I really didn't want to shove this into RankingsFragment.java
//TODO ...except there's actually more going on in here than I CAN replace with Data Binding...
public class CompareBottomSheetBinding {

    public static void bindBottomSheet(View dialogView, BottomSheetBehavior behavior, List<TeamWrapper> teams) {

        TeamWrapper station1 = teams.get(0);
        TeamWrapper station2 = teams.get(1);
        TeamWrapper station3 = teams.get(2);

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        Toolbar toolbar = dialogView.findViewById(R.id.toolbar);
        toolbar.setTitle("Comparison: " + station1.getTeam() + " / " + station2.getTeam() + " / " + station3.getTeam());

        toolbar.setOnClickListener(v -> {
            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_expand_less_white_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        //Init
        TextView team = dialogView.findViewById(R.id.info_alliance_station1_team);
        team.setText(station1.getTeam());
        TextView team2 = dialogView.findViewById(R.id.info_alliance_station2_team);
        team2.setText(station2.getTeam());
        TextView team3 = dialogView.findViewById(R.id.info_alliance_station3_team);
        team3.setText(station3.getTeam());

        // Auto
        TextView crossPercent = dialogView.findViewById(R.id.info_alliance_station1_autoCrossPercentage);
        crossPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(station1.getDataList(),
                data -> data.crossedAutoLine())) + "%");
        TextView crossPercent2 = dialogView.findViewById(R.id.info_alliance_station2_autoCrossPercentage);
        crossPercent2.setText(formatter.format(ScoutDataStatistics.getPercentage(station2.getDataList(),
                data -> data.crossedAutoLine())) + "%");
        TextView crossPercent3 = dialogView.findViewById(R.id.info_alliance_station3_autoCrossPercentage);
        crossPercent3.setText(formatter.format(ScoutDataStatistics.getPercentage(station3.getDataList(),
                data -> data.crossedAutoLine())) + "%");

        TextView auto_powerCubeAllianceSwitchAverage = dialogView.findViewById(R.id.info_alliance_station1_autoPowerCubeAllianceSwitchAverage);
        auto_powerCubeAllianceSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(station1.getDataList(),
                data -> data.getAutoPowerCubeAllianceSwitchCount())));
        TextView auto_powerCubeAllianceSwitchAverage2 = dialogView.findViewById(R.id.info_alliance_station2_autoPowerCubeAllianceSwitchAverage);
        auto_powerCubeAllianceSwitchAverage2.setText(formatter.format(ScoutDataStatistics.getAverage(station2.getDataList(),
                data -> data.getAutoPowerCubeAllianceSwitchCount())));
        TextView auto_powerCubeAllianceSwitchAverage3 = dialogView.findViewById(R.id.info_alliance_station3_autoPowerCubeAllianceSwitchAverage);
        auto_powerCubeAllianceSwitchAverage3.setText(formatter.format(ScoutDataStatistics.getAverage(station3.getDataList(),
                data -> data.getAutoPowerCubeAllianceSwitchCount())));

        TextView auto_powerCubeScaleAverage = dialogView.findViewById(R.id.info_alliance_station1_autoPowerCubeScaleAverage);
        auto_powerCubeScaleAverage.setText(formatter.format(ScoutDataStatistics.getAverage(station1.getDataList(),
                data -> data.getAutoPowerCubeScaleCount())));
        TextView auto_powerCubeScaleAverage2 = dialogView.findViewById(R.id.info_alliance_station2_autoPowerCubeScaleAverage);
        auto_powerCubeScaleAverage2.setText(formatter.format(ScoutDataStatistics.getAverage(station2.getDataList(),
                data -> data.getAutoPowerCubeScaleCount())));
        TextView auto_powerCubeScaleAverage3 = dialogView.findViewById(R.id.info_alliance_station3_autoPowerCubeScaleAverage);
        auto_powerCubeScaleAverage3.setText(formatter.format(ScoutDataStatistics.getAverage(station3.getDataList(),
                data -> data.getAutoPowerCubeScaleCount())));

        TextView auto_powerCubePlayerStationAverage = dialogView.findViewById(R.id.info_alliance_station1_autoPowerCubePlayerStationAverage);
        auto_powerCubePlayerStationAverage.setText(formatter.format(ScoutDataStatistics.getAverage(station1.getDataList(),
                data -> data.getAutoPowerCubePlayerStationCount())));
        TextView auto_powerCubePlayerStationAverage2 = dialogView.findViewById(R.id.info_alliance_station2_autoPowerCubePlayerStationAverage);
        auto_powerCubePlayerStationAverage2.setText(formatter.format(ScoutDataStatistics.getAverage(station2.getDataList(),
                data -> data.getAutoPowerCubePlayerStationCount())));
        TextView auto_powerCubePlayerStationAverage3 = dialogView.findViewById(R.id.info_alliance_station3_autoPowerCubePlayerStationAverage);
        auto_powerCubePlayerStationAverage3.setText(formatter.format(ScoutDataStatistics.getAverage(station3.getDataList(),
                data -> data.getAutoPowerCubePlayerStationCount())));

        // Teleop
        TextView teleop_powerCubeAllianceSwitchAverage = dialogView.findViewById(R.id.info_alliance_station1_teleopPowerCubeAllianceSwitchAverage);
        teleop_powerCubeAllianceSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(station1.getDataList(),
                data -> data.getTeleopPowerCubeAllianceSwitchCount())));
        TextView teleop_powerCubeAllianceSwitchAverage2 = dialogView.findViewById(R.id.info_alliance_station2_teleopPowerCubeAllianceSwitchAverage);
        teleop_powerCubeAllianceSwitchAverage2.setText(formatter.format(ScoutDataStatistics.getAverage(station2.getDataList(),
                data -> data.getTeleopPowerCubeAllianceSwitchCount())));
        TextView teleop_powerCubeAllianceSwitchAverage3 = dialogView.findViewById(R.id.info_alliance_station3_teleopPowerCubeAllianceSwitchAverage);
        teleop_powerCubeAllianceSwitchAverage3.setText(formatter.format(ScoutDataStatistics.getAverage(station3.getDataList(),
                data -> data.getTeleopPowerCubeAllianceSwitchCount())));

        TextView teleop_powerCubeScaleAverage = dialogView.findViewById(R.id.info_alliance_station1_teleopPowerCubeScaleAverage);
        teleop_powerCubeScaleAverage.setText(formatter.format(ScoutDataStatistics.getAverage(station1.getDataList(),
                data -> data.getTeleopPowerCubeScaleCount())));
        TextView teleop_powerCubeScaleAverage2 = dialogView.findViewById(R.id.info_alliance_station2_teleopPowerCubeScaleAverage);
        teleop_powerCubeScaleAverage2.setText(formatter.format(ScoutDataStatistics.getAverage(station2.getDataList(),
                data -> data.getTeleopPowerCubeScaleCount())));
        TextView teleop_powerCubeScaleAverage3 = dialogView.findViewById(R.id.info_alliance_station3_teleopPowerCubeScaleAverage);
        teleop_powerCubeScaleAverage3.setText(formatter.format(ScoutDataStatistics.getAverage(station3.getDataList(),
                data -> data.getTeleopPowerCubeScaleCount())));

        TextView teleop_powerCubeOpposingSwitchAverage = dialogView.findViewById(R.id.info_alliance_station1_teleopPowerCubeOpposingSwitchAverage);
        teleop_powerCubeOpposingSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(station1.getDataList(),
                data -> data.getTeleopPowerCubeOpposingSwitchCount())));
        TextView teleop_powerCubeOpposingSwitchAverage2 = dialogView.findViewById(R.id.info_alliance_station2_teleopPowerCubeOpposingSwitchAverage);
        teleop_powerCubeOpposingSwitchAverage2.setText(formatter.format(ScoutDataStatistics.getAverage(station2.getDataList(),
                data -> data.getTeleopPowerCubeOpposingSwitchCount())));
        TextView teleop_powerCubeOpposingSwitchAverage3 = dialogView.findViewById(R.id.info_alliance_station3_teleopPowerCubeOpposingSwitchAverage);
        teleop_powerCubeOpposingSwitchAverage3.setText(formatter.format(ScoutDataStatistics.getAverage(station3.getDataList(),
                data -> data.getTeleopPowerCubeOpposingSwitchCount())));

        TextView teleop_powerCubePlayerStationAverage = dialogView.findViewById(R.id.info_alliance_station1_teleopPowerCubePlayerStationAverage);
        teleop_powerCubePlayerStationAverage.setText(formatter.format(ScoutDataStatistics.getAverage(station1.getDataList(),
                data -> data.getTeleopPowerCubePlayerStationCount())));
        TextView teleop_powerCubePlayerStationAverage2 = dialogView.findViewById(R.id.info_alliance_station2_teleopPowerCubePlayerStationAverage);
        teleop_powerCubePlayerStationAverage2.setText(formatter.format(ScoutDataStatistics.getAverage(station2.getDataList(),
                data -> data.getTeleopPowerCubePlayerStationCount())));
        TextView teleop_powerCubePlayerStationAverage3 = dialogView.findViewById(R.id.info_alliance_station3_teleopPowerCubePlayerStationAverage);
        teleop_powerCubePlayerStationAverage3.setText(formatter.format(ScoutDataStatistics.getAverage(station3.getDataList(),
                data -> data.getTeleopPowerCubePlayerStationCount())));

        TextView climbPercent = dialogView.findViewById(R.id.info_alliance_station1_teleopClimbPercentage);
        climbPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(station1.getDataList(),
                data -> data.getClimbingStats() == ClimbingStats.CLIMBED)) + "%");
        TextView climbPercent2 = dialogView.findViewById(R.id.info_alliance_station2_teleopClimbPercentage);
        climbPercent2.setText(formatter.format(ScoutDataStatistics.getPercentage(station2.getDataList(),
                data -> data.getClimbingStats() == ClimbingStats.CLIMBED)) + "%");
        TextView climbPercent3 = dialogView.findViewById(R.id.info_alliance_station3_teleopClimbPercentage);
        climbPercent3.setText(formatter.format(ScoutDataStatistics.getPercentage(station3.getDataList(),
                data -> data.getClimbingStats() == ClimbingStats.CLIMBED)) + "%");

        TextView supportedRobotPercent = dialogView.findViewById(R.id.info_alliance_station1_teleopSupportedRobotPercent);
        supportedRobotPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(station1.getDataList(),
                data -> data.supportedOtherRobots())) + "%");
        TextView supportedRobotPercent2 = dialogView.findViewById(R.id.info_alliance_station2_teleopSupportedRobotPercent);
        supportedRobotPercent2.setText(formatter.format(ScoutDataStatistics.getPercentage(station2.getDataList(),
                data -> data.supportedOtherRobots())) + "%");
        TextView supportedRobotPercent3 = dialogView.findViewById(R.id.info_alliance_station3_teleopSupportedRobotPercent);
        supportedRobotPercent3.setText(formatter.format(ScoutDataStatistics.getPercentage(station3.getDataList(),
                data -> data.supportedOtherRobots())) + "%");

        // Summary
        RecyclerView strategies = dialogView.findViewById(R.id.info_alliance_strategies);
        TextView strategiesPlaceholder = dialogView.findViewById(R.id.info_alliance_strategiesPlaceholder);

        List<String> strategiesList = new ArrayList<>();
        strategiesList.addAll(ScoutDataStatistics.getStringList(station1.getDataList(),
                ScoutData::getStrategies, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        strategiesList.addAll(ScoutDataStatistics.getStringList(station2.getDataList(),
                ScoutData::getStrategies, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        strategiesList.addAll(ScoutDataStatistics.getStringList(station3.getDataList(),
                ScoutData::getStrategies, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        if (strategiesList == null || strategiesList.isEmpty() || listContentsAreEmpty(strategiesList)) {
            strategies.setVisibility(View.GONE);
            strategiesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            strategies.setVisibility(View.VISIBLE);
            strategiesPlaceholder.setVisibility(View.GONE);

            strategies.setLayoutManager(new LinearLayoutManager(dialogView.getContext()));
            strategies.setAdapter(new CommentsAdapter(strategiesList));
        }

        RecyclerView difficulties = dialogView.findViewById(R.id.info_alliance_difficulties);
        TextView difficultiesPlaceholder = dialogView.findViewById(R.id.info_alliance_difficultiesPlaceholder);

        List<String> difficultiesList = new ArrayList<>();
        difficultiesList.addAll(ScoutDataStatistics.getStringList(station1.getDataList(),
                ScoutData::getDifficulties, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        difficultiesList.addAll(ScoutDataStatistics.getStringList(station2.getDataList(),
                ScoutData::getDifficulties, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        difficultiesList.addAll(ScoutDataStatistics.getStringList(station3.getDataList(),
                ScoutData::getDifficulties, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        if (difficultiesList == null || difficultiesList.isEmpty() || listContentsAreEmpty(difficultiesList)) {
            difficulties.setVisibility(View.GONE);
            difficultiesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            difficulties.setVisibility(View.VISIBLE);
            difficultiesPlaceholder.setVisibility(View.GONE);

            difficulties.setLayoutManager(new LinearLayoutManager(dialogView.getContext()));
            difficulties.setAdapter(new CommentsAdapter(difficultiesList));
        }

        RecyclerView comments = dialogView.findViewById(R.id.info_alliance_comments);
        TextView commentsPlaceholder = dialogView.findViewById(R.id.info_alliance_commentsPlaceholder);

        List<String> commentsList = new ArrayList<>();
        commentsList.addAll(ScoutDataStatistics.getStringList(station1.getDataList(),
                ScoutData::getComments, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        commentsList.addAll(ScoutDataStatistics.getStringList(station2.getDataList(),
                ScoutData::getComments, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        commentsList.addAll(ScoutDataStatistics.getStringList(station3.getDataList(),
                ScoutData::getComments, data -> "[" + data.getTeam() + " - " + data.getMatchNumber() + "]"));
        if (commentsList == null || commentsList.isEmpty() || listContentsAreEmpty(commentsList)) {
            comments.setVisibility(View.GONE);
            commentsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.VISIBLE);
            commentsPlaceholder.setVisibility(View.GONE);

            comments.setLayoutManager(new LinearLayoutManager(dialogView.getContext()));
            comments.setAdapter(new CommentsAdapter(commentsList));
        }
    }

    private static boolean listContentsAreEmpty(List<String> list) {
        for (String s : list) {
            if (s != null && !s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
