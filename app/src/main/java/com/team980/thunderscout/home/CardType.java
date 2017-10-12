package com.team980.thunderscout.home;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.team980.thunderscout.R;
import com.team980.thunderscout.home.card_views.BluetoothServerViewHolder;
import com.team980.thunderscout.home.card_views.DeviceStorageViewHolder;
import com.team980.thunderscout.home.card_views.PendingMatchesViewHolder;
import com.team980.thunderscout.home.card_views.RecentMatchesViewHolder;
import com.team980.thunderscout.home.card_views.UnfinishedMatchesViewHolder;

public enum CardType {
    //WELCOME(R.layout.card_pending_matches), TODO First run card that explains the basics
    PENDING_MATCHES(R.layout.card_pending_matches), //Matches currently being processed by the scouting flow or just failed to be sent via Bluetooth
    UNFINISHED_MATCHES(R.layout.card_unfinished_matches), //Matches that were saved upon exiting the scouting flow
    DEVICE_STORAGE_STATS(R.layout.card_device_storage),
    RECENT_MATCHES(R.layout.card_recent_matches), //Matches that were recently scouted, and what happened to them
    BLUETOOTH_SERVER_STATS(R.layout.card_bluetooth_server); //Also includes recently received matches and what happened to them

    @LayoutRes
    private int layoutId;

    CardType(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @LayoutRes
    public int getLayoutId() {
        return layoutId;
    }

    public CardViewHolder createViewHolderInstance(View itemView) {
        switch (this) {
            case PENDING_MATCHES:
                return new PendingMatchesViewHolder(itemView);
            case UNFINISHED_MATCHES:
                return new UnfinishedMatchesViewHolder(itemView);
            case DEVICE_STORAGE_STATS:
                return new DeviceStorageViewHolder(itemView);
            case RECENT_MATCHES:
                return new RecentMatchesViewHolder(itemView);
            case BLUETOOTH_SERVER_STATS:
                return new BluetoothServerViewHolder(itemView);
            default:
                throw new UnsupportedOperationException("ViewHolder not implemented for CardType " + this.name());
        }
    }
}
