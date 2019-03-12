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

        // Sandstorm
        TextView startingLevel = view.findViewById(R.id.info_match_stormStartingLevel);
        startingLevel.setText(String.valueOf(data.getStartingLevel()));

        TextView crossedHabLine = view.findViewById(R.id.info_match_stormCrossedHabLine);
        crossedHabLine.setText(String.valueOf(data.crossedHabLine()).toUpperCase(Locale.ROOT));

        TextView storm_highRocketHatchCount = view.findViewById(R.id.info_match_stormHighRocketHatchCount);
        storm_highRocketHatchCount.setText(String.valueOf(data.getStormHighRocketHatchCount()));

        TextView storm_midRocketHatchCount = view.findViewById(R.id.info_match_stormMidRocketHatchCount);
        storm_midRocketHatchCount.setText(String.valueOf(data.getStormMidRocketHatchCount()));

        TextView storm_lowRocketHatchCount = view.findViewById(R.id.info_match_stormLowRocketHatchCount);
        storm_lowRocketHatchCount.setText(String.valueOf(data.getStormLowRocketHatchCount()));

        TextView storm_cargoShipHatchCount = view.findViewById(R.id.info_match_stormCargoShipHatchCount);
        storm_cargoShipHatchCount.setText(String.valueOf(data.getStormCargoShipHatchCount()));

        TextView storm_highRocketCargoCount = view.findViewById(R.id.info_match_stormHighRocketCargoCount);
        storm_highRocketCargoCount.setText(String.valueOf(data.getStormHighRocketCargoCount()));

        TextView storm_midRocketCargoCount = view.findViewById(R.id.info_match_stormMidRocketCargoCount);
        storm_midRocketCargoCount.setText(String.valueOf(data.getStormMidRocketCargoCount()));

        TextView storm_lowRocketCargoCount = view.findViewById(R.id.info_match_stormLowRocketCargoCount);
        storm_lowRocketCargoCount.setText(String.valueOf(data.getStormLowRocketCargoCount()));

        TextView storm_cargoShipCargoCount = view.findViewById(R.id.info_match_stormCargoShipCargoCount);
        storm_cargoShipCargoCount.setText(String.valueOf(data.getStormCargoShipCargoCount()));

        // Teleoperated
        TextView teleop_highRocketHatchCount = view.findViewById(R.id.info_match_teleopHighRocketHatchCount);
        teleop_highRocketHatchCount.setText(String.valueOf(data.getTeleopHighRocketHatchCount()));

        TextView teleop_midRocketHatchCount = view.findViewById(R.id.info_match_teleopMidRocketHatchCount);
        teleop_midRocketHatchCount.setText(String.valueOf(data.getTeleopMidRocketHatchCount()));

        TextView teleop_lowRocketHatchCount = view.findViewById(R.id.info_match_teleopLowRocketHatchCount);
        teleop_lowRocketHatchCount.setText(String.valueOf(data.getTeleopLowRocketHatchCount()));

        TextView teleop_cargoShipHatchCount = view.findViewById(R.id.info_match_teleopCargoShipHatchCount);
        teleop_cargoShipHatchCount.setText(String.valueOf(data.getTeleopCargoShipHatchCount()));

        TextView teleop_highRocketCargoCount = view.findViewById(R.id.info_match_teleopHighRocketCargoCount);
        teleop_highRocketCargoCount.setText(String.valueOf(data.getTeleopHighRocketCargoCount()));

        TextView teleop_midRocketCargoCount = view.findViewById(R.id.info_match_teleopMidRocketCargoCount);
        teleop_midRocketCargoCount.setText(String.valueOf(data.getTeleopMidRocketCargoCount()));

        TextView teleop_lowRocketCargoCount = view.findViewById(R.id.info_match_teleopLowRocketCargoCount);
        teleop_lowRocketCargoCount.setText(String.valueOf(data.getTeleopLowRocketCargoCount()));

        TextView teleop_cargoShipCargoCount = view.findViewById(R.id.info_match_teleopCargoShipCargoCount);
        teleop_cargoShipCargoCount.setText(String.valueOf(data.getTeleopCargoShipCargoCount()));

        // Endgame

        TextView endgame_climbLevel = view.findViewById(R.id.info_match_endgameClimbLevel);
        endgame_climbLevel.setText(String.valueOf(data.getEndgameClimbLevel()));

        TextView endgame_climbTime = view.findViewById(R.id.info_match_endgameClimbTime);
        endgame_climbTime.setText(String.valueOf(data.getEndgameClimbTime()));

        TextView supportedAnotherRobot = view.findViewById(R.id.info_match_teleopSupportedOtherRobotsWhenClimbing);
        supportedAnotherRobot.setText(String.valueOf(data.supportedOtherRobots()).toUpperCase(Locale.ROOT));

        TextView climbDescription = view.findViewById(R.id.info_match_endgameClimbDescription);
        if (data.getClimbDescription() != null && !data.getClimbDescription().isEmpty()) {
            climbDescription.setText(data.getClimbDescription());
        } else {
            climbDescription.setText("N/A");
        }

        // Notes
        TextView notes = view.findViewById(R.id.info_match_notes);
        if (data.getNotes() != null && !data.getNotes().isEmpty()) {
            notes.setText(data.getNotes());
        } else {
            notes.setText("N/A");
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
