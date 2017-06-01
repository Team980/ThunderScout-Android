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

public enum FuelDumpAmount {

    NONE("None", 0, 0),
    SMALL("Small", 1, 9),
    MEDIUM("Medium", 10, 29),
    LARGE("Large", 30, 49),
    EXTRA_LARGE("Extra Large", 50, 70); //maximum amount low goal can hold

    private String displayString;
    private int minimumAmount;
    private int maximumAmount;

    FuelDumpAmount(String par1, int min, int max) {
        displayString = par1;
        minimumAmount = min;
        maximumAmount = max;
    }

    public int getMinimumAmount() {
        return minimumAmount;
    }

    public int getMaximumAmount() {
        return maximumAmount;
    }

    public static FuelDumpAmount getByAmount(int amount) {
        if (amount <= NONE.getMinimumAmount()) {
            return FuelDumpAmount.NONE;
        } else if (amount >= SMALL.getMinimumAmount() && amount <= SMALL.getMaximumAmount()) {
            return FuelDumpAmount.SMALL;
        } else if (amount >= MEDIUM.getMinimumAmount() && amount <= MEDIUM.getMaximumAmount()) {
            return FuelDumpAmount.MEDIUM;
        } else if (amount >= LARGE.getMinimumAmount() && amount <= LARGE.getMaximumAmount()) {
            return FuelDumpAmount.LARGE;
        } else if (amount >= EXTRA_LARGE.getMinimumAmount()) {
            return FuelDumpAmount.EXTRA_LARGE;
        }

        return FuelDumpAmount.NONE;
    }

    @Override
    public String toString() {
        return displayString;
    }

}
