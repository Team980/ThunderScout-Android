package com.team980.thunderscout.util;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.Rank;

public class RankedSliderCompoundView extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    protected LayoutInflater inflater;

    Rank rank;
    private int min;
    private int max;

    public RankedSliderCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_slider, this);

        rank = Rank.NOT_ATTEMPTED;

        min = 1;
        max = 3;

        AppCompatSeekBar s = (AppCompatSeekBar) findViewById(R.id.sliderVal);

        s.setMax(max - min);
        s.setProgress(rank.getId() - min);
        s.setOnSeekBarChangeListener(this);

        ((TextView) findViewById(R.id.min)).setText(String.valueOf(min));
        ((TextView) findViewById(R.id.max)).setText(String.valueOf(max));
        ((TextView) findViewById(R.id.value)).setText(rank.getDescription());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        rank = Rank.fromId(seekBar.getProgress() + min);
        ((TextView) findViewById(R.id.value)).setText(rank.getDescription());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        rank = Rank.fromId(seekBar.getProgress() + min);
        ((TextView) findViewById(R.id.value)).setText(rank.getDescription());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        rank = Rank.fromId(seekBar.getProgress() + min);
        ((TextView) findViewById(R.id.value)).setText(rank.getDescription());
    }

    public Rank getRankValue() {
        return rank;
    }
}
