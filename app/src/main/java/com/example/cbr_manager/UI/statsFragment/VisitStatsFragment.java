package com.example.cbr_manager.UI.statsFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.StatsActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class VisitStatsFragment extends Fragment {

    private StatsActivity statsActivity;
    private VisitManager visitManager;
    private static final ArrayList<String> GOAL_MET = new ArrayList<String>();
    private static final ArrayList<String> ZONES = new ArrayList<String>();

    public VisitStatsFragment() {}

    public static VisitStatsFragment newInstance() {
        VisitStatsFragment fragment = new VisitStatsFragment();
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
        this.visitManager = VisitManager.getInstance(statsActivity);

        addGoalsMet();
        addZones();

        View view = inflater.inflate(R.layout.fragment_visit_stats, container, false);
        healthGoalGraph(view);
        educationGoalGraph(view);
        socialGoalGraph(view);
        visitsPerZone(view);

        return view;
    }

    private void displayGoalGraph(BarChart graph, BarDataSet barDataSet){
        BarData data = new BarData(barDataSet);
        graph.setData(data);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        graph.setFitBars(true);
        graph.animateY(2000);
        XAxis xaxis = graph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(false);
        xaxis.setLabelCount(3);
        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return GOAL_MET.get((int)value);
            }
        });
        graph.getDescription().setEnabled(false);
        graph.invalidate();
    }

    private void displayZoneGraph(BarChart graph, BarDataSet barDataSet){
        BarData data = new BarData(barDataSet);
        graph.setData(data);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        graph.setFitBars(true);
        graph.animateY(2000);
        XAxis xaxis = graph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(false);
        xaxis.setLabelCount(9);
        xaxis.setLabelRotationAngle(-80);

        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ZONES.get((int)value);
            }
        });
        graph.getDescription().setEnabled(false);
        graph.invalidate();
    }


    private void healthGoalGraph(View view){
        BarChart graph = view.findViewById(R.id.visit_health_graph);
        ArrayList<Integer>  healthGoalDataPoints = getHealthDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < healthGoalDataPoints.size(); i++){
            BarEntry barEntry = new BarEntry(i, healthGoalDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Visits Health Goals Met");
        displayGoalGraph(graph, barDataSet);
    }

    private ArrayList<Integer> getHealthDataPoints(){
        ArrayList<Integer> healthGoalDataPoints = new ArrayList<>();
        int cancelled_count = 0;
        int ongoing_count = 0;
        int concluded_count = 0;

        for(Visit visit : visitManager.getVisits()){
            if(visit.getHealthGoalMet().equals(GOAL_MET.get(0))){
                cancelled_count++;
            }else if(visit.getHealthGoalMet().equals(GOAL_MET.get(1))){
                ongoing_count++;
            }else if(visit.getHealthGoalMet().equals(GOAL_MET.get(2))){
                concluded_count++;
            }
        }
        healthGoalDataPoints.add(cancelled_count);
        healthGoalDataPoints.add(ongoing_count);
        healthGoalDataPoints.add(concluded_count);

        return healthGoalDataPoints;
    }

    private void educationGoalGraph(View view){
        BarChart graph = view.findViewById(R.id.visit_education_graph);
        ArrayList<Integer>  educationGoalDataPoints = getEducationDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < educationGoalDataPoints.size(); i++){
            BarEntry barEntry = new BarEntry(i, educationGoalDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Visits Education Goals Met");
        displayGoalGraph(graph, barDataSet);
    }

    private ArrayList<Integer> getEducationDataPoints(){
        ArrayList<Integer> educationGoalDataPoints = new ArrayList<>();
        int cancelled_count = 0;
        int ongoing_count = 0;
        int concluded_count = 0;

        for(Visit visit : visitManager.getVisits()){
            if(visit.getEducationGoalMet().equals(GOAL_MET.get(0))){
                cancelled_count++;
            }else if(visit.getEducationGoalMet().equals(GOAL_MET.get(1))){
                ongoing_count++;
            }else if(visit.getEducationGoalMet().equals(GOAL_MET.get(2))){
                concluded_count++;
            }
        }
        educationGoalDataPoints.add(cancelled_count);
        educationGoalDataPoints.add(ongoing_count);
        educationGoalDataPoints.add(concluded_count);

        return educationGoalDataPoints;
    }

    private void socialGoalGraph(View view){
        BarChart graph = view.findViewById(R.id.visit_Social_graph);
        ArrayList<Integer>  socialDataPoints = getSocialDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < socialDataPoints.size(); i++){
            BarEntry barEntry = new BarEntry(i, socialDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Visits Social Goals Met");
        displayGoalGraph(graph, barDataSet);
    }

    private ArrayList<Integer> getSocialDataPoints(){
        ArrayList<Integer> socialGoalDataPoints = new ArrayList<>();
        int cancelled_count = 0;
        int ongoing_count = 0;
        int concluded_count = 0;

        for(Visit visit : visitManager.getVisits()){
            if(visit.getSocialGoalMet().equals(GOAL_MET.get(0))){
                cancelled_count++;
            }else if(visit.getSocialGoalMet().equals(GOAL_MET.get(1))){
                ongoing_count++;
            }else if(visit.getSocialGoalMet().equals(GOAL_MET.get(2))){
                concluded_count++;
            }
        }
        socialGoalDataPoints.add(cancelled_count);
        socialGoalDataPoints.add(ongoing_count);
        socialGoalDataPoints.add(concluded_count);

        return socialGoalDataPoints;
    }

    private void visitsPerZone(View view){
        BarChart graph = view.findViewById(R.id.visits_per_zone_graph);
        ArrayList<Integer>  visitsPerZoneDataPoints = getVisitsPerZoneDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < visitsPerZoneDataPoints.size(); i++){
            BarEntry barEntry = new BarEntry(i, visitsPerZoneDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Visits Per Zone");
        displayZoneGraph(graph, barDataSet);
    }

    private ArrayList<Integer> getVisitsPerZoneDataPoints(){
        ArrayList<Integer> visitsPerZoneDataPoints = new ArrayList<>();
        int [] zone_count = new int[9];

        for(Visit visit : visitManager.getVisits()){
            String location = visit.getLocation();
            if(location.equals(ZONES.get(0))){
                zone_count[0]++;
            }else if(location.equals(ZONES.get(1))){
                zone_count[1]++;
            }else if(location.equals(ZONES.get(2))){
                zone_count[2]++;
            }else if(location.equals(ZONES.get(3))){
                zone_count[3]++;
            }else if(location.equals(ZONES.get(4))){
                zone_count[4]++;
            }else if(location.equals(ZONES.get(5))){
                zone_count[5]++;
            }else if(location.equals(ZONES.get(6))){
                zone_count[6]++;
            }else if(location.equals(ZONES.get(7))){
                zone_count[7]++;
            }else if(location.equals(ZONES.get(8))){
                zone_count[8]++;
            }
        }

        for(int i =0; i < 9; i++){
            visitsPerZoneDataPoints.add(zone_count[i]);
        }

        return visitsPerZoneDataPoints;
    }

    private void addGoalsMet(){
        GOAL_MET.add("Cancelled");
        GOAL_MET.add("Ongoing");
        GOAL_MET.add("Concluded");
    }

    private void addZones(){
        ZONES.add("BidiBidi Zone 1");
        ZONES.add("BidiBidi Zone 2");
        ZONES.add("BidiBidi Zone 3");
        ZONES.add("BidiBidi Zone 4");
        ZONES.add("BidiBidi Zone 5");
        ZONES.add("Palorinya Basecamp");
        ZONES.add("Palorinya Zone 1");
        ZONES.add("Palorinya Zone 2");
        ZONES.add("Palorinya Zone 3");
    }
}