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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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
import com.team980.thunderscout.data.TeamWrapper;
import com.team980.thunderscout.data.task.DatabaseClearTask;
import com.team980.thunderscout.data.task.DatabaseReadTask;

import java.util.ArrayList;

import static com.team980.thunderscout.data.TeamWrapper.TeamComparator.SORT_TEAM_NUMBER;

public class LocalStorageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnClickListener,
        PopupMenu.OnMenuItemClickListener {

    private RecyclerView dataView;
    private DataViewAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

    private BroadcastReceiver refreshReceiver;

    public static final String ACTION_REFRESH_VIEW_PAGER = "com.team980.thunderscout.REFRESH_VIEW_PAGER";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_storage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Local storage");
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dataView = (RecyclerView) view.findViewById(R.id.dataView);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        dataView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new DataViewAdapter(getContext(), new ArrayList<TeamWrapper>());
        dataView.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(this);

        swipeContainer.setColorSchemeResources(R.color.primary);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

        DatabaseReadTask query = new DatabaseReadTask(adapter, getContext(), swipeContainer);
        query.execute();

        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                DatabaseReadTask query = new DatabaseReadTask(adapter, getContext(), swipeContainer);
                query.execute();
            }
        };
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
        inflater.inflate(R.menu.menu_local_storage, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete && adapter.getItemCount() > 0) {
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        DatabaseReadTask query = new DatabaseReadTask(adapter, getContext(), swipeContainer);
        query.execute();
    }

    /**
     * Alert dialog shown for deletion prompt
     */
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        DatabaseClearTask clearTask = new DatabaseClearTask(adapter, getContext());
        clearTask.execute();
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
            default:
                return false;
        }
    }
}
