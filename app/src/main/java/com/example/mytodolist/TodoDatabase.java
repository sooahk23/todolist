package com.example.mytodolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class TodoDatabase {

    /**
     * TAG for debugging
     */
    public static final String TAG = "TodoDatabase";
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "todo";
    public static final String TODO_ID = "_ID";
    public static final String TODO_TEXT = "TEXT";
    public static final String TODO_STATUS = "STATUS";

    public static final String[] ALL_COLUMNS = {TODO_ID, TODO_TEXT, TODO_STATUS};

    /**
     * Helper class defined
     */
    private DatabaseHelper dbHelper;

    /**
     * Database object
     */
    private SQLiteDatabase db;


    private Context context;

    /**
     * Singleton instance
     */
    private static TodoDatabase database;

    private TodoDatabase(Context context) {
        this.context = context;
    }

    public static TodoDatabase getInstance(Context context) {
        if (database == null) {
            database = new TodoDatabase(context);
        }

        return database;
    }

    /**
     * open database
     *
     * @return
     */
    public boolean open() {
        println("opening database [" + DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    /**
     * close database
     */
    public void close() {
        println("closing database [" + DATABASE_NAME + "].");
        db.close();
        database = null;
    }

    /**
     * execute raw query using the input SQL
     * close the cursor after fetching any result
     *
     * @param SQL
     * @return
     */
    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    public void insertRecord(String text, String status) {
        try {
            Log.d(TAG, "insert into " + TABLE_NAME + "(TEXT, STATUS) values ('" + text + "', '" + status + "');");
            db.execSQL( "insert into " + TABLE_NAME + "(TEXT, STATUS) values ('" + text + "', '" + status + "');" );
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }
    }

    public void updateRecord(String todoId, String text, String status) {
        try {
            db.execSQL( "update " + TABLE_NAME + "set " + TODO_TEXT + " = " + text + " and "
                    + TODO_STATUS + " = " + status
                    + " where " + TODO_ID + " = " + todoId);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }
    }

    public void deleteRecord(String todoId) {
        try {
            db.execSQL( "delete from " + TABLE_NAME + "where " + TODO_ID + " = " + todoId);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }
    }

    public ArrayList<Todo> selectAll() {
        ArrayList<Todo> result = new ArrayList<Todo>();

        try {
            Cursor cursor = db.rawQuery("select TEXT, STATUS from " + TABLE_NAME, null);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String text = cursor.getString(0);
                String status = cursor.getString(1);

                Todo info = new Todo(text, status);
                result.add(info);
            }

        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }

        return result;
    }


    private void println(String msg) {
        Log.d(TAG, msg);
    }


}
