package com.example.cbr_manager.UI.clientInfoFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.R;

public class SurveyInfoActivity extends AppCompatActivity {
    private long id;
    private int position;

    SurveyManager surveyManager;
    Survey currentSurvey;

    public static final String R_SURVEY_ID_PASSED_IN = "r_survey_id_passed_in";
    public static final String R_SURVEY_POS_PASSED_IN = "r_survey_POS_passed_in";

    public static Intent makeIntent(Context context, long id, int position) {
        Intent intent =  new Intent(context, SurveyInfoActivity.class);
        intent.putExtra(R_SURVEY_ID_PASSED_IN, id);
        intent.putExtra(R_SURVEY_POS_PASSED_IN, position);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        this.position = intent.getIntExtra(R_SURVEY_POS_PASSED_IN, 0);
        this.id = intent.getLongExtra(R_SURVEY_ID_PASSED_IN, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_info);

        extractIntent();

        surveyManager = SurveyManager.getInstance(SurveyInfoActivity.this);
        currentSurvey = surveyManager.getSurveyById(this.id);

        setHealthTableValues();
        setEducationTableValues();
        setSocialTableValues();
        setLivelihoodTableValues();
        setFoodSecurityTableValues();
        setEmpowermentTableValues();
        setShelterAndCareTableValues();
    }

