package com.example.cbr_manager.UI.statsFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.StatsActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaselineStatsFragment extends Fragment {

    private StatsActivity statsActivity;
    private SurveyManager surveyManager;
    private static final ArrayList<String> DEVICE_TYPES = new ArrayList<String>();
    private static final ArrayList<String> REASON_NOT_IN_SCHOOL = new ArrayList<String>();
    private static final ArrayList<String> QUALITY = new ArrayList<String>();


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
        health_devicesType_DataPoints(view);
        health_generalHealth_DataPoints(view);
        health_satisfaction_DataPoints(view);
        health_whyNotInSchool_DataPoints(view);
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

    private void health_devicesType_DataPoints(View view){
        PieChart graph = view.findViewById(R.id.baseline_health_device_graph);
        ArrayList<Integer> deviceTypeDataPoints = getDeviceTypeDataPoints();
        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i = 0; i < deviceTypeDataPoints.size(); i++){
            PieEntry pieEntry = new PieEntry(deviceTypeDataPoints.get(i), DEVICE_TYPES.get(i));
            entries.add(pieEntry);
        }

        createPieChart(graph, entries);
    }

    private ArrayList<Integer> getDeviceTypeDataPoints(){
        HashMap<String, Integer> deviceTypeCount = new HashMap<>();
        ArrayList<Integer> deviceTypesDataPoints = new ArrayList<Integer>();
        DEVICE_TYPES.clear();

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
            DEVICE_TYPES.add(entry.getKey());
        }

        return deviceTypesDataPoints;
    }

    private void health_generalHealth_DataPoints(View view){
        PieChart graph = view.findViewById(R.id.baseline_generalHealth_graph);
        ArrayList<Integer> deviceTypeDataPoints = getGeneralHealthDataPoints();
        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i = 0; i < deviceTypeDataPoints.size(); i++){
            PieEntry pieEntry = new PieEntry(deviceTypeDataPoints.get(i), QUALITY.get(i));
            entries.add(pieEntry);
        }

        createPieChart(graph, entries);
    }

    private ArrayList<Integer> getGeneralHealthDataPoints(){
        HashMap<String, Integer> generalHealthCount = new HashMap<>();
        ArrayList<Integer> generalHealthDataPoints = new ArrayList<Integer>();
        QUALITY.clear();

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
            QUALITY.add(entry.getKey());
        }

        return generalHealthDataPoints;
    }

    private void health_satisfaction_DataPoints(View view){
        PieChart graph = view.findViewById(R.id.baseline_health_satisfaction_graph);
        ArrayList<Integer> deviceTypeDataPoints = getHealthSatisfactionDataPoints();
        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i = 0; i < deviceTypeDataPoints.size(); i++){
            PieEntry pieEntry = new PieEntry(deviceTypeDataPoints.get(i), QUALITY.get(i));
            entries.add(pieEntry);
        }

        createPieChart(graph, entries);
    }

    private ArrayList<Integer> getHealthSatisfactionDataPoints(){
        HashMap<String, Integer> isSatisfiedCount = new HashMap<>();
        ArrayList<Integer> isSatisfiedDataPoints = new ArrayList<Integer>();
        QUALITY.clear();

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
            QUALITY.add(entry.getKey());
        }

        return isSatisfiedDataPoints;
    }

    private void health_whyNotInSchool_DataPoints(View view){
        PieChart graph = view.findViewById(R.id.baseline_whyDontTheyGoToSchool_graph);
        ArrayList<Integer> deviceTypeDataPoints = getWhyNotInSchoolDataPoints();
        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i = 0; i < deviceTypeDataPoints.size(); i++){
            PieEntry pieEntry = new PieEntry(deviceTypeDataPoints.get(i), QUALITY.get(i));
            entries.add(pieEntry);
        }

        createPieChart(graph, entries);
    }

    private ArrayList<Integer> getWhyNotInSchoolDataPoints() {
        HashMap<String, Integer> reasonNotInSchoolCount = new HashMap<>();
        ArrayList<Integer> reasonNotInSchoolDataPoints = new ArrayList<Integer>();
        REASON_NOT_IN_SCHOOL.clear();

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
            REASON_NOT_IN_SCHOOL.add(entry.getKey());
        }

        return reasonNotInSchoolDataPoints;
    }
}