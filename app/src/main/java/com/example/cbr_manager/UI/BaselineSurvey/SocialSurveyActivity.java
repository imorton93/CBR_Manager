package com.example.cbr_manager.UI.BaselineSurvey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.R;

public class SocialSurveyActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, SocialSurveyActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_survey);
    }

    public void onRadioButtonClicked(View view) {
    }
}
