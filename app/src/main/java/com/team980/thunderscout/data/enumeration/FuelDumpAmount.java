package com.team980.thunderscout.data.enumeration;

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
