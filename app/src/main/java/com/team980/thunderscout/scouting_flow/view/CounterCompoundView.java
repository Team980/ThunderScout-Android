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

package com.team980.thunderscout.scouting_flow.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;

//TODO add an onUpdate listener, so we can stop initScoutData from existing?
public class CounterCompoundView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    protected LayoutInflater inflater;
    protected float maxValue;
    protected float minValue;
    protected float increment;
    protected boolean longPressEnabled;
    protected float longPressIncrement;
    float value;

    public CounterCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.counter_view, this);

        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.minus).setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CounterCompoundView, 0, 0);
        maxValue = a.getFloat(R.styleable.CounterCompoundView_maxValue, 1969); //man once dreamed to reach the stars
        minValue = a.getFloat(R.styleable.CounterCompoundView_minValue, 0);
        increment = a.getFloat(R.styleable.CounterCompoundView_increment, 1);

        longPressEnabled = a.getBoolean(R.styleable.CounterCompoundView_longPress, false); //no long press by default
        longPressIncrement = a.getFloat(R.styleable.CounterCompoundView_longPressIncrement, 5); //default long press increment: 5

        a.recycle();

        if (longPressEnabled) {
            findViewById(R.id.plus).setOnLongClickListener(this);
            findViewById(R.id.minus).setOnLongClickListener(this);
            ((Button) findViewById(R.id.plus)).setTextColor(getResources().getColor(R.color.accent_dark));
            ((Button) findViewById(R.id.minus)).setTextColor(getResources().getColor(R.color.accent_dark));
        }

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

            value += increment;

            if (value > maxValue)
                value = maxValue;

        } else if (v.getId() == R.id.minus) {

            value -= increment;

            if (value < minValue)
                value = minValue;
        }

        String text;

        if (Float.compare(value, Math.round(value)) != 0) {
            text = Float.toString(value);
        } else {
            text = Integer.toString((int) value);
        }

        ((TextView) findViewById(R.id.value)).setText(text);
    }

    @Override
    public boolean onLongClick(View v) {

        if (v.getId() == R.id.plus) {

            value += longPressIncrement;
            if (value > maxValue)
                value = maxValue;

        } else if (v.getId() == R.id.minus) {

            value -= longPressIncrement;

            if (value < minValue)
                value = minValue;
        }

        String text;

        if (Float.compare(value, Math.round(value)) != 0) {
            text = Float.toString(value);
        } else {
            text = Integer.toString((int) value);
        }

        ((TextView) findViewById(R.id.value)).setText(text);

        return true;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float v) {
        value = v;

        String text;

        if (Float.compare(value, Math.round(value)) != 0) {
            text = Float.toString(value);
        } else {
            text = Integer.toString((int) value);
        }

        ((TextView) findViewById(R.id.value)).setText(text);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.maxValue = this.maxValue;
        ss.minValue = this.minValue;
        ss.increment = this.increment;
        ss.value = this.value;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.maxValue = ss.maxValue;
        this.minValue = ss.minValue;
        this.increment = ss.increment;
        this.value = ss.value;

        String text;

        if (Float.compare(value, Math.round(value)) != 0) {
            text = Float.toString(value);
        } else {
            text = Integer.toString((int) value);
        }

        ((TextView) findViewById(R.id.value)).setText(text);
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        float maxValue;
        float minValue;
        float increment;
        float value;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.maxValue = in.readFloat();
            this.minValue = in.readFloat();
            this.increment = in.readFloat();
            this.value = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.maxValue);
            out.writeFloat(this.minValue);
            out.writeFloat(this.increment);
            out.writeFloat(this.value);
        }
    }
}