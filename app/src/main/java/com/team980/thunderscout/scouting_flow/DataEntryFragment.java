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

package com.team980.thunderscout.scouting_flow;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.team980.thunderscout.R;
import com.team980.thunderscout.schema.enumeration.ClimbTime;
import com.team980.thunderscout.schema.enumeration.HabLevel;
import com.team980.thunderscout.scouting_flow.view.CounterCompoundView;

public class DataEntryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ScoutingFlowActivity scoutingFlowActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sandstorm
        Spinner startingLevel = view.findViewById(R.id.storm_spinnerStartingLevel);
        startingLevel.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.hab_level_array, R.layout.spinner_data_entry);
        adapter.setDropDownViewResource(R.layout.spinner_data_entry_dropdown);
        startingLevel.setAdapter(adapter);

        CheckBox crossedHabLine = view.findViewById(R.id.storm_checkBoxCrossedHabLine);
        crossedHabLine.setChecked(scoutingFlowActivity.getData().crossedHabLine());
        crossedHabLine.setOnClickListener(this);

        CounterCompoundView storm_HighRocketHatchCount = view.findViewById(R.id.storm_counterHighRocketHatch);
        storm_HighRocketHatchCount.setValue(scoutingFlowActivity.getData().getStormHighRocketHatchCount());

        CounterCompoundView storm_MidRocketHatchCount = view.findViewById(R.id.storm_counterMidRocketHatch);
        storm_MidRocketHatchCount.setValue(scoutingFlowActivity.getData().getStormMiddleRocketHatchCount());

        CounterCompoundView storm_LowRocketHatchCount = view.findViewById(R.id.storm_counterLowRocketHatch);
        storm_LowRocketHatchCount.setValue(scoutingFlowActivity.getData().getStormLowRocketHatchCount());

        CounterCompoundView storm_CargoShipHatchCount = view.findViewById(R.id.storm_counterCargoShipHatch);
        storm_CargoShipHatchCount.setValue(scoutingFlowActivity.getData().getStormCargoShipHatchCount());

        CounterCompoundView storm_HighRocketCargoCount = view.findViewById(R.id.storm_counterHighRocketCargo);
        storm_HighRocketCargoCount.setValue(scoutingFlowActivity.getData().getStormHighRocketCargoCount());

        CounterCompoundView storm_MidRocketCargoCount = view.findViewById(R.id.storm_counterMidRocketCargo);
        storm_MidRocketCargoCount.setValue(scoutingFlowActivity.getData().getStormMiddleRocketCargoCount());

        CounterCompoundView storm_LowRocketCargoCount = view.findViewById(R.id.storm_counterLowRocketCargo);
        storm_LowRocketCargoCount.setValue(scoutingFlowActivity.getData().getStormLowRocketCargoCount());

        CounterCompoundView storm_CargoShipCargoCount = view.findViewById(R.id.storm_counterCargoShipCargo);
        storm_CargoShipCargoCount.setValue(scoutingFlowActivity.getData().getStormCargoShipCargoCount());

        // Teleoperated
        CounterCompoundView teleop_HighRocketHatchCount = view.findViewById(R.id.teleop_counterHighRocketHatch);
        teleop_HighRocketHatchCount.setValue(scoutingFlowActivity.getData().getTeleopHighRocketHatchCount());

        CounterCompoundView teleop_MidRocketHatchCount = view.findViewById(R.id.teleop_counterMidRocketHatch);
        teleop_MidRocketHatchCount.setValue(scoutingFlowActivity.getData().getTeleopMiddleRocketHatchCount());

        CounterCompoundView teleop_LowRocketHatchCount = view.findViewById(R.id.teleop_counterLowRocketHatch);
        teleop_LowRocketHatchCount.setValue(scoutingFlowActivity.getData().getTeleopLowRocketHatchCount());

        CounterCompoundView teleop_CargoShipHatchCount = view.findViewById(R.id.teleop_counterCargoShipHatch);
        teleop_CargoShipHatchCount.setValue(scoutingFlowActivity.getData().getTeleopCargoShipHatchCount());

        CounterCompoundView teleop_HighRocketCargoCount = view.findViewById(R.id.teleop_counterHighRocketCargo);
        teleop_HighRocketCargoCount.setValue(scoutingFlowActivity.getData().getTeleopHighRocketCargoCount());

        CounterCompoundView teleop_MidRocketCargoCount = view.findViewById(R.id.teleop_counterMidRocketCargo);
        teleop_MidRocketCargoCount.setValue(scoutingFlowActivity.getData().getTeleopMiddleRocketCargoCount());

        CounterCompoundView teleop_LowRocketCargoCount = view.findViewById(R.id.teleop_counterLowRocketCargo);
        teleop_LowRocketCargoCount.setValue(scoutingFlowActivity.getData().getTeleopLowRocketCargoCount());

        CounterCompoundView teleop_CargoShipCargoCount = view.findViewById(R.id.teleop_counterCargoShipCargo);
        teleop_CargoShipCargoCount.setValue(scoutingFlowActivity.getData().getTeleopCargoShipCargoCount());

        // Endgame
        Spinner climbLevel = view.findViewById(R.id.endgame_spinnerClimbLevel);
        climbLevel.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.hab_level_array, R.layout.spinner_data_entry);
        adapter.setDropDownViewResource(R.layout.spinner_data_entry_dropdown);
        climbLevel.setAdapter(adapter2);

        Spinner climbTime = view.findViewById(R.id.endgame_spinnerClimbTime);
        climbTime.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(),
                R.array.climb_time_array, R.layout.spinner_data_entry);
        adapter.setDropDownViewResource(R.layout.spinner_data_entry_dropdown);
        climbTime.setAdapter(adapter3);

        CheckBox supportedOtherRobot = view.findViewById(R.id.endgame_checkBoxSupportedOtherRobotsWhenClimbing);
        supportedOtherRobot.setChecked(scoutingFlowActivity.getData().supportedOtherRobots());
        supportedOtherRobot.setOnClickListener(this);

        EditText climbDescription = view.findViewById(R.id.endgame_edittextClimbDescription);
        climbDescription.setText(scoutingFlowActivity.getData().getClimbDescription());

        // Notes
        EditText notes = view.findViewById(R.id.edittextNotes);
        notes.setText(scoutingFlowActivity.getData().getNotes());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        scoutingFlowActivity = (ScoutingFlowActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Listener for data entry spinners
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.storm_spinnerStartingLevel) {
            HabLevel startingLevel = HabLevel.values()[position];
            scoutingFlowActivity.getData().setStartingLevel(startingLevel);

        } else if (parent.getId() == R.id.endgame_spinnerClimbLevel) {
            HabLevel climbLevel = HabLevel.values()[position];
            scoutingFlowActivity.getData().setEndgameClimbLevel(climbLevel);

        } else if (parent.getId() == R.id.endgame_spinnerClimbTime) {
            ClimbTime climbTime = ClimbTime.values()[position];
            scoutingFlowActivity.getData().setEndgameClimbTime(climbTime);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    /**
     * Listener for data entry checkboxes
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.storm_checkBoxCrossedHabLine) {

            AppCompatCheckBox checkBox = (AppCompatCheckBox) view;
            scoutingFlowActivity.getData().setCrossedHabLine(checkBox.isChecked());
        } else if (view.getId() == R.id.endgame_checkBoxSupportedOtherRobotsWhenClimbing) {

            AppCompatCheckBox checkBox = (AppCompatCheckBox) view;
            scoutingFlowActivity.getData().setSupportedOtherRobots(checkBox.isChecked());
        }
    }
}
