package com.team980.thunderscout.data.enumeration;

public enum FuelDumpAmount {

    //TODO these numbers need to be refined
    NONE(0),
    SMALL(15),
    MEDIUM(35),
    LARGE(70); //maximum amount low goal can hold?

    private int averageAmount;

    FuelDumpAmount(int par1) {
        averageAmount = par1;
    }

    public int getAverageAmount() {
        return averageAmount;
    }

    public FuelDumpAmount getByAverageAmount() {
        throw new UnsupportedOperationException("NYI"); //TODO
    }

    //TODO override toString()
}
