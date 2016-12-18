package com.team980.thunderscout.data;

import android.provider.BaseColumns;

public final class ScoutDataContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ScoutDataContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class ScoutDataTable implements BaseColumns {
        public static final String TABLE_NAME = "scout_data";

        public static final String COLUMN_NAME_TEAM_NUMBER = "team";
        public static final String COLUMN_NAME_MATCH_NUMBER = "match_number";
        public static final String COLUMN_NAME_ALLIANCE_COLOR = "alliance_color";

        public static final String COLUMN_NAME_DATE_ADDED = "date_added";
        public static final String COLUMN_NAME_DATA_SOURCE = "data_source";

        public static final String COLUMN_NAME_AUTO_DEFENSE_CROSSED = "auto_defense_crossed";
        public static final String COLUMN_NAME_AUTO_LOW_GOALS = "auto_low_goals";
        public static final String COLUMN_NAME_AUTO_HIGH_GOALS = "auto_high_goals";
        public static final String COLUMN_NAME_AUTO_MISSED_GOALS = "auto_missed_goals";

        public static final String COLUMN_NAME_TELEOP_DEFENSE_CROSSINGS = "teleop_defense_crossings";
        public static final String COLUMN_NAME_TELEOP_LOW_GOALS = "teleop_low_goals";
        public static final String COLUMN_NAME_TELEOP_HIGH_GOALS = "teleop_high_goals";
        public static final String COLUMN_NAME_TELEOP_MISSED_GOALS = "teleop_missed_goals";

        public static final String COLUMN_NAME_SCALING_STATS = "scaling_stats";
        public static final String COLUMN_NAME_CHALLENGED_TOWER = "challenged_tower";
        public static final String COLUMN_NAME_TROUBLE_WITH = "trouble_with";
        public static final String COLUMN_NAME_COMMENTS = "comments";
    }
}

