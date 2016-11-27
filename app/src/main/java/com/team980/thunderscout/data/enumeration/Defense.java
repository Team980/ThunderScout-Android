package com.team980.thunderscout.data.enumeration;

import com.team980.thunderscout.R;

//TODO Add toString() for the localized name
public enum Defense {
    NONE(-1),
    LOW_BAR(R.id.teleop_counterLowBar),
    PORTCULLIS(R.id.teleop_counterPortcullis),
    CHIVAL_DE_FRISE(R.id.teleop_counterChivalDeFrise),
    MOAT(R.id.teleop_counterMoat),
    RAMPARTS(R.id.teleop_counterRamparts),
    DRAWBRIDGE(R.id.teleop_counterDrawbridge),
    SALLYPORT(R.id.teleop_counterSallyport),
    ROCK_WALL(R.id.teleop_counterRockWall),
    ROUGH_TERRAIN(R.id.teleop_counterRoughTerrain);

    private int counterId;

    Defense(int id) {
        counterId = id;
    }

    @Deprecated
    @Override
    public String toString() {
        return super.toString();
    }

    public int getCounterId() {
        return counterId;
    }
}
