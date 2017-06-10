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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.team980.thunderscout.R;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;
import com.team980.thunderscout.schema.enumeration.FuelDumpAmount;

public class TeleopFragment extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {

    private ScoutingFlowActivity scoutingFlowActivity;

    private LinearLayoutManager layoutManager;
    private DumpCounterAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView fuelDumps = view.findViewById(R.id.teleop_recyclerViewFuelDumps);

        layoutManager = new LinearLayoutManager(getContext());
        fuelDumps.setLayoutManager(layoutManager);

        adapter = new DumpCounterAdapter();
        fuelDumps.setAdapter(adapter);

        if (savedInstanceState != null) {
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("LayoutManager"));
            adapter.onRestoreInstanceState(savedInstanceState);
        }

        Button addDumpButton = view.findViewById(R.id.teleop_buttonAddFuelDump);
        addDumpButton.setOnClickListener(this);

        Spinner climbingStats = view.findViewById(R.id.teleop_spinnerClimbingStats);
        climbingStats.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.climbing_stats_array, R.layout.spinner_climbing_stats);
        adapter.setDropDownViewResource(R.layout.spinner_climbing_stats_dropdown);
        climbingStats.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("LayoutManager", layoutManager.onSaveInstanceState());
        adapter.onSaveInstanceState(savedInstanceState);
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
     * Click listener for RecyclerView button
     */
    @Override
    public void onClick(View v) {
        adapter.add(FuelDumpAmount.NONE);
    }

    /**
     * Listener for ClimbingStats spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemSelected = (String) parent.getItemAtPosition(position);

        ClimbingStats climbingStats = ClimbingStats.valueOf(itemSelected.toUpperCase().replace(' ', '_'));
        scoutingFlowActivity.getData().getTeleop().setClimbingStats(climbingStats);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    public DumpCounterAdapter getFuelDumpAdapter() {
        return adapter;
    }

}
