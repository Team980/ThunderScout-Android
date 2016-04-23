package com.team980.thunderscout.data.enumeration;

import com.team980.thunderscout.R;

//TODO name() vs toString() for use in enums
public enum Defense {
    LOW_BAR(R.id.teleop_defenseLowBar, R.id.teleop_sliderLowBar),
    PORTCULLIS(R.id.teleop_defensePortcullis, R.id.teleop_sliderPortcullis),
    CHIVAL_DE_FRISE(R.id.teleop_defenseFries, R.id.teleop_sliderFries),
    MOAT(R.id.teleop_defenseMoat, R.id.teleop_sliderMoat),
    RAMPARTS(R.id.teleop_defenseRamparts, R.id.teleop_sliderRamparts),
    DRAWBRIDGE(R.id.teleop_defenseDrawbridge, R.id.teleop_sliderDrawbridge),
    SALLYPORT(R.id.teleop_defenseSallyport, R.id.teleop_sliderSallyport),
    ROCK_WALL(R.id.teleop_defenseRockWall, R.id.teleop_sliderRockWall),
    ROUGH_TERRAIN(R.id.teleop_defenseRoughTerrain, R.id.teleop_sliderRoughTerrain);

    private int id;
    private int sliderId;

    Defense(int i, int si) {
        id = i;
        sliderId = si;
    }

    public int getTeleopID() {
        return id;
    }

    public int getTeleopSliderID() {
        return sliderId;
    }

    @Deprecated
    @Override
    public String toString() {
        return super.toString();
    }
}
