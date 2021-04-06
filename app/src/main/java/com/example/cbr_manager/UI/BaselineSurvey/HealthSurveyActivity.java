package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.TaskViewActivity;

public class HealthSurveyActivity extends AppCompatActivity {

    private Spinner healthSpinner1, healthSpinner2;
    private RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5, radioGroup6;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, HealthSurveyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_survey);
        healthSpinner1 = findViewById(R.id.healthSurveySpinner1);
        healthSpinner2 = findViewById(R.id.healthSurveySpinner2);
        radioGroup1 = findViewById(R.id.healthSurveyRadioGroup1);
        radioGroup2 = findViewById(R.id.healthSurveyRadioGroup2);
        radioGroup3 = findViewById(R.id.healthSurveyRadioGroup3);
        radioGroup4 = findViewById(R.id.healthSurveyRadioGroup4);
        radioGroup5 = findViewById(R.id.healthSurveyRadioGroup5);
        radioGroup6 = findViewById(R.id.healthSurveyRadioGroup6);
        createSpinners();
        nextButton();
        ToolbarButtons();
    }

    private void nextButton() {
        Button nextButton = (Button) findViewById(R.id.nextButtonHealthSurvey);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(HealthSurveyActivity.this, "Please fill all the details!", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = EducationSurveyActivity.makeIntent(HealthSurveyActivity.this);
                    startActivity(intent);
                }
            }
        });
    }


    private void createSpinners() {
        Spinner dropdown1 = findViewById(R.id.healthSurveySpinner1);
        String[] items1 = new String[]{"Choose Option", "Good", "Fine", "Poor", "Very Poor"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown1.setAdapter(adapter1);

        Spinner dropdown2 = findViewById(R.id.healthSurveySpinner2);
        String[] items2 = new String[]{"Choose Option", "Wheelchair", "Prosthetic", "Orthotic", "Crutch"
                , "Walking Stick", "Hearing Aid", "Glasses", "Standing Frame", "Corner Seat"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);
    }

    private boolean validateEntries() {
        boolean bool = true;
        if (radioGroup1.getCheckedRadioButtonId() == -1 || radioGroup2.getCheckedRadioButtonId() == -1
                || radioGroup3.getCheckedRadioButtonId() == -1 || radioGroup4.getCheckedRadioButtonId() == -1
                || radioGroup5.getCheckedRadioButtonId() == -1 || radioGroup6.getCheckedRadioButtonId() == -1
                || healthSpinner1.getSelectedItem().toString() == "Choose Option"|| healthSpinner2.getSelectedItem().toString() == "Choose Option") {
            bool = false;
        }
        return bool;
    }

    //populate this to store in database
    public void onRadioButtonClicked(View view) {
    }

    private void ToolbarButtons() {
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(HealthSurveyActivity.this);
                startActivity(intent);
            }
        });
    }
}
