package com.example.cbr_manager.UI.statsFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.StatsActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaselineStatsFragment extends Fragment {

    private StatsActivity statsActivity;
    private SurveyManager surveyManager;
    private static final ArrayList<String> LABELS = new ArrayList<String>();


    public BaselineStatsFragment() {}

    public static BaselineStatsFragment newInstance() {
        BaselineStatsFragment fragment = new BaselineStatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.statsActivity = (StatsActivity) getActivity();
        surveyManager = SurveyManager.getInstance(statsActivity);
        View view = inflater.inflate(R.layout.fragment_baseline_stats, container, false);
        initializeGraphs(view);
        setHealthTableValues(view);
        setEducationTableValues(view);
        setSocialTableValues(view);
        setLivelihoodTableValues(view);
        setFoodSecurityTableValues(view);
        setEmpowermentTableValues(view);
        setShelterAndCareTableValues(view);
        return view;
    }

    private void createPieChart(PieChart graph, ArrayList<PieEntry> entries){
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(
                Color.parseColor("#C2BB00"), // yellow
                Color.parseColor("#1BB300"), // green2
                Color.parseColor("#B30003"), // red2
                Color.parseColor("#FFBB86FC"), // purple_200
                Color.parseColor("#FF03DAC5"), // teal_200
                Color.parseColor("#FC0032"), // red
                Color.parseColor("#56af31"), // green
                Color.parseColor("#FF3700B3"), // purple_700
                Color.parseColor("#863B8F") // purple
        );
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setValueLinePart1OffsetPercentage(100f);
        pieDataSet.setValueLinePart1Length(.6f);
        pieDataSet.setValueLinePart2Length(.6f);
        pieDataSet.setValueFormatter(new PercentFormatter());
        PieData data = new PieData(pieDataSet);
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);
        graph.setData(data);
        graph.getDescription().setEnabled(false);
        graph.setEntryLabelColor(Color.WHITE);
        graph.setEntryLabelTextSize(10f);
        graph.setDrawHoleEnabled(false);
        graph.setUsePercentValues(true);
        graph.getLegend().setWordWrapEnabled(true);
        graph.invalidate();
    }

    private void initializeGraphs(View view){
        PieChart graph = view.findViewById(R.id.baseline_health_device_graph);
        ArrayList<Integer> dataPoints = getDeviceTypeDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_generalHealth_graph);
        dataPoints = getGeneralHealthDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_health_satisfaction_graph);
        dataPoints = getHealthSatisfactionDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_whyDontTheyGoToSchool_graph);
        dataPoints = getWhyNotInSchoolDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_employed_graph);
        dataPoints = getIsEmployedDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_foodSecurity_graph);
        dataPoints = getFoodSecurityDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_isChild_graph);
        dataPoints = getChildConditionDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_grades_graph);
        dataPoints = getGradesOfStudentDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_workType_graph);
        dataPoints = getWorkTypeDataPoints();
        setDataPoints(graph, dataPoints);

        graph = view.findViewById(R.id.baseline_Organisation_graph);
        dataPoints = getOrganisationDataPoints();
        setDataPoints(graph, dataPoints);
    }

    private void setDataPoints(PieChart graph, ArrayList<Integer> deviceTypeDataPoints){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for(int i = 0; i < deviceTypeDataPoints.size(); i++){
            PieEntry pieEntry = new PieEntry(deviceTypeDataPoints.get(i), LABELS.get(i));
            entries.add(pieEntry);
        }
        createPieChart(graph, entries);
    }

    private ArrayList<Integer> getDeviceTypeDataPoints(){
        HashMap<String, Integer> deviceTypeCount = new HashMap<>();
        ArrayList<Integer> deviceTypesDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            String deviceType = survey.getDevice_type();
            if(deviceTypeCount.containsKey(deviceType)){
                Integer count = deviceTypeCount.get(deviceType);
                deviceTypeCount.put(deviceType, count+1);
            }else{
                deviceTypeCount.put(deviceType, 1);
            }
        }

        for(Map.Entry<String, Integer> entry : deviceTypeCount.entrySet()){
            deviceTypesDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return deviceTypesDataPoints;
    }

    private ArrayList<Integer> getGeneralHealthDataPoints(){
        HashMap<String, Integer> generalHealthCount = new HashMap<>();
        ArrayList<Integer> generalHealthDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            byte generalHealth = survey.getHealth_condition();
            String generalHealthString = "";

            if(generalHealth == 1){
                generalHealthString = "Very Poor";
            }else if(generalHealth == 2){
                generalHealthString = "Poor";
            }else if(generalHealth == 3){
                generalHealthString = "Fine";
            }else if(generalHealth == 4){
                generalHealthString = "Good";
            }

            if(!generalHealthString.equals("")) {
                if (generalHealthCount.containsKey(generalHealthString)) {
                    Integer count = generalHealthCount.get(generalHealthString);
                    generalHealthCount.put(generalHealthString, count + 1);
                } else {
                    generalHealthCount.put(generalHealthString, 1);
                }
            }
        }

        for(Map.Entry<String, Integer> entry : generalHealthCount.entrySet()){
            generalHealthDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return generalHealthDataPoints;
    }

    private ArrayList<Integer> getHealthSatisfactionDataPoints(){
        HashMap<String, Integer> isSatisfiedCount = new HashMap<>();
        ArrayList<Integer> isSatisfiedDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            byte is_satisfied = survey.getIs_satisfied();
            String is_satisfiedString = "";

            if(is_satisfied == 1){
                is_satisfiedString = "Very Poor";
            }else if(is_satisfied == 2){
                is_satisfiedString = "Poor";
            }else if(is_satisfied == 3){
                is_satisfiedString = "Fine";
            }else if(is_satisfied == 4){
                is_satisfiedString = "Good";
            }

            if(!is_satisfiedString.equals("")) {
                if (isSatisfiedCount.containsKey(is_satisfiedString)) {
                    Integer count = isSatisfiedCount.get(is_satisfiedString);
                    isSatisfiedCount.put(is_satisfiedString, count + 1);
                } else {
                    isSatisfiedCount.put(is_satisfiedString, 1);
                }
            }
        }

        for(Map.Entry<String, Integer> entry : isSatisfiedCount.entrySet()){
            isSatisfiedDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return isSatisfiedDataPoints;
    }

    private ArrayList<Integer> getWhyNotInSchoolDataPoints() {
        HashMap<String, Integer> reasonNotInSchoolCount = new HashMap<>();
        ArrayList<Integer> reasonNotInSchoolDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            String reasonNotInSchool = survey.getReason_no_school();
            if(reasonNotInSchoolCount.containsKey(reasonNotInSchool)){
                Integer count = reasonNotInSchoolCount.get(reasonNotInSchool);
                reasonNotInSchoolCount.put(reasonNotInSchool, count+1);
            }else{
                reasonNotInSchoolCount.put(reasonNotInSchool, 1);
            }
        }

        for(Map.Entry<String, Integer> entry : reasonNotInSchoolCount.entrySet()){
            reasonNotInSchoolDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return reasonNotInSchoolDataPoints;
    }

    private ArrayList<Integer> getWorkTypeDataPoints(){
        HashMap<String, Integer> EmploymentCount = new HashMap<>();
        ArrayList<Integer> employmentDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            String work_type = survey.getWork_type();
            if (EmploymentCount.containsKey(work_type)) {
                Integer count = EmploymentCount.get(work_type);
                EmploymentCount.put(work_type, count + 1);
            } else {
                EmploymentCount.put(work_type, 1);
            }
        }

        for(Map.Entry<String, Integer> entry : EmploymentCount.entrySet()){
            employmentDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return employmentDataPoints;
    }

    private ArrayList<Integer> getIsEmployedDataPoints() {
        HashMap<String, Integer> selfEmployedCount = new HashMap<>();
        ArrayList<Integer> selfEmployedDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            String selfEmployed = survey.getIs_self_employed();
            if(selfEmployedCount.containsKey(selfEmployed)){
                Integer count = selfEmployedCount.get(selfEmployed);
                selfEmployedCount.put(selfEmployed, count+1);
            }else{
                selfEmployedCount.put(selfEmployed, 1);
            }
        }

        for(Map.Entry<String, Integer> entry : selfEmployedCount.entrySet()){
            selfEmployedDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return selfEmployedDataPoints;
    }

    private ArrayList<Integer> getFoodSecurityDataPoints(){
        HashMap<String, Integer> foodSecurityCount = new HashMap<>();
        ArrayList<Integer> foodSecurityDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            String foodSecurity = survey.getFood_security();
            if(foodSecurityCount.containsKey(foodSecurity)){
                Integer count = foodSecurityCount.get(foodSecurity);
                foodSecurityCount.put(foodSecurity, count+1);
            }else{
                foodSecurityCount.put(foodSecurity, 1);
            }
        }

        for(Map.Entry<String, Integer> entry : foodSecurityCount.entrySet()){
            foodSecurityDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return foodSecurityDataPoints;
    }

    private ArrayList<Integer> getChildConditionDataPoints(){
        HashMap<String, Integer> childConditionCount = new HashMap<>();
        ArrayList<Integer> childConditionDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            String child_condition = survey.getChild_condition();
            if(child_condition != null){
                if (childConditionCount.containsKey(child_condition)) {
                    Integer count = childConditionCount.get(child_condition);
                    childConditionCount.put(child_condition, count + 1);
                } else {
                    childConditionCount.put(child_condition, 1);
                }
            }
        }

        for(Map.Entry<String, Integer> entry : childConditionCount.entrySet()){
            childConditionDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return childConditionDataPoints;
    }

    private ArrayList<Integer> getGradesOfStudentDataPoints() {
        HashMap<String, Integer> gradeCount = new HashMap<>();
        ArrayList<Integer> gradeDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for (Survey survey : surveyManager.getSurveyList()) {
            int grade_no = survey.getGrade_no();
            if (grade_no != 0) {
                String grade_no_string = String.valueOf(grade_no);
                if (gradeCount.containsKey(grade_no_string)) {
                    Integer count = gradeCount.get(grade_no_string);
                    gradeCount.put(grade_no_string, count + 1);
                } else {
                    gradeCount.put(grade_no_string, 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : gradeCount.entrySet()) {
            gradeDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return gradeDataPoints;
    }

    private ArrayList<Integer> getOrganisationDataPoints(){
        HashMap<String, Integer> organisationCount = new HashMap<>();
        ArrayList<Integer> organisationDataPoints = new ArrayList<Integer>();
        LABELS.clear();

        for(Survey survey : surveyManager.getSurveyList()){
            String organisation = survey.getOrganisation();
            if(organisation != null){
                if (organisationCount.containsKey(organisation)) {
                    Integer count = organisationCount.get(organisation);
                    organisationCount.put(organisation, count + 1);
                } else {
                    organisationCount.put(organisation, 1);
                }
            }
        }

        for(Map.Entry<String, Integer> entry : organisationCount.entrySet()){
            organisationDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return organisationDataPoints;
    }

    private void setHealthTableValues(View view){
        TextView accessToRehab_yes = view.findViewById(R.id.accessToRehab_yes);
        TextView accessToRehab_no = view.findViewById(R.id.accessToRehab_no);
        TextView needAccessToRehab_yes = view.findViewById(R.id.needAccessToRehab_yes);
        TextView needAccessToRehab_no = view.findViewById(R.id.needAccessToRehab_no);
        TextView haveAssistiveDevice_yes = view.findViewById(R.id.haveAssistiveDevice_yes);
        TextView haveAssistiveDevice_no = view.findViewById(R.id.haveAssistiveDevice_no);
        TextView assistiveDeviceWorking_yes = view.findViewById(R.id.assistiveDeviceWorking_yes);
        TextView assistiveDeviceWorking_no = view.findViewById(R.id.assistiveDeviceWorking_no);
        TextView needAssistiveDevice_yes = view.findViewById(R.id.needAssistiveDevice_yes);
        TextView needAssistiveDevice_no = view.findViewById(R.id.needAssistiveDevice_no);
        int accessToRehab_yes_count = 0;
        int accessToRehab_no_count = 0;
        int needAccessToRehab_yes_count = 0;
        int needAccessToRehab_no_count = 0;
        int haveAssistiveDevice_yes_count = 0;
        int haveAssistiveDevice_no_count = 0;
        int assistiveDeviceWorking_yes_count = 0;
        int assistiveDeviceWorking_no_count = 0;
        int needAssistiveDevice_yes_count = 0;
        int needAssistiveDevice_no_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isHave_rehab_access()){
                accessToRehab_yes_count++;
            }else{
                accessToRehab_no_count++;
            }

            if(survey.isNeed_rehab_access()){
                needAccessToRehab_yes_count++;
            }else{
                needAccessToRehab_no_count++;
            }

            if(survey.isHave_device()){
                haveAssistiveDevice_yes_count++;
            }else{
                haveAssistiveDevice_no_count++;
            }

            if(survey.isDevice_condition()){
                assistiveDeviceWorking_yes_count++;
            }else{
                assistiveDeviceWorking_no_count++;
            }

            if(survey.isDevice_condition()){
                needAssistiveDevice_yes_count++;
            }else{
                needAssistiveDevice_no_count++;
            }

            totalSurveys++;
        }

        String accessToRehab_yes_string = "0%";
        String accessToRehab_no_string = "0%";
        String needAccessToRehab_yes_string = "0%";
        String needAccessToRehab_no_string = "0%";
        String haveAssistiveDevice_yes_string = "0%";
        String haveAssistiveDevice_no_string = "0%";
        String assistiveDeviceWorking_yes_string = "0%";
        String assistiveDeviceWorking_no_string = "0%";
        String needAssistiveDevice_yes_string = "0%";
        String needAssistiveDevice_no_string = "0%";

        if(totalSurveys != 0){
            int valuedCommunityMember_yes_percent = (int) Math.round(((double)accessToRehab_yes_count/totalSurveys)*100);
            accessToRehab_yes_string = valuedCommunityMember_yes_percent + "%";

            int accessToRehab_no_percent = (int) Math.round(((double)accessToRehab_no_count/totalSurveys)*100);
            accessToRehab_no_string = accessToRehab_no_percent + "%";

            int needAccessToRehab_yes_percent = (int) Math.round(((double)needAccessToRehab_yes_count/totalSurveys)*100);
            needAccessToRehab_yes_string = needAccessToRehab_yes_percent + "%";

            int needAccessToRehab_no_percent = (int) Math.round(((double)needAccessToRehab_no_count/totalSurveys)*100);
            needAccessToRehab_no_string = needAccessToRehab_no_percent + "%";

            int haveAssistiveDevice_yes_percent = (int) Math.round(((double)haveAssistiveDevice_yes_count/totalSurveys)*100);
            haveAssistiveDevice_yes_string = haveAssistiveDevice_yes_percent + "%";

            int haveAssistiveDevice_no_percent = (int) Math.round(((double)haveAssistiveDevice_no_count/totalSurveys)*100);
            haveAssistiveDevice_no_string = haveAssistiveDevice_no_percent + "%";

            int assistiveDeviceWorking_yes_percent = (int) Math.round(((double)assistiveDeviceWorking_yes_count/totalSurveys)*100);
            assistiveDeviceWorking_yes_string = assistiveDeviceWorking_yes_percent + "%";

            int assistiveDeviceWorking_no_percent = (int) Math.round(((double)assistiveDeviceWorking_no_count/totalSurveys)*100);
            assistiveDeviceWorking_no_string = assistiveDeviceWorking_no_percent + "%";

            int needAssistiveDevice_yes_percent = (int) Math.round(((double)needAssistiveDevice_yes_count/totalSurveys)*100);
            needAssistiveDevice_yes_string = needAssistiveDevice_yes_percent + "%";

            int needAssistiveDevice_no_percent = (int) Math.round(((double)needAssistiveDevice_no_count/totalSurveys)*100);
            needAssistiveDevice_no_string = needAssistiveDevice_no_percent + "%";
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
    }

    private void setEducationTableValues(View view){
        TextView goToSchool_yes = view.findViewById(R.id.goToSchool_yes);
        TextView goToSchool_no = view.findViewById(R.id.goToSchool_no);
        TextView beenToSchoolBefore_yes = view.findViewById(R.id.beenToSchoolBefore_yes);
        TextView beenToSchoolBefore_no = view.findViewById(R.id.beenToSchoolBefore_no);
        TextView wantToGoToSchool_yes = view.findViewById(R.id.wantToGoToSchool_yes);
        TextView wantToGoToSchool_no = view.findViewById(R.id.wantToGoToSchool_no);
        int goToSchool_yes_count = 0;
        int goToSchool_no_count = 0;
        int beenToSchoolBefore_yes_count = 0;
        int beenToSchoolBefore_no_count = 0;
        int wantToGoToSchool_yes_count = 0;
        int wantToGoToSchool_no_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_student()){
                goToSchool_yes_count++;
            }else{
                goToSchool_no_count++;
            }

            if(survey.isWas_student()){
                beenToSchoolBefore_yes_count++;
            }else{
                beenToSchoolBefore_no_count++;
            }

            if(survey.isWant_school()){
                wantToGoToSchool_yes_count++;
            }else{
                wantToGoToSchool_no_count++;
            }

            totalSurveys++;
        }

        String goToSchool_yes_string = "0%";
        String goToSchool_no_string = "0%";
        String beenToSchoolBefore_yes_string = "0%";
        String beenToSchoolBefore_no_string = "0%";
        String wantToGoToSchool_yes_string = "0%";
        String wantToGoToSchool_no_string = "0%";

        if(totalSurveys != 0){
            int goToSchool_yes_percent = (int) Math.round(((double)goToSchool_yes_count/totalSurveys)*100);
            goToSchool_yes_string = goToSchool_yes_percent + "%";

            int goToSchool_no_percent = (int) Math.round(((double)goToSchool_no_count/totalSurveys)*100);
            goToSchool_no_string = goToSchool_no_percent + "%";

            int beenToSchoolBefore_yes_percent = (int) Math.round(((double)beenToSchoolBefore_yes_count/totalSurveys)*100);
            beenToSchoolBefore_yes_string = beenToSchoolBefore_yes_percent + "%";

            int beenToSchoolBefore_no_percent = (int) Math.round(((double)beenToSchoolBefore_no_count/totalSurveys)*100);
            beenToSchoolBefore_no_string = beenToSchoolBefore_no_percent + "%";

            int wantToGoToSchool_yes_percent = (int) Math.round(((double)wantToGoToSchool_yes_count/totalSurveys)*100);
            wantToGoToSchool_yes_string = wantToGoToSchool_yes_percent + "%";

            int wantToGoToSchool_no_percent = (int) Math.round(((double)wantToGoToSchool_no_count/totalSurveys)*100);
            wantToGoToSchool_no_string = wantToGoToSchool_no_percent + "%";
        }

        goToSchool_yes.setText(goToSchool_yes_string);
        goToSchool_no.setText(goToSchool_no_string);
        beenToSchoolBefore_yes.setText(beenToSchoolBefore_yes_string);
        beenToSchoolBefore_no.setText(beenToSchoolBefore_no_string);
        wantToGoToSchool_yes.setText(wantToGoToSchool_yes_string);
        wantToGoToSchool_no.setText(wantToGoToSchool_no_string);
    }

    private void setSocialTableValues(View view){
        TextView valuedCommunityMember_yes = view.findViewById(R.id.valuedCommunityMember_yes);
        TextView valuedCommunityMember_no = view.findViewById(R.id.valuedCommunityMember_no);
        TextView independent_yes = view.findViewById(R.id.independent_yes);
        TextView independent_no = view.findViewById(R.id.independent_no);
        TextView participateInEvents_yes = view.findViewById(R.id.participateInEvents_yes);
        TextView participateInEvents_no = view.findViewById(R.id.participateInEvents_no);
        TextView interactSocially_yes = view.findViewById(R.id.interactSocially_yes);
        TextView interactSocially_no = view.findViewById(R.id.interactSocially_no);
        TextView experienceDiscrimination_yes = view.findViewById(R.id.experienceDiscrimination_yes);
        TextView experienceDiscrimination_no = view.findViewById(R.id.experienceDiscrimination_no);
        int valuedCommunityMember_yes_count = 0;
        int valuedCommunityMember_no_count = 0;
        int independent_yes_count = 0;
        int independent_no_count = 0;
        int participateInEvents_yes_count = 0;
        int participateInEvents_no_count = 0;
        int interactSocially_yes_count = 0;
        int interactSocially_no_count = 0;
        int experienceDiscrimination_yes_count = 0;
        int experienceDiscrimination_no_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_valued()){
                valuedCommunityMember_yes_count++;
            }else{
                valuedCommunityMember_no_count++;
            }

            if(survey.isIs_independent()){
                independent_yes_count++;
            }else{
                independent_no_count++;
            }

            if(survey.isIs_social()){
                participateInEvents_yes_count++;
            }else{
                participateInEvents_no_count++;
            }

            if(survey.isIs_socially_affected()){
                interactSocially_yes_count++;
            }else{
                interactSocially_no_count++;
            }

            if(survey.isWas_discriminated()){
                experienceDiscrimination_yes_count++;
            }else{
                experienceDiscrimination_no_count++;
            }

            totalSurveys++;
        }

        String valuedCommunityMember_yes_string = "0%";
        String valuedCommunityMember_no_string = "0%";
        String independent_yes_string = "0%";
        String independent_no_string = "0%";
        String participateInEvents_yes_string = "0%";
        String participateInEvents_no_string = "0%";
        String interactSocially_yes_string = "0%";
        String interactSocially_no_string = "0%";
        String experienceDiscrimination_yes_string = "0%";
        String experienceDiscrimination_no_string = "0%";

        if(totalSurveys != 0){
            int valuedCommunityMember_yes_percent = (int) Math.round(((double)valuedCommunityMember_yes_count/totalSurveys)*100);
            valuedCommunityMember_yes_string = valuedCommunityMember_yes_percent + "%";

            int valuedCommunityMember_no_percent = (int) Math.round(((double)valuedCommunityMember_no_count/totalSurveys)*100);
            valuedCommunityMember_no_string = valuedCommunityMember_no_percent + "%";

            int independent_yes_percent = (int) Math.round(((double)independent_yes_count/totalSurveys)*100);
            independent_yes_string = independent_yes_percent + "%";

            int independent_no_count_percent = (int) Math.round(((double)independent_no_count/totalSurveys)*100);
            independent_no_string = independent_no_count_percent + "%";

            int participateInEvents_yes_percent = (int) Math.round(((double)participateInEvents_yes_count/totalSurveys)*100);
            participateInEvents_yes_string = participateInEvents_yes_percent + "%";

            int participateInEvents_no_percent = (int) Math.round(((double)participateInEvents_no_count/totalSurveys)*100);
            participateInEvents_no_string = participateInEvents_no_percent + "%";

            int interactSocially_yes_percent = (int) Math.round(((double)interactSocially_yes_count/totalSurveys)*100);
            interactSocially_yes_string = interactSocially_yes_percent + "%";

            int interactSocially_no_percent = (int) Math.round(((double)interactSocially_no_count/totalSurveys)*100);
            interactSocially_no_string = interactSocially_no_percent + "%";

            int experienceDiscrimination_yes_percent = (int) Math.round(((double)experienceDiscrimination_yes_count/totalSurveys)*100);
            experienceDiscrimination_yes_string = experienceDiscrimination_yes_percent + "%";

            int experienceDiscrimination_no_percent = (int) Math.round(((double)experienceDiscrimination_no_count/totalSurveys)*100);
            experienceDiscrimination_no_string = experienceDiscrimination_no_percent + "%";
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

    private void setLivelihoodTableValues(View view) {
        TextView working_yes = view.findViewById(R.id.working_yes);
        TextView working_no = view.findViewById(R.id.working_no);
        TextView financialNeeds_yes = view.findViewById(R.id.financialNeeds_yes);
        TextView financialNeeds_no = view.findViewById(R.id.financialNeeds_no);
        TextView abilityToWork_yes = view.findViewById(R.id.abilityToWork_yes);
        TextView abilityToWork_no = view.findViewById(R.id.abilityToWork_no);
        TextView wantToWork_yes = view.findViewById(R.id.wantToWork_yes);
        TextView wantToWork_no = view.findViewById(R.id.wantToWork_no);
        int working_yes_count = 0;
        int working_no_count = 0;
        int financialNeeds_yes_count = 0;
        int financialNeeds_no_count = 0;
        int abilityToWork_yes_count = 0;
        int abilityToWork_no_count = 0;
        int wantToWork_yes_count = 0;
        int wantToWork_no_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_working()){
                working_yes_count++;
            }else{
                working_no_count++;
            }

            if(survey.isNeeds_met()){
                financialNeeds_yes_count++;
            }else{
                financialNeeds_no_count++;
            }

            if(survey.isIs_work_affected()){
                abilityToWork_yes_count++;
            }else{
                abilityToWork_no_count++;
            }

            if(survey.isWant_work()){
                wantToWork_yes_count++;
            }else{
                wantToWork_no_count++;
            }

            totalSurveys++;
        }

        String working_yes_string = "0%";
        String working_no_string = "0%";
        String financialNeeds_yes_string = "0%";
        String financialNeeds_no_string = "0%";
        String abilityToWork_yes_string = "0%";
        String abilityToWork_no_string = "0%";
        String wantToWork_yes_string = "0%";
        String wantToWork_no_string = "0%";

        if(totalSurveys != 0){
            int working_yes_percent = (int) Math.round(((double)working_yes_count/totalSurveys)*100);
            working_yes_string = working_yes_percent + "%";

            int working_no_percent = (int) Math.round(((double)working_no_count/totalSurveys)*100);
            working_no_string = working_no_percent + "%";

            int financialNeeds_yes_percent = (int) Math.round(((double)financialNeeds_yes_count/totalSurveys)*100);
            financialNeeds_yes_string = financialNeeds_yes_percent + "%";

            int financialNeeds_no_percent = (int) Math.round(((double)financialNeeds_no_count/totalSurveys)*100);
            financialNeeds_no_string = financialNeeds_no_percent + "%";

            int abilityToWork_yes_percent = (int) Math.round(((double)abilityToWork_yes_count/totalSurveys)*100);
            abilityToWork_yes_string = abilityToWork_yes_percent + "%";

            int abilityToWork_no_percent = (int) Math.round(((double)abilityToWork_no_count/totalSurveys)*100);
            abilityToWork_no_string = abilityToWork_no_percent + "%";

            int wantToWork_yes_percent = (int) Math.round(((double)wantToWork_yes_count/totalSurveys)*100);
            wantToWork_yes_string = wantToWork_yes_percent + "%";

            int wantToWork_no_percent = (int) Math.round(((double)wantToWork_no_count/totalSurveys)*100);
            wantToWork_no_string = wantToWork_no_percent + "%";
        }

        working_yes.setText(working_yes_string);
        working_no.setText(working_no_string);
        financialNeeds_yes.setText(financialNeeds_yes_string);
        financialNeeds_no.setText(financialNeeds_no_string);
        abilityToWork_yes.setText(abilityToWork_yes_string);
        abilityToWork_no.setText(abilityToWork_no_string);
        wantToWork_yes.setText(wantToWork_yes_string);
        wantToWork_no.setText(wantToWork_no_string);
    }

    private void setFoodSecurityTableValues(View view){
        TextView enoughFood_yes = view.findViewById(R.id.enoughFood_yes);
        TextView enoughFood_no = view.findViewById(R.id.enoughFood_no);
        int enoughFood_yes_count = 0;
        int enoughFood_no_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_diet_enough()){
                enoughFood_yes_count++;
            }else{
                enoughFood_no_count++;
            }
            totalSurveys++;
        }

        String enoughFood_yes_string = "0%";
        String enoughFood_no_string = "0%";
        System.out.println("TOTAL SURVEYS: " + totalSurveys);
        if(totalSurveys != 0){
            int enoughFood_yes_percent = (int) Math.round(((double)enoughFood_yes_count/totalSurveys)*100);
            enoughFood_yes_string = enoughFood_yes_percent + "%";
            int enoughFood_no_percent = (int) Math.round(((double)enoughFood_no_count/totalSurveys)*100);
            enoughFood_no_string = enoughFood_no_percent + "%";
        }

        enoughFood_yes.setText(enoughFood_yes_string);
        enoughFood_no.setText(enoughFood_no_string);
    }

    private void setEmpowermentTableValues(View view){
        TextView assistPeople_yes = view.findViewById(R.id.assistPeople_yes);
        TextView assistPeople_no = view.findViewById(R.id.assistPeople_no);
        TextView awareOfRights_yes = view.findViewById(R.id.awareOfRights_yes);
        TextView awareOfRights_no = view.findViewById(R.id.awareOfRights_no);
        TextView influencePeople_yes = view.findViewById(R.id.influencePeople_yes);
        TextView influencePeople_no = view.findViewById(R.id.influencePeople_no);
        int assistPeople_yes_count = 0;
        int assistPeople_no_count = 0;
        int awareOfRights_yes_count = 0;
        int awareOfRights_no_count = 0;
        int influencePeople_yes_count = 0;
        int influencePeople_no_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_member()){
                assistPeople_yes_count++;
            }else{
                assistPeople_no_count++;
            }

            if(survey.isIs_aware()){
                awareOfRights_yes_count++;
            }else{
                awareOfRights_no_count++;
            }

            if(survey.isIs_influence()){
                influencePeople_yes_count++;
            }else{
                influencePeople_no_count++;
            }
            totalSurveys++;
        }

        String assistPeople_yes_string = "0%";
        String assistPeople_no_string = "0%";
        String awareOfRights_yes_string = "0%";
        String awareOfRights_no_string = "0%";
        String influencePeople_yes_string = "0%";
        String influencePeople_no_string = "0%";

        if(totalSurveys != 0){
            int assistPeople_yes_percent = (int) Math.round(((double)assistPeople_yes_count/totalSurveys)*100);
            assistPeople_yes_string = assistPeople_yes_percent + "%";

            int assistPeople_no_percent = (int) Math.round(((double)assistPeople_no_count/totalSurveys)*100);
            assistPeople_no_string = assistPeople_no_percent + "%";

            int awareOfRights_yes_percent = (int) Math.round(((double)awareOfRights_yes_count/totalSurveys)*100);
            awareOfRights_yes_string = awareOfRights_yes_percent + "%";

            int awareOfRights_no_percent = (int) Math.round(((double)awareOfRights_no_count/totalSurveys)*100);
            awareOfRights_no_string = awareOfRights_no_percent + "%";

            int influencePeople_yes_percent = (int) Math.round(((double)influencePeople_yes_count/totalSurveys)*100);
            influencePeople_yes_string = influencePeople_yes_percent + "%";

            int influencePeople_no_percent = (int) Math.round(((double)influencePeople_no_count/totalSurveys)*100);
            influencePeople_no_string = influencePeople_no_percent + "%";
        }

        assistPeople_yes.setText(assistPeople_yes_string);
        assistPeople_no.setText(assistPeople_no_string);
        awareOfRights_yes.setText(awareOfRights_yes_string);
        awareOfRights_no.setText(awareOfRights_no_string);
        influencePeople_yes.setText(influencePeople_yes_string);
        influencePeople_no.setText(influencePeople_no_string);
    }

    private void setShelterAndCareTableValues(View view){
        TextView adequateShelter_yes = view.findViewById(R.id.adequateShelter_yes);
        TextView adequateShelter_no = view.findViewById(R.id.adequateShelter_no);
        TextView essentialItems_yes = view.findViewById(R.id.essentialItems_yes);
        TextView essentialItems_no = view.findViewById(R.id.essentialItems_no);
        int adequateShelter_yes_count = 0;
        int adequateShelter_no_count = 0;
        int essentialItems_yes_count = 0;
        int essentialItems_no_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_shelter_adequate()){
                adequateShelter_yes_count++;
            }else{
                adequateShelter_no_count++;
            }

            if(survey.isItems_access()){
                essentialItems_yes_count++;
            }else{
                essentialItems_no_count++;
            }
            totalSurveys++;
        }

        String adequateShelter_yes_string = "0%";
        String adequateShelter_no_string = "0%";
        String essentialItems_yes_string = "0%";
        String essentialItems_no_string = "0%";

        if(totalSurveys != 0){
            int adequateShelter_yes_percent = (int) Math.round(((double)adequateShelter_yes_count/totalSurveys)*100);
            adequateShelter_yes_string = adequateShelter_yes_percent + "%";

            int adequateShelter_no_percent = (int) Math.round(((double)adequateShelter_no_count/totalSurveys)*100);
            adequateShelter_no_string = adequateShelter_no_percent + "%";

            int essentialItems_yes_percent = (int) Math.round(((double)essentialItems_yes_count/totalSurveys)*100);
            essentialItems_yes_string = essentialItems_yes_percent + "%";

            int essentialItems_no_percent = (int) Math.round(((double)essentialItems_no_count/totalSurveys)*100);
            essentialItems_no_string = essentialItems_no_percent + "%";
        }

        adequateShelter_yes.setText(adequateShelter_yes_string);
        adequateShelter_no.setText(adequateShelter_no_string);
        essentialItems_yes.setText(essentialItems_yes_string);
        essentialItems_no.setText(essentialItems_no_string);
    }
}