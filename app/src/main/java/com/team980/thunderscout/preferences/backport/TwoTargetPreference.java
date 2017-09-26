package com.team980.thunderscout.preferences.backport;

import android.annotation.TargetApi;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.team980.thunderscout.R;

/**
 * Ported from Android Oreo's Settings.apk source code - TODO add license?
 * <p>
 * Implemented by MasterSwitchPreference.java
 */

public class TwoTargetPreference extends Preference {

    @TargetApi(value = 21)
    public TwoTargetPreference(Context context, AttributeSet attrs,
                               int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public TwoTargetPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TwoTargetPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoTargetPreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayoutResource(R.layout.preference_two_target);
        final int secondTargetResId = getSecondTargetResId();
        if (secondTargetResId != 0) {
            setWidgetLayoutResource(secondTargetResId);
        }
    }

    /*@Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final View divider = holder.findViewById(R.id.two_target_divider);
        final View widgetFrame = holder.findViewById(android.R.id.widget_frame);
        final boolean shouldHideSecondTarget = shouldHideSecondTarget();
        if (divider != null) {
            divider.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
        }
        if (widgetFrame != null) {
            widgetFrame.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
        }
    }*/

    protected boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }

    protected int getSecondTargetResId() {
        return 0;
    }
}