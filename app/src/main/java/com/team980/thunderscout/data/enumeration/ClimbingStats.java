package com.team980.thunderscout.data.enumeration;

public enum ClimbingStats {

    DID_NOT_CLIMB("Did not climb"),
    ATTEMPTED_CLIMB("Attempted climb"),
    PRESSED_TOUCHPAD("Pressed touchpad"); //only option that scores

    private String displayString;

    ClimbingStats(String par1) {
        displayString = par1;
    }

    @Override
    public String toString() {
        return displayString;
    }
}
