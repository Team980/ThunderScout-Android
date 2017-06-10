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

package com.team980.thunderscout.legacy.feed;

import com.team980.thunderscout.R;
import com.team980.thunderscout.scouting_flow.ScoutingFlowActivity;

import java.io.Serializable;

/**
 * A wrapper for an operation
 * Required for ExpandableRecyclerView ;/
 */
public class EntryOperationWrapper implements Serializable {
    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1; //should never need to change this
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
        SAVED_TO_LOCAL_STORAGE("Data saved to local storage", R.drawable.ic_save_white_24dp), //saved to local storage
        SENT_TO_BLUETOOTH_SERVER("Data sent to Bluetooth server", R.drawable.ic_bluetooth_searching_white_24dp); //sent via Bluetooth server

        private String name;
        private int icon;

        EntryOperationType(String s, int i) {
            name = s;
            icon = i;
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

        @Override
        public String toString() {
            return name;
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
