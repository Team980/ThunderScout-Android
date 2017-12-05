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

package com.team980.thunderscout.analytics.alliances;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.analytics.rankings.TeamPointEstimator;
import com.team980.thunderscout.analytics.rankings.breakdown.AverageScoutData;
import com.team980.thunderscout.analytics.rankings.breakdown.CommentsAdapter;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.text.NumberFormat;
import java.util.List;

public class AlliancesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TeamWrapper[] alliance = new TeamWrapper[3];

    private TeamListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alliance[0] = new TeamWrapper("");
        alliance[1] = new TeamWrapper("");
        alliance[2] = new TeamWrapper("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alliances, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Alliances");
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setHasOptionsMenu(true);

        adapter = new TeamListAdapter(getContext());

        Spinner station1Spinner = view.findViewById(R.id.alliance_station1Spinner);
        station1Spinner.setAdapter(adapter);
        station1Spinner.setOnItemSelectedListener(this);
        station1Spinner.setPrompt("Test");
        station1Spinner.setSelection(0);

        Spinner station2Spinner = view.findViewById(R.id.alliance_station2Spinner);
        station2Spinner.setAdapter(adapter);
        station2Spinner.setOnItemSelectedListener(this);
        station1Spinner.setSelection(1);

        Spinner station3Spinner = view.findViewById(R.id.alliance_station3Spinner);
        station3Spinner.setAdapter(adapter);
        station3Spinner.setOnItemSelectedListener(this);
        station1Spinner.setSelection(2);

        AccountScope.getStorageWrapper(AccountScope.LOCAL, getContext()).queryData(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_alliance_tools, menu);
    }

    //Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.alliance_station1Spinner) {
            alliance[0] = adapter.getItem(position);
        } else if (parent.getId() == R.id.alliance_station2Spinner) {
            alliance[1] = adapter.getItem(position);
        } else if (parent.getId() == R.id.alliance_station3Spinner) {
            alliance[2] = adapter.getItem(position);
        }

        generateSummary();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Don't change anything
    }

    public void generateSummary() {
        AverageScoutData station1 = new AverageScoutData(alliance[0].getDataList());
        AverageScoutData station2 = new AverageScoutData(alliance[1].getDataList());
        AverageScoutData station3 = new AverageScoutData(alliance[2].getDataList());

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // Auto
        TextView crossPercent = getView().findViewById(R.id.info_alliance_station1_autoCrossPercentage);
        crossPercent.setText(formatter.format(station1.getCrossedBaselinePercentage()) + "%");
        TextView crossPercent2 = getView().findViewById(R.id.info_alliance_station2_autoCrossPercentage);
        crossPercent2.setText(formatter.format(station2.getCrossedBaselinePercentage()) + "%");
        TextView crossPercent3 = getView().findViewById(R.id.info_alliance_station3_autoCrossPercentage);
        crossPercent3.setText(formatter.format(station3.getCrossedBaselinePercentage()) + "%");

        TextView mobility = getView().findViewById(R.id.info_alliance_station1_autoMobilityPoints);
        mobility.setText(formatter.format(TeamPointEstimator.getBaselinePoints(station1)) + " pts");
        TextView mobility2 = getView().findViewById(R.id.info_alliance_station2_autoMobilityPoints);
        mobility2.setText(formatter.format(TeamPointEstimator.getBaselinePoints(station2)) + " pts");
        TextView mobility3 = getView().findViewById(R.id.info_alliance_station3_autoMobilityPoints);
        mobility3.setText(formatter.format(TeamPointEstimator.getBaselinePoints(station3)) + " pts");

        TextView autoLowGoalCount = getView().findViewById(R.id.info_alliance_station1_autoLowGoalCount);
        autoLowGoalCount.setText(formatter.format((station1.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageAutoLowGoalDumpAmount().getMaximumAmount()) / 2));
        TextView autoLowGoalCount2 = getView().findViewById(R.id.info_alliance_station2_autoLowGoalCount);
        autoLowGoalCount2.setText(formatter.format((station2.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + station2.getAverageAutoLowGoalDumpAmount().getMaximumAmount()) / 2));
        TextView autoLowGoalCount3 = getView().findViewById(R.id.info_alliance_station3_autoLowGoalCount);
        autoLowGoalCount3.setText(formatter.format((station3.getAverageAutoLowGoalDumpAmount().getMinimumAmount()
                + station3.getAverageAutoLowGoalDumpAmount().getMaximumAmount()) / 2));

        TextView autoHighGoalCount = getView().findViewById(R.id.info_alliance_station1_autoHighGoalCount);
        autoHighGoalCount.setText(formatter.format(station1.getAverageAutoHighGoals()));
        TextView autoHighGoalCount2 = getView().findViewById(R.id.info_alliance_station2_autoHighGoalCount);
        autoHighGoalCount2.setText(formatter.format(station2.getAverageAutoHighGoals()));
        TextView autoHighGoalCount3 = getView().findViewById(R.id.info_alliance_station3_autoHighGoalCount);
        autoHighGoalCount3.setText(formatter.format(station3.getAverageAutoHighGoals()));

        TextView autoMissedGoalCount = getView().findViewById(R.id.info_alliance_station1_autoMissedGoalCount);
        autoMissedGoalCount.setText(formatter.format(station1.getAverageAutoMissedHighGoals()));
        TextView autoMissedGoalCount2 = getView().findViewById(R.id.info_alliance_station2_autoMissedGoalCount);
        autoMissedGoalCount2.setText(formatter.format(station2.getAverageAutoMissedHighGoals()));
        TextView autoMissedGoalCount3 = getView().findViewById(R.id.info_alliance_station3_autoMissedGoalCount);
        autoMissedGoalCount3.setText(formatter.format(station3.getAverageAutoMissedHighGoals()));

        TextView autoFuelPoints = getView().findViewById(R.id.info_alliance_station1_autoFuelPoints);
        autoFuelPoints.setText(formatter.format(TeamPointEstimator.getAutoFuelPoints(station1)) + " pts");
        TextView autoFuelPoints2 = getView().findViewById(R.id.info_alliance_station2_autoFuelPoints);
        autoFuelPoints2.setText(formatter.format(TeamPointEstimator.getAutoFuelPoints(station2)) + " pts");
        TextView autoFuelPoints3 = getView().findViewById(R.id.info_alliance_station3_autoFuelPoints);
        autoFuelPoints3.setText(formatter.format(TeamPointEstimator.getAutoFuelPoints(station3)) + " pts");

        TextView autoGearDeliveryCount = getView().findViewById(R.id.info_alliance_station1_autoGearDeliveryCount);
        autoGearDeliveryCount.setText(formatter.format(station1.getAverageAutoGearsDelivered()));
        TextView autoGearDeliveryCount2 = getView().findViewById(R.id.info_alliance_station2_autoGearDeliveryCount);
        autoGearDeliveryCount2.setText(formatter.format(station2.getAverageAutoGearsDelivered()));
        TextView autoGearDeliveryCount3 = getView().findViewById(R.id.info_alliance_station3_autoGearDeliveryCount);
        autoGearDeliveryCount3.setText(formatter.format(station3.getAverageAutoGearsDelivered()));

        TextView autoGearDropCount = getView().findViewById(R.id.info_alliance_station1_autoGearDropCount);
        autoGearDropCount.setText(formatter.format(station1.getAverageAutoGearsDropped()));
        TextView autoGearDropCount2 = getView().findViewById(R.id.info_alliance_station2_autoGearDropCount);
        autoGearDropCount2.setText(formatter.format(station2.getAverageAutoGearsDropped()));
        TextView autoGearDropCount3 = getView().findViewById(R.id.info_alliance_station3_autoGearDropCount);
        autoGearDropCount3.setText(formatter.format(station3.getAverageAutoGearsDropped()));

        TextView autoRotorPoints = getView().findViewById(R.id.info_alliance_station1_autoRotorPoints);
        autoRotorPoints.setText(formatter.format(TeamPointEstimator.getAutoRotorPoints(station1)) + " pts");
        TextView autoRotorPoints2 = getView().findViewById(R.id.info_alliance_station2_autoRotorPoints);
        autoRotorPoints2.setText(formatter.format(TeamPointEstimator.getAutoRotorPoints(station2)) + " pts");
        TextView autoRotorPoints3 = getView().findViewById(R.id.info_alliance_station3_autoRotorPoints);
        autoRotorPoints3.setText(formatter.format(TeamPointEstimator.getAutoRotorPoints(station3)) + " pts");

        // Teleop
        TextView teleopLowGoalCount = getView().findViewById(R.id.info_alliance_station1_teleopLowGoalCount);
        // ugh I hate FuelDumpAmount soooo much
        float low = (station1.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station1.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low *= station1.getAverageTeleopDumpFrequency(); /// times average dump amount
        teleopLowGoalCount.setText(formatter.format(low));
        TextView teleopLowGoalCount2 = getView().findViewById(R.id.info_alliance_station2_teleopLowGoalCount);
        // ugh I hate FuelDumpAmount soooo much
        float low2 = (station2.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station2.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low2 *= station2.getAverageTeleopDumpFrequency(); /// times average dump amount
        teleopLowGoalCount2.setText(formatter.format(low2));
        TextView teleopLowGoalCount3 = getView().findViewById(R.id.info_alliance_station3_teleopLowGoalCount);
        // ugh I hate FuelDumpAmount soooo much
        float low3 = (station3.getAverageTeleopLowGoalDumpAmount().getMinimumAmount()
                + station3.getAverageTeleopLowGoalDumpAmount().getMaximumAmount()) / 2; //average dump size
        low3 *= station3.getAverageTeleopDumpFrequency(); /// times average dump amount
        teleopLowGoalCount3.setText(formatter.format(low3));

        TextView teleopHighGoalCount = getView().findViewById(R.id.info_alliance_station1_teleopHighGoalCount);
        teleopHighGoalCount.setText(formatter.format(station1.getAverageTeleopHighGoals()));
        TextView teleopHighGoalCount2 = getView().findViewById(R.id.info_alliance_station2_teleopHighGoalCount);
        teleopHighGoalCount2.setText(formatter.format(station2.getAverageTeleopHighGoals()));
        TextView teleopHighGoalCount3 = getView().findViewById(R.id.info_alliance_station3_teleopHighGoalCount);
        teleopHighGoalCount3.setText(formatter.format(station3.getAverageTeleopHighGoals()));

        TextView teleopMissedGoalCount = getView().findViewById(R.id.info_alliance_station1_teleopMissedGoalCount);
        teleopMissedGoalCount.setText(formatter.format(station1.getAverageTeleopMissedHighGoals()));
        TextView teleopMissedGoalCount2 = getView().findViewById(R.id.info_alliance_station2_teleopMissedGoalCount);
        teleopMissedGoalCount2.setText(formatter.format(station2.getAverageTeleopMissedHighGoals()));
        TextView teleopMissedGoalCount3 = getView().findViewById(R.id.info_alliance_station3_teleopMissedGoalCount);
        teleopMissedGoalCount3.setText(formatter.format(station3.getAverageTeleopMissedHighGoals()));

        TextView teleopFuelPoints = getView().findViewById(R.id.info_alliance_station1_teleopFuelPoints);
        teleopFuelPoints.setText(formatter.format(TeamPointEstimator.getTeleopFuelPoints(station1)) + " pts");
        TextView teleopFuelPoints2 = getView().findViewById(R.id.info_alliance_station2_teleopFuelPoints);
        teleopFuelPoints2.setText(formatter.format(TeamPointEstimator.getTeleopFuelPoints(station2)) + " pts");
        TextView teleopFuelPoints3 = getView().findViewById(R.id.info_alliance_station3_teleopFuelPoints);
        teleopFuelPoints3.setText(formatter.format(TeamPointEstimator.getTeleopFuelPoints(station3)) + " pts");

        TextView teleopGearDeliveryCount = getView().findViewById(R.id.info_alliance_station1_teleopGearDeliveryCount);
        teleopGearDeliveryCount.setText(formatter.format(station1.getAverageTeleopGearsDelivered()));
        TextView teleopGearDeliveryCount2 = getView().findViewById(R.id.info_alliance_station2_teleopGearDeliveryCount);
        teleopGearDeliveryCount2.setText(formatter.format(station2.getAverageTeleopGearsDelivered()));
        TextView teleopGearDeliveryCount3 = getView().findViewById(R.id.info_alliance_station3_teleopGearDeliveryCount);
        teleopGearDeliveryCount3.setText(formatter.format(station3.getAverageTeleopGearsDelivered()));

        TextView teleopGearDropCount = getView().findViewById(R.id.info_alliance_station1_teleopGearDropCount);
        teleopGearDropCount.setText(formatter.format(station1.getAverageTeleopGearsDropped()));
        TextView teleopGearDropCount2 = getView().findViewById(R.id.info_alliance_station2_teleopGearDropCount);
        teleopGearDropCount2.setText(formatter.format(station2.getAverageTeleopGearsDropped()));
        TextView teleopGearDropCount3 = getView().findViewById(R.id.info_alliance_station3_teleopGearDropCount);
        teleopGearDropCount3.setText(formatter.format(station3.getAverageTeleopGearsDropped()));

        TextView teleopRotorPoints = getView().findViewById(R.id.info_alliance_station1_teleopRotorPoints);
        teleopRotorPoints.setText(formatter.format(TeamPointEstimator.getTeleopRotorPoints(station1)) + " pts");
        TextView teleopRotorPoints2 = getView().findViewById(R.id.info_alliance_station2_teleopRotorPoints);
        teleopRotorPoints2.setText(formatter.format(TeamPointEstimator.getTeleopRotorPoints(station2)) + " pts");
        TextView teleopRotorPoints3 = getView().findViewById(R.id.info_alliance_station3_teleopRotorPoints);
        teleopRotorPoints3.setText(formatter.format(TeamPointEstimator.getTeleopRotorPoints(station3)) + " pts");

        TextView climbPercent = getView().findViewById(R.id.info_alliance_station1_teleopClimbPercentage);
        climbPercent.setText(formatter.format(station1.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");
        TextView climbPercent2 = getView().findViewById(R.id.info_alliance_station2_teleopClimbPercentage);
        climbPercent2.setText(formatter.format(station2.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");
        TextView climbPercent3 = getView().findViewById(R.id.info_alliance_station3_teleopClimbPercentage);
        climbPercent3.setText(formatter.format(station3.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD)) + "%");

        TextView climbPts = getView().findViewById(R.id.info_alliance_station1_teleopClimbPoints);
        climbPts.setText(formatter.format(TeamPointEstimator.getClimbingPoints(station1)) + " pts");
        TextView climbPts2 = getView().findViewById(R.id.info_alliance_station2_teleopClimbPoints);
        climbPts2.setText(formatter.format(TeamPointEstimator.getClimbingPoints(station2)) + " pts");
        TextView climbPts3 = getView().findViewById(R.id.info_alliance_station3_teleopClimbPoints);
        climbPts3.setText(formatter.format(TeamPointEstimator.getClimbingPoints(station3)) + " pts");

        // Summary
        TextView rankingPoints = getView().findViewById(R.id.info_alliance_station1_rankingPoints);
        rankingPoints.setText(formatter.format(TeamPointEstimator.getRankingPoints(station1)) + " pts");
        TextView rankingPoints2 = getView().findViewById(R.id.info_alliance_station2_rankingPoints);
        rankingPoints2.setText(formatter.format(TeamPointEstimator.getRankingPoints(station2)) + " pts");
        TextView rankingPoints3 = getView().findViewById(R.id.info_alliance_station3_rankingPoints);
        rankingPoints3.setText(formatter.format(TeamPointEstimator.getRankingPoints(station3)) + " pts");

        TextView total = getView().findViewById(R.id.info_alliance_station1_totalPoints);
        total.setText(formatter.format(TeamPointEstimator.getPointContribution(station1)) + " pts");
        TextView total2 = getView().findViewById(R.id.info_alliance_station2_totalPoints);
        total2.setText(formatter.format(TeamPointEstimator.getPointContribution(station2)) + " pts");
        TextView total3 = getView().findViewById(R.id.info_alliance_station3_totalPoints);
        total3.setText(formatter.format(TeamPointEstimator.getPointContribution(station3)) + " pts");

        RecyclerView troubleWith = getView().findViewById(R.id.info_alliance_troubleWith);
        TextView troubleWithPlaceholder = getView().findViewById(R.id.info_alliance_troubleWithPlaceholder);

        if (station1.getTroublesList() == null || station1.getTroublesList().isEmpty() || listIsEmpty(station1.getTroublesList())) {
            troubleWith.setVisibility(View.GONE);
            troubleWithPlaceholder.setVisibility(View.VISIBLE);
        } else {
            troubleWith.setVisibility(View.VISIBLE);
            troubleWithPlaceholder.setVisibility(View.GONE);

            troubleWith.setLayoutManager(new LinearLayoutManager(getContext()));
            troubleWith.setAdapter(new CommentsAdapter(station1.getTroublesList()));
        }

        RecyclerView comments = getView().findViewById(R.id.info_alliance_comments);
        TextView commentsPlaceholder = getView().findViewById(R.id.info_alliance_commentsPlaceholder);

        if (station1.getCommentsList() == null || station1.getCommentsList().isEmpty() || listIsEmpty(station1.getCommentsList())) {
            comments.setVisibility(View.GONE);
            commentsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.VISIBLE);
            commentsPlaceholder.setVisibility(View.GONE);

            comments.setLayoutManager(new LinearLayoutManager(getContext()));
            comments.setAdapter(new CommentsAdapter(station1.getCommentsList()));
        }
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
