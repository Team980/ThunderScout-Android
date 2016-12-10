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
import android.support.v7.widget.PopupMenu;
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
import com.team980.thunderscout.util.TransitionUtils;

import java.util.ArrayList;

import static com.team980.thunderscout.info.TeamWrapper.TeamComparator.SORT_AUTO_TOTAL_GOALS_SCORED;
import static com.team980.thunderscout.info.TeamWrapper.TeamComparator.SORT_AUTO_UNIQUE_DEFENSE_COUNT;
import static com.team980.thunderscout.info.TeamWrapper.TeamComparator.SORT_SUMMARY_CHALLENGED_TOWER_PERCENTAGE;
import static com.team980.thunderscout.info.TeamWrapper.TeamComparator.SORT_SUMMARY_FULL_SCALE_PERCENTAGE;
import static com.team980.thunderscout.info.TeamWrapper.TeamComparator.SORT_TEAM_NUMBER;
import static com.team980.thunderscout.info.TeamWrapper.TeamComparator.SORT_TELEOP_TOTAL_GOALS_SCORED;
import static com.team980.thunderscout.info.TeamWrapper.TeamComparator.SORT_TELEOP_UNIQUE_DEFENSE_COUNT;

public class ThisDeviceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnClickListener,
        PopupMenu.OnMenuItemClickListener, View.OnClickListener {

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

    public void saveAdapterState(Bundle outState) { //TODO this isn't working right
        Log.d("FRAGSTATE", "saving adapter state");
        adapter.onSaveInstanceState(outState);
    }

    public void restoreAdapterState(Bundle savedInstanceState) { //TODO this isn't working
        Log.d("FRAGSTATE", "restoring adapter state");
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
                    .setTitle("Are you sure?")
                    .setMessage("This will delete all scout data in your local database and the data cannot be recovered!")
                    .setIcon(R.drawable.ic_warning_white_24dp)
                    .setPositiveButton(android.R.string.yes, this)
                    .setNegativeButton(android.R.string.no, null).show();
        }

        if (id == R.id.action_sort) {
            PopupMenu popup = new PopupMenu(getContext(), getView().findViewById(R.id.action_sort));
            popup.setOnMenuItemClickListener(this);

            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.sort_modes, popup.getMenu());
            popup.show();
        }

        //Selection mode
        if (id == R.id.action_delete_selection) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Are you sure?")
                    .setMessage("This will delete the selected data and cannot be undone!") //TODO better phrasing
                    .setIcon(R.drawable.ic_warning_white_24dp)
                    .setPositiveButton(android.R.string.yes, this)
                    .setNegativeButton(android.R.string.no, null).show();
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
     * Popup menu for sorting mode selection
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_team_number:
                adapter.sort(SORT_TEAM_NUMBER);
                return true;
            case R.id.sort_auto_unique_defense_count:
                adapter.sort(SORT_AUTO_UNIQUE_DEFENSE_COUNT);
                return true;
            case R.id.sort_auto_total_goals_scored:
                adapter.sort(SORT_AUTO_TOTAL_GOALS_SCORED);
                return true;
            case R.id.sort_teleop_unique_defense_count:
                adapter.sort(SORT_TELEOP_UNIQUE_DEFENSE_COUNT);
                return true;
            case R.id.sort_teleop_total_goals_scored:
                adapter.sort(SORT_TELEOP_TOTAL_GOALS_SCORED);
                return true;
            case R.id.sort_summary_full_scale_percentage:
                adapter.sort(SORT_SUMMARY_FULL_SCALE_PERCENTAGE);
                return true;
            case R.id.sort_summary_challenged_tower_percentage:
                adapter.sort(SORT_SUMMARY_CHALLENGED_TOWER_PERCENTAGE);
                return true;
            default:
                return false;
        }
    }

    /**
     *  Listener for HOME button when in selection mode
     */
    @Override
    public void onClick(View view) {
        if(selectionMode) {
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
        if(selectionMode) {
            if (numItems == 1) {
                toolbar.setTitle("1 match selected");
            } else {
                toolbar.setTitle(numItems + " matches selected");
            }
        }
    }

}
