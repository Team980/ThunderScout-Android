/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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
