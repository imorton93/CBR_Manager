package com.example.cbr_manager.UI.statsFragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Forms.FormPage;
import com.example.cbr_manager.Forms.MultipleChoiceQuestion;
import com.example.cbr_manager.Forms.QuestionType;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.StatsActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ClientStatsFragment extends Fragment {

    private StatsActivity statsActivity;
    private ClientManager clientManager;
    private static final ArrayList<String> RISK_LEVEL = new ArrayList<String>();
    private static final ArrayList<String> GOAL = new ArrayList<String>();
    private static final ArrayList<String> ZONES = new ArrayList<String>();

    public ClientStatsFragment() {
    }

    public static ClientStatsFragment newInstance() {
        ClientStatsFragment fragment = new ClientStatsFragment();
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
        clientManager = ClientManager.getInstance(statsActivity);
        View view = inflater.inflate(R.layout.fragment_client_stats, container, false);
        FormPage page = new FormPage();
        Resources res = getResources();
        MultipleChoiceQuestion location = new MultipleChoiceQuestion(getString(R.string.location),getString(R.string.location_newClientForm), QuestionType.DROP_DOWN,res.getStringArray(R.array.locations), true);
        page.addToPage(location);
        RISK_LEVEL.add("Low Risk");
        RISK_LEVEL.add("Medium Risk");
        RISK_LEVEL.add("High Risk");
        RISK_LEVEL.add("Critical Risk");

        GOAL.add("Health");
        GOAL.add("Education");
        GOAL.add("Social");

        ZONES.add("BidiBidi Zone 1");
        ZONES.add("BidiBidi Zone 2");
        ZONES.add("BidiBidi Zone 3");
        ZONES.add("BidiBidi Zone 4");
        ZONES.add("BidiBidi Zone 5");
        ZONES.add("Palorinya Basecamp");
        ZONES.add("Palorinya Zone 1");
        ZONES.add("Palorinya Zone 2");
        ZONES.add("Palorinya Zone 3");

        criticalRiskDataPoints(view);
        highRiskDataPoints(view);
        mediumRiskDataPoints(view);
        lowRiskDataPoints(view);
        criticalRiskPerZone(view);
        highRiskPerZone(view);

        return view;
    }

    private void criticalRiskDataPoints(View view) {
        BarChart graph = view.findViewById(R.id.client_critical_graph);
        ArrayList<Integer> criticalRiskDataPoints = getCriticalDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < criticalRiskDataPoints.size(); i++) {
            BarEntry barEntry = new BarEntry(i, criticalRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Client Critical Risk Levels");
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
                return GOAL.get((int) value);
            }
        });
        graph.invalidate();
        graph.getDescription().setEnabled(false);

    }

    private ArrayList<Integer> getCriticalDataPoints() {
        ArrayList<Integer> criticalRiskDataPoints = new ArrayList<>();
        int critical_health_count = 3;
        int critical_education_count = 3;
        int critical_social_count = 3;


        criticalRiskDataPoints.add(getHealthDataPoints().get(critical_health_count));
        criticalRiskDataPoints.add(getEducationDataPoints().get(critical_education_count));
        criticalRiskDataPoints.add(getSocialDataPoints().get(critical_social_count));
        return criticalRiskDataPoints;
    }

    private void highRiskDataPoints(View view) {
        BarChart graph = view.findViewById(R.id.client_high_graph);
        ArrayList<Integer> highRiskDataPoints = getHighDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < highRiskDataPoints.size(); i++) {
            BarEntry barEntry = new BarEntry(i, highRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Client High Risk Levels");
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
                return GOAL.get((int) value);
            }
        });
        graph.invalidate();
        graph.getDescription().setEnabled(false);

    }

    private ArrayList<Integer> getHighDataPoints() {
        ArrayList<Integer> highRiskDataPoints = new ArrayList<>();
        int high_health_count = 2;
        int high_education_count = 2;
        int high_social_count = 2;

        highRiskDataPoints.add(getHealthDataPoints().get(high_health_count));
        highRiskDataPoints.add(getEducationDataPoints().get(high_education_count));
        highRiskDataPoints.add(getSocialDataPoints().get(high_social_count));
        return highRiskDataPoints;
    }

    private void mediumRiskDataPoints(View view) {
        BarChart graph = view.findViewById(R.id.client_medium_graph);
        ArrayList<Integer> mediumRiskDataPoints = getMediumDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < mediumRiskDataPoints.size(); i++) {
            BarEntry barEntry = new BarEntry(i, mediumRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Client Medium Risk Levels");
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
                return GOAL.get((int) value);
            }
        });
        graph.invalidate();
        graph.getDescription().setEnabled(false);

    }

    private ArrayList<Integer> getMediumDataPoints() {
        ArrayList<Integer> mediumRiskDataPoints = new ArrayList<>();
        int medium_health_count = 1;
        int medium_education_count = 1;
        int medium_social_count = 1;


        mediumRiskDataPoints.add(getHealthDataPoints().get(medium_health_count));
        mediumRiskDataPoints.add(getEducationDataPoints().get(medium_education_count));
        mediumRiskDataPoints.add(getSocialDataPoints().get(medium_social_count));
        return mediumRiskDataPoints;
    }



    private void lowRiskDataPoints(View view) {
        BarChart graph = view.findViewById(R.id.client_low_graph);
        ArrayList<Integer> lowRiskDataPoints = getLowDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < lowRiskDataPoints.size(); i++) {
            BarEntry barEntry = new BarEntry(i, lowRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Client Low Risk Levels");
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
                return GOAL.get((int) value);
            }
        });
        graph.invalidate();
        graph.getDescription().setEnabled(false);

    }

    private ArrayList<Integer> getLowDataPoints() {
        ArrayList<Integer> lowRiskDataPoints = new ArrayList<>();
        int low_health_count = 0;
        int low_education_count = 0;
        int low_social_count = 0;


        lowRiskDataPoints.add(getHealthDataPoints().get(low_health_count));
        lowRiskDataPoints.add(getEducationDataPoints().get(low_education_count));
        lowRiskDataPoints.add(getSocialDataPoints().get(low_social_count));
        return lowRiskDataPoints;
    }


        private ArrayList<Integer> getHealthDataPoints () {
            ArrayList<Integer> healthRiskDataPoints = new ArrayList<>();
            int low_count = 0;
            int medium_count = 0;
            int high_count = 0;
            int critical_count = 0;

            for (Client client : clientManager.getClients()) {
                if (client.getHealthRate().equals(RISK_LEVEL.get(0))) {
                    low_count++;
                } else if (client.getHealthRate().equals(RISK_LEVEL.get(1))) {
                    medium_count++;
                } else if (client.getHealthRate().equals(RISK_LEVEL.get(2))) {
                    high_count++;
                } else if (client.getHealthRate().equals(RISK_LEVEL.get(3))) {
                    critical_count++;
                }
            }
            healthRiskDataPoints.add(low_count);
            healthRiskDataPoints.add(medium_count);
            healthRiskDataPoints.add(high_count);
            healthRiskDataPoints.add(critical_count);

            return healthRiskDataPoints;
        }


    private ArrayList<Integer> getEducationDataPoints(){
        ArrayList<Integer> educationRiskDataPoints = new ArrayList<>();
        int low_count = 0;
        int medium_count = 0;
        int high_count = 0;
        int critical_count = 0;

        for(Client client : clientManager.getClients()){
            if(client.getEducationRate().equals(RISK_LEVEL.get(0))){
                low_count++;
            }else if(client.getEducationRate().equals(RISK_LEVEL.get(1))){
                medium_count++;
            }else if(client.getEducationRate().equals(RISK_LEVEL.get(2))){
                high_count++;
            }else if(client.getEducationRate().equals(RISK_LEVEL.get(3))){
                critical_count++;
            }
        }
        educationRiskDataPoints.add(low_count);
        educationRiskDataPoints.add(medium_count);
        educationRiskDataPoints.add(high_count);
        educationRiskDataPoints.add(critical_count);

        return educationRiskDataPoints;
    }


    private ArrayList<Integer> getSocialDataPoints(){
        ArrayList<Integer> socialRiskDataPoints = new ArrayList<>();
        int low_count = 0;
        int medium_count = 0;
        int high_count = 0;
        int critical_count = 0;

        for(Client client : clientManager.getClients()){
            if(client.getSocialStatusRate().equals(RISK_LEVEL.get(0))){
                low_count++;
            }else if(client.getSocialStatusRate().equals(RISK_LEVEL.get(1))){
                medium_count++;
            }else if(client.getSocialStatusRate().equals(RISK_LEVEL.get(2))){
                high_count++;
            }else if(client.getSocialStatusRate().equals(RISK_LEVEL.get(3))){
                critical_count++;
            }
        }
        socialRiskDataPoints.add(low_count);
        socialRiskDataPoints.add(medium_count);
        socialRiskDataPoints.add(high_count);
        socialRiskDataPoints.add(critical_count);

        return socialRiskDataPoints;
    }

    private void criticalRiskPerZone(View view){
        BarChart graph = view.findViewById(R.id.client_zoneCritical_graph);
        ArrayList<Integer> criticalRiskDataPoints = getCriticalClientsPerZone();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < criticalRiskDataPoints.size(); i++) {
            BarEntry barEntry = new BarEntry(i, criticalRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Critical Risk Levels Per Zone");
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
                return ZONES.get((int) value);
            }
        });
        graph.invalidate();
        graph.getDescription().setEnabled(false);
    }

    private ArrayList<Integer> getCriticalClientsPerZone(){
        ArrayList<Integer> riskPerZoneDataPoints = new ArrayList<>();
        int [] zone_count = new int[9];

        for(Client client : clientManager.getClients()){
            String location = client.getLocation();
            String riskLevelHealth = client.getHealthRate();
            String riskLevelEducation = client.getEducationRate();
            String riskLevelSocial = client.getSocialStatusRate();

            if(location.equals(ZONES.get(0))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[0]++;
            }else if(location.equals(ZONES.get(1))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[1]++;
            }else if(location.equals(ZONES.get(2))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[2]++;
            }else if(location.equals(ZONES.get(3))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[3]++;
            }else if(location.equals(ZONES.get(4))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[4]++;
            }else if(location.equals(ZONES.get(5))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[5]++;
            }else if(location.equals(ZONES.get(6))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[6]++;
            }else if(location.equals(ZONES.get(7))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[7]++;
            }else if(location.equals(ZONES.get(8))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(3))
                    || riskLevelHealth.equals(RISK_LEVEL.get(3))
                    || riskLevelSocial.equals((RISK_LEVEL.get(3))))){
                zone_count[8]++;
            }
        }

        for(int i =0; i < 9; i++){
            riskPerZoneDataPoints.add(zone_count[i]);
        }
        return riskPerZoneDataPoints;
    }

    private void highRiskPerZone(View view){
        BarChart graph = view.findViewById(R.id.client_zoneHigh_graph);
        ArrayList<Integer> highRiskDataPoints = getHighClientsPerZone();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < highRiskDataPoints.size(); i++) {
            BarEntry barEntry = new BarEntry(i, highRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "High Risk Levels Per Zone");
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
                return ZONES.get((int) value);
            }
        });
        graph.invalidate();
        graph.getDescription().setEnabled(false);
    }

    private ArrayList<Integer> getHighClientsPerZone(){
        ArrayList<Integer> riskPerZoneDataPoints = new ArrayList<>();
        int [] zone_count = new int[9];

        for(Client client : clientManager.getClients()){
            String location = client.getLocation();
            String riskLevelHealth = client.getHealthRate();
            String riskLevelEducation = client.getEducationRate();
            String riskLevelSocial = client.getSocialStatusRate();

            if(location.equals(ZONES.get(0))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[0]++;
            }else if(location.equals(ZONES.get(1))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[1]++;
            }else if(location.equals(ZONES.get(2))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[2]++;
            }else if(location.equals(ZONES.get(3))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[3]++;
            }else if(location.equals(ZONES.get(4))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[4]++;
            }else if(location.equals(ZONES.get(5))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[5]++;
            }else if(location.equals(ZONES.get(6))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[6]++;
            }else if(location.equals(ZONES.get(7))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[7]++;
            }else if(location.equals(ZONES.get(8))
                    && (riskLevelEducation.equals(RISK_LEVEL.get(2))
                    || riskLevelHealth.equals(RISK_LEVEL.get(2))
                    || riskLevelSocial.equals((RISK_LEVEL.get(2))))){
                zone_count[8]++;
            }
        }

        for(int i =0; i < 9; i++){
            riskPerZoneDataPoints.add(zone_count[i]);
        }
        return riskPerZoneDataPoints;
    }
}

