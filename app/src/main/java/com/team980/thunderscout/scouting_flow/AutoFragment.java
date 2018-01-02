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

package com.team980.thunderscout.scouting_flow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.schema.enumeration.FuelDumpAmount;
import com.team980.thunderscout.scouting_flow.view.CounterCompoundView;

public class AutoFragment extends Fragment implements View.OnClickListener {

    ScoutingFlowActivity scoutingFlowActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatCheckBox crossedBaseline = view.findViewById(R.id.auto_checkBoxCrossedBaseline);
        crossedBaseline.setOnClickListener(this);

        Button minus = view.findViewById(R.id.auto_buttonFuelMinus);
        Button plus = view.findViewById(R.id.auto_buttonFuelPlus);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);

        CheckBox autoCrossedBaseline = getView().findViewById(R.id.auto_checkBoxCrossedBaseline);
        autoCrossedBaseline.setChecked(scoutingFlowActivity.getData().getAutonomous().getCrossedBaseline());

        CounterCompoundView autoGearsDelivered = getView().findViewById(R.id.auto_counterGearsDelivered);
        autoGearsDelivered.setValue(scoutingFlowActivity.getData().getAutonomous().getGearsDelivered());

        CounterCompoundView autoGearsDropped = getView().findViewById(R.id.auto_counterGearsDropped);
        autoGearsDropped.setValue(scoutingFlowActivity.getData().getAutonomous().getGearsDropped());

        TextView autoTextViewFuelValue = getView().findViewById(R.id.auto_textViewFuelValue);
        TextView autoTextViewFuelNumericalValue = getView().findViewById(R.id.auto_textViewFuelNumericalValue);
        autoTextViewFuelValue.setText(scoutingFlowActivity.getData().getAutonomous().getLowGoalDumpAmount().toString());
        autoTextViewFuelNumericalValue.setText(scoutingFlowActivity.getData().getAutonomous().getLowGoalDumpAmount().getMinimumAmount() + " - " + scoutingFlowActivity.getData().getAutonomous().getLowGoalDumpAmount().getMaximumAmount());

        CounterCompoundView autoHighGoals = getView().findViewById(R.id.auto_counterHighGoals);
        autoHighGoals.setValue(scoutingFlowActivity.getData().getAutonomous().getHighGoals());

        CounterCompoundView autoMissedHighGoals = getView().findViewById(R.id.auto_counterMissedHighGoals);
        autoMissedHighGoals.setValue(scoutingFlowActivity.getData().getAutonomous().getMissedHighGoals());
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.auto_checkBoxCrossedBaseline) {
            AppCompatCheckBox checkBox = (AppCompatCheckBox) view;

            scoutingFlowActivity.getData().getAutonomous().setCrossedBaseline(checkBox.isChecked());
        } else {
            FuelDumpAmount value = scoutingFlowActivity.getData().getAutonomous().getLowGoalDumpAmount();

            if (view.getId() == R.id.auto_buttonFuelPlus) {
                int newOrdinal = value.ordinal() + 1;

                if ((FuelDumpAmount.values().length - 1) < newOrdinal) {
                    value = FuelDumpAmount.values()[FuelDumpAmount.values().length - 1];
                } else {
                    value = FuelDumpAmount.values()[newOrdinal];
                }

            } else if (view.getId() == R.id.auto_buttonFuelMinus) {
                int newOrdinal = value.ordinal() - 1;

                if (newOrdinal < 0) {
                    value = FuelDumpAmount.values()[0];
                } else {
                    value = FuelDumpAmount.values()[newOrdinal];
                }
            }

            scoutingFlowActivity.getData().getAutonomous().setLowGoalDumpAmount(value);

            TextView textValue = getView().findViewById(R.id.auto_textViewFuelValue);
            TextView numericalValue = getView().findViewById(R.id.auto_textViewFuelNumericalValue);
            textValue.setText(value.toString());
            numericalValue.setText(value.getMinimumAmount() + " - " + value.getMaximumAmount());
        }
    }
}
