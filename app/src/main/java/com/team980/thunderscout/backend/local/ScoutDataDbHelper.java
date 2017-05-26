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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@Deprecated //TODO refactor or replace
public class ScoutDataDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;

    public static final String DATABASE_NAME = "ThunderScout_SCOUT_DATA_2017.db"; //Store year in database name but DO NOT CHANGE FORMAT

    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ScoutDataContract.ScoutDataTable.TABLE_NAME + " (" +
                    ScoutDataContract.ScoutDataTable._ID + " INTEGER PRIMARY KEY," +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_TEAM_NUMBER + TEXT_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_MATCH_NUMBER + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_ALLIANCE_COLOR + TEXT_TYPE + COMMA_SEP +

                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATE_ADDED + BLOB_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_DATA_SOURCE + TEXT_TYPE + COMMA_SEP +

                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DELIVERED + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_GEARS_DROPPED + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOAL_DUMP_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_MISSED_HIGH_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_AUTO_CROSSED_BASELINE + INTEGER_TYPE + COMMA_SEP +

                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DELIVERED + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_GEARS_DROPPED + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOAL_DUMPS + BLOB_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_HIGH_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_CLIMBING_STATS + TEXT_TYPE + COMMA_SEP +

                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_TROUBLE_WITH + TEXT_TYPE + COMMA_SEP +
                    ScoutDataContract.ScoutDataTable.COLUMN_NAME_COMMENTS + TEXT_TYPE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ScoutDataContract.ScoutDataTable.TABLE_NAME;

    public ScoutDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for event data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
