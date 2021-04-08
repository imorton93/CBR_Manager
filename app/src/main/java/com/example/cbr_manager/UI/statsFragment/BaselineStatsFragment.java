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
        return view;
    }

    private void createPieChart(PieChart graph, ArrayList<PieEntry> entries){
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(
                Color.parseColor("#FFEC17"), // yellow
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
            if(childConditionCount.containsKey(child_condition)){
                Integer count = childConditionCount.get(child_condition);
                childConditionCount.put(child_condition, count+1);
            }else{
                childConditionCount.put(child_condition, 1);
            }
        }

        for(Map.Entry<String, Integer> entry : childConditionCount.entrySet()){
            childConditionDataPoints.add(entry.getValue());
            LABELS.add(entry.getKey());
        }

        return childConditionDataPoints;
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

        String accessToRehab_yes_string = ("" + (accessToRehab_yes_count/totalSurveys)*100) + "%";
        accessToRehab_yes.setText(accessToRehab_yes_string);
        String accessToRehab_no_string = ("" + (accessToRehab_no_count/totalSurveys)*100) + "%";
        accessToRehab_no.setText(accessToRehab_no_string);

        String needAccessToRehab_yes_string = ("" + (needAccessToRehab_yes_count/totalSurveys)*100) + "%";
        needAccessToRehab_yes.setText(needAccessToRehab_yes_string);
        String needAccessToRehab_no_string = ("" + (needAccessToRehab_no_count/totalSurveys)*100) + "%";
        needAccessToRehab_no.setText(needAccessToRehab_no_string);

        String haveAssistiveDevice_yes_string = ("" + (haveAssistiveDevice_yes_count/totalSurveys)*100) + "%";
        haveAssistiveDevice_yes.setText(haveAssistiveDevice_yes_string);
        String haveAssistiveDevice_no_string = ("" + (haveAssistiveDevice_no_count/totalSurveys)*100) + "%";
        haveAssistiveDevice_no.setText(haveAssistiveDevice_no_string);

        String assistiveDeviceWorking_yes_string = ("" + (assistiveDeviceWorking_yes_count/totalSurveys)*100) + "%";
        assistiveDeviceWorking_yes.setText(assistiveDeviceWorking_yes_string);
        String assistiveDeviceWorking_no_string = ("" + (assistiveDeviceWorking_no_count/totalSurveys)*100) + "%";
        assistiveDeviceWorking_no.setText(assistiveDeviceWorking_no_string);

        String needAssistiveDevice_yes_string = ("" + (needAssistiveDevice_yes_count/totalSurveys)*100) + "%";
        needAssistiveDevice_yes.setText(needAssistiveDevice_yes_string);
        String needAssistiveDevice_no_string = ("" + (needAssistiveDevice_no_count/totalSurveys)*100) + "%";
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

        String goToSchool_yes_string = ((goToSchool_yes_count/totalSurveys)*100) + "%";
        goToSchool_yes.setText(goToSchool_yes_string);
        String goToSchool_no_string = ((goToSchool_no_count/totalSurveys)*100) + "%";
        goToSchool_no.setText(goToSchool_no_string);

        String beenToSchoolBefore_yes_string = ((beenToSchoolBefore_yes_count/totalSurveys)*100) + "%";
        beenToSchoolBefore_yes.setText(beenToSchoolBefore_yes_string);
        String beenToSchoolBefore_no_string = ((beenToSchoolBefore_no_count/totalSurveys)*100) + "%";
        beenToSchoolBefore_no.setText(beenToSchoolBefore_no_string);

        String wantToGoToSchool_yes_string = ((wantToGoToSchool_yes_count/totalSurveys)*100) + "%";
        wantToGoToSchool_yes.setText(wantToGoToSchool_yes_string);
        String wantToGoToSchool_no_string = ((wantToGoToSchool_no_count/totalSurveys)*100) + "%";
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

        String valuedCommunityMember_yes_string = ((valuedCommunityMember_yes_count/totalSurveys)*100) + "%";
        valuedCommunityMember_yes.setText(valuedCommunityMember_yes_string);
        String valuedCommunityMember_no_string = ((valuedCommunityMember_no_count/totalSurveys)*100) + "%";
        valuedCommunityMember_no.setText(valuedCommunityMember_no_string);

        String independent_yes_string = ((independent_yes_count/totalSurveys)*100) + "%";
        independent_yes.setText(independent_yes_string);
        String independent_no_string = ((independent_no_count/totalSurveys)*100) + "%";
        independent_no.setText(independent_no_string);

        String participateInEvents_yes_string = ((participateInEvents_yes_count/totalSurveys)*100) + "%";
        participateInEvents_yes.setText(participateInEvents_yes_string);
        String participateInEvents_no_string = ((participateInEvents_no_count/totalSurveys)*100) + "%";
        participateInEvents_no.setText(participateInEvents_no_string);

        String interactSocially_yes_string = ((interactSocially_yes_count/totalSurveys)*100) + "%";
        interactSocially_yes.setText(interactSocially_yes_string);
        String interactSocially_no_string = ((interactSocially_no_count/totalSurveys)*100) + "%";
        interactSocially_no.setText(interactSocially_no_string);

        String experienceDiscrimination_yes_string = ((experienceDiscrimination_yes_count/totalSurveys)*100) + "%";
        experienceDiscrimination_yes.setText(experienceDiscrimination_yes_string);
        String experienceDiscrimination_no_string = ((experienceDiscrimination_no_count/totalSurveys)*100) + "%";
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

        String working_yes_string = ((working_yes_count/totalSurveys)*100) + "%";
        working_yes.setText(working_yes_string);
        String working_no_string = ((working_no_count/totalSurveys)*100) + "%";
        working_no.setText(working_no_string);

        String financialNeeds_yes_string = ((financialNeeds_yes_count/totalSurveys)*100) + "%";
        financialNeeds_yes.setText(financialNeeds_yes_string);
        String financialNeeds_no_string = ((financialNeeds_no_count/totalSurveys)*100) + "%";
        financialNeeds_no.setText(financialNeeds_no_string);

        String abilityToWork_yes_string = ((abilityToWork_yes_count/totalSurveys)*100) + "%";
        abilityToWork_yes.setText(abilityToWork_yes_string);
        String abilityToWork_no_string = ((abilityToWork_no_count/totalSurveys)*100) + "%";
        abilityToWork_no.setText(abilityToWork_no_string);

        String wantToWork_yes_string = ((wantToWork_yes_count/totalSurveys)*100) + "%";
        wantToWork_yes.setText(wantToWork_yes_string);
        String wantToWork_no_string = ((wantToWork_no_count/totalSurveys)*100) + "%";
        wantToWork_no.setText(wantToWork_no_string);
    }

}