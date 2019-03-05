/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.preferences.backport;

import android.content.Context;
import android.support.v7.preference.PreferenceViewHolder;
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
    public void onBindViewHolder(PreferenceViewHolder viewHolder) {
        super.onBindViewHolder(viewHolder);

        final View widgetView = viewHolder.findViewById(android.R.id.widget_frame);
        if (widgetView != null) {
            widgetView.setOnClickListener(v -> {
                if (mSwitch != null && !mSwitch.isEnabled()) {
                    return;
                }
                setChecked(!mChecked);
                if (!callChangeListener(mChecked)) {
                    setChecked(!mChecked);
                } else {
                    persistBoolean(mChecked);
                }
            });
        }

        mChecked = getPersistedBoolean(false);

        mSwitch = (SwitchCompat) viewHolder.findViewById(R.id.switchWidget);
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