    private void setHealthTableValues(){
        TextView accessToRehab_yes = findViewById(R.id.health1yes);
        TextView accessToRehab_no = findViewById(R.id.health1no);
        TextView needAccessToRehab_yes = findViewById(R.id.health2yes);
        TextView needAccessToRehab_no = findViewById(R.id.health2no);
        TextView haveAssistiveDevice_yes = findViewById(R.id.health3yes);
        TextView haveAssistiveDevice_no = findViewById(R.id.health3no);
        TextView assistiveDeviceWorking_yes = findViewById(R.id.health4yes);
        TextView assistiveDeviceWorking_no = findViewById(R.id.health4no);
        TextView needAssistiveDevice_yes = findViewById(R.id.health5yes);
        TextView needAssistiveDevice_no = findViewById(R.id.health5no);
        TextView generalVeryPoor = findViewById(R.id.health6VeryPoor);
        TextView generalPoor = findViewById(R.id.health6poor);
        TextView generalFine = findViewById(R.id.health6fine);
        TextView generalGood = findViewById(R.id.health6good);
        TextView satisfiedVeryPoor = findViewById(R.id.health7VeryPoor);
        TextView satisfiedPoor = findViewById(R.id.health7poor);
        TextView satisfiedFine = findViewById(R.id.health7fine);
        TextView satisfiedGood = findViewById(R.id.health7good);
        TextView deviceCorner = findViewById(R.id.health8Corner);
        TextView deviceCrutch = findViewById(R.id.health8Crutch);
        TextView deviceGlasses = findViewById(R.id.health8Glasses);
        TextView deviceHearing = findViewById(R.id.health8Hearing);
        TextView deviceOrthotic = findViewById(R.id.health8Orthotic);
        TextView deviceProsthetic = findViewById(R.id.health8Prosthetic);
        TextView deviceStanding = findViewById(R.id.health8Standing);
        TextView deviceWalkingStick = findViewById(R.id.health8WalkingStick);
        TextView deviceWheelchair = findViewById(R.id.health8Wheelchair);

        String accessToRehab_yes_string;
        String accessToRehab_no_string;
        String needAccessToRehab_yes_string;
        String needAccessToRehab_no_string;
        String haveAssistiveDevice_yes_string;
        String haveAssistiveDevice_no_string;
        String assistiveDeviceWorking_yes_string;
        String assistiveDeviceWorking_no_string;
        String needAssistiveDevice_yes_string;
        String needAssistiveDevice_no_string;
        String generalVeryPoor_string;
        String generalPoor_string;
        String generalFine_string;
        String generalGood_string;
        String satisfiedVeryPoor_string;
        String satisfiedPoor_string;
        String satisfiedFine_string;
        String satisfiedGood_string;
        String deviceCorner_string;
        String deviceCrutch_string;
        String deviceGlasses_string;
        String deviceHearing_string;
        String deviceOrthotic_string;
        String deviceProsthetic_string;
        String deviceStanding_string;
        String deviceWalkingStick_string;
        String deviceWheelchair_string;

        if(currentSurvey.isHave_rehab_access()) {
            accessToRehab_yes_string = "X";
            accessToRehab_no_string = "";
        } else {
            accessToRehab_yes_string = "";
            accessToRehab_no_string = "X";
        }

        if(currentSurvey.isNeed_rehab_access()){
            needAccessToRehab_yes_string = "X";
            needAccessToRehab_no_string = "";
        }else{
            needAccessToRehab_yes_string = "";
            needAccessToRehab_no_string = "X";
        }

        if(currentSurvey.isHave_device()){
            haveAssistiveDevice_yes_string = "X";
            haveAssistiveDevice_no_string = "";
        }else{
            haveAssistiveDevice_yes_string = "";
            haveAssistiveDevice_no_string = "X";
        }

        if(currentSurvey.isDevice_condition()){
            assistiveDeviceWorking_yes_string = "X";
            assistiveDeviceWorking_no_string = "";
        }else{
            assistiveDeviceWorking_yes_string = "";
            assistiveDeviceWorking_no_string = "X";
        }

        if(currentSurvey.isDevice_condition()){
            needAssistiveDevice_yes_string = "X";
            needAssistiveDevice_no_string = "";
        }else{
            needAssistiveDevice_yes_string = "";
            needAssistiveDevice_no_string = "X";
        }

        if(currentSurvey.getHealth_condition() == 4) {
            generalVeryPoor_string = "X";
            generalPoor_string = "";
            generalFine_string = "";
            generalGood_string = "";
        } else if(currentSurvey.getHealth_condition() == 3) {
            generalVeryPoor_string = "";
            generalPoor_string = "X";
            generalFine_string = "";
            generalGood_string = "";
        }else if(currentSurvey.getHealth_condition() == 2) {
            generalVeryPoor_string = "";
            generalPoor_string = "";
            generalFine_string = "X";
            generalGood_string = "";
        } else {
            generalVeryPoor_string = "";
            generalPoor_string = "";
            generalFine_string = "";
            generalGood_string = "X";
        }

        if(currentSurvey.getIs_satisfied() == 4) {
            satisfiedVeryPoor_string = "X";
            satisfiedPoor_string = "";
            satisfiedFine_string = "";
            satisfiedGood_string = "";
        } else if (currentSurvey.getIs_satisfied() == 3) {
            satisfiedVeryPoor_string = "";
            satisfiedPoor_string = "X";
            satisfiedFine_string = "";
            satisfiedGood_string = "";
        } else if (currentSurvey.getIs_satisfied() == 2) {
            satisfiedVeryPoor_string = "";
            satisfiedPoor_string = "";
            satisfiedFine_string = "X";
            satisfiedGood_string = "";
        } else {
            satisfiedVeryPoor_string = "";
            satisfiedPoor_string = "";
            satisfiedFine_string = "";
            satisfiedGood_string = "X";
        }

        switch (currentSurvey.getDevice_type()) {
            case "Wheelchair":
                deviceCorner_string = "";
                deviceCrutch_string = "";
                deviceGlasses_string = "";
                deviceHearing_string = "";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "";
                deviceStanding_string = "";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "X";
                break;
            case "Prosthetic":
                deviceCorner_string = "";
                deviceCrutch_string = "";
                deviceGlasses_string = "";
                deviceHearing_string = "";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "X";
                deviceStanding_string = "";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "";
                break;
            case "Orthotic":
                deviceCorner_string = "";
                deviceCrutch_string = "";
                deviceGlasses_string = "";
                deviceHearing_string = "";
                deviceOrthotic_string = "X";
                deviceProsthetic_string = "";
                deviceStanding_string = "";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "";
                break;
            case "Crutch":
                deviceCorner_string = "";
                deviceCrutch_string = "X";
                deviceGlasses_string = "";
                deviceHearing_string = "";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "";
                deviceStanding_string = "";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "";
                break;
            case "Walking Stick":
                deviceCorner_string = "";
                deviceCrutch_string = "";
                deviceGlasses_string = "";
                deviceHearing_string = "";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "";
                deviceStanding_string = "";
                deviceWalkingStick_string = "X";
                deviceWheelchair_string = "";
                break;
            case "Hearing Aid":
                deviceCorner_string = "";
                deviceCrutch_string = "";
                deviceGlasses_string = "";
                deviceHearing_string = "X";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "";
                deviceStanding_string = "";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "";
                break;
            case "Glasses":
                deviceCorner_string = "";
                deviceCrutch_string = "";
                deviceGlasses_string = "X";
                deviceHearing_string = "";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "";
                deviceStanding_string = "";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "";
                break;
            case "Standing Frame":
                deviceCorner_string = "";
                deviceCrutch_string = "";
                deviceGlasses_string = "";
                deviceHearing_string = "";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "";
                deviceStanding_string = "X";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "";
                break;
            default:
                deviceCorner_string = "X";
                deviceCrutch_string = "";
                deviceGlasses_string = "";
                deviceHearing_string = "";
                deviceOrthotic_string = "";
                deviceProsthetic_string = "";
                deviceStanding_string = "";
                deviceWalkingStick_string = "";
                deviceWheelchair_string = "";
                break;
        }

        accessToRehab_yes.setText(accessToRehab_yes_string);
        accessToRehab_no.setText(accessToRehab_no_string);
        needAccessToRehab_yes.setText(needAccessToRehab_yes_string);
        needAccessToRehab_no.setText(needAccessToRehab_no_string);
        haveAssistiveDevice_yes.setText(haveAssistiveDevice_yes_string);
        haveAssistiveDevice_no.setText(haveAssistiveDevice_no_string);
        assistiveDeviceWorking_yes.setText(assistiveDeviceWorking_yes_string);
        assistiveDeviceWorking_no.setText(assistiveDeviceWorking_no_string);
        needAssistiveDevice_yes.setText(needAssistiveDevice_yes_string);
        needAssistiveDevice_no.setText(needAssistiveDevice_no_string);
        generalVeryPoor.setText(generalVeryPoor_string);
        generalPoor.setText(generalPoor_string);
        generalFine.setText(generalFine_string);
        generalGood.setText(generalGood_string);
        satisfiedVeryPoor.setText(satisfiedVeryPoor_string);
        satisfiedPoor.setText(satisfiedPoor_string);
        satisfiedFine.setText(satisfiedFine_string);
        satisfiedGood.setText(satisfiedGood_string);
        deviceCorner.setText(deviceCorner_string);
        deviceCrutch.setText(deviceCrutch_string);
        deviceGlasses.setText(deviceGlasses_string);
        deviceHearing.setText(deviceHearing_string);
        deviceOrthotic.setText(deviceOrthotic_string);
        deviceProsthetic.setText(deviceProsthetic_string);
        deviceStanding.setText(deviceStanding_string);
        deviceWalkingStick.setText(deviceWalkingStick_string);
        deviceWheelchair.setText(deviceWheelchair_string);
    }

