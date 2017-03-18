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

package com.team980.thunderscout.data.enumeration;

import com.team980.thunderscout.R;

public enum AllianceColor {
    RED("Red Alliance", R.color.alliance_red_primary, R.color.alliance_red_primary_dark),
    BLUE("Blue Alliance", R.color.alliance_blue_primary, R.color.alliance_blue_primary_dark);

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
