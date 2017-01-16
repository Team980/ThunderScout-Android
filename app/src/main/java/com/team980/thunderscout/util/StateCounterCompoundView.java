package com.team980.thunderscout.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.team980.thunderscout.R;
import com.team980.thunderscout.data.enumeration.FuelDumpAmount;

/**
 * Hardcoded to use specific enum... Hope this can change
 */
public class StateCounterCompoundView extends FrameLayout implements View.OnClickListener {

    protected LayoutInflater inflater;
    protected float max;
    protected float min;
    protected float count;
    FuelDumpAmount value;

    public StateCounterCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.view_state_counter, this);

        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.minus).setOnClickListener(this);

        String text = value.toString();

        ((TextView) findViewById(R.id.value)).setText(text);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.plus) {
            int newOrdinal = value.ordinal() + 1;

            if (newOrdinal < 0) {
                value = FuelDumpAmount.values()[0];
            } else {
                value = FuelDumpAmount.values()[newOrdinal];
            }

        } else if (v.getId() == R.id.minus) {
            int newOrdinal = value.ordinal() + 1;

            if ((FuelDumpAmount.values().length - 1) < newOrdinal) {
                value = FuelDumpAmount.values()[FuelDumpAmount.values().length - 1];
            } else {
                value = FuelDumpAmount.values()[newOrdinal];
            }
        }

        String text;

        text = value.toString();

        ((TextView) findViewById(R.id.value)).setText(text);
    }

    public FuelDumpAmount getValue() {
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

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.max = ss.max;
        this.min = ss.min;
        this.count = ss.count;
        this.value = ss.value;

        String text = value.toString();

        ((TextView) findViewById(R.id.value)).setText(text);
    }

    static class SavedState extends BaseSavedState {
        float max;
        float min;
        float count;
        FuelDumpAmount value;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.max = in.readFloat();
            this.min = in.readFloat();
            this.count = in.readFloat();
            this.value = (FuelDumpAmount) in.readSerializable();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.max);
            out.writeFloat(this.min);
            out.writeFloat(this.count);
            out.writeSerializable(this.value);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}