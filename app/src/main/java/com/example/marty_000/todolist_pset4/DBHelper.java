package com.example.marty_000.todolist_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Set fields of database schema
    private static final String DATABASE_NAME = "db113.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "todo_table";

    private String todoString = "todo_string";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query
        String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT , "
                + todoString + " TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    // onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // CRUD methods

    // Create
    public void create(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(todoString, item.text);

        db.insert(TABLE, null, values);
        db.close();
    }

    // Read
    public ArrayList<ToDoItem> read() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ToDoItem> todo_list = new ArrayList<>();

        String query = "SELECT _id , " + todoString + " FROM " + TABLE;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String todo_string = cursor.getString(cursor.getColumnIndex(todoString));

            ToDoItem item = new ToDoItem(id, todo_string);
            todo_list.add(item);
        }

        cursor.close();
        db.close();
        return todo_list;
    }

    // update
    public void update(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(todoString, item.text);
        db.update(TABLE, values, "_id = ? ", new String[] {String.valueOf(item.id_number)});
        db.close();
    }

    // delete
    public void delete(ToDoItem item) {
        int id = item.id_number;
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " _id = ? ", new String[] {String.valueOf(id)});
        db.close();
    }
}