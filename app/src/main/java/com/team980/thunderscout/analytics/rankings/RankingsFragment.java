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

package com.team980.thunderscout.analytics.rankings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.analytics.TeamComparator;
import com.team980.thunderscout.analytics.TeamWrapper;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.iexport.ExportActivity;
import com.team980.thunderscout.iexport.ImportActivity;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.util.TransitionUtils;

import java.util.ArrayList;

public class RankingsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DialogInterface.OnClickListener, SearchView.OnQueryTextListener, View.OnClickListener, SearchView.OnCloseListener, MainActivity.BackPressListener {

    //Instance state parameters
    private static final String KEY_SELECTION_MODE = "selection_mode";
    private static final String KEY_BOTTOM_SHEET_STATE = "bottom_sheet_state";
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private RecyclerView dataView;
    private RankingsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private BottomSheetBehavior compareSheetBehavior;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Rankings");
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
        swipeContainer.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);

        compareSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_compare));
        compareSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        compareSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) { //Tint status bar when fully expanded
                if (newState == BottomSheetBehavior.STATE_HIDDEN && adapter.getSelectedItemCount() == 3) { //I wish there was a better way to check for a user initiated hide, but there isn't
                    //Explicitly hiding the sheet should cause selection mode to close
                    adapter.clearSelections();
                    setSelectionMode(false);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
                    }
                    Toolbar sheetToolbar = bottomSheet.findViewById(R.id.toolbar);
                    sheetToolbar.setNavigationIcon(R.drawable.ic_expand_more_white_24dp);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.secondary_dark));
                    }
                    Toolbar sheetToolbar = bottomSheet.findViewById(R.id.toolbar);
                    sheetToolbar.setNavigationIcon(R.drawable.ic_expand_less_white_24dp);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //do nothing
            }
        });

        if (savedInstanceState != null) {
            setSelectionMode(savedInstanceState.getBoolean(KEY_SELECTION_MODE, false));
            compareSheetBehavior.setState(savedInstanceState.getInt(KEY_BOTTOM_SHEET_STATE, BottomSheetBehavior.STATE_HIDDEN));
            adapter.onRestoreInstanceState(savedInstanceState);

            if (adapter.getSelectedItemCount() == 3) { //Three selected items? The bottom sheet should be bound and showing
                CompareBottomSheetBinding.bindBottomSheet(getView().findViewById(R.id.bottom_sheet_compare),
                        compareSheetBehavior, adapter.getSelectedItems()); //TODO this is VERY bad
            }
        } else {
            AccountScope.getStorageWrapper(AccountScope.LOCAL, getContext()).queryData(adapter);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (compareSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            compareSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_SELECTION_MODE, selectionMode);
        outState.putInt(KEY_BOTTOM_SHEET_STATE, compareSheetBehavior.getState());
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

            sortDialog = builder.create();
            sortDialog.show();
        }

        if (id == R.id.action_compare) {
            setSelectionMode(true);
            toolbar.setTitle("Select teams to compare");
            Toast.makeText(getContext(), "You can also long press a team to select it", Toast.LENGTH_LONG).show();
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
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

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

            compareSheetBehavior.setHideable(true);
            compareSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void updateSelectionModeContext(int numItems) {
        if (selectionMode) {
            if (numItems == 1) {
                toolbar.setTitle("1 team selected");
            } else {
                toolbar.setTitle(numItems + " teams selected");
            }

            if (numItems == 3) {
                CompareBottomSheetBinding.bindBottomSheet(getView().findViewById(R.id.bottom_sheet_compare),
                        compareSheetBehavior, adapter.getSelectedItems()); //TODO this is VERY bad

                compareSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                compareSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }

    @Override
    public void onRefresh() { //SwipeRefreshLayout
        AccountScope.getStorageWrapper(AccountScope.LOCAL, getContext()).queryData(adapter);
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeContainer;
    }

    @Override //Deletion dialog
    public void onClick(DialogInterface dialog, int which) { //TODO modular account scopes - CLOUD should prompt for password
        if (selectionMode) {
            ArrayList<ScoutData> dataToRemove = new ArrayList<>();
            for (TeamWrapper wrapper : adapter.getSelectedItems()) {
                dataToRemove.addAll(wrapper.getDataList());
            }
            AccountScope.getStorageWrapper(AccountScope.LOCAL, getContext()).removeData(dataToRemove, adapter);
            adapter.clearSelections();
        } else {
            AccountScope.getStorageWrapper(AccountScope.LOCAL, getContext()).clearAllData(adapter);
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
