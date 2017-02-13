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

public class CounterCompoundView extends FrameLayout implements View.OnClickListener {

    protected LayoutInflater inflater;
    protected float max;
    protected float min;
    protected float count;
    protected Size size;
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

        int numSize = a.getInt(R.styleable.CounterCompoundView_size, 0);
        size = Size.values()[numSize];

        switch (size) { //TODO add alternate resource configurations instead of this
            case LARGE:
                ((Button) findViewById(R.id.plus)).setTextSize(30);

                ((Button) findViewById(R.id.minus)).setTextSize(30);

                ((TextView) findViewById(R.id.value)).setTextSize(30);
                ((TextView) findViewById(R.id.value)).setWidth(96);
                break;
            case EXTRA_LARGE:
                ((Button) findViewById(R.id.plus)).setTextSize(40);

                ((Button) findViewById(R.id.minus)).setTextSize(40);

                ((TextView) findViewById(R.id.value)).setTextSize(40);
                ((TextView) findViewById(R.id.value)).setWidth(144);
                break;
            default:
                //the default sizes are fine
                break;
        }

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

        ss.size = this.size;

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

        this.size = ss.size;

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

        Size size;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.min = in.readFloat();
            this.count = in.readFloat();
            this.value = in.readFloat();

            this.size = (Size) in.readSerializable();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.max);
            out.writeFloat(this.min);
            out.writeFloat(this.count);
            out.writeFloat(this.value);

            out.writeSerializable(this.size);
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

    private enum Size { //Get by ordinal
        NORMAL,
        LARGE,
        EXTRA_LARGE
    }
}