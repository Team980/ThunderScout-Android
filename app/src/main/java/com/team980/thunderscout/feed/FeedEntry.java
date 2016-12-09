package com.team980.thunderscout.feed;

import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.team980.thunderscout.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * One entry in the home screen's activity feed.
 *
 * Each entry has ONE EntryType representing the initiating action.
 * It can then have multiple EntryOperationWrappers representing what was done during the action.
 * Each EntryOperationWrapper has a corresponding EntryOperationStatus representing the result of the operation.
 */
public class FeedEntry implements ParentListItem, Comparable<FeedEntry> {

    private EntryType type;
    private Date timestamp;

    private List<EntryOperationWrapper> operations;

    public FeedEntry(EntryType t, long date) {
        type = t;
        timestamp = new Date(date);

        operations = new ArrayList<>();
    }

    public EntryType getType(){
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }


    @Override
    public List<EntryOperationWrapper> getChildItemList() {
        return operations;
    }

    /**
     * Allows chaining calls
     */
    public FeedEntry addOperation(EntryOperationWrapper wrapper) {
        operations.add(wrapper);
        return this;
    }

    @Override
    public int compareTo(@NonNull FeedEntry other) {
        return -getTimestamp().compareTo(other.getTimestamp());
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public boolean containsFailure() {
        for (EntryOperationWrapper operation : operations) {
            if (operation.getStatus() != EntryOperationWrapper.EntryOperationStatus.OPERATION_SUCCESSFUL) {
                return true;
            }
        }

        return false; //no failures found
    }

    /**
     * Type of entry
     */
    public enum EntryType { //TODO @string values
        MATCH_SCOUTED("Match scouted", R.drawable.ic_send_white_24dp), //client scouts match
        SERVER_RECEIVED_MATCH("Data received from server", R.drawable.ic_bluetooth_searching_white_24dp); //server receives match via bluetooth

        private String name;
        private int icon;

        EntryType(String s, int i) {
            name = s;
            icon = i;
        }

        @Override
        public String toString() {
            return name;
        }

        public int getIcon() {
            return icon;
        }
    }
}
