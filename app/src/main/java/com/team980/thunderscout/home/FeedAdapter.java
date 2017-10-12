package com.team980.thunderscout.home;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.schema.ScoutData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private LayoutInflater mInflator;

    private HomeFragment fragment;

    private ArrayList<Card> cardList;

    public FeedAdapter(HomeFragment fragment) {
        mInflator = LayoutInflater.from(fragment.getContext());

        this.fragment = fragment;

        cardList = new ArrayList<>();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardType type = CardType.values()[viewType];

        View cardView = mInflator.inflate(type.getLayoutId(), parent, false);
        return type.createViewHolderInstance(cardView); //Switch statement that returns the proper class
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.bind(cardList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return cardList.get(position).getCardType().ordinal(); //Use the ordinal as the type
    }

    public void initCardList() {
        cardList.clear();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(fragment.getContext());

        AccountScope.getStorageWrapper(AccountScope.LOCAL, fragment.getContext()) //It already calls getApplicationContext
                .queryData(new StorageWrapper.StorageListener() {
                    @Override
                    public void onDataQuery(List<ScoutData> dataList) {
                        if (!dataList.isEmpty()) {
                            cardList.add(new Card(CardType.DEVICE_STORAGE_STATS));
                            Collections.sort(cardList);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onDataWrite(@Nullable List<ScoutData> dataWritten) {

                    }

                    @Override
                    public void onDataRemove(@Nullable List<ScoutData> dataRemoved) {

                    }

                    @Override
                    public void onDataClear(boolean success) {

                    }
                });

        if (sharedPrefs.getBoolean(fragment.getResources().getString(R.string.pref_enable_bluetooth_server), true)) {
            cardList.add(new Card(CardType.BLUETOOTH_SERVER_STATS));
        }

        Collections.sort(cardList);
        notifyDataSetChanged();
    }
}
