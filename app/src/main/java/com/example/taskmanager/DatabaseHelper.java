package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.taskmanager.ui.tasks.Task;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TaskManagerTest1";

    // Table Name
    private static final String TABLE_NAME = "tasks";

    // Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    // Add more columns as needed

    // Singleton instance
    private static DatabaseHelper instance;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton method to get instance of DatabaseHelper
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Title" + " TEXT,"
                + "Description" + " TEXT,"
                + "DueDate" + " TEXT,"
                + "Priority" + " INTEGER"
                + ")";
        // Execute query
        db.execSQL(CREATE_TABLE);
        Log.d("DatabaseHelper", "Database created successfully");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    // Method to add data to the database
    public boolean addData(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("Title", task.getTitle());
        contentValues.put("Description", task.getDescription());
        contentValues.put("DueDate", task.dueDateString);
        contentValues.put("Priority", task.priority);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result != -1){
            Log.d("DatabaseHelper", "Added task successfully");
        }
        // Returns true if data is inserted successfully, false otherwise
        return result != -1;
    }

    // Method to retrieve data from the database
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Method to update data in the database
    public boolean updateData(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("Title", task.getTitle());
        contentValues.put("Description", task.getDescription());
        contentValues.put("DueDate", task.dueDateString);
        contentValues.put("Priority", task.priority);

        String id = Integer.toString(task.id);

        int affectedRows = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{id});
        if(affectedRows > 0){
            Log.d("DatabaseHelper", "saved task successfully");

        }else{
            Log.d("DatabaseHelper", "failed to save task ");
        }
        return affectedRows > 0;
    }

//
//    // Method to delete data from the database
//    public boolean deleteData(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int affectedRows = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
//        // Returns true if data is deleted successfully, false otherwise
//        return affectedRows > 0;
//    }
}
