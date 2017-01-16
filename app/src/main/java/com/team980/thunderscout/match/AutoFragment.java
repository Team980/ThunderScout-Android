package com.team980.thunderscout.match;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

public class AutoFragment extends Fragment implements Spinner.OnItemSelectedListener, CheckBox.OnClickListener {

    ScoutingFlowActivity scoutingFlowActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner lowGoalDumpAmount = (Spinner) view.findViewById(R.id.auto_spinnerLowGoalDumpAmount);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //Spinner
        String itemSelected = (String) parent.getItemAtPosition(position);
        FuelDumpAmount fuelDumpAmount = FuelDumpAmount.valueOf(itemSelected.toUpperCase().replace(' ', '_'));
        scoutingFlowActivity.getData().setAutoLowGoalDumpAmount(fuelDumpAmount);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.auto_checkBoxCrossedBaseline) {
            CheckBox checkBox = (CheckBox) view;

            scoutingFlowActivity.getData().setCrossedBaseline(checkBox.isChecked());
        }
    }
}
