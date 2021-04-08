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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaselineStatsFragment extends Fragment {

    private StatsActivity statsActivity;
    private SurveyManager surveyManager;
    private static final ArrayList<String> DEVICE_TYPES = new ArrayList<String>();

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

        return view;
    }

    private void health_devicesType_DataPoints(View view){
        PieChart graph = view.findViewById(R.id.baseline_health_device_graph);
        ArrayList<Integer> deviceTypeDataPoints = getDeviceTypeDataPoints();
        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i = 0; i < deviceTypeDataPoints.size(); i++){
            PieEntry pieEntry = new PieEntry(deviceTypeDataPoints.get(i), DEVICE_TYPES.get(i));
            entries.add(pieEntry);
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "Assistive Devices Needed");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setValueLinePart1OffsetPercentage(100f);
        pieDataSet.setValueLinePart1Length(.6f);
        pieDataSet.setValueLinePart2Length(.6f);
        PieData data = new PieData(pieDataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        graph.setData(data);
        graph.invalidate();
    }

    private ArrayList<Integer> getDeviceTypeDataPoints(){
        HashMap<String, Integer> deviceTypeCount = new HashMap<>();
        ArrayList<Integer> deviceTypesDataPoints = new ArrayList<Integer>();
        System.out.println("Survey List: " + surveyManager.getSurveyList().size());

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
}