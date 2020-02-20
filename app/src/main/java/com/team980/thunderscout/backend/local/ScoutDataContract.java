/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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

        // --- SANDSTORM ---
        public static final String COLUMN_NAME_STORM_STARTING_LEVEL = "auto_starting_position";
        public static final String COLUMN_NAME_STORM_CROSSED_HAB_LINE = "auto_moved_off_line";

        public static final String COLUMN_NAME_STORM_HIGH_ROCKET_HATCH_COUNT = "auto_high_port";
    /*    public static final String COLUMN_NAME_STORM_MIDDLE_ROCKET_HATCH_COUNT = "storm_middle_rocket_hatch_count";
        public static final String COLUMN_NAME_STORM_LOW_ROCKET_HATCH_COUNT = "storm_low_rocket_hatch_count";
        public static final String COLUMN_NAME_STORM_CARGO_SHIP_HATCH_COUNT = "storm_cargo_ship_hatch_count";
*/
        public static final String COLUMN_NAME_STORM_HIGH_ROCKET_CARGO_COUNT = "auto_low_port";
        /*public static final String COLUMN_NAME_STORM_MIDDLE_ROCKET_CARGO_COUNT = "storm_middle_rocket_cargo_count";
        public static final String COLUMN_NAME_STORM_LOW_ROCKET_CARGO_COUNT = "storm_low_rocket_cargo_count";
        public static final String COLUMN_NAME_STORM_CARGO_SHIP_CARGO_COUNT = "storm_cargo_ship_cargo_count";
*/
        // --- TELEOPERATED ---
        public static final String COLUMN_NAME_TELEOP_HIGH_ROCKET_HATCH_COUNT = "teleop_power_cell_pickup";
        public static final String COLUMN_NAME_TELEOP_MIDDLE_ROCKET_HATCH_COUNT = "teleop_high_port_count";
        public static final String COLUMN_NAME_TELEOP_LOW_ROCKET_HATCH_COUNT = "teleop_low_port_count";
       // public static final String COLUMN_NAME_TELEOP_CARGO_SHIP_HATCH_COUNT = "teleop_cargo_ship_hatch_count";

        public static final String COLUMN_NAME_TELEOP_HIGH_ROCKET_CARGO_COUNT = "teleop_control_panel_rotation";
        public static final String COLUMN_NAME_TELEOP_MIDDLE_ROCKET_CARGO_COUNT = "teleop_control_panel_position";
        /*public static final String COLUMN_NAME_TELEOP_LOW_ROCKET_CARGO_COUNT = "teleop_low_rocket_cargo_count";
        public static final String COLUMN_NAME_TELEOP_CARGO_SHIP_CARGO_COUNT = "teleop_cargo_ship_cargo_count";
*/
        // --- ENDGAME ---
        public static final String COLUMN_NAME_ENDGAME_CLIMB_LEVEL = "endgame_climbing";
        public static final String COLUMN_NAME_ENDGAME_CLIMB_TIME = "endgame_bar_position";
        public static final String COLUMN_NAME_ENDGAME_SUPPORTED_OTHER_ROBOT_WHEN_CLIMBING = "endgame_supported_other_robot_when_climbing";
        public static final String COLUMN_NAME_ENDGAME_CLIMB_DESCRIPTION = "endgame_climb_description";

        // --- NOTES ---
        public static final String COLUMN_NAME_NOTES = "notes";
    }
}

