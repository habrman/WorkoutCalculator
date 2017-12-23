package com.habrman.smolovcalculator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by david on 10/20/17.
 */

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.WorkoutEntry.TABLE + " (" +
                TaskContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_TITLE + " TEXT NOT NULL, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_MAX + " REAL, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_INCREMENT + " REAL, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W1D1 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W1D2 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W1D3 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W1D4 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W2D1 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W2D2 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W2D3 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W2D4 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W3D1 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W3D2 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W3D3 + " INTEGER DEFAULT 0, " +
                TaskContract.WorkoutEntry.COL_WORKOUT_W3D4 + " INTEGER DEFAULT 0 " +
                ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TaskContract.WorkoutEntry.TABLE);
            onCreate(db);
        }
    }

    public String getMessage(String task) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TaskContract.WorkoutEntry.TABLE,
                new String[]{TaskContract.WorkoutEntry._ID,
                        TaskContract.WorkoutEntry.COL_WORKOUT_TITLE,
                        TaskContract.WorkoutEntry.COL_WORKOUT_MAX,
                        TaskContract.WorkoutEntry.COL_WORKOUT_INCREMENT},
                TaskContract.WorkoutEntry.COL_WORKOUT_TITLE + " = ?",
                new String[]{task},
                null, null, null);

        int idx_id = cursor.getColumnIndex(TaskContract.WorkoutEntry._ID);
        int idx_title = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_TITLE);
        int idx_max = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_MAX);
        int idx_inc = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_INCREMENT);

        cursor.moveToFirst();
        String message = cursor.getString(idx_id) + ":" + cursor.getString(idx_title) + ":" + cursor.getString(idx_max) + ":" + cursor.getString(idx_inc);


        cursor.close();
        db.close();

        return message;
    }

    public ArrayList<String> getTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.WorkoutEntry.TABLE,
                new String[]{TaskContract.WorkoutEntry._ID,
                        TaskContract.WorkoutEntry.COL_WORKOUT_TITLE,
                        TaskContract.WorkoutEntry.COL_WORKOUT_MAX},
                null, null, null, null, null);

        int idx = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_TITLE);
        int idx_max = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_MAX);

        while (cursor.moveToNext()) {
            taskList.add(cursor.getString(idx) + ": " + cursor.getString(idx_max));
        }

        cursor.close();
        db.close();

        return taskList;
    }

    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskContract.WorkoutEntry.TABLE,
                TaskContract.WorkoutEntry.COL_WORKOUT_TITLE + " = ?",
                new String[]{task});
        db.close();
    }

    public void updateTask(ContentValues values, long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TaskContract.WorkoutEntry.TABLE,
                values,
                TaskContract.WorkoutEntry._ID + "=" + "'" + id + "'",
                null);

        Cursor cursor = db.query(TaskContract.WorkoutEntry.TABLE,
                new String[]{TaskContract.WorkoutEntry._ID,
                        TaskContract.WorkoutEntry.COL_WORKOUT_TITLE,
                        TaskContract.WorkoutEntry.COL_WORKOUT_MAX},
                TaskContract.WorkoutEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        int idx_id = cursor.getColumnIndex(TaskContract.WorkoutEntry._ID);
        int idx_title = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_TITLE);
        int idx_max = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_MAX);
        int idx_inc = cursor.getColumnIndex(TaskContract.WorkoutEntry.COL_WORKOUT_INCREMENT);
        int idx_i = cursor.getColumnIndex(String.valueOf(3));
        int idx_in = cursor.getColumnIndex(String.valueOf(4));

        cursor.moveToFirst();

        db.close();
    }

    public long addTask(ContentValues values) {

        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insertWithOnConflict(TaskContract.WorkoutEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        return id;
    }
}
