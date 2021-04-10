package com.example.cbr_manager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import at.favre.lib.crypto.bcrypt.BCrypt;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cbr.db";

    //Worker Table
    private static final String TABLE_NAME = "WORKER_DATA";
    private static final String COL_1 = "FIRST_NAME";
    private static final String COL_2 = "LAST_NAME";
    private static final String COL_3 = "USERNAME";
    private static final String COL_7 = "ZONE";
    private static final String COL_4 = "PASSWORD";
    private static final String COL_5 = "ID";
    private static final String COL_6 = "IS_ADMIN";

    //Client Table
    private static final String client_table_name = "CLIENT_DATA";
    private static final String client_id = "ID";
    private static final String client_consent = "CONSENT";
    private static final String client_date = "DATE";
    private static final String client_first_name = "FIRST_NAME";
    private static final String client_last_name = "LAST_NAME";
    private static final String client_age = "AGE";
    private static final String client_gender = "GENDER";
    private static final String client_village_no = "VILLAGE_NUMBER";
    private static final String client_location = "LOCATION";
    private static final String client_latitude = "LATITUDE";
    private static final String client_longitude = "LONGITUDE";
    private static final String client_contact = "CONTACT";
    private static final String client_caregiver_presence = "CAREGIVER_PRESENCE";
    private static final String client_caregiver_number = "CAREGIVER_NUMBER";
    private static final String client_photo = "PHOTO";
    private static final String client_disability = "DISABILITY";
    private static final String client_heath_rate = "HEALTH_RATE";
    private static final String client_health_requirement = "HEALTH_REQUIREMENT";
    private static final String client_health_goal = "HEALTH_GOAL";
    private static final String client_education_rate = "EDUCATION_RATE";
    private static final String client_education_requirement = "EDUCATION_REQUIRE";
    private static final String client_education_goal = "EDUCATION_GOAL";
    private static final String client_social_rate = "SOCIAL_RATE";
    private static final String client_social_requirement= "SOCIAL_REQUIREMENT";
    private static final String client_social_goal = "SOCIAL_GOAL";
    private static final String is_synced = "IS_SYNCED";
    private static final String client_worker_id = "WORKER_ID";


    //Visits Table
    private static final String visit_table = "CLIENT_VISITS";
    private static final String visit_id = "ID";
    private static final String visit_date = "VISIT_DATE";
    private static final String visit_purpose = "PURPOSE_OF_VISIT";
    private static final String if_cbr = "IF_CBR";
    private static final String visit_location = "LOCATION";
    private static final String visit_village_no = "VILLAGE_NUMBER";
    private static final String health_provided = "HEALTH_PROVIDED";
    private static final String health_goal_status = "HEALTH_GOAL_STATUS";
    private static final String health_outcome = "HEALTH_OUTCOME";
    private static final String education_provided = "EDU_PROVIDED";
    private static final String edu_goal_status = "EDU_GOAL_STATUS";
    private static final String education_outcome = "EDUCATION_OUTCOME";
    private static final String social_provided = "SOCIAL_PROVIDED";
    private static final String social_goal_status = "SOCIAL_GOAL_STATUS";
    private static final String social_outcome = "SOCIAL_OUTCOME";
    private static final String client_visit_id = "CLIENT_ID";

    //Referral table
    private static final String referral_table = "CLIENT_REFERRALS";
    private static final String referral_id = "ID";
    private static final String service_req = "SERVICE_REQUIRED";
    private static final String referral_photo = "REFERRAL_PHOTO";
    private static final String basic_or_inter = "BASIC_OR_INTERMEDIATE";
    private static final String hip_width = "HIP_WIDTH";
    private static final String has_wheelchair = "HAS_WHEELCHAIR";
    private static final String wheelchair_repairable = "WHEELCHAIR_REPAIRABLE";
    private static final String bring_to_centre = "BRING_TO_CENTRE";
    private static final String conditions = "CONDITIONS";
    private static final String injury_location_knee = "INJURY_LOCATION_KNEE";
    private static final String injury_location_elbow = "INJURY_LOCATION_ELBOW";
    private static final String referral_status = "REFERRAL_STATUS";
    private static final String referral_outcome = "REFERRAL_OUTCOME";
    private static final String client_referral_id = "CLIENT_ID";

    //Admin messages table
    //WORKER ID COLUMN
    private static final String admin_message_table = "ADMIN_MESSAGES";
    private static final String admin_id = "ADMIN_ID";
    private static final String message_title = "TITLE";
    private static final String message_date = "DATE";
    private static final String message_location = "LOCATION";
    private static final String admin_message = "MESSAGE";
    private static final String viewed_status = "IS_VIEWED";
    //IS_SYNCED COLUMN

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_worker_table = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3
                + " TEXT UNIQUE NOT NULL, " + COL_7 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " INTEGER PRIMARY KEY , "
                + COL_6 + " BOOLEAN NOT NULL DEFAULT 0);";
        db.execSQL(create_worker_table);

        String create_client_table = "CREATE TABLE " + client_table_name + " (" + client_id + " INTEGER PRIMARY KEY , "
                + client_consent + " BOOLEAN, " + client_date + " STRING, " + client_first_name + " TEXT, "
                + client_last_name + " TEXT, " + client_age + " INTEGER, " + client_gender + " TEXT, "

                + client_village_no + " INTEGER, "  + client_location + " TEXT, " + client_latitude + " DOUBLE, " + client_longitude + " DOUBLE, " + client_contact + " STRING, "+ client_caregiver_presence
                + " BOOLEAN, " + client_caregiver_number +" STRING, " + client_photo + " BLOB, "+ client_disability + " TEXT, " + client_heath_rate

                + " STRING, "+ client_health_requirement + " STRING, " + client_health_goal + " STRING, " + client_education_rate +" STRING, "
                + client_education_requirement + " STRING, " + client_education_goal  + " STRING, " + client_social_rate + " STRING, "
                + client_social_requirement + " STRING, " +  client_social_goal + " STRING, " + client_worker_id + " INTEGER DEFAULT -1, "+ is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_client_table);

        String create_visit_table = "CREATE TABLE "
                + visit_table + " (" + visit_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + visit_date + " STRING, "
                + visit_purpose + " STRING, " + if_cbr + " TEXT, " +  visit_location + " TEXT, " + visit_village_no + " INTEGER, "
                + health_provided + " TEXT, " + health_goal_status + " TEXT, " + health_outcome + " STRING, "
                + education_provided + " TEXT, " + edu_goal_status + " TEXT, " + education_outcome + " STRING, "
                + social_provided + " TEXT, " + social_goal_status + " TEXT, " + social_outcome + " STRING, "
                + client_visit_id + " INTEGER, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_visit_table);

        String create_referral_table = "CREATE TABLE "
                + referral_table + " (" + referral_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + service_req + " TEXT, "
                + referral_photo + " BLOB, " + basic_or_inter + " TEXT, " + hip_width + " REAL, " + has_wheelchair + " BOOLEAN, "
                + wheelchair_repairable + " BOOLEAN, " + bring_to_centre + " BOOLEAN, " + conditions + " TEXT, "
                + injury_location_knee + " TEXT, " + injury_location_elbow + " TEXT, " + referral_status + " TEXT, "
                + referral_outcome + " STRING, " + client_referral_id + " INTEGER, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_referral_table);

        String create_adminMessage_table = "CREATE TABLE "
                + admin_message_table + " (" + admin_id + " INTEGER PRIMARY KEY, " + message_title + " STRING, "
                + message_date + " STRING, " + message_location + " STRING, " + admin_message + " STRING, "
                + viewed_status + " INTEGER NOT NULL DEFAULT 0, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_adminMessage_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME );
        db.execSQL(" DROP TABLE IF EXISTS " + client_table_name );
        db.execSQL(" DROP TABLE IF EXISTS " + visit_table );
        db.execSQL(" DROP TABLE IF EXISTS " + referral_table );
        db.execSQL(" DROP TABLE IF EXISTS " + admin_message_table );

        onCreate(db);
    }

    public boolean registerWorker(CBRWorker cbrWorker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_1, cbrWorker.getFirstName());
        cv.put(COL_2, cbrWorker.getLastName());
        cv.put(COL_3, cbrWorker.getUsername());
        cv.put(COL_7, cbrWorker.getZone());
        cv.put(COL_4, cbrWorker.getPassword());
        //cv.put(COL_5, cbrWorker.getId()); Delete comment after solving same username bug
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean registerClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(client_consent, client.getConsentToInterview());
        cv.put(client_id, client.getId());
        cv.put(client_date, client.getDate());
        cv.put(client_first_name, client.getFirstName());
        cv.put(client_last_name, client.getLastName());
        cv.put(client_age, client.getAge());
        cv.put(client_gender, client.getGender());
        cv.put(client_village_no, client.getVillageNumber());
        cv.put(client_location, client.getLocation());
        cv.put(client_latitude, client.getLatitude());
        cv.put(client_longitude, client.getLongitude());
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
        cv.put(is_synced, client.getIsSynced());
        cv.put(client_photo, client.getPhoto());
        cv.put(client_worker_id, client.getClient_worker_id());

        long result = db.insert(client_table_name, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(client_consent, client.getConsentToInterview());
        cv.put(client_id, client.getId());
        cv.put(client_date, client.getDate());
        cv.put(client_first_name, client.getFirstName());
        cv.put(client_last_name, client.getLastName());
        cv.put(client_age, client.getAge());
        cv.put(client_gender, client.getGender());
        cv.put(client_village_no, client.getVillageNumber());
        cv.put(client_location, client.getLocation());
        cv.put(client_latitude, client.getLatitude());
        cv.put(client_longitude, client.getLongitude());
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
        cv.put(is_synced, client.getIsSynced());
        cv.put(client_photo, client.getPhoto());
        cv.put(client_worker_id, client.getClient_worker_id());

        long id = client.getId();
        String whereClause = client_id.concat(" = ");
        whereClause = whereClause.concat(Long.toString(id));

        long result = db.update(client_table_name,cv,whereClause, null);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean addVisit(Visit visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(visit_purpose, visit.getPurposeOfVisit());
        cv.put(visit_date, visit.getDate());
        cv.put(if_cbr, android.text.TextUtils.join(",", visit.getIfCbr()));
        cv.put(visit_location, visit.getLocation());
        cv.put(visit_village_no, visit.getVillageNumber());
        cv.put(health_provided, visit.healthProvToString());
        cv.put(health_goal_status, visit.getHealthGoalMet());
        cv.put(health_outcome, visit.getHealthIfConcluded());
        cv.put(education_provided, visit.eduProvToString());
        cv.put(edu_goal_status, visit.getEducationGoalMet());
        cv.put(education_outcome, visit.getEducationIfConcluded());
        cv.put(social_provided, visit.socialProvToString());
        cv.put(social_goal_status, visit.getSocialGoalMet());
        cv.put(social_outcome, visit.getSocialIfConcluded());
        cv.put(client_visit_id, visit.getClientID());
        cv.put(is_synced, visit.getIsSynced());

        long result = db.insert(visit_table, null, cv);
        if (result == -1 )
            return false;
        else
            return true;
    }

    public boolean addReferral(Referral referral) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //TODO: cv.put(referral_photo, referral.getReferralPhoto());

        cv.put(client_referral_id, referral.getClientID());
        String serviceType = referral.getServiceReq();

        if(serviceType.equals("Physiotherapy")){
            cv.put(service_req, referral.getServiceReq());
            String condition = referral.getCondition();
            if(condition.equalsIgnoreCase("Other")){
                String explanation = referral.getConditionOtherExplanation();
                cv.put(conditions, explanation);
            }
            else{
                cv.put(conditions,condition);
            }
            cv.put(has_wheelchair, false);
            cv.put(wheelchair_repairable, false);
            cv.put(bring_to_centre, false);
        }
        else if(serviceType.equals("Prosthetic")){
            cv.put(service_req, referral.getServiceReq());
            cv.put(injury_location_knee, referral.getInjuryLocation());
            cv.put(has_wheelchair, false);
            cv.put(wheelchair_repairable, false);
            cv.put(bring_to_centre, false);
        }
        else if(serviceType.equals("Orthotic")){
            cv.put(service_req, referral.getServiceReq());
            cv.put(injury_location_elbow, referral.getInjuryLocation());
            cv.put(has_wheelchair, false);
            cv.put(wheelchair_repairable, false);
            cv.put(bring_to_centre, false);
        }
        else if(serviceType.equals("Wheelchair")){
            cv.put(service_req, referral.getServiceReq());
            cv.put(basic_or_inter, referral.getBasicOrInter());
            cv.put(hip_width, referral.getHipWidth());
            Boolean hasWheelchair = referral.getHasWheelchair();
            cv.put(has_wheelchair, hasWheelchair);
            if(hasWheelchair){
                cv.put(wheelchair_repairable, referral.getWheelchairReparable());
                cv.put(bring_to_centre, referral.getBringToCentre());
            }
            else{
                cv.put(wheelchair_repairable, false);
                cv.put(bring_to_centre, false);
            }
        }
        else if(serviceType.equals("Other")){
            String otherExplanation = referral.getOtherExplanation();
            cv.put(service_req, otherExplanation);
            cv.put(has_wheelchair, false);
            cv.put(wheelchair_repairable, false);
            cv.put(bring_to_centre, false);
        }

        long result = db.insert(referral_table, null, cv);
        if (result == -1 )
            return false;
        else
            return true;
    }

    public boolean addMessage (AdminMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(message_title, message.getTitle());
        cv.put(message_date, message.getDate());
        cv.put(message_location, message.getLocation());
        cv.put(admin_message, message.getMessage());
        cv.put(viewed_status, message.getViewedStatus());
        cv.put(is_synced, message.getIsSynced());

        long result = db.insert(admin_message_table, null, cv);
        if (result == -1 )
            return false;
        else
            return true;
    }

    public boolean checkUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_3 + " = '" + email + "'";

        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        if (count > 0) {
            cursor.moveToFirst();
            String curPw = cursor.getString(cursor.getColumnIndex(COL_4));
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), curPw);

            if (result.verified == true) {
                db.close();
                cursor.close();
                return true;
            }
        }

        db.close();
        cursor.close();
        return false;
    }

    public int getWorkerId(String username){
        String query = "SELECT ID FROM " + TABLE_NAME + " WHERE " + COL_3 + " = '" + username + "';" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c!= null && c.getCount()>0) {
            c.moveToLast();
            return c.getInt(0);
        }
        else {
            return -1;
        }
    }

    public Cursor getAllVisits(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id, * FROM CLIENT_VISITS", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getVisits(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id, * FROM CLIENT_VISITS WHERE CLIENT_ID = " + id, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getVisit(long visit_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id, * FROM CLIENT_VISITS WHERE ID = " + visit_id, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllReferrals(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id, * FROM CLIENT_REFERRALS", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor executeQuery(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT rowid _id,* FROM CLIENT_DATA", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllRowsOfCBR() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT rowid _id,* FROM WORKER_DATA", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRow(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT rowid _id, * FROM CLIENT_DATA WHERE ID = "+ id, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getdata() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + " CLIENT_DATA ";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public Cursor getItemId(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + " CLIENT_DATA " + " Where Name" + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public boolean isAdmin (String username ){
        String query = "SELECT IS_ADMIN FROM " + TABLE_NAME + " WHERE " + COL_3 + " = '" + username + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            boolean is_admin = c.getInt(0) > 0; // convert int to boolean
            return is_admin;
        } else
            return false;
    }

    public Cursor getAllMessageInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT rowid _id,* FROM ADMIN_MESSAGES", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllCBRWorkers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT rowid _id,* FROM WORKER_DATA", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public int numberOfClientsPerUser(int worker_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(ID) FROM " + client_table_name + " WHERE " + client_worker_id + " = " + worker_id + ";";
        Cursor c = db.rawQuery(query, null);
        if(c!= null && c.getCount()>0) {
            c.moveToLast();
            return c.getInt(0);
        }
        else {
            return -1;
        }
    }


    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "SELECT rowid _id, * FROM WORKER_DATA ", null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }


}
