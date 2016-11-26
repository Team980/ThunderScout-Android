package com.team980.thunderscout.feed;

import com.team980.thunderscout.R;

/**
 * A wrapper for an operation
 * Required for ExpandableRecyclerView ;/
 */
public class EntryOperationWrapper {
    private EntryOperationType type;
    private EntryOperationStatus status;

    public EntryOperationWrapper(EntryOperationType t, EntryOperationStatus s) {
        type = t;
        status = s;
    }

    public EntryOperationType getType() {
        return type;
    }

    public EntryOperationStatus getStatus() {
        return status;
    }

    /**
     * The type of an operation contained in an entry
     */
    public enum EntryOperationType {
        SENT_TO_BLUETOOTH_SERVER(R.drawable.ic_bluetooth_searching_white_24dp), //sent via Bluetooth
        SAVED_TO_LOCAL_STORAGE(R.drawable.ic_save_white_24dp); //saved to local storage

        private int icon;

        EntryOperationType(int i) {
            icon = i;
        }

        public int getIcon() {
            return icon;
        }
    }

    /**
     * The status of an operation contained in an entry
     */
    public enum EntryOperationStatus {
        OPERATION_SUCCESSFUL, //success
        OPERATION_FAILED, //fail
        OPERATION_ABORTED; //user canceled operation (NYI)
    }
}
