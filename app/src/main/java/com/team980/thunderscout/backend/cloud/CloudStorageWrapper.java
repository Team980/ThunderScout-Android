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

package com.team980.thunderscout.backend.cloud;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.WriteBatch;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.schema.ScoutData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CloudStorageWrapper implements StorageWrapper {

    private FirebaseFirestore db;

    public CloudStorageWrapper() {
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) //Enable offline caching
                .build();
        db.setFirestoreSettings(settings);
    }

    @Override
    public void queryData(@Nullable StorageListener listener) {
        List<ScoutData> dataList = new ArrayList<>();

        db.collection("data")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            ScoutData data = document.toObject(ScoutData.class); //TODO this crashes?
                            data.setId(document.getReference().getId()); //Firestore reference IDs are complicated
                            dataList.add(data);
                            Log.d(CloudStorageWrapper.this.getClass().getName(), "Document found: " + document.getId());
                        }
                        Log.d(CloudStorageWrapper.this.getClass().getName(), "Successfully got documents");
                    } else {
                        Log.d(CloudStorageWrapper.this.getClass().getName(), "Error getting documents", task.getException());
                    }
                    listener.onDataQuery(dataList);
                });
    }

    @Override
    public void writeData(ScoutData data, @Nullable StorageListener listener) {
        db.collection("data")
                .add(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(CloudStorageWrapper.this.getClass().getName(), "Successfully added documents");
                        listener.onDataWrite(Collections.singletonList(data));
                    } else {
                        Log.d(CloudStorageWrapper.this.getClass().getName(), "Error adding documents", task.getException());
                        listener.onDataWrite(null);
                    }
                });
    }

    @Override
    public void writeData(List<ScoutData> dataList, @Nullable StorageListener listener) {
        WriteBatch batch = db.batch();

        for (ScoutData data : dataList) {
            DocumentReference ref = db.collection("data").document();
            batch.set(ref, data);
        }

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(CloudStorageWrapper.this.getClass().getName(), "Successfully added documents");
                listener.onDataWrite(dataList);
            } else {
                Log.d(CloudStorageWrapper.this.getClass().getName(), "Error adding documents", task.getException());
                listener.onDataWrite(null);
            }
        });
    }

    @Override
    public void removeData(ScoutData data, @Nullable StorageListener listener) {
        db.collection("data")
                .document(data.getId())
                .delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(CloudStorageWrapper.this.getClass().getName(), "Successfully removed documents");
                listener.onDataRemove(Collections.singletonList(data));
            } else {
                Log.d(CloudStorageWrapper.this.getClass().getName(), "Error removing documents", task.getException());
                listener.onDataRemove(null);
            }
        });
    }

    @Override
    public void removeData(List<ScoutData> dataList, @Nullable StorageListener listener) {
        WriteBatch batch = db.batch();

        for (ScoutData data : dataList) {
            batch.delete(db.collection("data").document(data.getId()));
        }

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(CloudStorageWrapper.this.getClass().getName(), "Successfully removed documents");
                listener.onDataRemove(dataList);
            } else {
                Log.d(CloudStorageWrapper.this.getClass().getName(), "Error removing documents", task.getException());
                listener.onDataRemove(null);
            }
        });
    }

    @Override
    public void clearAllData(@Nullable StorageListener listener) {
        queryData(new StorageListener() {
            @Override
            public void onDataQuery(List<ScoutData> dataList) {
                removeData(dataList, new StorageListener() {
                    @Override
                    public void onDataRemove(@Nullable List<ScoutData> dataRemoved) {
                        listener.onDataClear(dataRemoved == dataList);
                    }
                });
            }
        });
    }
}
