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

package com.team980.thunderscout.backend;

import android.support.annotation.Nullable;

import com.team980.thunderscout.data.ScoutData;

import java.util.List;

/**
 * Encapsulates different methods of storage into one framework.
 */
public interface StorageWrapper { //TODO refine this

    /**
     * Runs a query to fetch the data
     * @param listener Class extending StorageListener to return the data to
     */
    void queryData(@Nullable StorageListener listener);

    void writeData(ScoutData data, @Nullable StorageListener listener);

    void writeData(List<ScoutData> dataList, @Nullable StorageListener listener);

    void removeData(ScoutData data, @Nullable StorageListener listener);

    void removeData(List<ScoutData> dataList, @Nullable StorageListener listener);

    void clearAllData(@Nullable StorageListener listener);

    interface StorageListener {
        void onDataQuery(List<ScoutData> dataList);

        void onDataWrite(boolean success);

        void onDataRemove(boolean success);

        void onDataClear(boolean success);
    }
}
