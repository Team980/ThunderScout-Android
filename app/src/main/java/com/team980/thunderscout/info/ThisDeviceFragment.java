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

package com.team980.thunderscout.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.data.task.ScoutDataClearTask;
import com.team980.thunderscout.data.task.ScoutDataDeleteTask;
import com.team980.thunderscout.data.task.ScoutDataReadTask;
import com.team980.thunderscout.export.ExportActivity;
import com.team980.thunderscout.util.TransitionUtils;

import java.util.ArrayList;

public class ThisDeviceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnClickListener,
        View.OnClickListener {

    private RecyclerView dataView;
    private LocalDataAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

    private BroadcastReceiver refreshReceiver;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private boolean selectionMode = false;

    public static final String ACTION_REFRESH_VIEW_PAGER = "com.team980.thunderscout.info.REFRESH_VIEW_PAGER";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_this_device, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("This device");
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dataView = (RecyclerView) view.findViewById(R.id.dataView);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        dataView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dataView.getContext(),
                LinearLayout.VERTICAL);
        dataView.addItemDecoration(dividerItemDecoration);

        // specify an adapter (see also next example)
        adapter = new LocalDataAdapter(getContext(), new ArrayList<TeamWrapper>(), this);
        dataView.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(this);

        swipeContainer.setColorSchemeResources(R.color.accent);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

        ScoutDataReadTask query = new ScoutDataReadTask(adapter, getContext(), swipeContainer);
        query.execute();

        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ScoutDataReadTask query = new ScoutDataReadTask(adapter, getContext(), swipeContainer);
                query.execute();
            }
        };
    }

    // TODO convert to method used by TeleopFragment
    public void saveAdapterState(Bundle outState) { //
        adapter.onSaveInstanceState(outState);
    }

    public void restoreAdapterState(Bundle savedInstanceState) { //
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshReceiver, new IntentFilter(ACTION_REFRESH_VIEW_PAGER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_this_device, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Default mode
        if (id == R.id.action_delete_all && adapter.getItemCount() > 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete all scout data from local device?")
                    .setMessage("This cannot be undone!")
                    .setIcon(R.drawable.ic_warning_white_24dp)
                    .setPositiveButton("Delete", this)
                    .setNegativeButton("Cancel", null).show();
        }

        if (id == R.id.action_export) {
            Intent exportIntent = new Intent(getContext(), ExportActivity.class);
            startActivity(exportIntent);
        }

        if (id == R.id.action_sort) {

            AlertDialog sortDialog;

            // Creating and Building the Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Sort teams by...");

            builder.setSingleChoiceItems(TeamWrapper.TeamComparator.getFormattedList(), adapter.getCurrentSortMode().ordinal(),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            TeamWrapper.TeamComparator sortMode = TeamWrapper.TeamComparator.values()[item];

                            adapter.sort(sortMode);

                            dialog.dismiss();
                        }
                    });

            sortDialog = builder.create();
            sortDialog.show();
        }

        //Selection mode
        if (id == R.id.action_delete_selection) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete selected scout data from local device?")
                    .setMessage("This cannot be undone!")
                    .setIcon(R.drawable.ic_warning_white_24dp)
                    .setPositiveButton("Delete", this)
                    .setNegativeButton("Cancel", null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Refresh for SwipeRefreshLayout
     */
    @Override
    public void onRefresh() {
        ScoutDataReadTask query = new ScoutDataReadTask(adapter, getContext(), swipeContainer);
        query.execute();
    }

    /**
     * Alert dialog shown for deletion prompts
     */
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        if (selectionMode) {
            ScoutDataDeleteTask deleteTask = new ScoutDataDeleteTask(adapter, getContext(), adapter.getSelectedItems());
            deleteTask.execute();
        } else {
            ScoutDataClearTask clearTask = new ScoutDataClearTask(adapter, getContext());
            clearTask.execute();
        }
    }

    /**
     * Listener for HOME button when in selection mode
     */
    @Override
    public void onClick(View view) {
        if (selectionMode) {
            adapter.clearSelections();
            setSelectionMode(false);
        }
    }

    public boolean isInSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(boolean value) {
        selectionMode = value;

        if (selectionMode) {
            toolbar.setTitle("1 match selected");
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_data_select);
            TransitionUtils.toolbarAndStatusBarTransitionFromResources(R.color.primary, R.color.primary_dark,
                    R.color.secondary, R.color.secondary_dark, (AppCompatActivity) getActivity());

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();

            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

            toolbar.setNavigationOnClickListener(this);
        } else {
            toolbar.setTitle("This device");
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_this_device);
            TransitionUtils.toolbarAndStatusBarTransitionFromResources(R.color.secondary, R.color.secondary_dark,
                    R.color.primary, R.color.primary_dark, (AppCompatActivity) getActivity());

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle = new ActionBarDrawerToggle(
                    getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    public void updateSelectionModeTitle(int numItems) {
        if (selectionMode) {
            if (numItems == 1) {
                toolbar.setTitle("1 match selected");
            } else {
                toolbar.setTitle(numItems + " matches selected");
            }
        }
    }
}
