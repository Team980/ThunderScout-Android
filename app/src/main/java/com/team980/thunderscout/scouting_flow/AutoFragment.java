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
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.team980.thunderscout.R;
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

        CheckBox crossedAutoLine = getView().findViewById(R.id.auto_checkBoxCrossedAutoLine);
        crossedAutoLine.setChecked(scoutingFlowActivity.getData().getCrossedAutoLine());
        crossedAutoLine.setOnClickListener(this);

        CounterCompoundView powerCubeAllianceSwitchCount = getView().findViewById(R.id.auto_counterPowerCubeAllianceSwitchCount);
        powerCubeAllianceSwitchCount.setValue(scoutingFlowActivity.getData().getAutoPowerCubeAllianceSwitchCount());

        CounterCompoundView powerCubeScaleCount = getView().findViewById(R.id.auto_counterPowerCubeScaleCount);
        powerCubeScaleCount.setValue(scoutingFlowActivity.getData().getAutoPowerCubeScaleCount());

        CounterCompoundView powerCubePlayerStationCount = getView().findViewById(R.id.auto_counterPowerCubePlayerStationCount);
        powerCubePlayerStationCount.setValue(scoutingFlowActivity.getData().getAutoPowerCubePlayerStationCount());
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
        if (view.getId() == R.id.auto_checkBoxCrossedAutoLine) {
            AppCompatCheckBox checkBox = (AppCompatCheckBox) view;

            scoutingFlowActivity.getData().setCrossedAutoLine(checkBox.isChecked());
        }
    }
}
