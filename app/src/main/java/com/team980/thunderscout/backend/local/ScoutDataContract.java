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

package com.team980.thunderscout.backend.local;

import android.provider.BaseColumns;

public final class ScoutDataContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ScoutDataContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class ScoutDataTable implements BaseColumns {
        public static final String TABLE_NAME = "scout_data";

        // --- INIT ---
        public static final String COLUMN_NAME_TEAM_NUMBER = "team";
        public static final String COLUMN_NAME_MATCH_NUMBER = "match_number";
        public static final String COLUMN_NAME_ALLIANCE_STATION = "alliance_station";

        public static final String COLUMN_NAME_DATE_ADDED = "date_added";
        public static final String COLUMN_NAME_DATA_SOURCE = "data_source";

        // --- AUTO ---
        public static final String COLUMN_NAME_AUTO_GEARS_DELIVERED = "auto_gears_delivered";
        public static final String COLUMN_NAME_AUTO_GEARS_DROPPED = "auto_gears_dropped";
        public static final String COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT = "auto_low_goal_dump_amount";
        public static final String COLUMN_NAME_AUTO_HIGH_GOALS = "auto_high_goals";
        public static final String COLUMN_NAME_AUTO_MISSED_HIGH_GOALS = "auto_missed_high_goals";
        public static final String COLUMN_NAME_AUTO_CROSSED_BASELINE = "auto_crossed_baseline";

        // --- TELEOP ---
        public static final String COLUMN_NAME_TELEOP_GEARS_DELIVERED = "teleop_gears_delivered";
        public static final String COLUMN_NAME_TELEOP_GEARS_DROPPED = "teleop_gears_dropped";
        public static final String COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS = "teleop_low_goal_dumps";
        public static final String COLUMN_NAME_TELEOP_HIGH_GOALS = "teleop_high_goals";
        public static final String COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS = "teleop_missed_high_goals";
        public static final String COLUMN_NAME_CLIMBING_STATS = "climbing_stats";

        // --- SUMMARY ---
        public static final String COLUMN_NAME_TROUBLE_WITH = "trouble_with";
        public static final String COLUMN_NAME_COMMENTS = "comments";
    }
}

