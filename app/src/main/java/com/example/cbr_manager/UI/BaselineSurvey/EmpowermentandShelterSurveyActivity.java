package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.ProfileActivity;
import com.example.cbr_manager.UI.TaskViewActivity;
import com.google.android.gms.tasks.Task;

public class EmpowermentandShelterSurveyActivity extends AppCompatActivity {

    private Button submitButton, backButton;
    private RadioGroup radio1, radio2, radio3, radio4, radio5;
    private RadioButton radio1Yes, radio1No;
    EditText organisationName;
    Survey survey;

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

        radio1 = findViewById(R.id.empowermentSurveyRadioGroup1);
        radio2 = findViewById(R.id.empowermentSurveyRadioGroup2);
        radio3 = findViewById(R.id.empowermentSurveyRadioGroup3);
        radio4 = findViewById(R.id.empowermentSurveyRadioGroup4);
        radio5 = findViewById(R.id.empowermentSurveyRadioGroup5);

        organisationName = findViewById(R.id.editTextOrganisation);

        survey = (Survey) getIntent().getSerializableExtra("Survey");

        submitButton = findViewById(R.id.submitButtonEmpowermentSurvey);
        backButton = findViewById(R.id.backButtonEmpowermentSurvey);

        backButton();
        submitButton();
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(EmpowermentandShelterSurveyActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);
    }

    private void submitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(EmpowermentandShelterSurveyActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                else {
                    storeSurveyInput();
                    DatabaseHelper db = new DatabaseHelper(EmpowermentandShelterSurveyActivity.this);
                    setUniqueSurveyId();
                    boolean success = db.addSurvey(survey);
                    if (success) {
                        SurveyManager surveyManager = SurveyManager.getInstance(EmpowermentandShelterSurveyActivity.this);
                        surveyManager.addSurvey(survey);
                        Toast.makeText(EmpowermentandShelterSurveyActivity.this, "Thanks for taking the survey!", Toast.LENGTH_LONG).show();
                        Intent intent = TaskViewActivity.makeIntent(EmpowermentandShelterSurveyActivity.this);
                        startActivity(intent);
                    } else {
                        //Toast.makeText(EmpowermentandShelterSurveyActivity.this, String.valueOf(survey.getId()), Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    private void storeSurveyInput() {
        String answer1 = ((RadioButton) findViewById(radio1.getCheckedRadioButtonId())).getText().toString();
        boolean is_member;
        String organisation_name = null;
        if (answer1.equals("No"))
            is_member = false;
        else {
            is_member = true;
            organisation_name = organisationName.getText().toString();
        }

        String answer2 = ((RadioButton) findViewById(radio2.getCheckedRadioButtonId())).getText().toString();
        boolean is_aware;
        if (answer2.equals("No"))
            is_aware = false;
        else
            is_aware = true;

        String answer3 = ((RadioButton) findViewById(radio3.getCheckedRadioButtonId())).getText().toString();
        boolean influence;
        if (answer3.equals("No"))
            influence = false;
        else
            influence = true;

        String answer4 = ((RadioButton) findViewById(radio4.getCheckedRadioButtonId())).getText().toString();
        boolean shelter;
        if (answer4.equals("No"))
            shelter = false;
        else
            shelter = true;

        String answer5 = ((RadioButton) findViewById(radio5.getCheckedRadioButtonId())).getText().toString();
        boolean access;
        if (answer5.equals("No"))
            access = false;
        else
            access = true;

        survey.setIs_member(is_member);
        survey.setIs_aware(is_aware);
        survey.setIs_influence(influence);
        survey.setIs_shelter_adequate(shelter);
        survey.setItems_access(access);
        survey.setOrganisation(organisation_name);
    }

    private boolean validateEntries() {
        if (radio3.getCheckedRadioButtonId() == -1 || radio2.getCheckedRadioButtonId() == -1 ||
                radio3.getCheckedRadioButtonId() == -1 || radio4.getCheckedRadioButtonId() == -1 || radio5.getCheckedRadioButtonId() == -1)
            return false;
        else if(radio1Yes.isChecked() && organisationName.getText().toString().equals(""))
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
                    organisationName.setVisibility(View.VISIBLE);
                    organisationName.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.empowermentSurveyNoRadio1:
                if (checked) {
                    organisationName.setVisibility(View.INVISIBLE);
                    organisationName.setVisibility(View.INVISIBLE);
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

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(EmpowermentandShelterSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(EmpowermentandShelterSurveyActivity.this);
                startActivity(intent);
            }
        });
    }

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.numUnread();

        if (badge != null) {
            if (size == 0) {
                if (badge.getVisibility() != View.GONE) {
                    badge.setVisibility(View.GONE);
                }
            } else {
                badge.setText(String.valueOf(Math.min(size, 99)));
                if (badge.getVisibility() != View.VISIBLE) {
                    badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setUniqueSurveyId(){
        DatabaseHelper db =  new DatabaseHelper(EmpowermentandShelterSurveyActivity.this);

        int survey_no = db.numberOfSurveysPerClient(survey.getClient_id());
        survey_no++;//next available survey id

        // Concatenate both strings
        String uniqueID = String.valueOf(survey.getClient_id()) + String.valueOf(survey_no);

        // Convert the concatenated string to integer
        long uniqueID_long = Long.parseLong(uniqueID);

        survey.setId(uniqueID_long);
    }
}
