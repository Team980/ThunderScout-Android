package com.team980.thunderscout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.team980.thunderscout.R;
import com.team980.thunderscout.adapter.DataViewAdapter;
import com.team980.thunderscout.data.object.TeamWrapper;
import com.team980.thunderscout.task.DatabaseReadTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private DataViewAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("ThunderScout");

        RecyclerView dataView = (RecyclerView) findViewById(R.id.dataView);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        dataView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter = new DataViewAdapter(this, new ArrayList<TeamWrapper>());
        dataView.setAdapter(adapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(this);

        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

        DatabaseReadTask query = new DatabaseReadTask(adapter, this, swipeContainer);
        query.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
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

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the scout button is pressed.
     */
    public void onButtonPressed(View v) {
        if (v.getId() == R.id.button_scout) {
            Intent scoutIntent = new Intent(this, ScoutActivity.class);
            startActivity(scoutIntent);
        }
    }

    /**
     * SwipeRefreshLayout
     */
    @Override
    public void onRefresh() {
        DatabaseReadTask query = new DatabaseReadTask(adapter, this, swipeContainer);
        query.execute();
    }

}
