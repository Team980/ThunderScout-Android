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

    public FuelDumpAmount getByAmount(int amount) {
        throw new UnsupportedOperationException("NYI"); //TODO
    }

    //TODO override toString()
}
