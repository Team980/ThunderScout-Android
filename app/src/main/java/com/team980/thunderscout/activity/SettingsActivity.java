package com.team980.thunderscout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.team980.thunderscout.R;
import com.team980.thunderscout.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.contentPanel, new SettingsFragment())
                    .commit();
        }
    }
}
