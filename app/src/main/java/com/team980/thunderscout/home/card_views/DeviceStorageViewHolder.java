package com.team980.thunderscout.home.card_views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.matches.MatchWrapper;
import com.team980.thunderscout.analytics.matches.breakdown.MatchInfoActivity;
import com.team980.thunderscout.home.CardViewHolder;
import com.team980.thunderscout.schema.enumeration.AllianceStation;

public class DeviceStorageViewHolder extends CardViewHolder {

    //private ProgressBar dataCompletionProgress;
    private TextView dataCompletionText;

    private TextView matchNumber;
    private GridLayout matchGrid;

    public DeviceStorageViewHolder(View itemView) {
        super(itemView);

        //dataCompletionProgress = itemView.findViewById(R.id.card_device_data_completion_progress);
        dataCompletionText = itemView.findViewById(R.id.card_device_data_completion_text);

        matchNumber = itemView.findViewById(R.id.match_number);
        matchGrid = itemView.findViewById(R.id.match_grid);
    }

    @Override
    public void bind() {
        dataCompletionText.setText("XX% complete - missing Y matches");

        ((LinearLayout) matchNumber.getParent()).setVisibility(View.GONE); //TODO figure out how to refresh
    }

    @Override
    public void update() {

    }

    public void refreshLastMatch(final Context context, final MatchWrapper wrapper) {
        matchNumber.setText(wrapper.getMatchNumber() + "");

        for (final AllianceStation station : AllianceStation.values()) {
            final TextView matchView = matchGrid.findViewById(station.getMatchCellViewID());

            if (wrapper.getData(station) != null) {
                matchView.setText(wrapper.getData(station).getTeam());

                matchView.setVisibility(View.VISIBLE);

                matchView.setBackgroundColor(context.getResources().getColor(station.getColorStratified()));

                matchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchInfoActivity = new Intent(context, MatchInfoActivity.class);
                        launchInfoActivity.putExtra("com.team980.thunderscout.INFO_SCOUT", wrapper.getData(station));
                        context.startActivity(launchInfoActivity);

                    }
                });
            } else {
                matchView.setText("");

                matchView.setVisibility(View.INVISIBLE);

                matchView.setOnClickListener(null);
                matchView.setClickable(false);

                matchView.setOnLongClickListener(null);
                matchView.setLongClickable(false);
            }
        }
    }
}
