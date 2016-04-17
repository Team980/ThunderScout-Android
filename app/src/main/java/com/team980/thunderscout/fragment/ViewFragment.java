package com.team980.thunderscout.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team980.thunderscout.R;
import com.team980.thunderscout.adapter.DataViewAdapter;
import com.team980.thunderscout.data.object.TeamWrapper;
import com.team980.thunderscout.task.DatabaseReadTask;

import java.util.ArrayList;

public class ViewFragment extends Fragment {

    private DataViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        RecyclerView dataView = (RecyclerView) view.findViewById(R.id.dataView);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        dataView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new DataViewAdapter(getActivity(), new ArrayList<TeamWrapper>());
        dataView.setAdapter(adapter);

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseReadTask query = new DatabaseReadTask(adapter, getContext(), swipeContainer);
                query.execute();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

        DatabaseReadTask query = new DatabaseReadTask(adapter, getContext(), swipeContainer);
        query.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            //TODO confirmation dialog

            //TODO launch query instead of directly calling clearData()

            //TODO find a way to trigger a full refresh of the adapter
        }

        return super.onOptionsItemSelected(item);
    }

}
