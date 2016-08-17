package com.team980.thunderscout.match;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.CrossingStats;
import com.team980.thunderscout.data.enumeration.Defense;
import com.team980.thunderscout.data.enumeration.ScoringStats;

public class AutoFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener {

    ScoutActivity scoutActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner defense = (Spinner) view.findViewById(R.id.auto_spinnerDefenses);
        defense.setOnItemSelectedListener(this);

        RadioGroup crossing = (RadioGroup) view.findViewById(R.id.auto_radioGroupCrossing);
        crossing.setOnCheckedChangeListener(this);

        RadioGroup scoring = (RadioGroup) view.findViewById(R.id.auto_radioGroupScoring);
        scoring.setOnCheckedChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        scoutActivity = (ScoutActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //Spinner
        String itemSelected = (String) parent.getItemAtPosition(position);
        Defense defense = Defense.valueOf(itemSelected.toUpperCase().replace(' ', '_'));
        scoutActivity.getData().setAutoDefenseCrossed(defense); //TODO crash on orientation change
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) { //RadioGroup
        switch (checkedId) {
            case R.id.auto_buttonNoAction:
                scoutActivity.getData().setAutoCrossingStats(CrossingStats.NONE);
                toggleDefenseVisibility(View.GONE);
                toggleScoringVisibility(View.GONE);
                break;
            case R.id.auto_buttonReachedDefense:
                scoutActivity.getData().setAutoCrossingStats(CrossingStats.REACHED);
                toggleDefenseVisibility(View.VISIBLE);
                toggleScoringVisibility(View.GONE);
                break;
            case R.id.auto_buttonCrossedDefense:
                scoutActivity.getData().setAutoCrossingStats(CrossingStats.CROSSED);
                toggleDefenseVisibility(View.VISIBLE);
                toggleScoringVisibility(View.VISIBLE);
                break;
            case R.id.auto_buttonNoGoal:
                scoutActivity.getData().setAutoScoringStats(ScoringStats.NONE);
                break;
            case R.id.auto_buttonLowGoal:
                scoutActivity.getData().setAutoScoringStats(ScoringStats.LOW_GOAL);
                break;
            case R.id.auto_buttonHighGoal:
                scoutActivity.getData().setAutoScoringStats(ScoringStats.HIGH_GOAL);
                break;
        }
    }

    /**
     * Called when view state changes for this fragment
     *
     * @param isVisibleToUser
     */
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible() && !isVisibleToUser && scoutActivity.getData() != null) { //Leaving

            AppCompatEditText teamNumber = (AppCompatEditText) getView().findViewById(R.id.auto_editTextTeamNumber);
            scoutActivity.getData().setTeamNumber(teamNumber.getText().toString());
        }

        if (this.isVisible() && isVisibleToUser && scoutActivity.getData() != null) { //Returning

            AppCompatEditText teamNumber = (AppCompatEditText) getView().findViewById(R.id.auto_editTextTeamNumber);
            teamNumber.setText(scoutActivity.getData().getTeamNumber(), TextView.BufferType.NORMAL);

            AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
            appBarLayout.setExpanded(true, true);
        }
    }

    /**
     * @param visibility View.VISIBILE, View.INVISIBLE, or View.GONE
     */
    private void toggleDefenseVisibility(int visibility) {

        View defense = getView().findViewById(R.id.auto_layoutDefense);

        defense.setVisibility(visibility);
    }

    /**
     * @param visibility View.VISIBILE, View.INVISIBLE, or View.GONE
     */
    private void toggleScoringVisibility(int visibility) {

        View scoring = getView().findViewById(R.id.auto_layoutScoring);

        scoring.setVisibility(visibility);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}