    private void setEducationTableValues(){
        TextView goToSchool_yes = findViewById(R.id.education1yes);
        TextView goToSchool_no = findViewById(R.id.education1no);
        TextView gradeText = findViewById(R.id.education2);
        TextView beenToSchoolBefore_yes = findViewById(R.id.education3yes);
        TextView beenToSchoolBefore_no = findViewById(R.id.education3no);
        TextView wantToGoToSchool_yes = findViewById(R.id.education4yes);
        TextView wantToGoToSchool_no = findViewById(R.id.education4no);
        TextView whyNoSchoolDisability = findViewById(R.id.education5disability);
        TextView whyNoSchoolLack = findViewById(R.id.education5lack);
        TextView whyNoSchoolOther = findViewById(R.id.education5other);

        String wantToGoToSchool_yes_string;
        String wantToGoToSchool_no_string;
        String goToSchool_yes_string;
        String goToSchool_no_string;
        String beenToSchoolBefore_yes_string;
        String beenToSchoolBefore_no_string;
        String whyNoSchoolDisability_string;
        String whyNoSchoolLack_string;
        String whyNoSchoolOther_string;

        if(currentSurvey.isIs_student()){
            goToSchool_yes_string = "X";
            goToSchool_no_string = "";

//            byte grade = currentSurvey.getGrade_no();
//            gradeText.setText(grade);

            whyNoSchoolDisability_string = "";
            whyNoSchoolLack_string = "";
            whyNoSchoolOther_string = "";

            wantToGoToSchool_yes_string = "";
            wantToGoToSchool_no_string = "";

            beenToSchoolBefore_yes_string = "";
            beenToSchoolBefore_no_string = "";

        }else{
            goToSchool_yes_string = "";
            goToSchool_no_string = "X";

            gradeText.setText("");

            switch (currentSurvey.getReason_no_school()) {
                case "Lack of Funding":
                    whyNoSchoolDisability_string = "";
                    whyNoSchoolLack_string = "X";
                    whyNoSchoolOther_string = "";
                    break;
                case "My Disability Stops Me":
                    whyNoSchoolDisability_string = "X";
                    whyNoSchoolLack_string = "";
                    whyNoSchoolOther_string = "";
                    break;
                default:
                    whyNoSchoolDisability_string = "";
                    whyNoSchoolLack_string = "";
                    whyNoSchoolOther_string = "X";
                    break;
            }

            if(currentSurvey.isWant_school()){
                wantToGoToSchool_yes_string = "X";
                wantToGoToSchool_no_string = "";

            } else {
                wantToGoToSchool_yes_string = "";
                wantToGoToSchool_no_string = "X";
            }

            if(currentSurvey.isWas_student()){
                beenToSchoolBefore_yes_string = "X";
                beenToSchoolBefore_no_string = "";
            }else {
                beenToSchoolBefore_yes_string = "";
                beenToSchoolBefore_no_string = "X";
            }
        }

        goToSchool_yes.setText(goToSchool_yes_string);
        goToSchool_no.setText(goToSchool_no_string);
        beenToSchoolBefore_yes.setText(beenToSchoolBefore_yes_string);
        beenToSchoolBefore_no.setText(beenToSchoolBefore_no_string);
        wantToGoToSchool_yes.setText(wantToGoToSchool_yes_string);
        wantToGoToSchool_no.setText(wantToGoToSchool_no_string);
        whyNoSchoolDisability.setText(whyNoSchoolDisability_string);
        whyNoSchoolLack.setText(whyNoSchoolLack_string);
        whyNoSchoolOther.setText(whyNoSchoolOther_string);
    }

