package com.team980.thunderscout.feed;

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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.match.ScoutingFlowActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView feed;
    private ActivityFeedAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

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

        //TODO use real entries
        adapter.addFeedEntry(new FeedEntry(FeedEntry.EntryType.MATCH_SCOUTED, 14222424)
                .addOperation(new EntryOperationWrapper(EntryOperationWrapper.EntryOperationType.SENT_TO_BLUETOOTH_SERVER, EntryOperationWrapper.EntryOperationStatus.OPERATION_SUCCESSFUL)));
        adapter.addFeedEntry(new FeedEntry(FeedEntry.EntryType.SERVER_RECEIVED_MATCH, 335656322)
                .addOperation(new EntryOperationWrapper(EntryOperationWrapper.EntryOperationType.SAVED_TO_LOCAL_STORAGE, EntryOperationWrapper.EntryOperationStatus.OPERATION_FAILED))
                .addOperation(new EntryOperationWrapper(EntryOperationWrapper.EntryOperationType.SENT_TO_BLUETOOTH_SERVER, EntryOperationWrapper.EntryOperationStatus.OPERATION_ABORTED)));
        adapter.addFeedEntry(new FeedEntry(FeedEntry.EntryType.MATCH_SCOUTED, 232366772)
                .addOperation(new EntryOperationWrapper(EntryOperationWrapper.EntryOperationType.SAVED_TO_LOCAL_STORAGE, EntryOperationWrapper.EntryOperationStatus.OPERATION_SUCCESSFUL))
                .addOperation(new EntryOperationWrapper(EntryOperationWrapper.EntryOperationType.SENT_TO_BLUETOOTH_SERVER, EntryOperationWrapper.EntryOperationStatus.OPERATION_SUCCESSFUL)));

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(this);

        swipeContainer.setColorSchemeResources(R.color.accent);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

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
    public void onClick(View view) {
        if (view.getId() == R.id.fab_scout) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

            if (!prefs.getBoolean("enable_match_scout", true)) { //Saving locally
                return; //TODO prompt
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

    /**
     * SwipeRefreshLayout
     */
    @Override
    public void onRefresh() {
        //TODO refresh the view
        swipeContainer.setRefreshing(false);
    }
}
