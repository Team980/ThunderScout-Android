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

package com.team980.thunderscout.analytics.matches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.data.ScoutData;

import java.util.ArrayList;
import java.util.List;


public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> implements StorageWrapper.StorageListener {

    private LayoutInflater mInflator;

    //private List<MatchData> matchList;

    public MatchesAdapter(Context context) {
        mInflator = LayoutInflater.from(context);

        //matchList = new ArrayList<>();
    }

    @Override
    public MatchesAdapter.MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View matchView = mInflator.inflate(R.layout.match_view, parent, false);
        return new MatchViewHolder(matchView);
    }

    @Override
    public void onBindViewHolder(MatchesAdapter.MatchViewHolder holder, int position) {
        //MatchData match = (MatchData) parentListItem;
        //holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onDataQuery(List<ScoutData> dataList) {
        //TODO translate ScoutData to a proxy ("MatchWrapper")?
    }

    @Override
    public void onDataWrite(boolean success) {
        //do nothing
    }

    @Override
    public void onDataRemove(boolean success) {
        //do nothing
    }

    @Override
    public void onDataClear(boolean success) {
        //do nothing
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        public MatchViewHolder(View itemView) {
            super(itemView);
        }
    }
}
