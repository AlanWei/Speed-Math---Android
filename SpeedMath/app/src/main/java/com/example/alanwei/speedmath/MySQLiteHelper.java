package com.example.alanwei.speedmath;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alanwei on 6/05/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_SCORE = "HighScores";
    public static final String COLUMN_ID= "_id"; // time when achieved (long)
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_HIGHSCORE = "highScore";
    public static final String COLUMN_TIME = "time";

    private static final String DATABASE_NAME = "speedmath.db";
    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SCORE + "("
            + COLUMN_ID + " INTEGER primary key autoincrement, "
            + COLUMN_NAME + " TEXT not null, "
            + COLUMN_HIGHSCORE + " INTEGER not null, "
            + COLUMN_TIME + " INTEGER not null);"
            ;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL(DATABASE_CREATE);
    }

    public void createHighScoreTable(){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        db.execSQL(DATABASE_CREATE);
        onCreate(db);
    }
}