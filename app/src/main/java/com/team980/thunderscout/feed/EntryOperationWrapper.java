package com.team980.thunderscout.feed;

import com.team980.thunderscout.R;
import com.team980.thunderscout.match.ScoutingFlowActivity;

import java.io.Serializable;

/**
 * A wrapper for an operation
 * Required for ExpandableRecyclerView ;/
 */
public class EntryOperationWrapper implements Serializable {
    private EntryOperationType type;
    private EntryOperationStatus status;

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1; //should never need to change this

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
        SAVED_TO_LOCAL_STORAGE("Data saved to local storage", R.drawable.ic_save_white_24dp), //saved to local storage
        SENT_TO_BLUETOOTH_SERVER("Data sent to Bluetooth server", R.drawable.ic_bluetooth_searching_white_24dp); //sent via Bluetooth server

        private String name;
        private int icon;

        EntryOperationType(String s, int i) {
            name = s;
            icon = i;
        }

        @Override
        public String toString() {
            return name;
        }

        public static EntryOperationType fromOperationId(String operationId) {
            switch (operationId) {
                case ScoutingFlowActivity.OPERATION_SAVE_THIS_DEVICE:
                    return EntryOperationType.SAVED_TO_LOCAL_STORAGE;
                case ScoutingFlowActivity.OPERATION_SEND_BLUETOOTH:
                    return EntryOperationType.SENT_TO_BLUETOOTH_SERVER;
                default:
                    return null;
            }
        }

        public int getIcon() {
            return icon;
        }
    }

    /**
     * The status of an operation contained in an entry
     */
    public enum EntryOperationStatus {
        OPERATION_SUCCESSFUL("Operation successful"), //success
        OPERATION_FAILED("Operation failed"), //fail
        OPERATION_ABORTED("Operation failed and aborted by user"); //user cancelled operation after failure

        private String name;

        EntryOperationStatus(String s) {
            name = s;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
