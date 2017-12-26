package com.team980.thunderscout.preferences;

import android.content.Context;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.AttributeSet;

import com.team980.thunderscout.R;

public class SwitchBarPreference extends SwitchPreferenceCompat { //this is so simple I love it

    public SwitchBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutResource(R.layout.preference_switch_bar);

        setSummaryOn("On");
        setSummaryOff("Off");
    }
}
