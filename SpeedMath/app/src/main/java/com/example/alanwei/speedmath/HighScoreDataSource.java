package com.example.alanwei.speedmath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanwei on 6/05/15.
 */
public class HighScoreDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.TABLE_SCORE };

    public HighScoreDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createHighScore(String name, Integer highScore, Integer time){
        if (name.equals("")){
            return;
        }
        else{
            ContentValues values = new ContentValues();

            values.put(MySQLiteHelper.COLUMN_NAME, name);
            values.put(MySQLiteHelper.COLUMN_HIGHSCORE, highScore);
            values.put(MySQLiteHelper.COLUMN_TIME, time);

            database.insert(MySQLiteHelper.TABLE_SCORE, null, values);
        }
    }

    public List<HighScore> getAllHighScores(){

        List<HighScore> highScores = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from HighScores order by time asc limit 10", null);

        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                HighScore highScore = cursorToHighScore(cursor);
                highScores.add(highScore);
                cursor.moveToNext();
            }
            cursor.close();
            return highScores;
        }
        else
        {
            return highScores;
        }
    }

    private static HighScore cursorToHighScore(Cursor cursor) {
        HighScore highScore = new HighScore();
        highScore.setId(cursor.getLong(0));
        highScore.setName(cursor.getString(1));
        highScore.setHighscore(cursor.getInt(2));
        highScore.setTime(cursor.getInt(3));
        return highScore;
    }

    public void updateDB(){
        dbHelper.onUpgrade(database, 2,3);
    }
}
