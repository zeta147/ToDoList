package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "to_do_list_app";
    private final static String TODO_TABLE_NAME = "to_do_table";
    private final static String TODO_COLUMN_ID = "id";
    private final static String TODO_COLUMN_NAME = "name";
    private final static String TODO_COLUMN_DATE = "date";
    private final static String TODO_COLUMN_DURATION = "duration";
    private final static String TODO_COLUMN_DESCRIPTION = "description";
    private final SQLiteDatabase database;

    private final static String CREATE_TODO_TABLE = String.format(
            "CREATE TABLE %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT)",
            TODO_TABLE_NAME,
            TODO_COLUMN_ID,
            TODO_COLUMN_NAME,
            TODO_COLUMN_DATE,
            TODO_COLUMN_DURATION,
            TODO_COLUMN_DESCRIPTION);


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_NAME);
        Log.w(this.getClass().getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
    }

    public void insertToDo(ToDo toDo) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(TODO_COLUMN_NAME, toDo.getName());
        rowValues.put(TODO_COLUMN_DATE, toDo.getDate());
        rowValues.put(TODO_COLUMN_DURATION, toDo.getDuration());
        rowValues.put(TODO_COLUMN_DESCRIPTION, toDo.getDescription());
        database.insert(TODO_TABLE_NAME, null, rowValues);
    }


    public void deleteToDo(ToDo toDo) {
        database.delete(TODO_TABLE_NAME, TODO_COLUMN_ID + "=" + toDo.getId(), null);
    }

    public void updateToDo(ToDo toDo){
        ContentValues rowValues = new ContentValues();
        rowValues.put(TODO_COLUMN_NAME, toDo.getName());
        rowValues.put(TODO_COLUMN_DATE, toDo.getDate());
        rowValues.put(TODO_COLUMN_DURATION, toDo.getDuration());
        rowValues.put(TODO_COLUMN_DESCRIPTION, toDo.getDescription());
        database.update(TODO_TABLE_NAME, rowValues, TODO_COLUMN_ID + "=" + toDo.getId(), null);
    }

    public ArrayList<ToDo> getToDoList(){
        Cursor cursor = database.query(TODO_TABLE_NAME, null, null, null, null, null, null);
        ArrayList<ToDo> toDoList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String duration = cursor.getString(3);
            String description = cursor.getString(4);
            toDoList.add(new ToDo(id, name, date, duration, description));
            cursor.moveToNext();
        }
        cursor.close();
        return toDoList;
    }

    public ToDo getToDo(int id){
        Cursor cursor = database.query(TODO_TABLE_NAME, null, TODO_COLUMN_ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        String name = cursor.getString(1);
        String date = cursor.getString(2);
        String duration = cursor.getString(3);
        String description = cursor.getString(4);
        cursor.close();
        return new ToDo(id, name, date, duration, description);
    }
}
