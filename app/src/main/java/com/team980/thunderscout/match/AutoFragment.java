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

package com.team980.thunderscout.match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

public class AutoFragment extends Fragment implements View.OnClickListener {

    ScoutingFlowActivity scoutingFlowActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CheckBox crossedBaseline = (CheckBox) view.findViewById(R.id.auto_checkBoxCrossedBaseline);
        crossedBaseline.setOnClickListener(this);

        Button minus = (Button) view.findViewById(R.id.auto_buttonFuelMinus);
        Button plus = (Button) view.findViewById(R.id.auto_buttonFuelPlus);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);

        TextView textValue = (TextView) getView().findViewById(R.id.auto_textViewFuelValue);
        TextView numericalValue = (TextView) getView().findViewById(R.id.auto_textViewFuelNumericalValue);
        textValue.setText(FuelDumpAmount.NONE.toString());
        numericalValue.setText(FuelDumpAmount.NONE.getMinimumAmount() + " - " + FuelDumpAmount.NONE.getMaximumAmount());
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
            CheckBox checkBox = (CheckBox) view;

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

            TextView textValue = (TextView) getView().findViewById(R.id.auto_textViewFuelValue);
            TextView numericalValue = (TextView) getView().findViewById(R.id.auto_textViewFuelNumericalValue);
            textValue.setText(value.toString());
            numericalValue.setText(value.getMinimumAmount() + " - " + value.getMaximumAmount());
        }
    }
}
