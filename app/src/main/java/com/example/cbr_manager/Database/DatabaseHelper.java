package com.example.cbr_manager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cbr.db";
    private static final String TABLE_NAME = "WORKER_DATA";
    private static final String COL_1 = "FIRST_NAME";
    private static final String COL_2 = "LAST_NAME";
    private static final String COL_3 = "EMAIL";
    private static final String COL_4 = "PASSWORD";

    public DatabaseHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtablestatement = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3 + " TEXT PRIMARY KEY, " + COL_4 + " TEXT);";
        db.execSQL(createtablestatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    public boolean registerWorker(CBRWorker cbrWorker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, cbrWorker.getFirstName());
        cv.put(COL_2, cbrWorker.getLastName());
        cv.put(COL_3, cbrWorker.getEmail());
        cv.put(COL_4, cbrWorker.getPassword());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean checkUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String [] columns = { COL_3 };
        String selection = COL_3 + "=?" + " and " + COL_4 + "=?" ;
        String [] selectionArgs = { email , password};
        Cursor cursor = db.query(TABLE_NAME , columns , selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        if (count > 0)
            return true;
        else
            return false;

    }
}
