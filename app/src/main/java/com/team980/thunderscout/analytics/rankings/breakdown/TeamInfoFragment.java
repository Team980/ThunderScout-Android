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
import com.team980.thunderscout.schema.enumeration.ClimbTime;
import com.team980.thunderscout.schema.enumeration.HabLevel;

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

        // Sandstorm
        TextView startPercent = view.findViewById(R.id.info_team_stormLevel2StartFrequency);
        startPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getStartingLevel().equals(HabLevel.LEVEL_2))) + "%");

        TextView crossPercent = view.findViewById(R.id.info_team_stormCrossPercentage);
        crossPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList, ScoutData::crossedHabLine)) + "%");

        TextView storm_highRocketHatchAverage = view.findViewById(R.id.info_team_stormHighRocketHatchAverage);
        storm_highRocketHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormHighRocketHatchCount)));

        TextView storm_midRocketHatchAverage = view.findViewById(R.id.info_team_stormMidRocketHatchAverage);
        storm_midRocketHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormMidRocketHatchCount)));

        TextView storm_lowRocketHatchAverage = view.findViewById(R.id.info_team_stormLowRocketHatchAverage);
        storm_lowRocketHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormLowRocketHatchCount)));

        TextView storm_cargoShipHatchAverage = view.findViewById(R.id.info_team_stormCargoShipHatchAverage);
        storm_cargoShipHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormCargoShipHatchCount)));

        TextView storm_highRocketCargoAverage = view.findViewById(R.id.info_team_stormHighRocketCargoAverage);
        storm_highRocketCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormHighRocketCargoCount)));

        TextView storm_midRocketCargoAverage = view.findViewById(R.id.info_team_stormMidRocketCargoAverage);
        storm_midRocketCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormMidRocketCargoCount)));

        TextView storm_lowRocketCargoAverage = view.findViewById(R.id.info_team_stormLowRocketCargoAverage);
        storm_lowRocketCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormLowRocketCargoCount)));

        TextView storm_cargoShipCargoAverage = view.findViewById(R.id.info_team_stormCargoShipCargoAverage);
        storm_cargoShipCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getStormCargoShipCargoCount)));

        // Teleoperated
        TextView teleop_highRocketHatchAverage = view.findViewById(R.id.info_team_teleopHighRocketHatchAverage);
        teleop_highRocketHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopHighRocketHatchCount)));

        TextView teleop_midRocketHatchAverage = view.findViewById(R.id.info_team_teleopMidRocketHatchAverage);
        teleop_midRocketHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopMidRocketHatchCount)));

        TextView teleop_lowRocketHatchAverage = view.findViewById(R.id.info_team_teleopLowRocketHatchAverage);
        teleop_lowRocketHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopLowRocketHatchCount)));

        TextView teleop_cargoShipHatchAverage = view.findViewById(R.id.info_team_teleopCargoShipHatchAverage);
        teleop_cargoShipHatchAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopCargoShipHatchCount)));

        TextView teleop_highRocketCargoAverage = view.findViewById(R.id.info_team_teleopHighRocketCargoAverage);
        teleop_highRocketCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopHighRocketCargoCount)));

        TextView teleop_midRocketCargoAverage = view.findViewById(R.id.info_team_teleopMidRocketCargoAverage);
        teleop_midRocketCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopMidRocketCargoCount)));

        TextView teleop_lowRocketCargoAverage = view.findViewById(R.id.info_team_teleopLowRocketCargoAverage);
        teleop_lowRocketCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopLowRocketCargoCount)));

        TextView teleop_cargoShipCargoAverage = view.findViewById(R.id.info_team_teleopCargoShipCargoAverage);
        teleop_cargoShipCargoAverage.setText(formatter.format(ScoutDataStatistics.getAverage(dataList, ScoutData::getTeleopCargoShipCargoCount)));

        // Endgame
        TextView level3ClimbPercent = view.findViewById(R.id.info_team_endgameLevel3ClimbFrequency);
        level3ClimbPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getEndgameClimbLevel().equals(HabLevel.LEVEL_3))) + "%");

        TextView level2ClimbPercent = view.findViewById(R.id.info_team_endgameLevel2ClimbFrequency);
        level2ClimbPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList,
                data -> data.getEndgameClimbLevel().equals(HabLevel.LEVEL_2))) + "%");

        TextView climbTimeAverage = view.findViewById(R.id.info_team_endgameClimbTimeAverage);
        climbTimeAverage.setText(ClimbTime.values()[(int) ScoutDataStatistics.getAverage(dataList,
                data -> data.getEndgameClimbTime().ordinal())].toString());

        TextView supportedRobotPercent = view.findViewById(R.id.info_team_endgameSupportedRobotPercent);
        supportedRobotPercent.setText(formatter.format(ScoutDataStatistics.getPercentage(dataList, ScoutData::supportedOtherRobots)) + "%");

        RecyclerView climbDescriptions = view.findViewById(R.id.info_team_endgameClimbDescriptions);
        TextView climbDescriptionsPlaceholder = view.findViewById(R.id.info_team_endgameClimbDescriptionsPlaceholder);

        List<String> climbDescriptionsList = ScoutDataStatistics.getStringList(dataList,
                ScoutData::getClimbDescription, data -> "[" + data.getMatchNumber() + "]");
        if (climbDescriptionsList == null || climbDescriptionsList.isEmpty() || listContentsAreEmpty(climbDescriptionsList)) {
            climbDescriptions.setVisibility(View.GONE);
            climbDescriptionsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            climbDescriptions.setVisibility(View.VISIBLE);
            climbDescriptionsPlaceholder.setVisibility(View.GONE);

            climbDescriptions.setLayoutManager(new LinearLayoutManager(getContext()));
            climbDescriptions.setAdapter(new CommentsAdapter(climbDescriptionsList));
        }

        // Notes
        RecyclerView notes = view.findViewById(R.id.info_team_notes);
        TextView notesPlaceholder = view.findViewById(R.id.info_team_notesPlaceholder);

        List<String> notesList = ScoutDataStatistics.getStringList(dataList,
                ScoutData::getNotes, data -> "[" + data.getMatchNumber() + "]");
        if (notesList == null || notesList.isEmpty() || listContentsAreEmpty(notesList)) {
            notes.setVisibility(View.GONE);
            notesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            notes.setVisibility(View.VISIBLE);
            notesPlaceholder.setVisibility(View.GONE);

            notes.setLayoutManager(new LinearLayoutManager(getContext()));
            notes.setAdapter(new CommentsAdapter(notesList));
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