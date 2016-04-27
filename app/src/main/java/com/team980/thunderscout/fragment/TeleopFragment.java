package com.team980.thunderscout.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.activity.ScoutActivity;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.view.RankedSliderCompoundView;

public class TeleopFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private ScoutData scoutData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NestedScrollView teleopScroll = (NestedScrollView) view.findViewById(R.id.teleop_scrollView);
        teleopScroll.setOnScrollChangeListener((ScoutActivity) getActivity());

        for (Defense defense : Defense.values()) {
            CheckBox def = (CheckBox) view.findViewById(defense.getTeleopID());
            def.setOnCheckedChangeListener(this);
        }

        CheckBox lowGoal = (CheckBox) view.findViewById(R.id.teleop_goalLow);
        lowGoal.setOnCheckedChangeListener(this);

        CheckBox highGoal = (CheckBox) view.findViewById(R.id.teleop_goalHigh);
        highGoal.setOnCheckedChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ScoutActivity scoutActivity = (ScoutActivity) getActivity();
        scoutData = scoutActivity.getData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (Defense defense : Defense.values()) {
            if (buttonView.getId() == defense.getTeleopID()) {
                toggleVisibility((RankedSliderCompoundView) getView().findViewById(defense.getTeleopSliderID()), isChecked);
                return;
            }
        }

        if (buttonView.getId() == R.id.teleop_goalLow) {
            toggleVisibility((RankedSliderCompoundView) getView().findViewById(R.id.teleop_sliderLowGoal), isChecked);
        } else if (buttonView.getId() == R.id.teleop_goalHigh) {
            toggleVisibility((RankedSliderCompoundView) getView().findViewById(R.id.teleop_sliderHighGoal), isChecked);
        }
    }

    public void toggleVisibility(RankedSliderCompoundView view, boolean visibility) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Called when view state changes for this fragment
     *
     * @param isVisibleToUser
     */
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible() && !isVisibleToUser && scoutData != null) { //Leaving

            AppCompatEditText teamNumber = (AppCompatEditText) getView().findViewById(R.id.teleop_editTextTeamNumber);
            scoutData.setTeamNumber(teamNumber.getText().toString());
        }

        if (this.isVisible() && isVisibleToUser && scoutData != null) { //Returning

            AppCompatEditText teamNumber = (AppCompatEditText) getView().findViewById(R.id.teleop_editTextTeamNumber);
            teamNumber.setText(scoutData.getTeamNumber(), TextView.BufferType.NORMAL);
        }
    }
}
