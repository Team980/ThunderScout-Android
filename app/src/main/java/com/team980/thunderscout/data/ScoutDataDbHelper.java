package com.team980.thunderscout.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.team980.thunderscout.data.ScoutDataContract.ScoutDataTable;

public class ScoutDataDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2016; //Set this to the year of the game

    public static final String DATABASE_NAME = "ThunderScout_SCOUTDATA.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ScoutDataTable.TABLE_NAME + " (" +
                    ScoutDataTable._ID + " INTEGER PRIMARY KEY," +
                    ScoutDataTable.COLUMN_NAME_TEAM_NUMBER + TEXT_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_DATE_ADDED + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_DATA_SOURCE + TEXT_TYPE + COMMA_SEP +

                    ScoutDataTable.COLUMN_NAME_AUTO_DEFENSE_CROSSED + TEXT_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_AUTO_LOW_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_AUTO_HIGH_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_AUTO_MISSED_GOALS + INTEGER_TYPE + COMMA_SEP +

                    ScoutDataTable.COLUMN_NAME_TELEOP_DEFENSE_CROSSINGS + BLOB_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_TELEOP_LOW_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_TELEOP_HIGH_GOALS + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_TELEOP_MISSED_GOALS + INTEGER_TYPE + COMMA_SEP +

                    ScoutDataTable.COLUMN_NAME_SCALING_STATS + TEXT_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_CHALLENGED_TOWER + INTEGER_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_TROUBLE_WITH + TEXT_TYPE + COMMA_SEP +
                    ScoutDataTable.COLUMN_NAME_COMMENTS + TEXT_TYPE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ScoutDataTable.TABLE_NAME;

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
