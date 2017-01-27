package com.team980.thunderscout.data.enumeration;

public enum FuelDumpAmount {

    NONE(0, 0),
    SMALL(1, 9),
    MEDIUM(10, 29),
    LARGE(30, 49),
    EXTRA_LARGE(50, 70); //maximum amount low goal can hold

    private int minimumAmount;
    private int maximumAmount;

    FuelDumpAmount(int par1, int par2) {
        minimumAmount = par1;
        maximumAmount = par2;
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

    //TODO average multiple FuelDumpAmounts

    //TODO override toString()
}
