/*
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Luke Myers (FRC Team 980 ThunderBots)
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

package com.team980.thunderscout.util;

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

public class CounterCompoundView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    protected LayoutInflater inflater;
    protected float max;
    protected float min;
    protected float count;
    float value;

    protected boolean longPressEnabled;
    protected float longPressCount;

    public CounterCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.counter_view, this);

        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.minus).setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CounterCompoundView, 0, 0);
        max = a.getFloat(R.styleable.CounterCompoundView_max, 1337); //This impossibly high number should never be reached
        min = a.getFloat(R.styleable.CounterCompoundView_min, 0);
        count = a.getFloat(R.styleable.CounterCompoundView_count, 1);

        longPressEnabled = a.getBoolean(R.styleable.CounterCompoundView_longPress, false); //no long press by default
        longPressCount = a.getFloat(R.styleable.CounterCompoundView_longPressCount, 5); //default long press count: 5

        a.recycle();

        if (longPressEnabled) {
            findViewById(R.id.plus).setOnLongClickListener(this);
            findViewById(R.id.minus).setOnLongClickListener(this);
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

    @Override
    public boolean onLongClick(View v) {

        if (v.getId() == R.id.plus) {

            value += longPressCount;
            if (value > max)
                value = max;

        } else if (v.getId() == R.id.minus) {

            value -= longPressCount;

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

        return true;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float v) {
        value = v;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.max = this.max;
        ss.min = this.min;
        ss.count = this.count;
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

        this.max = ss.max;
        this.min = ss.min;
        this.count = ss.count;
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
        float max;
        float min;
        float count;
        float value;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.min = in.readFloat();
            this.count = in.readFloat();
            this.value = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.max);
            out.writeFloat(this.min);
            out.writeFloat(this.count);
            out.writeFloat(this.value);
        }

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
    }
}