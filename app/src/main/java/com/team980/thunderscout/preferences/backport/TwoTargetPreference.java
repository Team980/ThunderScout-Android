package com.team980.thunderscout.preferences.backport;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.team980.thunderscout.R;

/**
 * Ported from Android Oreo's Settings.apk source code - TODO add license?
 * <p>
 * Implemented by MasterSwitchPreference.java
 */

public abstract class TwoTargetPreference extends Preference {

    public TwoTargetPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        setLayoutResource(R.layout.preference_two_target);
        final int secondTargetResId = getSecondTargetResId();
        if (secondTargetResId != 0) {
            setWidgetLayoutResource(secondTargetResId);
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View created = inflater.inflate(R.layout.preference_two_target, parent, false);

        if (getSecondTargetResId() != 0) {
            final LinearLayout widgetFrame = created.findViewById(android.R.id.widget_frame);
            inflater.inflate(getSecondTargetResId(), widgetFrame, true);
        }

        return created;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);

        final View divider = view.findViewById(R.id.two_target_divider);
        final View widgetFrame = view.findViewById(android.R.id.widget_frame);

        final boolean shouldHideSecondTarget = shouldHideSecondTarget();
        if (divider != null) {
            divider.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
        }
        if (widgetFrame != null) {
            widgetFrame.setVisibility(shouldHideSecondTarget ? View.GONE : View.VISIBLE);
        }
    }

    protected boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }

    protected int getSecondTargetResId() {
        return 0;
    }
}