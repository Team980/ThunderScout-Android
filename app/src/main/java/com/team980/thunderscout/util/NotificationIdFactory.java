package com.team980.thunderscout.util;

/**
 * Used to create unique IDs for notifications, among other things. Android loves unique IDs...
 * I don't love unique IDs but that's another story
 */
public class NotificationIdFactory {

    private static int LAST_USED_NOTIFICATION_ID = 0;
    private static int LAST_USED_REQUEST_CODE = 0;

    /**
     * Used for notification IDs
     */
    public static int getNewNotificationId() {
        return ++LAST_USED_NOTIFICATION_ID; //Increment by 1, then return that value
    }

    /**
     * Used for PendingIntent request codes - also related to notifications
     */
    public static int getNewRequestCode() {
        return ++LAST_USED_REQUEST_CODE; //Increment by 1, then return that value
    }
}
