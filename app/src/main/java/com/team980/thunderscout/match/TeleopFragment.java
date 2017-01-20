package com.team980.thunderscout.match;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.ClimbingStats;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

public class TeleopFragment extends Fragment implements View.OnClickListener {

    private ScoutingFlowActivity scoutingFlowActivity;

    private FuelDumpAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView fuelDumps = (RecyclerView) view.findViewById(R.id.teleop_recyclerViewFuelDumps);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        fuelDumps.setLayoutManager(mLayoutManager);

        adapter = new FuelDumpAdapter();
        fuelDumps.setAdapter(adapter);

        Button addDumpButton = (Button) view.findViewById(R.id.teleop_buttonAddFuelDump);
        addDumpButton.setOnClickListener(this);
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

    public FuelDumpAdapter getFuelDumpAdapter() {
        return adapter;
    }
}
