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
import com.team980.thunderscout.analytics.rankings.breakdown.CommentsAdapter;
import com.team980.thunderscout.backend.AccountScope;

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
        AverageAllianceData data = new AverageAllianceData(alliance[0], alliance[1], alliance[2]);

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);

        // Auto
        TextView mobility = getView().findViewById(R.id.info_alliance_autoMobilityPoints);
        mobility.setText(formatter.format(AlliancePointEstimator.getBaselinePoints(data)) + " pts");

        TextView autoLowGoalCount = getView().findViewById(R.id.info_alliance_autoLowGoalCount);
        autoLowGoalCount.setText(formatter.format((data.getAverageAutoLowGoals())));

        TextView autoHighGoalCount = getView().findViewById(R.id.info_alliance_autoHighGoalCount);
        autoHighGoalCount.setText(formatter.format(data.getAverageAutoHighGoals()));

        TextView autoFuelPoints = getView().findViewById(R.id.info_alliance_autoFuelPoints);
        autoFuelPoints.setText(formatter.format(AlliancePointEstimator.getAutoFuelPoints(data)) + " pts");

        TextView autoGearDeliveryCount = getView().findViewById(R.id.info_alliance_autoGearDeliveryCount);
        autoGearDeliveryCount.setText(formatter.format(data.getAverageAutoGearsDelivered()));

        TextView autoRotorPoints = getView().findViewById(R.id.info_alliance_autoRotorPoints);
        autoRotorPoints.setText(formatter.format(AlliancePointEstimator.getAutoRotorPoints(data)) + " pts");

        // Teleop
        TextView teleopLowGoalCount = getView().findViewById(R.id.info_alliance_teleopLowGoalCount);
        teleopLowGoalCount.setText(formatter.format(data.getAverageTeleopLowGoals()));

        TextView teleopHighGoalCount = getView().findViewById(R.id.info_alliance_teleopHighGoalCount);
        teleopHighGoalCount.setText(formatter.format(data.getAverageTeleopHighGoals()));

        TextView teleopFuelPoints = getView().findViewById(R.id.info_alliance_teleopFuelPoints);
        teleopFuelPoints.setText(formatter.format(AlliancePointEstimator.getTeleopFuelPoints(data)) + " pts");

        TextView teleopGearDeliveryCount = getView().findViewById(R.id.info_alliance_teleopGearDeliveryCount);
        teleopGearDeliveryCount.setText(formatter.format(data.getAverageTeleopGearsDelivered()));

        TextView teleopRotorPoints = getView().findViewById(R.id.info_alliance_teleopRotorPoints);
        teleopRotorPoints.setText(formatter.format(AlliancePointEstimator.getTeleopRotorPoints(data)) + " pts");

        TextView climbPts = getView().findViewById(R.id.info_alliance_teleopClimbPoints);
        climbPts.setText(formatter.format(AlliancePointEstimator.getClimbingPoints(data)) + " pts");

        // Summary
        TextView rankingPoints = getView().findViewById(R.id.info_alliance_rankingPoints);
        rankingPoints.setText(formatter.format(AlliancePointEstimator.getAverageRankingPoints(data)) + " pts");

        TextView total = getView().findViewById(R.id.info_alliance_totalPoints);
        total.setText(formatter.format(AlliancePointEstimator.getPointContribution(data)) + " pts");

        RecyclerView troubleWith = getView().findViewById(R.id.info_alliance_troubleWith);
        TextView troubleWithPlaceholder = getView().findViewById(R.id.info_alliance_troubleWithPlaceholder);

        if (data.getTroublesList() == null || data.getTroublesList().isEmpty() || listIsEmpty(data.getTroublesList())) {
            troubleWith.setVisibility(View.GONE);
            troubleWithPlaceholder.setVisibility(View.VISIBLE);
        } else {
            troubleWith.setVisibility(View.VISIBLE);
            troubleWithPlaceholder.setVisibility(View.GONE);

            troubleWith.setLayoutManager(new LinearLayoutManager(getContext()));
            troubleWith.setAdapter(new CommentsAdapter(data.getTroublesList()));
        }

        RecyclerView comments = getView().findViewById(R.id.info_alliance_comments);
        TextView commentsPlaceholder = getView().findViewById(R.id.info_alliance_commentsPlaceholder);

        if (data.getCommentsList() == null || data.getCommentsList().isEmpty() || listIsEmpty(data.getCommentsList())) {
            comments.setVisibility(View.GONE);
            commentsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.VISIBLE);
            commentsPlaceholder.setVisibility(View.GONE);

            comments.setLayoutManager(new LinearLayoutManager(getContext()));
            comments.setAdapter(new CommentsAdapter(data.getCommentsList()));
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
