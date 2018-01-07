/*
 * MIT License
 *
 * Copyright (c) 2016 - 2018 Luke Myers (FRC Team 980 ThunderBots)
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
        public static final String COLUMN_NAME_TEAM_NUMBER = "team_number";
        public static final String COLUMN_NAME_MATCH_NUMBER = "match_number";
        public static final String COLUMN_NAME_ALLIANCE_STATION = "alliance_station";

        public static final String COLUMN_NAME_DATE_ADDED = "date_added";
        public static final String COLUMN_NAME_DATA_SOURCE = "data_source";

        // --- AUTO ---
        public static final String COLUMN_NAME_AUTO_CROSSED_AUTO_LINE = "auto_crossed_auto_line";
        public static final String COLUMN_NAME_AUTO_POWER_CUBE_ALLIANCE_SWITCH_COUNT = "auto_power_cube_alliance_switch_count";
        public static final String COLUMN_NAME_AUTO_POWER_CUBE_SCALE_COUNT = "auto_power_cube_scale_count";
        public static final String COLUMN_NAME_AUTO_POWER_CUBE_PLAYER_STATION_COUNT = "auto_power_cube_player_station_count";

        // --- TELEOP ---
        public static final String COLUMN_NAME_TELEOP_POWER_CUBE_ALLIANCE_SWITCH_COUNT = "teleop_power_cube_alliance_switch_count";
        public static final String COLUMN_NAME_TELEOP_POWER_CUBE_SCALE_COUNT = "teleop_power_cube_scale_count";
        public static final String COLUMN_NAME_TELEOP_POWER_CUBE_OPPOSING_SWITCH_COUNT = "teleop_power_cube_opposing_switch_count";
        public static final String COLUMN_NAME_TELEOP_POWER_CUBE_PLAYER_STATION_COUNT = "teleop_power_cube_player_station_count";
        public static final String COLUMN_NAME_TELEOP_CLIMBING_STATS = "teleop_climbing_stats";
        public static final String COLUMN_NAME_TELEOP_SUPPORTED_OTHER_ROBOT_WHEN_CLIMBING = "teleop_supported_other_robot_when_climbing";

        // --- SUMMARY ---
        public static final String COLUMN_NAME_STRATEGIES = "strategies";
        public static final String COLUMN_NAME_DIFFICULTIES = "difficulties";
        public static final String COLUMN_NAME_COMMENTS = "comments";
    }
}

