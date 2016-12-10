package com.team980.thunderscout.data.enumeration;

import com.team980.thunderscout.R;

public enum AllianceColor {
    ALLIANCE_COLOR_RED(R.color.alliance_red_primary, R.color.alliance_red_primary_dark),
    ALLIANCE_COLOR_BLUE(R.color.alliance_blue_primary, R.color.alliance_blue_primary_dark);

    int colorPrimary;
    int colorPrimaryDark;

    AllianceColor(int colorPrimary, int colorPrimaryDark) {
        this.colorPrimary = colorPrimary;
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public int getColorPrimaryDark() {
        return colorPrimaryDark;
    }
}
