package com.example.cbr_manager.UI.statsFragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.StatsActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class GeneralStatsFragment extends Fragment {
    private StatsActivity statsActivity;
    private ClientManager clientManager;
    private static final ArrayList<String> DATES = new ArrayList<String>();
    private static final ArrayList<Integer> DATES_DAYS = new ArrayList<Integer>();
    private final int NUMBER_OF_POINTS = 4;


    public GeneralStatsFragment() {}

    public static GeneralStatsFragment newInstance() {
        GeneralStatsFragment fragment = new GeneralStatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.statsActivity = (StatsActivity) getActivity();
        this.clientManager = ClientManager.getInstance(statsActivity);

        View view = inflater.inflate(R.layout.fragment_general_stats, container, false);
        clientsOverTimeGraph(view);

        return view;
    }

    private void displayClientOverTimeGraph(LineChart graph, LineDataSet lineDataSet){
        lineDataSet.setColor(Color.BLACK);
        LineData data = new LineData(lineDataSet);
        graph.setData(data);
        XAxis xaxis = graph.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawGridLines(false);
        xaxis.setLabelCount(3);
        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return DATES.get((int)value);
            }
        });
        graph.getDescription().setEnabled(false);
        graph.invalidate();
    }

    private void setDatesToNumberOfPoints(){
        while(DATES.size() < 4){
            DATES.add("INVALID");
            DATES_DAYS.add(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void clientsOverTimeGraph(View view){
        LineChart graph = view.findViewById(R.id.general_clientsOverTime_graph);
        ArrayList<Integer> clientsOverTimeDataPoints = getClientsOverTimeDataPoints();
        setDatesToNumberOfPoints();
        ArrayList<Entry> entries = new ArrayList<>();

        for(int i = 0; i < clientsOverTimeDataPoints.size(); i++){
            Entry entry = new Entry(i, clientsOverTimeDataPoints.get(i));
            entries.add(entry);
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Visits Health Goals Met");
        displayClientOverTimeGraph(graph, lineDataSet);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Integer> getClientsOverTimeDataPoints(){
        ArrayList<Integer> clientsOverTimeDataPoints = new ArrayList<>();
        ArrayList<String> recentClients = getRecentClientDates();
        System.out.println("recentClients.size: " + recentClients.size());
        if(!recentClients.isEmpty()) {
            int numberOfMissingClients = clientManager.getClients().size() - recentClients.size();
            getUniqueClientDates(recentClients);
            for(int i = 0; i < DATES_DAYS.size(); i++){
                clientsOverTimeDataPoints.add(numberOfMissingClients);
            }

            Collections.sort(recentClients);
            Integer count;

            for(String clientDate : recentClients){
                String [] dateSplit = clientDate.split("/");
                int day = Integer.parseInt(dateSplit[0]);

                if(day <= DATES_DAYS.get(0)){
                    count = clientsOverTimeDataPoints.get(0);
                    clientsOverTimeDataPoints.set(0, count+1);
                }
                if(DATES_DAYS.size() > 1) {
                    if (day <= DATES_DAYS.get(1)) {
                        count = clientsOverTimeDataPoints.get(1);
                        clientsOverTimeDataPoints.set(1, count + 1);
                    }
                }
                if(DATES_DAYS.size() > 2){
                    if(day <= DATES_DAYS.get(2)){
                        count = clientsOverTimeDataPoints.get(2);
                        clientsOverTimeDataPoints.set(2, count+1);
                    }
                }
                if(DATES_DAYS.size() > 3) {
                    if (day <= DATES_DAYS.get(3)) {
                        count = clientsOverTimeDataPoints.get(3);
                        clientsOverTimeDataPoints.set(3, count + 1);
                    }
                }
            }
        }else{
            for(int i = 0; i < NUMBER_OF_POINTS; i++){
                clientsOverTimeDataPoints.add(0);
            }
        }
        return clientsOverTimeDataPoints;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getUniqueClientDates(ArrayList<String> recentClients){
        List<String> uniqueValues = recentClients.stream().distinct().collect(Collectors.toList());
        DATES_DAYS.clear();
        DATES.clear();
        if(uniqueValues.size() > NUMBER_OF_POINTS){
            int bucketSize = (int)Math.ceil(uniqueValues.size()/NUMBER_OF_POINTS);
            int count = 0;
            for(int i = 0; i < uniqueValues.size(); i++){
                if(i % bucketSize == 0 && count < NUMBER_OF_POINTS-1){
                    DATES.add(uniqueValues.get(i));
                }
                count++;
            }
            DATES.add(uniqueValues.get(uniqueValues.size()-1));
        }else{
            for(String value : uniqueValues){
                DATES.add(value);
            }
        }
        Collections.sort(DATES);

        for(String date : DATES){
            String [] dateSplit = date.split("/");
            DATES_DAYS.add(Integer.valueOf(dateSplit[0]));
        }
    }

    private ArrayList<String> getRecentClientDates(){
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<String> recentClients = new ArrayList<>();

        for (Client client : clientManager.getClients()) {
            String date = client.getDate();
            String[] dateSplit = date.split("/");
            int monthSplit = Integer.parseInt(dateSplit[1]);
            int yearSplit = Integer.parseInt(dateSplit[2]);
            if (monthSplit == month && yearSplit == year) {
                recentClients.add(client.getDate());
            }
        }

        return recentClients;
    }
}