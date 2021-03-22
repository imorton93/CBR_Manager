package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.LoginActivity;
import com.example.cbr_manager.UI.SignUpActivity;

public class HealthSurveyActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, HealthSurveyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_survey);
        createSpinners();
        nextButton();
    }

    private void nextButton() {
        Button nextButton = (Button) findViewById(R.id.nextButtonHealthSurvey);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EducationSurveyActivity.makeIntent(HealthSurveyActivity.this);
                startActivity(intent);
            }
        });
    }


    private void createSpinners() {
        Spinner dropdown1 = findViewById(R.id.healthSurveySpinner1);
        String[] items1 = new String[]{"Good", "Fine", "Poor", "Very Poor"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown1.setAdapter(adapter1);

        Spinner dropdown2 = findViewById(R.id.spinner2);
        String[] items2 = new String[]{"Wheelchair", "Pprosthetic", "Orthotic", "Crutch"
                , "Walking Stick", "Hearing Aid", "Glasses", "Standing Frame", "Corner Seat"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);
    }

    //populate this to store in database
    public void onRadioButtonClicked(View view) {
        Toast.makeText(HealthSurveyActivity.this, "This is my Toast message!", Toast.LENGTH_LONG).show();
    }
}
