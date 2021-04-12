package com.example.cbr_manager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cbr.db";

    //Worker Table
    private static final String cbr_table = "WORKER_DATA";
    private static final String cbr_first_name = "FIRST_NAME";
    private static final String cbr_last_name = "LAST_NAME";
    private static final String cbr_username = "USERNAME";
    private static final String cbr_zone = "ZONE";
    private static final String cbr_password = "PASSWORD";
    private static final String cbr_id = "ID";
    private static final String cbr_is_admin = "IS_ADMIN";
    private static final String cbr_photo = "PHOTO";

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
    private static final String client_social_requirement = "SOCIAL_REQUIREMENT";
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

    //Baseline Survey Table
    private static final String survey_table = "CLIENT_SURVEYS";
    private static final String survey_id = "ID";
    private static final String survey_is_synced = "IS_SYNCED";
    private static final String survey_client_id = "CLIENT_ID";

    //Part I - Health
    private static final String survey_health_condition = "HEALTH_CONDITION";
    private static final String survey_have_rehab_access = "HAVE_REHAB_ACCESS";
    private static final String survey_need_rehab_access = "NEED_REHAB_ACCESS";
    private static final String survey_have_device = "HAVE_DEVICE";
    private static final String survey_device_condition = "DEVICE_CONDITION";
    private static final String survey_need_device = "NEED_DEVICE";
    private static final String survey_device_type = "DEVICE_TYPE";
    private static final String survey_is_satisfied = "IS_SATISFIED";

    //Part II - Education
    private static final String survey_is_student = "IS_STUDENT";
    private static final String survey_grade_no = "GRADE_NO";
    private static final String survey_reason = "REASON";
    private static final String survey_was_student = "WAS_STUDENT";
    private static final String survey_want_school = "WANT_SCHOOL";

    //Part III
    private static final String survey_is_valued = "IS_VALUED";
    private static final String survey_is_independent = "IS_INDEPENDENT";
    private static final String survey_is_social = "IS_SOCIAL";
    private static final String survey_is_socially_affected = "IS_SOCIALLY_AFFECTED";
    private static final String survey_was_discriminated = "WAS_DISCRIMINATED";

    //Part IV
    private static final String survey_is_working = "IS_WORKING";
    private static final String survey_work_type = "WORK_TYPE";
    private static final String survey_is_self_employed = "IS_SELF_EMPLOYED";
    private static final String survey_needs_met = "NEEDS_MET";
    private static final String survey_is_work_affected = "IS_WORK_AFFECTED";
    private static final String survey_want_work = "WANT_WORK";

    //Part V
    private static final String survey_food_security = "FOOD_SECURITY";
    private static final String survey_is_diet_enough = "IS_DIET_ENOUGH";
    private static final String survey_child_condition = "CHILD_CONDITION";
    private static final String survey_referral_required = "REFERRAL_REQUIRED";

    //Part VI
    private static final String survey_is_member = "IS_MEMBER";
    private static final String survey_organisation = "ORGANISATION";
    private static final String survey_is_aware = "IS_AWARE";
    private static final String survey_is_influence = "IS_INFLUENCE";

    //Part VII
    private static final String survey_is_shelter_adequate = "IS_SHELTER_ADEQUATE";
    private static final String survey_items_access = "ITEMS_ACCESS";

    //Admin messages table
    //WORKER ID COLUMN
    private static final String admin_message_table = "ADMIN_MESSAGES";
    private static final String admin_id = "ADMIN_ID";
    private static final String message_id = "ID";
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
        String create_worker_table = "CREATE TABLE " + cbr_table + " (" + cbr_first_name + " TEXT, " + cbr_last_name + " TEXT, " + cbr_username
                + " TEXT UNIQUE NOT NULL, " + cbr_zone + " TEXT, " + cbr_password + " TEXT, " + cbr_id + " INTEGER PRIMARY KEY , "
                + cbr_is_admin + " BOOLEAN NOT NULL DEFAULT 0, " + cbr_photo + " BLOB);";
        db.execSQL(create_worker_table);

        String create_client_table = "CREATE TABLE " + client_table_name + " (" + client_id + " INTEGER PRIMARY KEY , "
                + client_consent + " BOOLEAN, " + client_date + " STRING, " + client_first_name + " TEXT, "
                + client_last_name + " TEXT, " + client_age + " INTEGER, " + client_gender + " TEXT, "

                + client_village_no + " INTEGER, " + client_location + " TEXT, " + client_latitude + " DOUBLE, " + client_longitude + " DOUBLE, " + client_contact + " STRING, " + client_caregiver_presence
                + " BOOLEAN, " + client_caregiver_number + " STRING, " + client_photo + " BLOB, " + client_disability + " TEXT, " + client_heath_rate

                + " STRING, " + client_health_requirement + " STRING, " + client_health_goal + " STRING, " + client_education_rate + " STRING, "
                + client_education_requirement + " STRING, " + client_education_goal + " STRING, " + client_social_rate + " STRING, "
                + client_social_requirement + " STRING, " + client_social_goal + " STRING, " + client_worker_id + " INTEGER DEFAULT -1, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_client_table);

        String create_visit_table = "CREATE TABLE "
                + visit_table + " (" + visit_id + " INTEGER PRIMARY KEY, " + visit_date + " STRING, "
                + visit_purpose + " STRING, " + if_cbr + " TEXT, " + visit_location + " TEXT, " + visit_village_no + " INTEGER, "
                + health_provided + " TEXT, " + health_goal_status + " TEXT, " + health_outcome + " STRING, "
                + education_provided + " TEXT, " + edu_goal_status + " TEXT, " + education_outcome + " STRING, "
                + social_provided + " TEXT, " + social_goal_status + " TEXT, " + social_outcome + " STRING, "
                + client_visit_id + " INTEGER, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_visit_table);

        String create_referral_table = "CREATE TABLE "
                + referral_table + " (" + referral_id + " INTEGER PRIMARY KEY, " + service_req + " TEXT, "
                + referral_photo + " BLOB, " + basic_or_inter + " TEXT, " + hip_width + " REAL, " + has_wheelchair + " BOOLEAN, "
                + wheelchair_repairable + " BOOLEAN, " + bring_to_centre + " BOOLEAN, " + conditions + " TEXT, "
                + injury_location_knee + " TEXT, " + injury_location_elbow + " TEXT, " + referral_status + " TEXT, "
                + referral_outcome + " STRING, " + client_referral_id + " INTEGER, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_referral_table);

        String create_adminMessage_table = "CREATE TABLE "
                + admin_message_table + " (" + message_id + " INTEGER PRIMARY KEY, " + message_title + " STRING, "
                + message_date + " STRING, " + message_location + " STRING, " + admin_message + " STRING, " + admin_id + " INTEGER, "
                + viewed_status + " INTEGER NOT NULL DEFAULT 0, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(create_adminMessage_table);

        String create_survey_table = "CREATE TABLE " + survey_table + " (" + survey_id
                + " INTEGER PRIMARY KEY, " + survey_health_condition + " INTEGER, " + survey_have_rehab_access
                + " BOOLEAN, " + survey_need_rehab_access + " BOOLEAN, " + survey_have_device+ " BOOLEAN, " + survey_device_condition
                + " BOOLEAN, " + survey_need_device + " BOOLEAN, " + survey_device_type + " STRING, " + survey_is_satisfied
                + " INTEGER, " + survey_is_student + " BOOLEAN, " + survey_grade_no + " INTEGER, " + survey_reason
                + " STRING, " + survey_was_student + " BOOLEAN, " + survey_want_school + " BOOLEAN, " + survey_is_valued
                + " BOOLEAN, " + survey_is_independent + " BOOLEAN, " + survey_is_social + " BOOLEAN, " + survey_is_socially_affected
                + " BOOLEAN, " + survey_was_discriminated + " BOOLEAN, " + survey_is_working + " BOOLEAN, " + survey_work_type
                + " STRING, " + survey_is_self_employed + " STRING, " + survey_needs_met + " BOOLEAN, " + survey_is_work_affected
                + " BOOLEAN, " + survey_want_work + " BOOLEAN, " + survey_food_security + " STRING, " + survey_is_diet_enough
                + " BOOLEAN, " + survey_child_condition + " STRING, " + survey_referral_required + " BOOLEAN, " + survey_is_member
                + " BOOLEAN, " + survey_organisation + " STRING, " + survey_is_aware + " BOOLEAN, " + survey_is_influence
                + " BOOLEAN, " + survey_is_shelter_adequate + " BOOLEAN, " + survey_items_access + " BOOLEAN, " + survey_client_id
                + " INTEGER, " + survey_is_synced + " BOOLEAN NOT NULL DEFAULT 0);";
        db.execSQL(create_survey_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + cbr_table);
        db.execSQL(" DROP TABLE IF EXISTS " + client_table_name);
        db.execSQL(" DROP TABLE IF EXISTS " + visit_table);
        db.execSQL(" DROP TABLE IF EXISTS " + referral_table);
        db.execSQL(" DROP TABLE IF EXISTS " + admin_message_table);
        db.execSQL(" DROP TABLE IF EXISTS " + survey_table);

        onCreate(db);
    }

    public boolean registerWorker(CBRWorker cbrWorker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(cbr_id, cbrWorker.getId());
        cv.put(cbr_first_name, cbrWorker.getFirstName());
        cv.put(cbr_last_name, cbrWorker.getLastName());
        cv.put(cbr_username, cbrWorker.getUsername());
        cv.put(cbr_zone, cbrWorker.getZone());
        cv.put(cbr_password, cbrWorker.getPassword());
        cv.put(cbr_is_admin, cbrWorker.getIs_admin());

        long result = db.insert(cbr_table, null, cv);
        return result != -1;
    }

    public boolean updateWorker(CBRWorker cbrWorker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(cbr_first_name, cbrWorker.getFirstName());
        cv.put(cbr_last_name, cbrWorker.getLastName());
        cv.put(cbr_username, cbrWorker.getUsername());
        cv.put(cbr_zone, cbrWorker.getZone());
        cv.put(cbr_password, cbrWorker.getPassword());

        if ((cbrWorker.getPhoto() != null) && (cbrWorker.getPhoto().length != 0)) {
            cv.put(cbr_photo, cbrWorker.getPhoto());
        }

        long id = cbrWorker.getId();
        String whereClause = cbr_id.concat(" = ");
        whereClause = whereClause.concat(Long.toString(id));

        long result = db.update(cbr_table, cv, whereClause,null);
        return result != -1;
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

        if ((client.getPhoto() != null) && (client.getPhoto().length != 0)) {
            cv.put(client_photo, client.getPhoto());
        }

        cv.put(client_worker_id, client.getClient_worker_id());

        long result = db.insert(client_table_name, null, cv);
        return result != -1;
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

        if ((client.getPhoto() != null) && (client.getPhoto().length != 0)) {
            cv.put(client_photo, client.getPhoto());
        }

        cv.put(client_worker_id, client.getClient_worker_id());

        long id = client.getId();
        String whereClause = client_id.concat(" = ");
        whereClause = whereClause.concat(Long.toString(id));

        long result = db.update(client_table_name,cv,whereClause, null);
        return result != -1;
    }

    public boolean addVisit(Visit visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(visit_id, visit.getVisit_id());
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
        return result != -1;
    }

    public boolean addReferral(Referral referral) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(referral_id, referral.getId());
        cv.put(referral_outcome, referral.getOutcome());
        cv.put(client_referral_id, referral.getClientID());

        if ((referral.getReferralPhoto() != null) && (referral.getReferralPhoto().length != 0)) {
            cv.put(referral_photo, referral.getReferralPhoto());
        }

        String serviceType = referral.getServiceReq();

        switch (serviceType) {
            case "Physiotherapy":
                cv.put(service_req, referral.getServiceReq());
                String condition = referral.getCondition();
                if (condition.equalsIgnoreCase("Other")) {
                    String explanation = referral.getConditionOtherExplanation();
                    cv.put(conditions, explanation);
                } else {
                    cv.put(conditions, condition);
                }
                cv.put(has_wheelchair, false);
                cv.put(wheelchair_repairable, false);
                cv.put(bring_to_centre, false);
                break;
            case "Prosthetic":
                cv.put(service_req, referral.getServiceReq());
                cv.put(injury_location_knee, referral.getInjuryLocation());
                cv.put(has_wheelchair, false);
                cv.put(wheelchair_repairable, false);
                cv.put(bring_to_centre, false);
                break;
            case "Orthotic":
                cv.put(service_req, referral.getServiceReq());
                cv.put(injury_location_elbow, referral.getInjuryLocation());
                cv.put(has_wheelchair, false);
                cv.put(wheelchair_repairable, false);
                cv.put(bring_to_centre, false);
                break;
            case "Wheelchair":
                cv.put(service_req, referral.getServiceReq());
                cv.put(basic_or_inter, referral.getBasicOrInter());
                cv.put(hip_width, referral.getHipWidth());
                Boolean hasWheelchair = referral.getHasWheelchair();
                cv.put(has_wheelchair, hasWheelchair);
                if (hasWheelchair) {
                    cv.put(wheelchair_repairable, referral.getWheelchairReparable());
                    cv.put(bring_to_centre, referral.getBringToCentre());
                } else {
                    cv.put(wheelchair_repairable, false);
                    cv.put(bring_to_centre, false);
                }
                break;
            case "Other":
                String otherExplanation = referral.getOtherExplanation();
                cv.put(service_req, otherExplanation);
                cv.put(has_wheelchair, false);
                cv.put(wheelchair_repairable, false);
                cv.put(bring_to_centre, false);
                break;
        }

        cv.put(is_synced, referral.getIsSynced());

        long result = db.insert(referral_table, null, cv);
        return result != -1;
    }

    public boolean addMessage(AdminMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(message_id, message.getId());
        cv.put(message_title, message.getTitle());
        cv.put(message_date, message.getDate());
        cv.put(message_location, message.getLocation());
        cv.put(admin_message, message.getMessage());
        cv.put(admin_id, message.getAdminID());
        cv.put(viewed_status, message.getViewedStatus());
        cv.put(is_synced, message.getIsSynced());

        long result = db.insert(admin_message_table, null, cv);
        return result != -1;
    }

    public boolean addSurvey(Survey survey) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(survey_id, survey.getId());
        cv.put(survey_health_condition, survey.getHealth_condition());
        cv.put(survey_have_rehab_access, survey.isHave_rehab_access());
        cv.put(survey_need_rehab_access, survey.isNeed_rehab_access());
        cv.put(survey_have_device, survey.isHave_device());
        cv.put(survey_device_condition, survey.isDevice_condition());
        cv.put(survey_need_device, survey.isNeed_device());
        cv.put(survey_device_type, survey.getDevice_type());
        cv.put(survey_is_satisfied, survey.getIs_satisfied());
        cv.put(survey_is_student, survey.isIs_student());
        cv.put(survey_grade_no, survey.getGrade_no());
        cv.put(survey_reason, survey.getReason_no_school());
        cv.put(survey_was_student, survey.isWas_student());
        cv.put(survey_want_school, survey.isWant_school());
        cv.put(survey_is_valued, survey.isIs_valued());
        cv.put(survey_is_independent, survey.isIs_independent());
        cv.put(survey_is_social, survey.isIs_social());
        cv.put(survey_is_socially_affected, survey.isIs_socially_affected());
        cv.put(survey_was_discriminated, survey.isWas_discriminated());
        cv.put(survey_is_working, survey.isIs_working());
        cv.put(survey_work_type, survey.getWork_type());
        cv.put(survey_is_self_employed, survey.getIs_self_employed());
        cv.put(survey_needs_met, survey.isNeeds_met());
        cv.put(survey_is_work_affected, survey.isIs_work_affected());
        cv.put(survey_want_work, survey.isWant_work());
        cv.put(survey_food_security, survey.getFood_security());
        cv.put(survey_is_diet_enough, survey.isIs_diet_enough());
        cv.put(survey_child_condition, survey.getChild_condition());
        cv.put(survey_referral_required, survey.isReferral_required());
        cv.put(survey_is_member, survey.isIs_member());
        cv.put(survey_organisation, survey.getOrganisation());
        cv.put(survey_is_aware, survey.isIs_aware());
        cv.put(survey_is_influence, survey.isIs_influence());
        cv.put(survey_is_shelter_adequate, survey.isIs_shelter_adequate());
        cv.put(survey_items_access, survey.isItems_access());
        cv.put(survey_is_member, survey.isIs_member());
        cv.put(survey_client_id, survey.getClient_id());
        cv.put(survey_is_synced, survey.isIs_synced());

        long result = db.insert(survey_table, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + cbr_table + " WHERE " + cbr_username + " = '" + email + "'";

        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        if (count > 0) {
            cursor.moveToFirst();
            String curPw = cursor.getString(cursor.getColumnIndex(cbr_password));
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), curPw);

            if (result.verified) {
                db.close();
                cursor.close();
                return true;
            }
        }

        db.close();
        cursor.close();
        return false;
    }

    public int getWorkerId(String username) {
        String query = "SELECT ID FROM " + cbr_table + " WHERE " + cbr_username + " = '" + username + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            return c.getInt(0);
        } else {
            return -1;
        }
    }

    public Cursor getAllVisits() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id, * FROM CLIENT_VISITS", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllReferrals() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id, * FROM CLIENT_REFERRALS", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor executeQuery(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }

        return c;
    }

    public Cursor getAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id,* FROM CLIENT_DATA", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllRowsOfCBR() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id,* FROM WORKER_DATA", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllRowsOfSurvey() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =  db.rawQuery( "SELECT rowid _id,* FROM CLIENT_SURVEYS", null);
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

    public boolean isAdmin (String username ){
        String query = "SELECT IS_ADMIN FROM " + cbr_table + " WHERE " + cbr_username + " = '" + username + "';";
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
        Cursor c = db.rawQuery("SELECT rowid _id,* FROM ADMIN_MESSAGES", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public int numberOfClientsPerUser(int worker_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(ID) FROM " + client_table_name + " WHERE " + client_worker_id + " = " + worker_id + ";";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            return c.getInt(0);
        } else {
            return -1;
        }
    }

    public int numberOfVisitsPerClient(long client_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(ID) FROM " + visit_table + " WHERE " + client_visit_id + " = " + client_id + ";";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            return c.getInt(0);
        } else {
            return -1;
        }
    }

    public int numberOfReferralsPerClient(long client_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(ID) FROM " + referral_table + " WHERE " + client_referral_id + " = " + client_id + ";";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            return c.getInt(0);
        } else {
            return -1;
        }
    }


    public int numberOfMessagesPerAdmin(long adminID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(ID) FROM " + admin_message_table + " WHERE " + admin_id + " = " + adminID + ";";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            return c.getInt(0);
        } else {
            return -1;
        }
    }

    public int numberOfSurveysPerClient(long client_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(ID) FROM " + survey_table + " WHERE " + survey_client_id + " = " + client_id + ";";
        Cursor c = db.rawQuery(query, null);
        if(c!= null && c.getCount()>0) {
            c.moveToLast();
            return c.getInt(0);
        }
        else {
            return -1;
        }
    }

    public int numberOfUnreadMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(ID) FROM " + admin_message_table + " WHERE " + viewed_status + " = 0;";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            return c.getInt(0);
        } else {
            return -1;
        }
    }

    public int numberOfWorkers() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM " + cbr_table + ";";
        Cursor c = db.rawQuery(query, null);
        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            return c.getInt(0);
        } else {
            return -1;
        }
    }

    public boolean msgAlreadyExists(Long msgID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + admin_message_table + " WHERE " + message_id + " = " + msgID + ";";
        Cursor c = db.rawQuery(query, null);

        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            c.close();
            db.close();

            return true;
        }

        c.close();
        db.close();
        return false;
    }

    public void setStatusToRead() {
        String query = "UPDATE " + admin_message_table + " SET " + viewed_status + " = 1;";
        this.executeQuery(query);
    }


    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT rowid _id, * FROM WORKER_DATA ", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void resolveReferral(long referralId) {

        String query = "UPDATE CLIENT_REFERRALS " +
                "SET REFERRAL_OUTCOME = 'RESOLVED' " +
                "WHERE ID = " + referralId;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void undoResolveReferral(long referralId) {
        String query = "UPDATE CLIENT_REFERRALS " +
                "SET REFERRAL_OUTCOME = 'UNRESOLVED' " +
                "WHERE ID = " + referralId;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isResolved(long referralId) {
        String is_resolved = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT REFERRAL_OUTCOME FROM " + referral_table + " WHERE " + referral_id + " = " + referralId;
            Cursor c = db.rawQuery(query, null);
            c.moveToLast();
            is_resolved = c.getString(0);
            db.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return (is_resolved.equals("RESOLVED"));
    }

    public void setRefferalToNotSynced (long referralID) {
        String query = "UPDATE CLIENT_REFERRALS " +
                "SET IS_SYNCED = 0 " +
                "WHERE ID = " + referralID;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
