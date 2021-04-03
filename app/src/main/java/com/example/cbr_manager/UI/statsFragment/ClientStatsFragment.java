package com.example.cbr_manager.UI.statsFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.StatsActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;


public class ClientStatsFragment extends Fragment {

    private StatsActivity statsActivity;
    private ClientManager clientManager;
    private static final ArrayList<String> RISK_LEVEL = new ArrayList<String>();

    public ClientStatsFragment() {}

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

        RISK_LEVEL.add("Low Risk");
        RISK_LEVEL.add("Medium Risk");
        RISK_LEVEL.add("High Risk");
        RISK_LEVEL.add("Critical Risk");

        healthDataPoints(view);
        educationDataPoints(view);
        socialDataPoints(view);
        return view;
    }

    private void healthDataPoints(View view){
        BarChart graph = view.findViewById(R.id.client_health_graph);
        ArrayList<Integer>  healthRiskDataPoints = getHealthDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < healthRiskDataPoints.size(); i++){
            BarEntry barEntry = new BarEntry(i, healthRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Client Health Risk Levels");
        BarData data = new BarData(barDataSet);
        graph.setData(data);
        XAxis xaxis = graph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(false);
        xaxis.setLabelCount(4);
        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return RISK_LEVEL.get((int)value);
            }
        });
        graph.invalidate();
    }

    private ArrayList<Integer> getHealthDataPoints(){
        ArrayList<Integer> healthRiskDataPoints = new ArrayList<>();
        int low_count = 0;
        int medium_count = 0;
        int high_count = 0;
        int critical_count = 0;

        for(Client client : clientManager.getClients()){
            if(client.getHealthRate().equals(RISK_LEVEL.get(0))){
                low_count++;
            }else if(client.getHealthRate().equals(RISK_LEVEL.get(1))){
                medium_count++;
            }else if(client.getHealthRate().equals(RISK_LEVEL.get(2))){
                high_count++;
            }else if(client.getHealthRate().equals(RISK_LEVEL.get(3))){
                critical_count++;
            }
        }
        healthRiskDataPoints.add(low_count);
        healthRiskDataPoints.add(medium_count);
        healthRiskDataPoints.add(high_count);
        healthRiskDataPoints.add(critical_count);

        return healthRiskDataPoints;
    }

    private void educationDataPoints(View view){
        BarChart graph = view.findViewById(R.id.client_education_graph);
        ArrayList<Integer>  educationRiskDataPoints = getEducationDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < educationRiskDataPoints.size(); i++){
            BarEntry barEntry = new BarEntry(i, educationRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Client Education Risk Levels");
        BarData data = new BarData(barDataSet);
        graph.setData(data);
        XAxis xaxis = graph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(false);
        xaxis.setLabelCount(4);
        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return RISK_LEVEL.get((int)value);
            }
        });
        graph.invalidate();
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

    private void socialDataPoints(View view){
        BarChart graph = view.findViewById(R.id.client_Social_graph);
        ArrayList<Integer>  socialRiskDataPoints = getSocialDataPoints();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < socialRiskDataPoints.size(); i++){
            BarEntry barEntry = new BarEntry(i, socialRiskDataPoints.get(i));
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Client Social Status Risk Levels");
        BarData data = new BarData(barDataSet);
        graph.setData(data);
        XAxis xaxis = graph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(false);
        xaxis.setLabelCount(4);
        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return RISK_LEVEL.get((int)value);
            }
        });
        graph.invalidate();
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
}