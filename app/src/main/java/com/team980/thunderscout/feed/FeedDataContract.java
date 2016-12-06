package com.team980.thunderscout.feed;

import android.provider.BaseColumns;

public final class FeedDataContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private FeedDataContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedDataTable implements BaseColumns {
        public static final String TABLE_NAME = "feed_data";

        public static final String COLUMN_NAME_ENTRY_TYPE = "entry_type";
        public static final String COLUMN_NAME_ENTRY_DATE = "entry_date";
        public static final String COLUMN_NAME_ENTRY_OPERATIONS = "entry_operations";
    }
}

