package com.team980.thunderscout.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.Rank;

public class RankedCounterCompoundView extends FrameLayout implements View.OnClickListener {

    protected int max;
    protected int min;
    protected int count;
    private LayoutInflater inflater;
    private Rank rank;

    public RankedCounterCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_counter, this);

        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.minus).setOnClickListener(this);

        rank = Rank.FAILED;

        max = 3;
        min = 0;
        count = 1;

        ((TextView) findViewById(R.id.value)).setText(rank.getDescription());
    }


    @Override
    public void onClick(View v) {
        int value = rank.getId();

        if (v.getId() == R.id.plus) {

            value += count;

            if (value > max)
                value = max;

        } else if (v.getId() == R.id.minus) {

            value -= count;

            if (value < min)
                value = min;
        }

        rank = Rank.fromId(value);

        ((TextView) findViewById(R.id.value)).setText(rank.getId());

    }

    public Rank getRankValue() {
        return rank;
    }
}
