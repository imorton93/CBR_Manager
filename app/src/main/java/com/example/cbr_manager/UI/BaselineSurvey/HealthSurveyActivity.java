package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.NewReferralActivity;
import com.example.cbr_manager.UI.NewVisitActivity;
import com.example.cbr_manager.UI.ProfileActivity;
import com.example.cbr_manager.UI.TaskViewActivity;

public class HealthSurveyActivity extends AppCompatActivity {

    private Spinner healthSpinner1, healthSpinner2, healthSpinner3;
    private RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5;
    private Survey survey = new Survey();

    private static long client_id;
    private static int client_pos;
    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";
    public static final String R_CLIENT_POS_PASSED_IN = "r_client_pos_passed_in";

    public static Intent makeIntent(Context context, int position, long id) {
        Intent intent =  new Intent(context, HealthSurveyActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        intent.putExtra(R_CLIENT_POS_PASSED_IN, position);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        client_id = intent.getLongExtra(R_CLIENT_ID_PASSED_IN, 0);
        client_pos = intent.getIntExtra(R_CLIENT_POS_PASSED_IN, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_survey);

        extractIntent();

        healthSpinner1 = findViewById(R.id.healthSurveySpinner1);
        healthSpinner2 = findViewById(R.id.healthSurveySpinner2);
        healthSpinner3 = findViewById(R.id.healthSurveySpinner3);
        radioGroup1 = findViewById(R.id.healthSurveyRadioGroup1);
        radioGroup2 = findViewById(R.id.healthSurveyRadioGroup2);
        radioGroup3 = findViewById(R.id.healthSurveyRadioGroup3);
        radioGroup4 = findViewById(R.id.healthSurveyRadioGroup4);
        radioGroup5 = findViewById(R.id.healthSurveyRadioGroup5);
        createSpinners();
        nextButton();
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(HealthSurveyActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);
    }

    private void nextButton() {
        Button nextButton = (Button) findViewById(R.id.nextButtonHealthSurvey);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(HealthSurveyActivity.this, "Please fill all the details!", Toast.LENGTH_SHORT).show();
                else {
                    storeSurveyInput();
                    Intent intent = EducationSurveyActivity.makeIntent(HealthSurveyActivity.this);
                    intent.putExtra("Survey", survey);
                    startActivity(intent);
                }
            }
        });
    }

    private void storeSurveyInput() {

        String answer1 = healthSpinner1.getSelectedItem().toString();
        byte health_condition = 0;
        if(answer1.equals("Good"))
            health_condition = 1;
        else if(answer1.equals("Fine"))
            health_condition = 2;
        else if(answer1.equals("Poor"))
            health_condition = 3;
        else if(answer1.equals("Very Poor"))
            health_condition = 4;

        String answer2 = ((RadioButton)findViewById(radioGroup1.getCheckedRadioButtonId())).getText().toString();
        boolean have_rehab;
        if(answer2.equals("No"))
            have_rehab = false;
        else
            have_rehab = true;

        String answer3 = ((RadioButton)findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString();
        boolean need_rehab;
        if(answer3.equals("No"))
            need_rehab = false;
        else
            need_rehab = true;

        String answer4 = ((RadioButton)findViewById(radioGroup3.getCheckedRadioButtonId())).getText().toString();
        boolean have_device;
        if(answer4.equals("No"))
            have_device = false;
        else
            have_device = true;

        String answer5 = ((RadioButton)findViewById(radioGroup4.getCheckedRadioButtonId())).getText().toString();
        boolean is_device;
        if(answer5.equals("No"))
            is_device = false;
        else
            is_device = true;

        String answer6 = ((RadioButton)findViewById(radioGroup5.getCheckedRadioButtonId())).getText().toString();
        boolean need_device;
        if(answer6.equals("No"))
            need_device = false;
        else
            need_device = true;

        String what_device = healthSpinner2.getSelectedItem().toString();

        String answer8 = healthSpinner3.getSelectedItem().toString();
        byte is_satisfied = 0;
        if(answer8.equals("Good"))
            is_satisfied = 1;
        else if(answer8.equals("Fine"))
            is_satisfied = 2;
        else if(answer8.equals("Poor"))
            is_satisfied = 3;
        else if(answer8.equals("Very Poor"))
            is_satisfied = 4;

        setUniqueSurveyId();
        survey.setHealth_condition(health_condition);
        survey.setHave_rehab_access(have_rehab);
        survey.setNeed_rehab_access(need_rehab);
        survey.setHave_device(have_device);
        survey.setNeed_device(need_device);
        survey.setDevice_type(what_device);
        survey.setDevice_condition(is_device);
        survey.setIs_satisfied(is_satisfied);
        survey.setClient_id(client_id);
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

        Spinner dropdown3 = findViewById(R.id.healthSurveySpinner3);
        String[] items3 = new String[]{"Choose Option", "Good", "Fine", "Poor", "Very Poor"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown3.setAdapter(adapter3);
    }

    private boolean validateEntries() {
        boolean bool = true;
        if (radioGroup1.getCheckedRadioButtonId() == -1 || radioGroup2.getCheckedRadioButtonId() == -1
                || radioGroup3.getCheckedRadioButtonId() == -1 || radioGroup4.getCheckedRadioButtonId() == -1
                || radioGroup5.getCheckedRadioButtonId() == -1 || healthSpinner1.getSelectedItem().toString() == "Choose Option"
                || healthSpinner2.getSelectedItem().toString() == "Choose Option"
                || healthSpinner3.getSelectedItem().toString() == "Choose Option") {
            bool = false;
        }
        return bool;
    }

    //populate this to store in database
    public void onRadioButtonClicked(View view) {
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(HealthSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(HealthSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(HealthSurveyActivity.this);
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
        DatabaseHelper db =  new DatabaseHelper(HealthSurveyActivity.this);

        int referral_no = db.numberOfSurveysPerClient(survey.getClient_id());
        referral_no++;//next available referral id

        // Concatenate both strings
        String uniqueID = String.valueOf(survey.getClient_id()) + String.valueOf(referral_no);

        // Convert the concatenated string to integer
        long uniqueID_long = Long.parseLong(uniqueID);

        survey.setId(uniqueID_long);
    }
}
