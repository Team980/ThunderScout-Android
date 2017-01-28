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

            scoutingFlowActivity.getData().setCrossedBaseline(checkBox.isChecked());
        } else {
            FuelDumpAmount value = scoutingFlowActivity.getData().getAutoLowGoalDumpAmount();

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

            scoutingFlowActivity.getData().setAutoLowGoalDumpAmount(value);

            TextView textValue = (TextView) getView().findViewById(R.id.auto_textViewFuelValue);
            TextView numericalValue = (TextView) getView().findViewById(R.id.auto_textViewFuelNumericalValue);
            textValue.setText(value.toString());
            numericalValue.setText(value.getMinimumAmount() + " - " + value.getMaximumAmount());
        }
    }
}
