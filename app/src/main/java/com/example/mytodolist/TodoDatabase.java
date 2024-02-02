package com.example.mytodolist;

import android.content.ContentValues;
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

    public long insertRecord(String text, String status) {
        try {
            Log.d(TAG, "insert into " + TABLE_NAME + "(TEXT, STATUS) values ('" + text + "', '" + status + "');");
            ContentValues cv = new ContentValues();
            cv.put(TODO_TEXT, text);
            cv.put(TODO_STATUS, status);
            long id = db.insert(TABLE_NAME, null, cv);
//            db.execSQL( "insert into " + TABLE_NAME + "(TEXT, STATUS) values ('" + text + "', '" + status + "');" );
            return id;
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
            return -1;
        }
    }

    public void updateRecord(long todoId, String text, String status) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(TODO_TEXT, text);
            cv.put(TODO_STATUS, status);
            db.update(TABLE_NAME, cv, TODO_ID+ " = ?",new String[]{Long.toString(todoId)});
//            db.execSQL( "update " + TABLE_NAME + "set " + TODO_TEXT + " = " + text + " and "
//                    + TODO_STATUS + " = " + status
//                    + " where " + TODO_ID + " = " + todoId);
//            Log.d(TAG, "HEY!!!!! id, text, status " + todoId + " " +  text + " " + status);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }
    }

    public void deleteRecord(long todoId) {
        try {
            db.execSQL( "delete from " + TABLE_NAME + "where " + TODO_ID + " = " + todoId);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }
    }

    public ArrayList<Todo> selectAll() {
        ArrayList<Todo> result = new ArrayList<Todo>();

        try {
            Cursor cursor = db.rawQuery("select _ID, TEXT, STATUS from " + TABLE_NAME, null);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                long id = cursor.getLong(0);
                String text = cursor.getString(1);
                String status = cursor.getString(2);

//                Log.d(TAG, "HEY!!!!! id, text, status " + id + " " +  text + " " + status);
                Todo info = new Todo(id, text, status);
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
