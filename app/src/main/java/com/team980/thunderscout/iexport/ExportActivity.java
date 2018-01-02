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
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team980.thunderscout.R;
import com.team980.thunderscout.backend.AccountScope;
import com.team980.thunderscout.backend.StorageWrapper;
import com.team980.thunderscout.iexport.task.CSVExportTask;
import com.team980.thunderscout.schema.ScoutData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    public static final String EXTRA_SELECTED_DATA = "com.team980.thunderscout.SELECTED_DATA";

    private ArrayList<ScoutData> dataToExport;
    private File exportedFile;

    private TextView selectionInfo;

    private Button exportButton;
    private Button openButton;
    private Button shareButton;

    private ProgressBar exportProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        selectionInfo = findViewById(R.id.selectionInfo);

        exportButton = findViewById(R.id.button_export);
        openButton = findViewById(R.id.button_open_folder);
        shareButton = findViewById(R.id.button_share);

        if (getIntent().hasExtra(EXTRA_SELECTED_DATA)) {
            //Export selected
            dataToExport = (ArrayList<ScoutData>) getIntent().getSerializableExtra(EXTRA_SELECTED_DATA);
            populateExportOptions();
        } else {
            //Export all
            dataToExport = new ArrayList<>();
            AccountScope.getStorageWrapper(AccountScope.LOCAL, this).queryData(new StorageWrapper.StorageListener() {
                @Override
                public void onDataQuery(List<ScoutData> dataList) {
                    dataToExport.addAll(dataList);
                    populateExportOptions();
                }
            });
        }

        exportButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                exportProgress.setVisibility(View.VISIBLE);
                exportProgress.setMax(dataToExport.size());

                CSVExportTask exportTask = new CSVExportTask(this);
                exportTask.execute(dataToExport.toArray(new ScoutData[dataToExport.size()]));
            } else {
                //Request permission
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1
                );
            }
        });

        openButton.setOnClickListener(v -> {
            Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/ThunderScout/");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(selectedUri, "resource/folder");

            if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No activities found to handle request", Toast.LENGTH_LONG).show();
            }
        });

        shareButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain"); //This is needed to force Bluetooth to show in the list
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, "com.team980.thunderscout.provider", exportedFile));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Share " + exportedFile.getName()));
            } else {
                Toast.makeText(this, "No activities found to handle request", Toast.LENGTH_LONG).show();
            }
        });

        exportProgress = findViewById(R.id.exportProgress);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportProgress.setVisibility(View.VISIBLE);
                exportProgress.setMax(dataToExport.size());

                CSVExportTask exportTask = new CSVExportTask(this);
                exportTask.execute(dataToExport.toArray(new ScoutData[dataToExport.size()]));
            } else {
                //Why would you ever deny the permission?
                Toast.makeText(this, "Please manually grant the permission", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 0);
            }
        }
    }

    private void populateExportOptions() {
        exportButton.setEnabled(true);
        if (dataToExport.size() == 1) {
            selectionInfo.setText("1 match available to export");
        } else {
            selectionInfo.setText(dataToExport.size() + " matches available to export");
        }
        //TODO RecyclerView population
    }

    public void onExportProgressUpdate(int matchesWritten) {
        exportProgress.setProgress(matchesWritten);
    }

    public void onExportCompletion(File exportedFile) {
        this.exportedFile = exportedFile;

        exportButton.setEnabled(false);
        shareButton.setEnabled(true);

        exportProgress.setVisibility(View.GONE);
    }
}
