package com.team980.thunderscout.feed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.team980.thunderscout.feed.task.FeedDataClearTask;
import com.team980.thunderscout.feed.task.FeedDataReadTask;
import com.team980.thunderscout.match.ScoutingFlowActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnClickListener {

    private RecyclerView feed;
    private ActivityFeedAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

    private BroadcastReceiver refreshReceiver;

    public static final String ACTION_REFRESH_VIEW_PAGER = "com.team980.thunderscout.feed.REFRESH_VIEW_PAGER"; //conveniently different from the other one

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("ThunderScout");
        activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        feed = (RecyclerView) view.findViewById(R.id.feed);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        feed.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayout.VERTICAL);
        feed.addItemDecoration(dividerItemDecoration);

        // specify an adapter (see also next example)
        adapter = new ActivityFeedAdapter(new ArrayList<FeedEntry>());
        feed.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(this);

        swipeContainer.setColorSchemeResources(R.color.accent);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

        FeedDataReadTask query = new FeedDataReadTask(adapter, getContext(), swipeContainer);
        query.execute();

        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("PINGTEST", "I hear you");

                FeedDataReadTask query = new FeedDataReadTask(adapter, getContext(), swipeContainer);
                query.execute();
            }
        };

        FloatingActionButton b = (FloatingActionButton) view.findViewById(R.id.fab_scout);
        b.setOnClickListener(this);
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

        Log.d("PINGTEST", "unregister");
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("PINGTEST", "register");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshReceiver, new IntentFilter(ACTION_REFRESH_VIEW_PAGER));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_scout) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

            if (!prefs.getBoolean("enable_match_scout", true)) { //Saving locally
                return; //TODO hide the button
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

        if (id == R.id.action_clear && adapter.getItemCount() > 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Are you sure?")
                    .setMessage("This will remove all feed entries from your local database and the entries cannot be recovered!")
                    .setIcon(R.drawable.ic_warning_white_24dp)
                    .setPositiveButton(android.R.string.yes, this)
                    .setNegativeButton(android.R.string.no, null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * SwipeRefreshLayout
     */
    @Override
    public void onRefresh() {
        FeedDataReadTask query = new FeedDataReadTask(adapter, getContext(), swipeContainer);
        query.execute();
    }

    /**
     * Alert dialog shown for deletion prompts
     */
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        FeedDataClearTask clearTask = new FeedDataClearTask(adapter, getContext());
        clearTask.execute();

    }
}
