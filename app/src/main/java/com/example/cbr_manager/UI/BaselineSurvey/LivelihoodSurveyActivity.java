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
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.ProfileActivity;
import com.example.cbr_manager.UI.TaskViewActivity;

public class LivelihoodSurveyActivity extends AppCompatActivity {

    private Button nextButton, backButton;
    private RadioGroup radio1, radio2, radio3, radio4, radio5;
    private Spinner spinner1;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, LivelihoodSurveyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livelihood_survey);

        nextButton = findViewById(R.id.nextButtonLivelihoodSurvey);
        backButton = findViewById(R.id.backButtonLivelihoodSurvey);
        radio1 = findViewById(R.id.livelihoodSurveyRadioGroup1);
        radio2 = findViewById(R.id.livelihoodSurveyRadioGroup2);
        radio3 = findViewById(R.id.livelihoodSurveyRadioGroup3);
        radio4 = findViewById((R.id.livelihoodSurveyRadioGroup4));
        radio5 = findViewById((R.id.livelihoodSurveyRadioGroup5));
        spinner1 = findViewById(R.id.livelihoodSurveySpinner1);

        createSpinner();
        nextButton();
        backButton();
        ToolbarButtons();
    }

    private void createSpinner() {
        String[] items1 = new String[]{"Choose Option", "Self-Employed", "Employed", "Other"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
    }

    public void onRadioButtonClicked(View view) {
    }

    void nextButton(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(LivelihoodSurveyActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = FoodSurveyActivity.makeIntent(LivelihoodSurveyActivity.this);
                    startActivity(intent);
                }
            }
        });
    }

    void backButton(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    boolean validateEntries(){
        boolean bool = true;
        if(spinner1.getSelectedItem().toString() == "Choose Option"||radio5.getCheckedRadioButtonId() == -1||
        radio4.getCheckedRadioButtonId() == -1|| radio3.getCheckedRadioButtonId() == -1||
        radio1.getCheckedRadioButtonId() == -1||radio2.getCheckedRadioButtonId() == -1) {
            bool = false;
        }
        return bool;
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(LivelihoodSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(LivelihoodSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(LivelihoodSurveyActivity.this);
                startActivity(intent);
            }
        });
    }
}