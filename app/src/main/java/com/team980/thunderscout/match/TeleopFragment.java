package com.team980.thunderscout.match;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.ScoutData;

public class TeleopFragment extends Fragment {

    private ScoutData scoutData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teleop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ScoutingFlowActivity scoutingFlowActivity = (ScoutingFlowActivity) getActivity();
        scoutData = scoutingFlowActivity.getData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