    private void setSocialTableValues(){
        TextView valuedCommunityMember_yes = findViewById(R.id.social1yes);
        TextView valuedCommunityMember_no = findViewById(R.id.social1no);
        TextView independent_yes = findViewById(R.id.social2yes);
        TextView independent_no = findViewById(R.id.social2no);
        TextView participateInEvents_yes = findViewById(R.id.social3yes);
        TextView participateInEvents_no = findViewById(R.id.social3no);
        TextView interactSocially_yes = findViewById(R.id.social4yes);
        TextView interactSocially_no = findViewById(R.id.social4no);
        TextView experienceDiscrimination_yes = findViewById(R.id.social5yes);
        TextView experienceDiscrimination_no = findViewById(R.id.social5no);

        String valuedCommunityMember_yes_string;
        String valuedCommunityMember_no_string;
        String independent_yes_string;
        String independent_no_string;
        String participateInEvents_yes_string;
        String participateInEvents_no_string;
        String interactSocially_yes_string;
        String interactSocially_no_string;
        String experienceDiscrimination_yes_string;
        String experienceDiscrimination_no_string;

        if(currentSurvey.isIs_valued()){
            valuedCommunityMember_yes_string = "X";
            valuedCommunityMember_no_string = "";
        }else{
            valuedCommunityMember_yes_string = "";
            valuedCommunityMember_no_string = "X";
        }

        if(currentSurvey.isIs_independent()){
            independent_yes_string = "X";
            independent_no_string = "";
        }else{
            independent_yes_string = "";
            independent_no_string = "X";
        }

        if(currentSurvey.isIs_social()){
            participateInEvents_yes_string = "X";
            participateInEvents_no_string = "";
        }else{
            participateInEvents_yes_string = "";
            participateInEvents_no_string = "X";
        }

        if(currentSurvey.isIs_socially_affected()){
            interactSocially_yes_string = "X";
            interactSocially_no_string = "";
        }else{
            interactSocially_yes_string = "";
            interactSocially_no_string = "X";
        }

        if(currentSurvey.isWas_discriminated()){
            experienceDiscrimination_yes_string = "X";
            experienceDiscrimination_no_string = "";
        }else{
            experienceDiscrimination_yes_string = "";
            experienceDiscrimination_no_string = "X";
        }

        valuedCommunityMember_yes.setText(valuedCommunityMember_yes_string);
        valuedCommunityMember_no.setText(valuedCommunityMember_no_string);
        independent_yes.setText(independent_yes_string);
        independent_no.setText(independent_no_string);
        participateInEvents_yes.setText(participateInEvents_yes_string);
        participateInEvents_no.setText(participateInEvents_no_string);
        interactSocially_yes.setText(interactSocially_yes_string);
        interactSocially_no.setText(interactSocially_no_string);
        experienceDiscrimination_yes.setText(experienceDiscrimination_yes_string);
        experienceDiscrimination_no.setText(experienceDiscrimination_no_string);
    }

