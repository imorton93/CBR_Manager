package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class SurveyManager {
    private List<Survey> surveyList = new ArrayList<>();
    private static SurveyManager instance;
    private DatabaseHelper databaseHelper;

    public static SurveyManager getInstance(Context context) {
        if (instance == null) {
            instance = new SurveyManager(context);
        }

        return instance;
    }

    public SurveyManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public List<Survey> getSurveyList() {
        return surveyList;
    }

    public void updateList() {
    }

    public void clear() {
        surveyList.clear();
    }

    public int size() {
        return surveyList.size();
    }
}
