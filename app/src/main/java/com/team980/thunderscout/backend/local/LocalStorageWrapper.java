/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.backend.local;

import android.content.Context;
import android.support.annotation.Nullable;

import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.backend.local.task.ScoutDataClearTask;
import com.team980.thunderscout.backend.local.task.ScoutDataReadTask;
import com.team980.thunderscout.backend.local.task.ScoutDataRemoveTask;
import com.team980.thunderscout.backend.local.task.ScoutDataWriteTask;
import com.team980.thunderscout.schema.ScoutData;

import java.util.List;

// TODO this doesn't have to be a Singleton
public class LocalStorageWrapper implements StorageWrapper {

    private Context applicationContext;

    public LocalStorageWrapper(Context context) {
        applicationContext = context.getApplicationContext();
    }

    @Override
    public void queryData(@Nullable StorageListener listener) {
        ScoutDataReadTask readTask = new ScoutDataReadTask(listener, applicationContext);
        readTask.execute();
    }

    @Override
    public void writeData(ScoutData data, @Nullable StorageListener listener) {
        ScoutDataWriteTask writeTask = new ScoutDataWriteTask(listener, applicationContext);
        writeTask.execute(data);
    }

    @Override
    public void writeData(List<ScoutData> dataList, @Nullable StorageListener listener) {
        ScoutDataWriteTask writeTask = new ScoutDataWriteTask(listener, applicationContext);
        writeTask.execute((ScoutData[]) dataList.toArray(new ScoutData[dataList.size()]));
    }

    @Override
    public void removeData(ScoutData data, @Nullable StorageListener listener) {
        ScoutDataRemoveTask deleteTask = new ScoutDataRemoveTask(listener, applicationContext);
        deleteTask.execute(data);
    }

    @Override
    public void removeData(List<ScoutData> dataList, @Nullable StorageListener listener) {
        ScoutDataRemoveTask deleteTask = new ScoutDataRemoveTask(listener, applicationContext);
        deleteTask.execute((ScoutData[]) dataList.toArray(new ScoutData[dataList.size()]));
    }

    @Override
    public void clearAllData(@Nullable StorageListener listener) {
        ScoutDataClearTask clearTask = new ScoutDataClearTask(listener, applicationContext);
        clearTask.execute();
    }
}
