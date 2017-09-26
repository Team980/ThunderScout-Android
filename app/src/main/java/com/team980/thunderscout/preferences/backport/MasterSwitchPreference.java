package com.team980.thunderscout.preferences.backport;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

import com.team980.thunderscout.R;

/**
 * Ported from Android Oreo's Settings.apk source code - TODO add license?
 * <p>
 * A custom preference that provides inline switch toggle. It has a mandatory field for title, and
 * optional fields for icon and sub-text.
 * <p>
 * TODO will require AppCompat - Preference v7 to work...
 */
public class MasterSwitchPreference extends TwoTargetPreference {

    private Switch mSwitch;
    private boolean mChecked;
    private boolean mEnableSwitch = true;

    public MasterSwitchPreference(Context context, AttributeSet attrs,
                                  int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MasterSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MasterSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MasterSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected int getSecondTargetResId() {
        return R.layout.preference_widget_master_switch;
    }

    /*@Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final View widgetView = holder.findViewById(android.R.id.widget_frame);
        if (widgetView != null) {
            widgetView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSwitch != null && !mSwitch.isEnabled()) {
                        return;
                    }
                    setChecked(!mChecked);
                    if (!callChangeListener(mChecked)) {
                        setChecked(!mChecked);
                    } else {
                        persistBoolean(mChecked);
                    }
                }
            });
        }

        mSwitch = (Switch) holder.findViewById(R.id.switchWidget);
        if (mSwitch != null) {
            mSwitch.setContentDescription(getTitle());
            mSwitch.setChecked(mChecked);
            mSwitch.setEnabled(mEnableSwitch);
        }
    }*/

    public boolean isChecked() {
        return mSwitch != null && mSwitch.isEnabled() && mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        if (mSwitch != null) {
            mSwitch.setChecked(checked);
        }
    }

    public void setSwitchEnabled(boolean enabled) {
        mEnableSwitch = enabled;
        if (mSwitch != null) {
            mSwitch.setEnabled(enabled);
        }
    }

    public Switch getSwitch() {
        return mSwitch;
    }
}