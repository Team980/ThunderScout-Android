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

package com.team980.thunderscout.schema.enumeration;

import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;

import com.team980.thunderscout.R;

public enum AllianceStation {
    RED_1(AllianceColor.RED, R.color.alliance_red_primary, R.id.match_red1, 0),
    RED_2(AllianceColor.RED, R.color.alliance_red_primary_dark, R.id.match_red2, 100),
    RED_3(AllianceColor.RED, R.color.alliance_red_primary, R.id.match_red3, 200),

    BLUE_1(AllianceColor.BLUE, R.color.alliance_blue_primary, R.id.match_blue1, 300),
    BLUE_2(AllianceColor.BLUE, R.color.alliance_blue_primary_dark, R.id.match_blue2, 400),
    BLUE_3(AllianceColor.BLUE, R.color.alliance_blue_primary, R.id.match_blue3, 500);

    AllianceColor color;
    int colorStratified;
    int matchCellViewID;
    long delayInMillis;

    AllianceStation(AllianceColor color, int colorStratified, @IdRes int matchCellViewID, long delayInMillis) {
        this.color = color;
        this.colorStratified = colorStratified;
        this.matchCellViewID = matchCellViewID;
        this.delayInMillis = delayInMillis;
    }

    public AllianceColor getColor() {
        return color;
    }

    public int getColorStratified() {
        return colorStratified;
    }

    @IdRes
    public int getMatchCellViewID() {
        return matchCellViewID;
    }

    public long getDelay() { //Client delay to prevent socket errors
        return delayInMillis;
    }

    public enum AllianceColor {
        RED("Red Alliance", R.color.alliance_red_primary, R.color.alliance_red_primary_dark),
        BLUE("Blue Alliance", R.color.alliance_blue_primary, R.color.alliance_blue_primary_dark);

        String displayString;

        int colorPrimary;
        int colorPrimaryDark;

        AllianceColor(String displayString, @ColorRes int colorPrimary, @ColorRes int colorPrimaryDark) {
            this.displayString = displayString;
            this.colorPrimary = colorPrimary;
            this.colorPrimaryDark = colorPrimaryDark;
        }

        public String toString() {
            return displayString;
        }

        @ColorRes
        public int getColorPrimary() {
            return colorPrimary;
        }

        @ColorRes
        public int getColorPrimaryDark() {
            return colorPrimaryDark;
        }
    }
}
