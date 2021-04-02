package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.TaskViewActivity;
import com.google.android.gms.tasks.Task;

public class EmpowermentandShelterSurveyActivity extends AppCompatActivity {

    private Button submitButton, backButton;
    private RadioGroup radio2, radio3, radio4, radio5;
    private RadioButton radio1Yes, radio1No;
    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, EmpowermentandShelterSurveyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empowerment_survey);

        radio1Yes = findViewById(R.id.empowermentSurveyYesRadio1);
        radio1No = findViewById(R.id.empowermentSurveyNoRadio1);
        radio2 = findViewById(R.id.empowermentSurveyRadioGroup2);
        radio3 = findViewById(R.id.empowermentSurveyRadioGroup3);
        radio4 = findViewById(R.id.empowermentSurveyRadioGroup4);
        radio5 = findViewById(R.id.empowermentSurveyRadioGroup5);

        submitButton= findViewById(R.id.submitButtonEmpowermentSurvey);
        backButton= findViewById(R.id.backButtonEmpowermentSurvey);

        backButton();
        submitButton();
        ToolbarButtons();
    }

    private void submitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(EmpowermentandShelterSurveyActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = TaskViewActivity.makeIntent(EmpowermentandShelterSurveyActivity.this);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateEntries() {
        if(radio1Yes.isChecked()&&(radio2.getCheckedRadioButtonId() == -1||
        radio3.getCheckedRadioButtonId() == -1||radio4.getCheckedRadioButtonId() == -1||radio5.getCheckedRadioButtonId() == -1))
            return false;
        else if(radio1No.isChecked()&&(radio4.getCheckedRadioButtonId() == -1||radio5.getCheckedRadioButtonId() == -1))
            return false;
        return true;
    }

    private void backButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void ToolbarButtons() {
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(EmpowermentandShelterSurveyActivity.this);
                startActivity(intent);
            }
        });
    }
}