    private void setLivelihoodTableValues() {
        TextView working_yes = findViewById(R.id.live1yes);
        TextView working_no = findViewById(R.id.live1no);
        TextView ifYes = findViewById(R.id.live2yes);
        TextView financialNeeds_yes = findViewById(R.id.live3yes);
        TextView financialNeeds_no = findViewById(R.id.live3no);
        TextView abilityToWork_yes = findViewById(R.id.live4yes);
        TextView abilityToWork_no = findViewById(R.id.live4no);
        TextView wantToWork_yes = findViewById(R.id.live5yes);
        TextView wantToWork_no = findViewById(R.id.live5no);
        TextView employed = findViewById(R.id.live6employed);
        TextView selfEmployed = findViewById(R.id.live6selfEmployed);

        String working_yes_string ;
        String working_no_string;
        String financialNeeds_yes_string;
        String financialNeeds_no_string;
        String abilityToWork_yes_string;
        String abilityToWork_no_string;
        String wantToWork_yes_string;
        String wantToWork_no_string;
        String employed_string;
        String selfEmployed_string;

        if(currentSurvey.isIs_working()){
            working_yes_string = "X";
            working_no_string = "";
        }else {
            working_yes_string = "";
            working_no_string = "X";
        }

        String workType = currentSurvey.getWork_type();
        ifYes.setText(workType);

        if(currentSurvey.isNeeds_met()){
            financialNeeds_yes_string = "X";
            financialNeeds_no_string = "";
        }else {
            financialNeeds_yes_string = "";
            financialNeeds_no_string = "X";
        }

        if(currentSurvey.isIs_work_affected()){
            abilityToWork_yes_string = "X";
            abilityToWork_no_string = "";
        }else {
            abilityToWork_yes_string = "";
            abilityToWork_no_string = "X";
        }

        if(currentSurvey.isWant_work()){
            wantToWork_yes_string = "X";
            wantToWork_no_string = "";
        }else{
            wantToWork_yes_string = "";
            wantToWork_no_string = "X";
        }

        if ("Self-Employed".equals(currentSurvey.getIs_self_employed())) {
            employed_string = "";
            selfEmployed_string = "X";
        } else {
            employed_string = "X";
            selfEmployed_string = "";
        }

        working_yes.setText(working_yes_string);
        working_no.setText(working_no_string);
        financialNeeds_yes.setText(financialNeeds_yes_string);
        financialNeeds_no.setText(financialNeeds_no_string);
        abilityToWork_yes.setText(abilityToWork_yes_string);
        abilityToWork_no.setText(abilityToWork_no_string);
        wantToWork_yes.setText(wantToWork_yes_string);
        wantToWork_no.setText(wantToWork_no_string);
        employed.setText(employed_string);
        selfEmployed.setText(selfEmployed_string);
    }

