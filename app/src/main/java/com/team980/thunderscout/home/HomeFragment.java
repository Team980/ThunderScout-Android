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

package com.team980.thunderscout.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.scouting_flow.ScoutingFlowActivity;

public class HomeFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener, View.OnLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView dataView;
    private FeedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    private FloatingActionButton scoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("ThunderScout");
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        dataView = view.findViewById(R.id.dataView);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        dataView.setLayoutManager(mLayoutManager);

        // specify an adapter
        adapter = new FeedAdapter(this);
        dataView.setAdapter(adapter);
        adapter.initCardList();

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(this);

        swipeContainer.setColorSchemeResources(R.color.accent);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

        scoutButton = view.findViewById(R.id.fab_scout);
        scoutButton.setOnClickListener(this);
        scoutButton.setOnLongClickListener(this);

        Boolean matchScout = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(getResources().getString(R.string.pref_enable_match_scouting), true);

        if (matchScout) {
            scoutButton.setVisibility(View.VISIBLE);
        } else {
            scoutButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_scout) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

            if (!prefs.getBoolean(getResources().getString(R.string.pref_enable_match_scouting), true)) {
                return;
            }

            Intent scoutIntent = new Intent(getContext(), ScoutingFlowActivity.class);
            startActivity(scoutIntent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        adapter.initCardList(); //TODO actually refresh the cards
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getResources().getString(R.string.pref_enable_match_scouting))) {
            Boolean matchScout = sharedPreferences.getBoolean(getResources().getString(R.string.pref_enable_match_scouting), false);

            if (matchScout) {
                scoutButton.setVisibility(View.VISIBLE);
            } else {
                scoutButton.setVisibility(View.GONE);
            }
        }
    }

    public boolean onLongClick(View view) {

        /*ArrayList<ScoutData> debug = new ArrayList<>();

        Random random = new Random();

        for (int i = 1; i < 101; i++) {

            for (int j = 0; j < 6; j++) {

                ScoutData data = new ScoutData();
                data.setTeam(String.valueOf(random.nextInt(70)));
                data.setMatchNumber(i);
                data.setSource(Build.MANUFACTURER + " " + Build.MODEL);

                data.setAllianceStation(AllianceStation.values()[j]);

                debug.add(data);

            }
        }

        AccountScope.getStorageWrapper(AccountScope.LOCAL, getContext()).writeData(debug, new StorageWrapper.StorageListener() {
            @Override
            public void onDataQuery(List<ScoutData> dataList) {

            }

            @Override
            public void onDataWrite(@Nullable List<ScoutData> dataWritten) {
                Toast.makeText(getContext(), "Written " + dataWritten.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataRemove(@Nullable List<ScoutData> dataRemoved) {

            }

            @Override
            public void onDataClear(boolean success) {

            }
        });

        Toast.makeText(getContext(), "Started " + debug.size(), Toast.LENGTH_SHORT).show();*/
        return true;
    }
}
