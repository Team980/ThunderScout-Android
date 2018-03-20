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

package com.team980.thunderscout.analytics.rankings.breakdown;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.ScoutDataStatistics;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TeamInfoFragment extends Fragment {

    public static final String EXTRA_SCOUT_DATA_LIST = "com.team980.thunderscout.SCOUT_DATA_LIST";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        ArrayList<ScoutData> dataList = (ArrayList<ScoutData>) args.getSerializable(EXTRA_SCOUT_DATA_LIST);

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // Init
        TextView lastUpdated = view.findViewById(R.id.info_team_lastUpdated);
        lastUpdated.setText(SimpleDateFormat.getDateTimeInstance().format(ScoutDataStatistics.getLastUpdated(dataList)));

        // Auto
        TextView crossPercent = view.findViewById(R.id.info_team_autoCrossPercentage);
        crossPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getAutonomous().crossedAutoLine())) + "%");

        TextView auto_powerCubeAllianceSwitchAverage = view.findViewById(R.id.info_team_autoPowerCubeAllianceSwitchAverage);
        auto_powerCubeAllianceSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getAutonomous().getPowerCubeAllianceSwitchCount())));

        TextView auto_powerCubeScaleAverage = view.findViewById(R.id.info_team_autoPowerCubeScaleAverage);
        auto_powerCubeScaleAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getAutonomous().getPowerCubeScaleCount())));

        TextView auto_powerCubePlayerStationAverage = view.findViewById(R.id.info_team_autoPowerCubePlayerStationAverage);
        auto_powerCubePlayerStationAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getAutonomous().getPowerCubePlayerStationCount())));

        // Teleop
        TextView teleop_powerCubeAllianceSwitchAverage = view.findViewById(R.id.info_team_teleopPowerCubeAllianceSwitchAverage);
        teleop_powerCubeAllianceSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubeAllianceSwitchCount())));

        TextView teleop_powerCubeScaleAverage = view.findViewById(R.id.info_team_teleopPowerCubeScaleAverage);
        teleop_powerCubeScaleAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubeScaleCount())));

        TextView teleop_powerCubeOpposingSwitchAverage = view.findViewById(R.id.info_team_teleopPowerCubeOpposingSwitchAverage);
        teleop_powerCubeOpposingSwitchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubeOpposingSwitchCount())));

        TextView teleop_powerCubePlayerStationAverage = view.findViewById(R.id.info_team_teleopPowerCubePlayerStationAverage);
        teleop_powerCubePlayerStationAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList,
                data -> data.getTeleop().getPowerCubePlayerStationCount())));

        TextView climbPercent = view.findViewById(R.id.info_team_teleopClimbPercentage);
        climbPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getTeleop().getClimbingStats() == ClimbingStats.CLIMBED)) + "%");

        TextView supportedRobotPercent = view.findViewById(R.id.info_team_teleopSupportedRobotPercent);
        supportedRobotPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getTeleop().supportedOtherRobots())) + "%");

        // Summary
        RecyclerView strategies = view.findViewById(R.id.info_team_strategies);
        TextView strategiesPlaceholder = view.findViewById(R.id.info_team_strategiesPlaceholder);

        List<String> strategiesList = ScoutDataStatistics.getStringList(dataList,
                ScoutData::getStrategies, data -> "[" + data.getMatchNumber() + "]");
        if (strategiesList == null || strategiesList.isEmpty() || listContentsAreEmpty(strategiesList)) {
            strategies.setVisibility(View.GONE);
            strategiesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            strategies.setVisibility(View.VISIBLE);
            strategiesPlaceholder.setVisibility(View.GONE);

            strategies.setLayoutManager(new LinearLayoutManager(getContext()));
            strategies.setAdapter(new CommentsAdapter(strategiesList));
        }

        RecyclerView difficulties = view.findViewById(R.id.info_team_difficulties);
        TextView difficultiesPlaceholder = view.findViewById(R.id.info_team_difficultiesPlaceholder);

        List<String> difficultiesList = ScoutDataStatistics.getStringList(dataList,
                ScoutData::getDifficulties, data -> "[" + data.getMatchNumber() + "]");
        if (difficultiesList == null || difficultiesList.isEmpty() || listContentsAreEmpty(difficultiesList)) {
            difficulties.setVisibility(View.GONE);
            difficultiesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            difficulties.setVisibility(View.VISIBLE);
            difficultiesPlaceholder.setVisibility(View.GONE);

            difficulties.setLayoutManager(new LinearLayoutManager(getContext()));
            difficulties.setAdapter(new CommentsAdapter(difficultiesList));
        }

        RecyclerView comments = view.findViewById(R.id.info_team_comments);
        TextView commentsPlaceholder = view.findViewById(R.id.info_team_commentsPlaceholder);

        List<String> commentsList = ScoutDataStatistics.getStringList(dataList,
                ScoutData::getComments, data -> "[" + data.getMatchNumber() + "]");
        if (commentsList == null || commentsList.isEmpty() || listContentsAreEmpty(commentsList)) {
            comments.setVisibility(View.GONE);
            commentsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.VISIBLE);
            commentsPlaceholder.setVisibility(View.GONE);

            comments.setLayoutManager(new LinearLayoutManager(getContext()));
            comments.setAdapter(new CommentsAdapter(commentsList));
        }
    }

    private boolean listContentsAreEmpty(List<String> list) {
        for (String s : list) {
            if (s != null && !s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}