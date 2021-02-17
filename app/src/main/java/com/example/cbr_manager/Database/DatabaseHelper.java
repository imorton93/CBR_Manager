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

    private static final String client_table_name = "CLIENT_DATA";
    private static final String client_first_name = "FIRST_NAME";
    private static final String client_last_name = "LAST_NAME";
    private static final String client_age = "AGE";
    private static final String client_village_no = "VILLAGE_NUMBER";
    private static final String client_location = "LOCATION";
    private static final String client_disability = "DISABILITY";
    private static final String is_synced = "IS_SYNCED";

    //private static final String client_id = "ID";


    public DatabaseHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtablestatement = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3 + " TEXT PRIMARY KEY, " + COL_4 + " TEXT);";
        db.execSQL(createtablestatement);
        String create_client_table = "CREATE TABLE " + client_table_name + " (" + client_first_name + " TEXT, "
                + client_last_name + " TEXT, " + client_age + " INTEGER, "
                + client_village_no + " INTEGER, "  + client_location + " TEXT, "
                + client_disability + " TEXT, " + is_synced + "INTEGER DEFAULT 0);";
        db.execSQL(create_client_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME );
        db.execSQL(" DROP TABLE IF EXISTS " + client_table_name );
        onCreate(db);
    }

    public boolean registerClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(client_first_name, client.getFirstName());
        cv.put(client_last_name, client.getLastName());
        cv.put(client_age, client.getAge());
        cv.put(client_village_no, client.getVillageNo());
        cv.put(client_location, client.getLocation());
        cv.put(client_disability, client.getDisabilityType());

        long result = db.insert(client_table_name, null, cv);
        if (result == -1)
            return false;
        else
            return true;
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
