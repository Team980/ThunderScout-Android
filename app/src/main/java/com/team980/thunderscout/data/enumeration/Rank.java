package com.team980.thunderscout.data.enumeration;

public enum Rank {

    NOT_ATTEMPTED(0), //did not try // did nothing
    FAILED(1), //tried and failed
    SUCCEEDED_BUT_SLOW(2), //took a while but managed to get something done
    FAST_AS_LIGHTNING(3); //team 330

    int id;

    Rank(int id) {
        this.id = id;
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
        return String.valueOf(id);
    }

    public String getShortDescription() { //TODO one word description
        return getDescription();
    }
}