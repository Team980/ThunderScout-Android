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

package com.team980.thunderscout.iexport;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team980.thunderscout.MainActivity;
import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.iexport.task.CSVImportTask;
import com.team980.thunderscout.schema.ScoutData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// NOTE: Selecting the file is actually what imports and converts the data
// Pressing "IMPORT" simply adds it to the DB
// This lets you pick what you want to keep
public class ImportActivity extends AppCompatActivity {

    private Uri fileUri;
    private ArrayList<ScoutData> dataToImport;

    private TextView fileInfo;
    private TextView selectionInfo;

    private Button selectButton;
    private Button importButton;

    private ProgressBar importProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        dataToImport = new ArrayList<>();

        fileInfo = findViewById(R.id.fileInfo);
        selectionInfo = findViewById(R.id.selectionInfo);

        selectButton = findViewById(R.id.button_select_file);
        importButton = findViewById(R.id.button_import);

        selectButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); //I wish this wasn't necessary
            Intent i = Intent.createChooser(intent, "Select a .csv file to import");
            startActivityForResult(i, 2);
        });

        //TODO implement data view

        importButton.setOnClickListener(v -> {
            selectButton.setEnabled(false);
            importButton.setEnabled(false);

            importProgress.setVisibility(View.VISIBLE);
            importProgress.setProgress(0);
            //TODO find a way to determine the maximum so this isn't indeterminate

            AccountScope.getStorageWrapper(AccountScope.LOCAL, this).writeData(dataToImport, new StorageWrapper.StorageListener() {
                @Override
                public void onDataWrite(@Nullable List<ScoutData> dataWritten) {
                    importProgress.setVisibility(View.GONE);
                    Toast.makeText(ImportActivity.this, "CSV import complete: " + dataWritten.size() + " matches added", Toast.LENGTH_SHORT).show();

                    finish();

                    Intent refreshIntent = new Intent().setAction(MainActivity.ACTION_REFRESH_DATA_VIEW);
                    LocalBroadcastManager.getInstance(ImportActivity.this).sendBroadcast(refreshIntent);
                }
            });
        });

        importProgress = findViewById(R.id.importProgress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    fileInfo.setText("File selected: " + new File(fileUri.getPath()).getName());
                    selectionInfo.setVisibility(View.VISIBLE);
                    selectionInfo.setText("Loading...");

                    importProgress.setVisibility(View.VISIBLE);
                    importProgress.setProgress(0);
                    //TODO find a way to determine the maximum so this isn't indeterminate

                    CSVImportTask task = new CSVImportTask(this);
                    task.execute(fileUri);

                    selectButton.setEnabled(false);
                    importButton.setEnabled(false);
                } else {
                    //Request permission
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1
                    );
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fileInfo.setText("File selected: " + new File(fileUri.getPath()).getName());
                selectionInfo.setVisibility(View.VISIBLE);
                selectionInfo.setText("Loading...");

                CSVImportTask task = new CSVImportTask(this);
                task.execute(fileUri);

                selectButton.setEnabled(false);
                importButton.setEnabled(false);
            } else {
                //Why would you ever deny the permission?
                Toast.makeText(this, "Please manually grant the permission", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 0);
            }
        }
    }

    public void onImportProgressUpdate(int matchesRead) {
        importProgress.setProgress(matchesRead);
    }

    public void onImportCompletion(List<ScoutData> dataList) {
        dataToImport.clear(); //TODO disable this once the data view is implemented
        dataToImport.addAll(dataList);

        selectButton.setEnabled(true);
        importButton.setEnabled(true);

        selectionInfo.setVisibility(View.VISIBLE);
        if (dataToImport.size() == 1) {
            selectionInfo.setText("1 match available to import");
        } else {
            selectionInfo.setText(dataToImport.size() + " matches available to import");
        }

        importProgress.setVisibility(View.GONE);

        //TODO implement data view
    }
}
