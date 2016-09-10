package com.team980.thunderscout.data.enumeration;

import com.team980.thunderscout.R;

//TODO name() vs toString() for use in enums
public enum Defense {
    NONE,
    LOW_BAR,
    PORTCULLIS,
    CHIVAL_DE_FRISE,
    MOAT,
    RAMPARTS,
    DRAWBRIDGE,
    SALLYPORT,
    ROCK_WALL,
    ROUGH_TERRAIN;

    @Deprecated
    @Override
    public String toString() {
        return super.toString();
    }
}