    private void setFoodSecurityTableValues(){
        TextView securityVeryPoor = findViewById(R.id.food1VeryPoor);
        TextView securityPoor = findViewById(R.id.food1poor);
        TextView securityFine = findViewById(R.id.food1fine);
        TextView securityGood = findViewById(R.id.food1good);
        TextView enoughFood_yes = findViewById(R.id.food2yes);
        TextView enoughFood_no = findViewById(R.id.food2no);
        TextView childMalnourished = findViewById(R.id.food3malnourished);
        TextView childUndernourished = findViewById(R.id.food3undernourished);
        TextView childWellnourished = findViewById(R.id.food3wellnourished);

        String securityVeryPoor_string;
        String securityPoor_string;
        String securityFine_string;
        String securityGood_string;
        String enoughFood_yes_string;
        String enoughFood_no_string;
        String childMalnourished_string;
        String childUndernourished_string;
        String childWellnourished_string;

        switch (currentSurvey.getFood_security()) {
            case "Good":
                securityVeryPoor_string = "";
                securityPoor_string = "";
                securityFine_string = "";
                securityGood_string = "X";
                break;
            case "Fine":
                securityVeryPoor_string = "";
                securityPoor_string = "";
                securityFine_string = "X";
                securityGood_string = "";
                break;
            case "Poor":
                securityVeryPoor_string = "";
                securityPoor_string = "X";
                securityFine_string = "";
                securityGood_string = "";
                break;
            default:
                securityVeryPoor_string = "X";
                securityPoor_string = "";
                securityFine_string = "";
                securityGood_string = "";
                break;
        }

        if(currentSurvey.isIs_diet_enough()) {
            enoughFood_yes_string = "X";
            enoughFood_no_string = "";
        } else {
            enoughFood_yes_string = "";
            enoughFood_no_string = "X";
        }

        // TODO check if child

//        switch (currentSurvey.getChild_condition()) {
//            case "Malnourished":
//                childMalnourished_string = "X";
//                childUndernourished_string = "";
//                childWellnourished_string = "";
//                break;
//            case "Undernourished":
//                childMalnourished_string = "";
//                childUndernourished_string = "X";
//                childWellnourished_string = "";
//                break;
//            default:
//                childMalnourished_string = "";
//                childUndernourished_string = "";
//                childWellnourished_string = "X";
//                break;
//        }

        securityVeryPoor.setText(securityVeryPoor_string);
        securityPoor.setText(securityPoor_string);
        securityFine.setText(securityFine_string);
        securityGood.setText(securityGood_string);
        enoughFood_yes.setText(enoughFood_yes_string);
        enoughFood_no.setText(enoughFood_no_string);
//        childMalnourished.setText(childMalnourished_string);
//        childUndernourished.setText(childUndernourished_string);
//        childWellnourished.setText(childWellnourished_string);
    }

    private void setEmpowermentTableValues(){
        TextView assistPeople_yes = findViewById(R.id.empower1yes);
        TextView assistPeople_no = findViewById(R.id.empower1no);
        TextView awareOfRights_yes = findViewById(R.id.empower2yes);
        TextView awareOfRights_no = findViewById(R.id.empower2no);
        TextView influencePeople_yes = findViewById(R.id.empower3yes);
        TextView influencePeople_no = findViewById(R.id.empower3no);

        String assistPeople_yes_string;
        String assistPeople_no_string;
        String awareOfRights_yes_string;
        String awareOfRights_no_string;
        String influencePeople_yes_string;
        String influencePeople_no_string;

        if(currentSurvey.isIs_member()) {
            assistPeople_yes_string = "X";
            assistPeople_no_string = "";
        } else {
            assistPeople_yes_string = "";
            assistPeople_no_string = "X";
        }

        if(currentSurvey.isIs_aware()) {
            awareOfRights_yes_string = "X";
            awareOfRights_no_string = "";
        } else {
            awareOfRights_yes_string = "";
            awareOfRights_no_string = "X";
        }

        if(currentSurvey.isIs_influence()) {
            influencePeople_yes_string = "X";
            influencePeople_no_string = "";
        } else {
            influencePeople_yes_string = "";
            influencePeople_no_string = "X";
        }

        assistPeople_yes.setText(assistPeople_yes_string);
        assistPeople_no.setText(assistPeople_no_string);
        awareOfRights_yes.setText(awareOfRights_yes_string);
        awareOfRights_no.setText(awareOfRights_no_string);
        influencePeople_yes.setText(influencePeople_yes_string);
        influencePeople_no.setText(influencePeople_no_string);
    }

    private void setShelterAndCareTableValues(){
        TextView adequateShelter_yes = findViewById(R.id.shelter1yes);
        TextView adequateShelter_no = findViewById(R.id.shelter1no);
        TextView essentialItems_yes = findViewById(R.id.shelter2yes);
        TextView essentialItems_no = findViewById(R.id.shelter2no);

        String adequateShelter_yes_string;
        String adequateShelter_no_string;
        String essentialItems_yes_string;
        String essentialItems_no_string;

        if(currentSurvey.isIs_shelter_adequate()) {
            adequateShelter_yes_string = "X";
            adequateShelter_no_string = "";
        } else {
            adequateShelter_yes_string = "";
            adequateShelter_no_string = "X";
        }

        if(currentSurvey.isItems_access()) {
            essentialItems_yes_string = "X";
            essentialItems_no_string = "";
        } else {
            essentialItems_yes_string = "";
            essentialItems_no_string = "X";
        }

        adequateShelter_yes.setText(adequateShelter_yes_string);
        adequateShelter_no.setText(adequateShelter_no_string);
        essentialItems_yes.setText(essentialItems_yes_string);
        essentialItems_no.setText(essentialItems_no_string);
    }
}