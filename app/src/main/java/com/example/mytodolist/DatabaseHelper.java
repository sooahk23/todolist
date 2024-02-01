package com.example.mytodolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "todo";
    public static final String TODO_ID = "_ID";
    public static final String TODO_TEXT = "TEXT";
    public static final String TODO_STATUS = "STATUS";

    public static final String[] ALL_COLUMNS = {TODO_ID, TODO_TEXT, TODO_STATUS};

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TODO_TEXT + " TEXT, " +
            TODO_STATUS + " INTEGER" +
            ")";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
