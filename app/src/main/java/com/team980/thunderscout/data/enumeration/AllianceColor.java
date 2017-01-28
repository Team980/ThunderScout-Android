package com.team980.thunderscout.data.enumeration;

import com.team980.thunderscout.R;

public enum AllianceColor {
    ALLIANCE_COLOR_RED("Red Alliance", R.color.alliance_red_primary, R.color.alliance_red_primary_dark),
    ALLIANCE_COLOR_BLUE("Blue Alliance", R.color.alliance_blue_primary, R.color.alliance_blue_primary_dark);

    String displayString;
    int colorPrimary;
    int colorPrimaryDark;

    AllianceColor(String displayString, int colorPrimary, int colorPrimaryDark) {
        this.displayString = displayString;
        this.colorPrimary = colorPrimary;
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public String toString() {
        return displayString;
    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public int getColorPrimaryDark() {
        return colorPrimaryDark;
    }
}
