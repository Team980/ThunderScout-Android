package com.team980.thunderscout.match;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.ClimbingStats;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

public class TeleopFragment extends Fragment implements Spinner.OnItemSelectedListener {

    private ScoutingFlowActivity scoutingFlowActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner lowGoalDumpAmount = (Spinner) view.findViewById(R.id.teleop_spinnerLowGoalDumpAmount);
        lowGoalDumpAmount.setOnItemSelectedListener(this);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemSelected = (String) parent.getItemAtPosition(position);

        if (view.getId() == R.id.teleop_spinnerLowGoalDumpAmount) {
            FuelDumpAmount fuelDumpAmount = FuelDumpAmount.valueOf(itemSelected.toUpperCase().replace(' ', '_'));
            scoutingFlowActivity.getData().setAutoLowGoalDumpAmount(fuelDumpAmount);
        } else if (view.getId() == R.id.teleop_spinnerClimbingStats) {
            ClimbingStats climbingStats = ClimbingStats.valueOf(itemSelected.toUpperCase().replace(' ', '_'));
            scoutingFlowActivity.getData().setClimbingStats(climbingStats);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //do nothing
    }
}
