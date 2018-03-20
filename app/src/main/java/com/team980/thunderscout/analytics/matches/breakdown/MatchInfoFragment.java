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

package com.team980.thunderscout.analytics.matches.breakdown;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.schema.ScoutData;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MatchInfoFragment extends Fragment {

    public static final String EXTRA_SCOUT_DATA = "com.team980.thunderscout.SCOUT_DATA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments(); //TODO use Argument bundle
        ScoutData data = (ScoutData) args.getSerializable(EXTRA_SCOUT_DATA);


        // Init
        TextView date = view.findViewById(R.id.info_match_date);
        date.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDate()));

        TextView source = view.findViewById(R.id.info_match_source);
        source.setText(data.getSource());

        // Auto
        TextView crossedAutoLine = view.findViewById(R.id.info_match_autoCrossedAutoLine);
        crossedAutoLine.setText(String.valueOf(data.crossedAutoLine()).toUpperCase(Locale.ROOT));

        TextView auto_powerCubeAllianceSwitchCount = view.findViewById(R.id.info_match_autoPowerCubeAllianceSwitchCount);
        auto_powerCubeAllianceSwitchCount.setText(data.getAutoPowerCubeAllianceSwitchCount() + "");

        TextView auto_powerCubeScaleCount = view.findViewById(R.id.info_match_autoPowerCubeScaleCount);
        auto_powerCubeScaleCount.setText(data.getAutoPowerCubeScaleCount() + "");

        TextView auto_powerCubePlayerStationCount = view.findViewById(R.id.info_match_autoPowerCubePlayerStationCount);
        auto_powerCubePlayerStationCount.setText(data.getAutoPowerCubePlayerStationCount() + "");

        // Teleop
        TextView teleop_powerCubeAllianceSwitchCount = view.findViewById(R.id.info_match_teleopPowerCubeAllianceSwitchCount);
        teleop_powerCubeAllianceSwitchCount.setText(data.getTeleopPowerCubeAllianceSwitchCount() + "");

        TextView teleop_powerCubeScaleCount = view.findViewById(R.id.info_match_teleopPowerCubeScaleCount);
        teleop_powerCubeScaleCount.setText(data.getTeleopPowerCubeScaleCount() + "");

        TextView teleop_powerCubeOpposingSwitchCount = view.findViewById(R.id.info_match_teleopPowerCubeOpposingSwitchCount);
        teleop_powerCubeOpposingSwitchCount.setText(data.getTeleopPowerCubeOpposingSwitchCount() + "");

        TextView teleop_powerCubePlayerStationCount = view.findViewById(R.id.info_match_teleopPowerCubePlayerStationCount);
        teleop_powerCubePlayerStationCount.setText(data.getTeleopPowerCubePlayerStationCount() + "");

        TextView climbingStats = view.findViewById(R.id.info_match_teleopClimbingStats);
        climbingStats.setText(data.getClimbingStats().toString().toUpperCase());

        TextView supportedOtherRobotsWhenClimbing = view.findViewById(R.id.info_match_teleopSupportedOtherRobotsWhenClimbing);
        supportedOtherRobotsWhenClimbing.setText(String.valueOf(data.supportedOtherRobots()).toUpperCase(Locale.ROOT));

        // Summary
        TextView strategies = view.findViewById(R.id.info_match_strategies);
        if (data.getStrategies() != null && !data.getStrategies().isEmpty()) {
            strategies.setText(data.getStrategies());
        } else {
            strategies.setText("N/A");
        }

        TextView difficulties = view.findViewById(R.id.info_match_difficulties);
        if (data.getDifficulties() != null && !data.getDifficulties().isEmpty()) {
            difficulties.setText(data.getDifficulties());
        } else {
            difficulties.setText("N/A");
        }

        TextView comments = view.findViewById(R.id.info_match_comments);
        if (data.getComments() != null && !data.getComments().isEmpty()) {
            comments.setText(data.getComments());
        } else {
            comments.setText("N/A");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
