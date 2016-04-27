package com.team980.thunderscout.data.enumeration;

public enum Rank {

    NOT_ATTEMPTED(0, "Did not attempt"), //did not try // did nothing
    FAILED(1, "Tried and failed"), //tried and failed
    SUCCEEDED_BUT_SLOW(2, "Managed with effort"), //took a while but managed to get something done
    FAST_AS_LIGHTNING(3, "Quickly and effortlessly"); //team 330

    int id;

    String description;

    Rank(int id, String desc) {
        this.id = id;
        description = desc;
    }

    public static Rank fromId(int id) {
        switch (id) {
            case 0:
                return NOT_ATTEMPTED;
            case 1:
                return FAILED;
            case 2:
                return SUCCEEDED_BUT_SLOW;
            case 3:
                return FAST_AS_LIGHTNING;

            default:
                throw new IndexOutOfBoundsException(id + " is not a valid Rank ID");
        }
    }

    public int getId() {
        return id;
    } //TODO store as string constant in Db

    @Deprecated
    public String getDescription() {
        return id + ": " + description;
    }
}