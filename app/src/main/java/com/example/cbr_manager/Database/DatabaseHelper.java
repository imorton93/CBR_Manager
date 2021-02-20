package com.example.cbr_manager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cbr.db";
    private static final String TABLE_NAME = "WORKER_DATA";
    private static final String COL_1 = "FIRST_NAME";
    private static final String COL_2 = "LAST_NAME";
    private static final String COL_3 = "EMAIL";
    private static final String COL_4 = "PASSWORD";
    private static final String COL_5 = "ID";
    /*
     healthRequire, healthIndividualGoal, educationRate, educationRequire,
    educationIndividualGoal, socialStatusRate, socialStatusRequire, SocialStatusIndividualGoal

     */
    private static final String client_table_name = "CLIENT_DATA";
    private static final String client_first_name = "FIRST_NAME";
    private static final String client_last_name = "LAST_NAME";
    private static final String client_age = "AGE";
    private static final String client_village_no = "VILLAGE_NUMBER";
    private static final String client_location = "LOCATION";
    private static final String client_disability = "DISABILITY";
    private static final String is_synced = "IS_SYNCED";
    private static final String client_consent = "CONSENT";
    private static final String client_date = "DATE";
    private static final String client_caregiver_presence = "CAREGIVER_PRESENCE";
    private static final String client_caregiver_number = "CAREGIVER_NUMBER";
    private static final String client_heath_rate = "HEALTH_RATE";
    private static final String client_contact = "CONTACT";
    private static final String client_health_requirement = "HEALTH_REQUIREMENT";
    private static final String client_health_goal = "HEALTH_GOAL";
    private static final String client_education_rate = "EDUCATION_RATE";
    private static final String client_education_requirement = "EDUCATION_REQUIRE";
    private static final String client_education_goal = "EDUCATION_GOAL";
    private static final String client_social_rate = "SOCIAL_RATE";
    private static final String  client_social_requirement= "SOCIAL_REQUIREMENT";
    private static final String client_social_goal = "SOCIAL_GOAL";
    private static final String client_id = "ID";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtablestatement = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3
                + " TEXT UNIQUE NOT NULL, " + COL_4 + " TEXT, " + COL_5 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);";
        db.execSQL(createtablestatement);
        String create_client_table = "CREATE TABLE " + client_table_name + " (" + client_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + client_consent + " BOOLEAN NOT NULL, " + client_date + " STRING NOT NULL, "
                + client_first_name + " TEXT, "
                + client_last_name + " TEXT, " + client_age + " INTEGER, "
                + client_village_no + " INTEGER, "  + client_location + " TEXT, " + client_contact + " STRING, "+ client_caregiver_presence
                + " BOOLEAN NOT NULL, " +client_caregiver_number +" STRING, "
                + client_disability + " TEXT, " + client_heath_rate + " STRING, "+ client_health_requirement +
                " STRING, " + client_health_goal + " STRING, " + client_education_rate +" STRING, " +
                client_education_requirement + " STRING, " + client_education_goal  + " STRING, " +
                client_social_rate + " STRING, " + client_social_requirement + " STRING, " +
                client_social_goal + " STRING, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
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
        cv.put(client_consent, client.getConsentToInterview());
        cv.put(client_date, client.getDate());
        cv.put(client_first_name, client.getFirstName());
        cv.put(client_last_name, client.getLastName());
        cv.put(client_age, client.getAge());
        cv.put(client_village_no, client.getVillageNumber());
        cv.put(client_location, client.getLocation());
        cv.put(client_disability, client.disabilitiesToString());
        cv.put(client_contact, client.getContactPhoneNumber());
        cv.put(client_caregiver_presence, client.getCaregiverPresent());
        cv.put(client_caregiver_number, client.getCaregiverPhoneNumber());
        cv.put(client_heath_rate, client.getHealthRate());
        cv.put(client_health_requirement, client.getHealthRequire());
        cv.put(client_health_goal, client.getHealthIndividualGoal());
        cv.put(client_education_rate, client.getEducationRate());
        cv.put(client_education_requirement, client.getEducationRequire());
        cv.put(client_education_goal, client.getEducationIndividualGoal());
        cv.put(client_social_rate, client.getSocialStatusRate());
        cv.put(client_social_goal, client.getSocialStatusIndividualGoal());
        cv.put(client_social_requirement, client.getSocialStatusRequire());

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

    public int getWorkerId(String username){
        String query = "SELECT ID FROM " + TABLE_NAME + " WHERE " + COL_3 + " = '" + username + "';" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToLast();
        return c.getInt(0);

    }

    public Cursor executeQuery(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor getAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =  db.rawQuery( "select rowid _id,* from CLIENT_DATA", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
}
