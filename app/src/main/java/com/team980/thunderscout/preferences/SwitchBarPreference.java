package com.team980.thunderscout.preferences;

import android.content.Context;
import android.preference.TwoStatePreference;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.team980.thunderscout.R;

// Amalgam of Android source, Stack Overflow, and the other custom one
public class SwitchBarPreference extends TwoStatePreference {

    public SwitchBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.preference_switch_bar, parent, false);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        final SwitchCompat toggle = view.findViewById(R.id.switch_widget);
        final TextView status = view.findViewById(R.id.switch_text);

        toggle.setChecked(isChecked());

        if (isChecked()) {
            status.setText("On");
        } else {
            status.setText("Off");
        }

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!callChangeListener(isChecked)) {
                    // Listener didn't like it, change it back.
                    // CompoundButton will make sure we don't recurse.
                    buttonView.setChecked(!isChecked);
                    return;
                }

                if (isChecked) {
                    status.setText("On");
                } else {
                    status.setText("Off");
                }

                setChecked(isChecked);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle.setChecked(!isChecked());
            }
        });
    }
}
