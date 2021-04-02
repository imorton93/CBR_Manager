package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.TaskViewActivity;

public class EducationSurveyActivity extends AppCompatActivity {

    private RadioGroup doesGoRadio, haveRadio, doRadio;
    private EditText editGrade;
    private TextView gradeText, whyText, haveText, doText;
    private Spinner whySpinner;
    private Button nextButton, backButton;
    private RadioButton doesGoYesRadio, doesGoNoRadio;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, EducationSurveyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_survey);

        doesGoRadio = findViewById(R.id.educationSurveyRadioGroup1);
        editGrade = findViewById(R.id.educationSurveyEditNo);
        gradeText = findViewById(R.id.textViewEducation3);
        whyText = findViewById(R.id.textViewEducation4);
        whySpinner = findViewById(R.id.educationSurveySpinner1);
        haveText = findViewById(R.id.textViewEducation5);
        haveRadio = findViewById(R.id.educationSurveyRadioGroup2);
        doText = findViewById(R.id.textViewEducation6);
        doRadio = findViewById(R.id.educationSurveyRadioGroup3);
        nextButton = findViewById(R.id.nextButtonEducationSurvey);
        backButton = findViewById(R.id.backButtonEducationSurvey);
        doesGoYesRadio= findViewById(R.id.educationSurveyYesRadio1);
        doesGoNoRadio = findViewById(R.id.educationSurveyNoRadio1);

        createSpinners();
        nextButton();
        backButton();
        ToolbarButtons();
    }

    private void nextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(EducationSurveyActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = SocialSurveyActivity.makeIntent(EducationSurveyActivity.this);
                    startActivity(intent);
                }
            }
        });
    }

    private void backButton() {
        Button backButton = (Button) findViewById(R.id.backButtonEducationSurvey);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createSpinners() {
        Spinner dropdown1 = findViewById(R.id.educationSurveySpinner1);
        String[] items1 = new String[]{"Lack of Funding", "My Disability Stops Me", "Other"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown1.setAdapter(adapter1);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.educationSurveyYesRadio1:
                if (checked) {
                    editGrade.setVisibility(View.VISIBLE);
                    gradeText.setVisibility(View.VISIBLE);
                    whyText.setVisibility(View.INVISIBLE);
                    whySpinner.setVisibility(View.INVISIBLE);
                    haveRadio.setVisibility(View.INVISIBLE);
                    haveText.setVisibility(View.INVISIBLE);
                    doText.setVisibility(View.INVISIBLE);
                    doRadio.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.educationSurveyNoRadio1:
                if (checked) {
                    editGrade.setVisibility(View.INVISIBLE);
                    whyText.setVisibility(View.VISIBLE);
                    whySpinner.setVisibility(View.VISIBLE);
                    haveRadio.setVisibility(View.VISIBLE);
                    haveText.setVisibility(View.VISIBLE);
                    doText.setVisibility(View.VISIBLE);
                    doRadio.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private boolean validateEntries() {
        boolean bool = true;
        if (doesGoRadio.getCheckedRadioButtonId() == -1) {
            bool = false;
        }
        else if (doesGoYesRadio.isChecked() && editGrade.length() == 0) {
            bool = false;
        }
        else if ((doesGoNoRadio.isChecked()&&whySpinner.getSelectedItem().toString() == "") || (doesGoNoRadio.isChecked()&&haveRadio.getCheckedRadioButtonId() == -1)
                || (doesGoNoRadio.isChecked()&&doRadio.getCheckedRadioButtonId() == -1)) {
            bool = false;
        }
        return bool;
    }

    private void ToolbarButtons() {
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(EducationSurveyActivity.this);
                startActivity(intent);
            }
        });
    }
}