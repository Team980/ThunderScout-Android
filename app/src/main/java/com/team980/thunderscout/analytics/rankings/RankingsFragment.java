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

package com.team980.thunderscout.analytics.rankings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.TeamComparator;
import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.bluetooth.ClientConnectionTask;
import com.team980.thunderscout.bluetooth.util.BluetoothDeviceManager;
import com.team980.thunderscout.iexport.ExportActivity;
import com.team980.thunderscout.iexport.ImportActivity;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.util.TransitionUtils;

import java.util.ArrayList;

public class RankingsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnClickListener, SearchView.OnQueryTextListener, View.OnClickListener, SearchView.OnCloseListener, MainActivity.BackPressListener {

    //Instance state parameters
    private static final String KEY_SELECTION_MODE = "selection_mode";

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private RecyclerView dataView;
    private RankingsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    private BroadcastReceiver receiver;

    private boolean selectionMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rankings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        drawer = getActivity().findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setHasOptionsMenu(true);

        dataView = view.findViewById(R.id.dataView);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        dataView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dataView.getContext(),
                LinearLayout.VERTICAL);
        dataView.addItemDecoration(dividerItemDecoration);

        // specify an adapter
        adapter = new RankingsAdapter(this);
        dataView.setAdapter(adapter);

        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.accent);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.background_card);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onRefresh();
            }
        };

        if (savedInstanceState != null) {
            setSelectionMode(savedInstanceState.getBoolean(KEY_SELECTION_MODE, false));
            adapter.onRestoreInstanceState(savedInstanceState);
        } else {
            AccountScope.getStorageWrapper(getContext()).queryData(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(MainActivity.ACTION_REFRESH_DATA_VIEW));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public boolean onBackPressed() {
        if (selectionMode) {
            adapter.clearSelections();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_SELECTION_MODE, selectionMode);
        adapter.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (selectionMode) {
            inflater.inflate(R.menu.menu_rank_selection, menu);
        } else {
            inflater.inflate(R.menu.menu_rank_tools, menu);

            SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
            searchView.setOnSearchClickListener(this);
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(this);
            searchView.setQueryHint("Search for team...");
            searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        //Default mode
        if (id == R.id.action_sort) {
            AlertDialog sortDialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Sort teams by...");

            builder.setSingleChoiceItems(TeamComparator.getFormattedList(), adapter.getCurrentSortMode().ordinal(),
                    (dialog, item1) -> {
                        TeamComparator sortMode = TeamComparator.values()[item1];

                        adapter.sort(sortMode);

                        dialog.dismiss();
                    });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
            });

            sortDialog = builder.create();
            sortDialog.show();
        }

        if (id == R.id.action_import) {
            Intent importIntent = new Intent(getContext(), ImportActivity.class);
            startActivity(importIntent);
            return true;
        }

        if (id == R.id.action_export) {
            Intent exportIntent = new Intent(getContext(), ExportActivity.class);
            startActivity(exportIntent);
            return true;
        }

        if (id == R.id.action_delete_all && adapter.getItemCount() > 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete all data?")
                    .setPositiveButton("Delete", this)
                    .setNegativeButton("Cancel", null).show();
            return true;
        }


        //Selection mode
        if (id == R.id.action_export_selection) {
            Intent exportIntent = new Intent(getContext(), ExportActivity.class);
            ArrayList<ScoutData> dataList = new ArrayList<>();
            for (TeamWrapper wrapper : adapter.getSelectedItems()) {
                dataList.addAll(wrapper.getDataList());
            }
            exportIntent.putExtra(ExportActivity.EXTRA_SELECTED_DATA, dataList);
            startActivity(exportIntent);
            return true;
        }

        if (id == R.id.action_bluetooth_transfer) {
            BluetoothDeviceManager bdm = new BluetoothDeviceManager(getContext());
            bdm.pickDevice(device -> {
                if (device == null) {
                    Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedMatches = 0;
                for (TeamWrapper team : adapter.getSelectedItems()) {
                    selectedMatches += team.getDataList().size();
                }

                Toast.makeText(getContext(), "Sending " + selectedMatches + " matches to " + device.getName(), Toast.LENGTH_LONG).show();

                for (TeamWrapper team : adapter.getSelectedItems()) {
                    for (ScoutData data : team.getDataList()) {
                        ClientConnectionTask connectionTask = new ClientConnectionTask(device, data, getContext());
                        connectionTask.execute();
                    }
                }
            });
        }

        if (id == R.id.action_delete_selection) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete selected teams?")
                    .setPositiveButton("Delete", this)
                    .setNegativeButton("Cancel", null).show();
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_search) {
            swipeContainer.setEnabled(false);
        }
    }

    public boolean isInSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(boolean value) {
        selectionMode = value;

        if (selectionMode) {
            toolbar.setTitle("1 team selected");
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_rank_selection);
            TransitionUtils.toolbarAndStatusBarTransitionFromResources(R.color.primary, R.color.primary_dark,
                    R.color.secondary, R.color.secondary_dark, (AppCompatActivity) getActivity());

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();

            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);

            toolbar.setNavigationOnClickListener(v -> {
                if (selectionMode) {
                    adapter.clearSelections();
                    setSelectionMode(false);
                }
            });

            swipeContainer.setEnabled(false);
        } else {
            toolbar.setTitle("Rankings");
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_rank_tools);

            SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
            searchView.setOnSearchClickListener(this);
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(this);
            searchView.setQueryHint("Search for team...");
            searchView.setInputType(InputType.TYPE_CLASS_NUMBER);

            TransitionUtils.toolbarAndStatusBarTransitionFromResources(R.color.secondary, R.color.secondary_dark,
                    R.color.primary, R.color.primary_dark, (AppCompatActivity) getActivity());

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle = new ActionBarDrawerToggle(
                    getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            swipeContainer.setEnabled(true);
        }
    }

    public void updateSelectionModeContext(int numItems) {
        if (selectionMode) {
            if (numItems == 1) {
                toolbar.setTitle("1 team selected");
            } else {
                toolbar.setTitle(numItems + " teams selected");
            }
        }
    }

    @Override
    public void onRefresh() { //SwipeRefreshLayout
        AccountScope.getStorageWrapper(getContext()).queryData(adapter);
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeContainer;
    }

    @Override //Deletion dialog
    public void onClick(DialogInterface dialog, int which) {
        if (selectionMode) {
            ArrayList<ScoutData> dataToRemove = new ArrayList<>();
            for (TeamWrapper wrapper : adapter.getSelectedItems()) {
                dataToRemove.addAll(wrapper.getDataList());
            }
            AccountScope.getStorageWrapper(getContext()).removeData(dataToRemove, adapter);
            adapter.clearSelections();
        } else {
            AccountScope.getStorageWrapper(getContext()).clearAllData(adapter);
        }
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.filterByTeam(query);
        dataView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onClose() { //SearchView
        adapter.filterByTeam("");

        swipeContainer.setEnabled(true);
        return false;
    }
}
