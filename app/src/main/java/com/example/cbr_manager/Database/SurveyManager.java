package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class SurveyManager {
    private List<Survey> surveyList = new ArrayList<>();
    private static SurveyManager instance;
    private DatabaseHelper databaseHelper;

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

    public static SurveyManager getInstance(Context context) {
        if (instance == null) {
            instance = new SurveyManager(context);
        }

        return instance;
    }

    public SurveyManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public List<Survey> getSurveyList() {
        return surveyList;
    }

    public void updateList() {
        Cursor c = databaseHelper.getAllRowsOfSurvey();

        int survey_idI = c.getColumnIndex(survey_id);
        int health_conditionI = c.getColumnIndex(survey_health_condition);
        int have_rehab_accessI = c.getColumnIndex(survey_have_rehab_access);
        int need_rehab_accessI = c.getColumnIndex(survey_need_rehab_access);
        int have_deviceI = c.getColumnIndex(survey_have_device);
        int device_conditionI = c.getColumnIndex(survey_device_condition);
        int need_deviceI = c.getColumnIndex(survey_need_device);
        int device_typeI = c.getColumnIndex(survey_device_type);
        int is_satisfiedI = c.getColumnIndex(survey_is_satisfied);
        int is_studentI = c.getColumnIndex(survey_is_student);
        int grade_noI = c.getColumnIndex(survey_grade_no);
        int reason_no_schoolI = c.getColumnIndex(survey_reason);
        int was_studentI = c.getColumnIndex(survey_was_student);
        int want_schoolI = c.getColumnIndex(survey_want_school);
        int is_valuedI = c.getColumnIndex(survey_is_valued);
        int is_independentI = c.getColumnIndex(survey_is_independent);
        int is_socialI = c.getColumnIndex(survey_is_social);
        int is_socially_affectedI = c.getColumnIndex(survey_is_socially_affected);
        int was_discriminatedI = c.getColumnIndex(survey_was_discriminated);
        int is_workingI = c.getColumnIndex(survey_is_working);
        int work_typeI = c.getColumnIndex(survey_work_type);
        int is_self_employedI = c.getColumnIndex(survey_is_self_employed);
        int needs_metI = c.getColumnIndex(survey_needs_met);
        int is_work_affectedI = c.getColumnIndex(survey_is_work_affected);
        int want_workI = c.getColumnIndex(survey_want_work);
        int food_securityI = c.getColumnIndex(survey_food_security);
        int is_diet_enoughI = c.getColumnIndex(survey_is_diet_enough);
        int child_conditionI = c.getColumnIndex(survey_child_condition);
        int referral_requiredI = c.getColumnIndex(survey_referral_required);
        int is_memberI = c.getColumnIndex(survey_is_member);
        int organisationI = c.getColumnIndex(survey_organisation);
        int is_awareI = c.getColumnIndex(survey_is_aware);
        int is_influenceI = c.getColumnIndex(survey_is_influence);
        int is_shelter_adequateI = c.getColumnIndex(survey_is_shelter_adequate);
        int items_accessI = c.getColumnIndex(survey_items_access);
        int client_idI = c.getColumnIndex(survey_client_id);
        int is_syncedI = c.getColumnIndex(survey_is_synced);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            long survey_id = c.getLong(survey_idI);
            byte health_condition = (byte) c.getInt(health_conditionI);
            boolean have_rehab_access = c.getInt(have_rehab_accessI) > 0;
            boolean need_rehab_access = c.getInt(need_rehab_accessI) > 0;
            boolean have_device = c.getInt(have_deviceI) > 0;
            boolean device_condition = c.getInt(device_conditionI) > 0;
            boolean need_device = c.getInt(need_deviceI) > 0;
            String device_type = c.getString(device_typeI);
            byte is_satisfied = (byte) c.getInt(is_satisfiedI);
            boolean is_student = c.getInt(is_studentI) > 0;
            byte grade_no = (byte) c.getInt(grade_noI);
            String reason_no_school = c.getString(reason_no_schoolI);
            boolean was_student = c.getInt(was_studentI) > 0;
            boolean want_school = c.getInt(want_schoolI) > 0;
            boolean is_valued = c.getInt(is_valuedI) > 0;
            boolean is_independent = c.getInt(is_independentI) > 0;
            boolean is_social = c.getInt(is_socialI) > 0;
            boolean is_socially_affected = c.getInt(is_socially_affectedI) > 0;
            boolean was_discriminated = c.getInt(was_discriminatedI) > 0;
            boolean is_working = c.getInt(is_workingI) > 0;
            String work_type = c.getString(work_typeI);
            String is_self_employed = c.getString(is_self_employedI);
            boolean needs_met = c.getInt(needs_metI) > 0;
            boolean is_work_affected = c.getInt(is_work_affectedI) > 0;
            boolean want_work = c.getInt(want_workI) > 0;
            String food_security = c.getString(food_securityI);
            boolean is_diet_enough = c.getInt(is_diet_enoughI) > 0;
            String child_condition = c.getString(child_conditionI);
            boolean referral_required = c.getInt(referral_requiredI) > 0;
            boolean is_member = c.getInt(is_memberI) > 0;
            String organisation = c.getString(organisationI);
            boolean is_aware = c.getInt(is_awareI) > 0;
            boolean is_influence = c.getInt(is_influenceI) > 0;
            boolean is_shelter_adequate = c.getInt(is_shelter_adequateI) > 0;
            boolean items_access = c.getInt(items_accessI) > 0;
            long client_id = c.getLong(client_idI);
            boolean is_synced = c.getInt(is_syncedI) > 0;

            Survey newSurvey = new Survey(survey_id, health_condition, have_rehab_access, need_rehab_access,
                    have_device, device_condition, need_device, device_type, is_satisfied,
                    is_student, grade_no, reason_no_school, was_student, want_school, is_valued,
                    is_independent, is_social, is_socially_affected, was_discriminated, is_working,
                    work_type, is_self_employed, needs_met, is_work_affected, want_work, food_security,
                    is_diet_enough, child_condition, referral_required, is_member, organisation, is_aware,
                    is_influence, is_shelter_adequate, items_access, client_id, is_synced);
            surveyList.add(newSurvey);

        }
    }

    public void addSurvey(Survey survey){
        surveyList.add(0, survey);
    }

    public void clear() {
        surveyList.clear();
    }

    public int size() {
        return surveyList.size();
    }

    public List<Survey> getSurveys(long id) {
        List<Survey> finalSurveys = new ArrayList<>();

        for (Survey currentSurvey : this.surveyList) {
            if(currentSurvey.getClient_id() == id) {
                finalSurveys.add(currentSurvey);
            }
        }
        return finalSurveys;
    }

    public Survey getSurveyById(long id) {
        for (Survey survey: surveyList) {
            if(survey.getId() == id) {
                return survey;
            }
        }
        return new Survey();
    }
}
