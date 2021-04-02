package com.example.cbr_manager.UI.BaselineSurvey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.TaskViewActivity;

public class SocialSurveyActivity extends AppCompatActivity {

    private Button nextButton, backButton;
    private RadioGroup valueRadio, independentRadio, participateRadio, disabilityRadio, satisfiedRadio;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, SocialSurveyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_survey);

        nextButton = findViewById(R.id.nextButtonSocialSurvey);
        backButton = findViewById(R.id.backButtonSocialSurvey);
        valueRadio = findViewById(R.id.socialSurveyRadioGroup1);
        independentRadio = findViewById(R.id.socialSurveyRadioGroup2);
        participateRadio = findViewById(R.id.socialSurveyRadioGroup3);
        disabilityRadio = findViewById((R.id.socialSurveyRadioGroup4));
        satisfiedRadio = findViewById((R.id.socialSurveyRadioGroup5));

        nextButton();
        backButton();
        ToolbarButtons();
    }

    public void onRadioButtonClicked(View view) {
    }

    private void nextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(SocialSurveyActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = LivelihoodSurveyActivity.makeIntent(SocialSurveyActivity.this);
                    startActivity(intent);
                }
            }
        });
    }

    private void backButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validateEntries() {
        boolean bool = true;
        if (valueRadio.getCheckedRadioButtonId() == -1 || participateRadio.getCheckedRadioButtonId() == -1
        || satisfiedRadio.getCheckedRadioButtonId() == -1 || independentRadio.getCheckedRadioButtonId() == -1 ||
        disabilityRadio.getCheckedRadioButtonId() == -1 ) {
            bool = false;
        }
        return bool;
    }

    private void ToolbarButtons() {
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(SocialSurveyActivity.this);
                startActivity(intent);
            }
        });
    }
}
