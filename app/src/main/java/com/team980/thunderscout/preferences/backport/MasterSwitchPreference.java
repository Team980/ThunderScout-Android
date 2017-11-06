package com.team980.thunderscout.preferences.backport;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;

import com.team980.thunderscout.R;

/**
 * Ported from Android Oreo's Settings.apk source code - TODO add license?
 * <p>
 * A custom preference that provides inline switch toggle. It has a mandatory field for title, and
 * optional fields for icon and sub-text.
 * <p>
 * TODO Doesn't work well... does it need AppCompatPreference?
 */
public class MasterSwitchPreference extends TwoTargetPreference {

    private SwitchCompat mSwitch;
    private boolean mChecked;
    private boolean mEnableSwitch = true;

    public MasterSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getSecondTargetResId() {
        return R.layout.preference_widget_master_switch;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);

        final View widgetView = view.findViewById(android.R.id.widget_frame);
        if (widgetView != null) {
            widgetView.setOnClickListener(new View.OnClickListener() {
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

        mChecked = getPersistedBoolean(false);

        mSwitch = view.findViewById(R.id.switchWidget);
        if (mSwitch != null) {
            mSwitch.setContentDescription(getTitle());
            mSwitch.setChecked(mChecked);
            mSwitch.setEnabled(mEnableSwitch);
        }
    }

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

    public SwitchCompat getSwitch() {
        return mSwitch;
    }
}