/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.analytics.matches;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.matches.breakdown.MatchInfoActivity;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.schema.ScoutData;

import java.util.List;

public class MatchConflictAdapter extends RecyclerView.Adapter<MatchConflictAdapter.DataViewHolder> {

    private List<ScoutData> dataList;

    private MatchesAdapter parentAdapter;

    private Dialog parentDialog;

    private Context context;

    public MatchConflictAdapter(List<ScoutData> l, MatchesAdapter adapter, Dialog dialog, Context context) {
        dataList = l;
        parentAdapter = adapter;
        parentDialog = dialog;
        this.context = context;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.data_view, parent, false);

        return new DataViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int i) {
        ScoutData data = dataList.get(i);

        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView teamNumber;
        private TextView dataSource;
        //private TextView dateAdded;

        private ImageButton infoButton;
        private ImageButton deleteButton;

        public DataViewHolder(View itemView) {
            super(itemView);

            teamNumber = itemView.findViewById(R.id.team_number);
            dataSource = itemView.findViewById(R.id.data_source);
            //dateAdded = itemView.findViewById(R.id.date_added);

            infoButton = itemView.findViewById(R.id.button_info);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }

        public void bind(final ScoutData data) {
            teamNumber.setText("Team " + data.getTeam());
            dataSource.setText(data.getSource());
            //dateAdded.setText(SimpleDateFormat.getDateTimeInstance().format(data.getDate()));

            infoButton.setOnClickListener(v -> {
                Intent launchInfoActivity = new Intent(context, MatchInfoActivity.class);
                launchInfoActivity.putExtra(MatchInfoActivity.EXTRA_SCOUT_DATA, data);
                context.startActivity(launchInfoActivity);
            });

            deleteButton.setOnClickListener(v -> new AlertDialog.Builder(context)
                    .setTitle("Delete this match?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        AccountScope.getStorageWrapper(context).removeData(data, parentAdapter); //TODO use snackbar

                        dataList.remove(data);
                        notifyItemRemoved(getAdapterPosition());

                        if (dataList.size() <= 1) {
                            parentDialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", null).show());
        }
    }
}
