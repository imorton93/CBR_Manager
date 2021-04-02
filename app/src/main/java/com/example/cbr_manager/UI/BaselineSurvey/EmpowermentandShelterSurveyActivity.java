package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.cbr_manager.R;

public class EmpowermentandShelterSurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empowerment_survey);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.empowermentSurveyYesRadio1:
                if (checked) {
                    findViewById(R.id.empowermentSurveyRadioGroup3).setVisibility(View.VISIBLE);
                    findViewById(R.id.empowermentSurveyRadioGroup2).setVisibility(View.VISIBLE);
                }
                break;
            case R.id.empowermentSurveyNoRadio1:
                if (checked) {
                    findViewById(R.id.empowermentSurveyRadioGroup3).setVisibility(View.INVISIBLE);
                    findViewById(R.id.empowermentSurveyRadioGroup2).setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}