package com.team980.thunderscout.match;

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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.team980.thunderscout.R;
import com.team980.thunderscout.ThunderScout;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.AllianceColor;

public class ScoutingFlowDialogFragment extends AppCompatDialogFragment {

    // Use this instance of the interface to deliver action events
    private ScoutingFlowDialogFragmentListener mListener;

    private EditText teamNumber;
    private EditText matchNumber;
    private AppCompatButton allianceToggle;
    private AllianceColor allianceColor;

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

        allianceColor = AllianceColor.valueOf(prefs.getString("last_used_alliance_color", AllianceColor.ALLIANCE_COLOR_RED.name()));
        allianceToggle = (AppCompatButton) dialogView.findViewById(R.id.dialog_allianceToggleButton);

        if (getArguments() != null && getArguments().containsKey(EXTRA_DEFAULT_DATA)) { //Fill the data with previously set values
            ScoutData fillData = (ScoutData) getArguments().getSerializable(EXTRA_DEFAULT_DATA);

            teamNumber.setText(fillData.getTeamNumber());
            matchNumber.setText(fillData.getMatchNumber() + "");

            if (fillData.getAllianceColor() == AllianceColor.ALLIANCE_COLOR_RED) {
                allianceToggle.setSupportBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.alliance_red_primary));
                allianceToggle.setText("Red Alliance");
                allianceColor = AllianceColor.ALLIANCE_COLOR_RED;
            } else { //If red, switch to blue, and vice versa
                allianceToggle.setSupportBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.alliance_blue_primary));
                allianceToggle.setText("Blue Alliance");
                allianceColor = AllianceColor.ALLIANCE_COLOR_BLUE;
            }
        }

        if (allianceColor == AllianceColor.ALLIANCE_COLOR_BLUE) { //Red is default
            allianceToggle.setSupportBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.alliance_blue_primary));
            allianceToggle.setText("Blue Alliance");
        }

        allianceToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allianceColor == AllianceColor.ALLIANCE_COLOR_RED) { //If red, switch to blue, and vice versa
                    allianceToggle.setSupportBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.alliance_blue_primary));
                    allianceToggle.setText("Blue Alliance");
                    allianceColor = AllianceColor.ALLIANCE_COLOR_BLUE;
                } else {
                    allianceToggle.setSupportBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.alliance_red_primary));
                    allianceToggle.setText("Red Alliance");
                    allianceColor = AllianceColor.ALLIANCE_COLOR_RED;
                }
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
        data.setTeamNumber(teamNumber.getText().toString());

        data.setMatchNumber(Integer.valueOf(matchNumber.getText().toString()));

        data.setAllianceColor(allianceColor);
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ScoutingFlowDialogFragmentListener {
        void onDialogPositiveClick(ScoutingFlowDialogFragment dialog);

        void onDialogNegativeClick(ScoutingFlowDialogFragment dialog);
    }

}
