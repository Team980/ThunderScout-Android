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

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.AllianceStation;

public class ScoutingFlowDialogFragment extends AppCompatDialogFragment {

    // Use this instance of the interface to deliver action events
    private ScoutingFlowDialogFragmentListener mListener;

    private EditText teamNumber;
    private EditText matchNumber;
    private AppCompatSpinner allianceStationSpinner; //TODO this needs nicer UI - spinners are impossibe to customize (possibly make custom View?)
    private AllianceStation allianceStation;

    public static final String EXTRA_DEFAULT_DATA = "com.team980.thunderscout.EXTRA_DEFAULT_DATA";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_scouting_flow, null);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        final Toolbar toolbar = (Toolbar) dialogView.findViewById(R.id.toolbar);
        toolbar.setTitle("Match Settings");

        teamNumber = (EditText) dialogView.findViewById(R.id.dialog_editTextTeamNumber);
        teamNumber.requestFocus();

        matchNumber = (EditText) dialogView.findViewById(R.id.dialog_editTextMatchNumber);
        matchNumber.setText(String.valueOf(prefs.getInt("last_used_match_number", 0) + 1)); //increment the last match number

        allianceStation = AllianceStation.valueOf(prefs.getString("last_used_alliance_station", AllianceStation.RED_1.name()));
        allianceStationSpinner = (AppCompatSpinner) dialogView.findViewById(R.id.dialog_allianceStationSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.alliance_stations_array,
                R.layout.spinner_alliance_station);
        adapter.setDropDownViewResource(R.layout.spinner_alliance_station_dropdown);

        allianceStationSpinner.setAdapter(adapter);
        allianceStationSpinner.setSelection(allianceStation.ordinal());

        if (getArguments() != null && getArguments().containsKey(EXTRA_DEFAULT_DATA)) { //Fill the data with previously set values
            ScoutData fillData = (ScoutData) getArguments().getSerializable(EXTRA_DEFAULT_DATA);

            teamNumber.setText(fillData.getTeam());
            matchNumber.setText(fillData.getMatchNumber() + "");

            allianceStation = fillData.getAllianceStation();
            allianceStationSpinner.setSelection(allianceStation.ordinal());
        }

        allianceStationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = (String) parent.getItemAtPosition(position);
                allianceStation = AllianceStation.valueOf(itemSelected.toUpperCase().replace(' ', '_'));

                if (allianceStation.getColor() == AllianceStation.AllianceColor.RED) { //If red, switch to blue, and vice versa
                    ((TextView) allianceStationSpinner.getChildAt(0)).setBackground(getResources().getDrawable(R.color.alliance_red_primary));
                } else {
                    ((TextView) allianceStationSpinner.getChildAt(0)).setBackground(getResources().getDrawable(R.color.alliance_blue_primary));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(ScoutingFlowDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ScoutingFlowDialogFragment.this);
                    }
                });

        final Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() { //Complex code to override auto dismiss

            @Override
            public void onShow(DialogInterface d) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mListener.onDialogPositiveClick(ScoutingFlowDialogFragment.this);
                    }
                });
            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ScoutingFlowDialogFragmentListener so we can send events to the host
            mListener = (ScoutingFlowDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ScoutingFlowDialogFragmentListener");
        }
    }

    public boolean allFieldsComplete() {
        if (teamNumber.getText().toString().isEmpty()) {
            return false;
        }

        if (matchNumber.getText().toString().isEmpty() || !ThunderScout.isInteger(matchNumber.getText().toString())) {
            return false;
        }

        return true;
    }

    public void initScoutData(ScoutData data) {
        data.setTeam(teamNumber.getText().toString());

        data.setMatchNumber(Integer.valueOf(matchNumber.getText().toString()));

        data.setAllianceStation(allianceStation);
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ScoutingFlowDialogFragmentListener {
        void onDialogPositiveClick(ScoutingFlowDialogFragment dialog);

        void onDialogNegativeClick(ScoutingFlowDialogFragment dialog);
    }

}
