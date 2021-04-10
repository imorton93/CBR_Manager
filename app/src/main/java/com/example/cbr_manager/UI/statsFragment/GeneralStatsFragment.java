package com.example.cbr_manager.UI.statsFragment;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.StatsActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class GeneralStatsFragment extends Fragment {
    private StatsActivity statsActivity;
    private ClientManager clientManager;
    private SurveyManager surveyManager;
    private static final ArrayList<String> DATES = new ArrayList<String>();
    private static final ArrayList<Integer> DATES_DAYS = new ArrayList<Integer>();
    private final int NUMBER_OF_POINTS = 4;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public GeneralStatsFragment() {
    }

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
        this.surveyManager = SurveyManager.getInstance(statsActivity);

        View view = inflater.inflate(R.layout.fragment_general_stats, container, false);
        clientsOverTimeGraph(view);

        downloadButton(view);
        return view;
    }

    private void displayClientOverTimeGraph(LineChart graph, LineDataSet lineDataSet) {
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
                return DATES.get((int) value);
            }
        });
        graph.getDescription().setEnabled(false);
        graph.invalidate();
    }

    private void setDatesToNumberOfPoints() {
        while (DATES.size() < 4) {
            DATES.add("INVALID");
            DATES_DAYS.add(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void clientsOverTimeGraph(View view) {
        LineChart graph = view.findViewById(R.id.general_clientsOverTime_graph);
        ArrayList<Integer> clientsOverTimeDataPoints = getClientsOverTimeDataPoints();
        setDatesToNumberOfPoints();
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < clientsOverTimeDataPoints.size(); i++) {
            Entry entry = new Entry(i, clientsOverTimeDataPoints.get(i));
            entries.add(entry);
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Clients added within the month");
        displayClientOverTimeGraph(graph, lineDataSet);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Integer> getClientsOverTimeDataPoints() {
        ArrayList<Integer> clientsOverTimeDataPoints = new ArrayList<>();
        ArrayList<String> recentClients = getRecentClientDates();
        if (!recentClients.isEmpty()) {
            int numberOfMissingClients = clientManager.getClients().size() - recentClients.size();
            getUniqueClientDates(recentClients);
            for (int i = 0; i < DATES_DAYS.size(); i++) {
                clientsOverTimeDataPoints.add(numberOfMissingClients);
            }

            Collections.sort(recentClients);
            Integer count;

            for (String clientDate : recentClients) {
                String[] dateSplit = clientDate.split("/");
                int day = Integer.parseInt(dateSplit[0]);

                if (day <= DATES_DAYS.get(0)) {
                    count = clientsOverTimeDataPoints.get(0);
                    clientsOverTimeDataPoints.set(0, count + 1);
                }
                if (DATES_DAYS.size() > 1) {
                    if (day <= DATES_DAYS.get(1)) {
                        count = clientsOverTimeDataPoints.get(1);
                        clientsOverTimeDataPoints.set(1, count + 1);
                    }
                }
                if (DATES_DAYS.size() > 2) {
                    if (day <= DATES_DAYS.get(2)) {
                        count = clientsOverTimeDataPoints.get(2);
                        clientsOverTimeDataPoints.set(2, count + 1);
                    }
                }
                if (DATES_DAYS.size() > 3) {
                    if (day <= DATES_DAYS.get(3)) {
                        count = clientsOverTimeDataPoints.get(3);
                        clientsOverTimeDataPoints.set(3, count + 1);
                    }
                }
            }
        } else {
            for (int i = 0; i < NUMBER_OF_POINTS; i++) {
                clientsOverTimeDataPoints.add(0);
            }
        }
        return clientsOverTimeDataPoints;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getUniqueClientDates(ArrayList<String> recentClients) {
        List<String> uniqueValues = recentClients.stream().distinct().collect(Collectors.toList());
        DATES_DAYS.clear();
        DATES.clear();
        if (uniqueValues.size() > NUMBER_OF_POINTS) {
            int bucketSize = (int) Math.ceil(uniqueValues.size() / NUMBER_OF_POINTS);
            int count = 0;
            for (int i = 0; i < uniqueValues.size(); i++) {
                if (i % bucketSize == 0 && count < NUMBER_OF_POINTS - 1) {
                    DATES.add(uniqueValues.get(i));
                }
                count++;
            }
            DATES.add(uniqueValues.get(uniqueValues.size() - 1));
        } else {
            for (String value : uniqueValues) {
                DATES.add(value);
            }
        }
        Collections.sort(DATES);

        for (String date : DATES) {
            String[] dateSplit = date.split("/");
            DATES_DAYS.add(Integer.valueOf(dateSplit[0]));
        }
    }

    private ArrayList<String> getRecentClientDates() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
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

    private void downloadButton(View v) {
        ImageView downloadButton = v.findViewById(R.id.download);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    writeToCSV();
                    Toast.makeText(v.getContext(), "Downloaded baseline survey data to documents folder", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), "Unable to download Baseline survey data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeToCSV() throws IOException {
        File baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(baseDir, "baseline-survey-data.csv");
        try {
            int permission = ActivityCompat.checkSelfPermission(this.statsActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                        this.statsActivity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
            FileWriter fileWriter = new FileWriter(file);
            StringBuilder baselineData = buildBaselineString();
            fileWriter.append(baselineData);
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private StringBuilder buildBaselineString() {
        StringBuilder baselineData = new StringBuilder();
        baselineData.append("Health");
        writeHealthPercentages(baselineData);

        baselineData.append("\n\n\nEducation");
        writeEducationPercentages(baselineData);

        baselineData.append("\n\n\nSocial Status");
        writeSocialPercentages(baselineData);

        baselineData.append("\n\n\nLivelihood");
        writeLivelihoodPercentages(baselineData);

        baselineData.append("\n\n\nFood and Nutrition");
        writeFoodAndNutritionPercentages(baselineData);

        baselineData.append("\n\n\nEmpowerment");
        writeEmpowermentPercentages(baselineData);

        baselineData.append("\n\n\nShelter and Care");
        writeShelterAndCarePercentages(baselineData);
        return baselineData;
    }

    private void writeHealthPercentages(StringBuilder baselineData) {
        HashMap<String, Integer> healthCondition_count = createQualityHash();
        HashMap<String, Integer> healthServiceSatisfaction_count = createQualityHash();
        HashMap<String, Integer> healthAssistiveDeviceNeeded_count = createAssistiveDeviceHash();
        int accessToRehab_yes_count = 0;
        int needAccessToRehab_yes_count = 0;
        int haveAssistiveDevice_yes_count = 0;
        int assistiveDeviceWorking_yes_count = 0;
        int needAssistiveDevice_yes_count = 0;
        int totalSurveys = 0;

        for (Survey survey : surveyManager.getSurveyList()) {
            calculateHealthQuality(survey.getHealth_condition(), healthCondition_count);
            calculateHealthQuality(survey.getIs_satisfied(), healthServiceSatisfaction_count);
            addToHashCount(survey.getDevice_type(),  healthAssistiveDeviceNeeded_count);
            if (survey.isHave_rehab_access()) {
                accessToRehab_yes_count++;
            }

            if (survey.isNeed_rehab_access()) {
                needAccessToRehab_yes_count++;
            }

            if (survey.isHave_device()) {
                haveAssistiveDevice_yes_count++;
            }

            if (survey.isDevice_condition()) {
                assistiveDeviceWorking_yes_count++;
            }

            if (survey.isDevice_condition()) {
                needAssistiveDevice_yes_count++;
            }

            totalSurveys++;
        }

        baselineData.append("\n,Very Poor,Poor,Fine,Good,\nRate 1-4 your general health,");
        calculateHealthPercentages(baselineData, healthCondition_count.get("Very Poor"), totalSurveys);
        calculateHealthPercentages(baselineData, healthCondition_count.get("Poor"), totalSurveys);
        calculateHealthPercentages(baselineData, healthCondition_count.get("Fine"), totalSurveys);
        calculateHealthPercentages(baselineData, healthCondition_count.get("Good"), totalSurveys);

        baselineData.append("\nAre you satisfied with the health services you receive?,");
        calculateHealthPercentages(baselineData, healthServiceSatisfaction_count.get("Very Poor"), totalSurveys);
        calculateHealthPercentages(baselineData, healthServiceSatisfaction_count.get("Poor"), totalSurveys);
        calculateHealthPercentages(baselineData, healthServiceSatisfaction_count.get("Fine"), totalSurveys);
        calculateHealthPercentages(baselineData, healthServiceSatisfaction_count.get("Good"), totalSurveys);

        baselineData.append("\n,Yes,No,\nDo you have access to rehabilitation services?,");
        calculateHealthPercentages(baselineData, accessToRehab_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - accessToRehab_yes_count), totalSurveys);

        baselineData.append("\nDo you need access to rehabilitation services?,");
        calculateHealthPercentages(baselineData, needAccessToRehab_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - needAccessToRehab_yes_count), totalSurveys);

        baselineData.append("\nDo you have an assistive device?,");
        calculateHealthPercentages(baselineData, haveAssistiveDevice_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - haveAssistiveDevice_yes_count), totalSurveys);

        baselineData.append("\nIs your assistive device working well?,");
        calculateHealthPercentages(baselineData, assistiveDeviceWorking_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - assistiveDeviceWorking_yes_count), totalSurveys);

        baselineData.append("\nDo you need an assistive device?,");
        calculateHealthPercentages(baselineData, needAssistiveDevice_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - needAssistiveDevice_yes_count), totalSurveys);

        baselineData.append("\n,Wheelchair,Prosthetic,Orthotic,Crutch,Walking Stick,Hearing Aid,Glasses,Standing Frame,Corner Seat,\nWhat assistive device do you need?,");
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Wheelchair"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Prosthetic"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Orthotic"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Crutch"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Walking Stick"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Hearing Aid"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Glasses"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Standing Frame"), totalSurveys);
        calculateHealthPercentages(baselineData, healthAssistiveDeviceNeeded_count.get("Corner Seat"), totalSurveys);
    }

    private void writeEducationPercentages(StringBuilder baselineData) {
        HashMap<String, Integer> grade_count = new HashMap<>();
        HashMap<String, Integer> reasonNotInSchool_count =  createNotInSchoolHash();
        int goToSchool_yes_count = 0;
        int beenToSchoolBefore_yes_count = 0;
        int wantToGoToSchool_yes_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            int grade = survey.getGrade_no();
            addToHashCount(String.valueOf(grade), grade_count);
            addToHashCount(survey.getReason_no_school(), reasonNotInSchool_count);
            if(survey.isIs_student()){
                goToSchool_yes_count++;
            }

            if(survey.isWas_student()){
                beenToSchoolBefore_yes_count++;
            }

            if(survey.isWant_school()){
                wantToGoToSchool_yes_count++;
            }

            totalSurveys++;
        }

        baselineData.append("\n,Yes,No,\nDo you go to school?,");
        calculateHealthPercentages(baselineData, goToSchool_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - goToSchool_yes_count), totalSurveys);

        StringBuilder firstRow = new StringBuilder(",");
        StringBuilder secondRow = new StringBuilder(",");
        baselineData.append("\nIf yes what grade?");
        for(Map.Entry<String, Integer> entry : grade_count.entrySet()){
            firstRow.append(entry.getKey()).append(",");
            calculateHealthPercentages(secondRow, entry.getValue(), grade_count.size());
        }
        baselineData.append(firstRow.append("\n"));
        baselineData.append(secondRow);

        baselineData.append("\n,Lack of Funding,My Disability Stops Me,Other,\nIf no why do you not go to school?,");
        calculateHealthPercentages(baselineData, reasonNotInSchool_count.get("Lack of Funding"), totalSurveys);
        calculateHealthPercentages(baselineData, reasonNotInSchool_count.get("My Disability Stops Me"), totalSurveys);
        calculateHealthPercentages(baselineData, reasonNotInSchool_count.get("Other"), totalSurveys);

        baselineData.append("\n,Yes,No,\nIf no have you ever been to school before?,");
        calculateHealthPercentages(baselineData, beenToSchoolBefore_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - beenToSchoolBefore_yes_count), totalSurveys);

        baselineData.append("\nDo you want to go to school?,");
        calculateHealthPercentages(baselineData, wantToGoToSchool_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - wantToGoToSchool_yes_count), totalSurveys);

    }

    private void writeSocialPercentages(StringBuilder baselineData) {
        int valuedCommunityMember_yes_count = 0;
        int independent_yes_count = 0;
        int participateInEvents_yes_count = 0;
        int interactSocially_yes_count = 0;
        int experienceDiscrimination_yes_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_valued()){
                valuedCommunityMember_yes_count++;
            }

            if(survey.isIs_independent()){
                independent_yes_count++;
            }

            if(survey.isIs_social()){
                participateInEvents_yes_count++;
            }

            if(survey.isIs_socially_affected()){
                interactSocially_yes_count++;
            }

            if(survey.isWas_discriminated()){
                experienceDiscrimination_yes_count++;
            }

            totalSurveys++;
        }

        baselineData.append("\n,Yes,No,\nDo you feel valued as a member of your community?,");
        calculateHealthPercentages(baselineData, valuedCommunityMember_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - valuedCommunityMember_yes_count), totalSurveys);

        baselineData.append("\nDo you feel independent?,");
        calculateHealthPercentages(baselineData, independent_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - independent_yes_count), totalSurveys);

        baselineData.append("\nAre you able to participate in community/social events?,");
        calculateHealthPercentages(baselineData, participateInEvents_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - participateInEvents_yes_count), totalSurveys);

        baselineData.append("\nDoes your disability affect your ability to interact socially?,");
        calculateHealthPercentages(baselineData, interactSocially_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - interactSocially_yes_count), totalSurveys);

        baselineData.append("\nHave you experienced discrimination because of your disability?,");
        calculateHealthPercentages(baselineData, experienceDiscrimination_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - experienceDiscrimination_yes_count), totalSurveys);
    }

    private void writeLivelihoodPercentages(StringBuilder baselineData) {
        HashMap<String, Integer> work_count = new HashMap<>();
        HashMap<String, Integer> employed_count = createEmployedHash();
        int working_yes_count = 0;
        int financialNeeds_yes_count = 0;
        int abilityToWork_yes_count = 0;
        int wantToWork_yes_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            addToHashCount(survey.getWork_type(), work_count);
            addToHashCount(survey.getIs_self_employed(), employed_count);
            if(survey.isIs_working()){
                working_yes_count++;
            }

            if(survey.isNeeds_met()){
                financialNeeds_yes_count++;
            }

            if(survey.isIs_work_affected()){
                abilityToWork_yes_count++;
            }

            if(survey.isWant_work()){
                wantToWork_yes_count++;
            }

            totalSurveys++;
        }

        baselineData.append("\n,Yes,No,\nAre you working?,");
        calculateHealthPercentages(baselineData, working_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - working_yes_count), totalSurveys);

        StringBuilder firstRow = new StringBuilder(",");
        StringBuilder secondRow = new StringBuilder(",");
        baselineData.append("\nIf yes what do you do?");
        for(Map.Entry<String, Integer> entry : work_count.entrySet()){
            firstRow.append(entry.getKey()).append(",");
            calculateHealthPercentages(secondRow, entry.getValue(), work_count.size());
        }
        baselineData.append(firstRow.append("\n"));
        baselineData.append(secondRow);

        baselineData.append("\n,Employed,Self-Employed,Other,\nAre you employed or self-employed?,");
        calculateHealthPercentages(baselineData, employed_count.get("Employed"), totalSurveys);
        calculateHealthPercentages(baselineData, employed_count.get("Self-Employed"), totalSurveys);
        calculateHealthPercentages(baselineData, employed_count.get("Other"), totalSurveys);

        baselineData.append("\n,Yes,No,\nDoes this meed your financial needs?,");
        calculateHealthPercentages(baselineData, financialNeeds_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - financialNeeds_yes_count), totalSurveys);

        baselineData.append("\nDoes your disability affect your ability to go to work?,");
        calculateHealthPercentages(baselineData, abilityToWork_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - abilityToWork_yes_count), totalSurveys);

        baselineData.append("\nDo you want to work?,");
        calculateHealthPercentages(baselineData, wantToWork_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - wantToWork_yes_count), totalSurveys);
    }

    private void writeFoodAndNutritionPercentages(StringBuilder baselineData) {
        HashMap<String, Integer> foodSecurity_count = createQualityHash();
        HashMap<String, Integer> childNutrition_count = createNutritionHash();
        int enoughFood_yes_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            calculateQuality(survey.getFood_security() ,foodSecurity_count);
            calculateQuality(survey.getChild_condition(), childNutrition_count);
            if(survey.isIs_diet_enough()){
                enoughFood_yes_count++;
            }
            totalSurveys++;
        }

        baselineData.append("\n,Very Poor,Poor,Fine,Good,\nFood Security,");
        calculateHealthPercentages(baselineData, foodSecurity_count.get("Very Poor"), totalSurveys);
        calculateHealthPercentages(baselineData, foodSecurity_count.get("Poor"), totalSurveys);
        calculateHealthPercentages(baselineData, foodSecurity_count.get("Fine"), totalSurveys);
        calculateHealthPercentages(baselineData, foodSecurity_count.get("Good"), totalSurveys);

        baselineData.append("\n,Yes,No,\nDo you have enough food every month?,");
        calculateHealthPercentages(baselineData, enoughFood_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - enoughFood_yes_count), totalSurveys);

        baselineData.append("\n,Malnourished,Undernourished,Well nourished,\nChild's condition,");
        calculateHealthPercentages(baselineData, childNutrition_count.get("Malnourished"), totalSurveys);
        calculateHealthPercentages(baselineData, childNutrition_count.get("Undernourished"), totalSurveys);
        calculateHealthPercentages(baselineData, childNutrition_count.get("Well Nourished"), totalSurveys);
    }

    private void writeEmpowermentPercentages(StringBuilder baselineData){
        HashMap<String, Integer> organization_count = new HashMap<>();
        int assistPeople_yes_count = 0;
        int awareOfRights_yes_count = 0;
        int influencePeople_yes_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            addToHashCount(survey.getOrganisation(), organization_count);
            if(survey.isIs_member()){
                assistPeople_yes_count++;
            }

            if(survey.isIs_aware()){
                awareOfRights_yes_count++;
            }

            if(survey.isIs_influence()){
                influencePeople_yes_count++;
            }
            totalSurveys++;
        }

        baselineData.append("\n,Yes,No,\nAre you a member of any organisation which assist people with disabilities?,");
        calculateHealthPercentages(baselineData, assistPeople_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - assistPeople_yes_count), totalSurveys);

        StringBuilder firstRow = new StringBuilder(",");
        StringBuilder secondRow = new StringBuilder(",");
        baselineData.append("\nOrganizations");
        for(Map.Entry<String, Integer> entry : organization_count.entrySet()){
            firstRow.append(entry.getKey()).append(",");
            calculateHealthPercentages(secondRow, entry.getValue(), organization_count.size());
        }
        baselineData.append(firstRow.append("\n"));
        baselineData.append(secondRow);

        baselineData.append("\n,Yes,No,\nAre you aware of your rights as a citizen living with disabilities?,");
        calculateHealthPercentages(baselineData, awareOfRights_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - awareOfRights_yes_count), totalSurveys);

        baselineData.append("\nDo you feel like you are able to influence people around you?,");
        calculateHealthPercentages(baselineData, influencePeople_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - influencePeople_yes_count), totalSurveys);
    }

    private void writeShelterAndCarePercentages(StringBuilder baselineData){
        int adequateShelter_yes_count = 0;
        int essentialItems_yes_count = 0;
        int totalSurveys = 0;

        for(Survey survey : surveyManager.getSurveyList()){
            if(survey.isIs_shelter_adequate()){
                adequateShelter_yes_count++;
            }

            if(survey.isItems_access()){
                essentialItems_yes_count++;
            }

            totalSurveys++;
        }

        baselineData.append("\n,Yes,No,\nDo you have adequate shelter?,");
        calculateHealthPercentages(baselineData, adequateShelter_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - adequateShelter_yes_count), totalSurveys);

        baselineData.append("\nDo you have access to essential items for you household?,");
        calculateHealthPercentages(baselineData, essentialItems_yes_count, totalSurveys);
        calculateHealthPercentages(baselineData, (totalSurveys - essentialItems_yes_count), totalSurveys);
    }

    private void calculateHealthPercentages(StringBuilder baselineData, int value, int total){
        String value_string = "0%,";
        if(total != 0){
            int percent = (int) Math.round(((double)value/total)*100);
            value_string = percent + "%,";
        }
        baselineData.append(value_string);
    }

    private void calculateQuality(String quality, HashMap<String, Integer> quality_count){
        if(quality != null) {
            if (!quality.equals("")) {
                if (quality_count.containsKey(quality)) {
                    Integer count = quality_count.get(quality);
                    quality_count.put(quality, count + 1);
                } else {
                    quality_count.put(quality, 1);
                }
            }
        }
    }

    private void calculateHealthQuality(Byte qualityByte, HashMap<String, Integer> quality_count) {
        String qualityString = "";

        if (qualityByte == 1) {
            qualityString = "Very Poor";
        } else if (qualityByte == 2) {
            qualityString = "Poor";
        } else if (qualityByte == 3) {
            qualityString = "Fine";
        } else if (qualityByte == 4) {
            qualityString = "Good";
        }

        if (!qualityString.equals("")) {
            if (quality_count.containsKey(qualityString)) {
                Integer count = quality_count.get(qualityString);
                quality_count.put(qualityString, count + 1);
            } else {
                quality_count.put(qualityString, 1);
            }
        }
    }

    private void addToHashCount(String value, HashMap<String, Integer> counts) {
        if(counts.containsKey(value)){
            Integer count = counts.get(value);
            counts.put(value, count+1);
        }else{
            counts.put(value, 1);
        }
    }

    private HashMap<String, Integer> createNotInSchoolHash(){
        return new HashMap<String, Integer>() {{
            put("Lack of Funding", 0);
            put("My Disability Stops Me", 0);
            put("Other", 0);
        }};
    }

    private HashMap<String, Integer> createEmployedHash(){
        return new HashMap<String, Integer>() {{
            put("Employed", 0);
            put("Self-Employed", 0);
            put("Other", 0);
        }};
    }

    private HashMap<String, Integer> createNutritionHash(){
        return new HashMap<String, Integer>() {{
            put("Malnourished", 0);
            put("Undernourished", 0);
            put("Well Nourished", 0);
        }};
    }

    private HashMap<String, Integer> createQualityHash(){
        return new HashMap<String, Integer>() {{
            put("Very Poor", 0);
            put("Poor", 0);
            put("Fine", 0);
            put("Good", 0);
        }};
    }

    private HashMap<String, Integer> createAssistiveDeviceHash(){
        return new HashMap<String, Integer>() {{
            put("Wheelchair", 0);
            put("Prosthetic", 0);
            put("Orthotic", 0);
            put("Crutch", 0);
            put("Walking Stick", 0);
            put("Hearing Aid", 0);
            put("Glasses", 0);
            put("Standing Frame", 0);
            put("Corner Seat", 0);
        }};
    }

}