package com.team980.thunderscout.match;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

public class TeleopFragment extends Fragment implements View.OnClickListener {

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

        RecyclerView fuelDumps = (RecyclerView) view.findViewById(R.id.teleop_recyclerViewFuelDumps);

        layoutManager = new LinearLayoutManager(getContext());
        fuelDumps.setLayoutManager(layoutManager);

        adapter = new DumpCounterAdapter();
        fuelDumps.setAdapter(adapter);

        if (savedInstanceState != null) {
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("LayoutManager"));
            adapter.onRestoreInstanceState(savedInstanceState);
        }

        Button addDumpButton = (Button) view.findViewById(R.id.teleop_buttonAddFuelDump);
        addDumpButton.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("LayoutManager", layoutManager.onSaveInstanceState());
        adapter.onSaveInstanceState(savedInstanceState);
        Log.d("InstanceRedux", "Saved");
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

    public DumpCounterAdapter getFuelDumpAdapter() {
        return adapter;
    }
}
