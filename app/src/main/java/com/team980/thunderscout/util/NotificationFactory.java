package com.team980.thunderscout.util;

/**
 * Stub until I decide to consolidate notification logic again
 */
public class NotificationFactory {

    private static int LAST_USED_ID = 0;

    public static int getNewId() {
        return ++LAST_USED_ID; //Increment by 1, then return that value
    }
}
