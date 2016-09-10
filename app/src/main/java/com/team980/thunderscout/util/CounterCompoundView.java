package com.team980.thunderscout.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;

public class CounterCompoundView extends FrameLayout implements View.OnClickListener {

    protected LayoutInflater inflater;
    protected float max;
    protected float min;
    protected float count;
    float value;

    public CounterCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_counter, this);

        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.minus).setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CounterCompoundView, 0, 0);
        max = a.getFloat(R.styleable.CounterCompoundView_max, 100); //Default max: 100
        min = a.getFloat(R.styleable.CounterCompoundView_min, 0);
        count = a.getFloat(R.styleable.CounterCompoundView_count, 1);

        a.recycle();

        String text;

        if (Float.compare(value, Math.round(value)) != 0) {
            text = Float.toString(value);
        } else {
            text = Integer.toString((int) value);
        }

        ((TextView) findViewById(R.id.value)).setText(text);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.plus) {

            value += count;

            if (value > max)
                value = max;

        } else if (v.getId() == R.id.minus) {

            value -= count;

            if (value < min)
                value = min;
        }

        String text;

        if (Float.compare(value, Math.round(value)) != 0) {
            text = Float.toString(value);
        } else {
            text = Integer.toString((int) value);
        }

        ((TextView) findViewById(R.id.value)).setText(text);
    }

    public float getValue() {
        return value;
    }
}