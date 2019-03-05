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

package com.team980.thunderscout.backend;

import android.content.Context;

import com.team980.thunderscout.backend.local.LocalStorageWrapper;

public enum AccountScope {
    LOCAL; //This device

    private static LocalStorageWrapper localStorageWrapper;

    /**
     * Singleton to fetch StorageWrappers from AccountScopes
     * Unless you are surfacing separate options for each AccountScope, using the other method is preferred.
     *
     * @param scope   the current AccountScope being used
     * @param context Android context
     * @return corresponding StorageWrapper instance
     */
    public static StorageWrapper getStorageWrapper(AccountScope scope, Context context) {
        switch (scope) {
            case LOCAL:
                if (localStorageWrapper == null) {
                    localStorageWrapper = new LocalStorageWrapper(context);
                }
                return localStorageWrapper;
            default:
                return null;
        }
    }

    /**
     * Singleton to fetch StorageWrappers from the current AccoutnScope
     *
     * @param context Android context
     * @return StorageWrapper instance for the currently selected scope
     */
    public static StorageWrapper getStorageWrapper(Context context) {
        return getStorageWrapper(LOCAL, context);
    }
}
