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
import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.ProfileActivity;
import com.example.cbr_manager.UI.TaskViewActivity;

public class FoodSurveyActivity extends AppCompatActivity {

    private Spinner foodSpinner1, foodSpinner2;
    private RadioGroup radioGroup1, radioGroup2;
    private RadioButton yesRadio2, noRadio2;
    private Button nextButton, backButton;
    Survey survey;
    
    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, FoodSurveyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_survey);
        foodSpinner1 = findViewById(R.id.foodSurveySpinner1);
        foodSpinner2 = findViewById(R.id.foodSurveySpinner2);
        radioGroup1 = findViewById(R.id.foodSurveyRadioGroup1);
        radioGroup2 = findViewById(R.id.foodSurveyRadioGroup2);
        noRadio2 = findViewById(R.id.foodSurveyNoRadio2);
        yesRadio2 = findViewById(R.id.foodSurveyYesRadio2);
        nextButton = findViewById(R.id.nextButtonFoodSurvey);
        backButton = findViewById(R.id.backButtonFoodSurvey);
        survey = (Survey) getIntent().getSerializableExtra("Survey");
        
        createSpinners();
        nextButton();
        backButton();
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(FoodSurveyActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);
    }

    private void backButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void nextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(FoodSurveyActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                else {
                    storeSurveyInput();
                    Intent intent = EmpowermentandShelterSurveyActivity.makeIntent(FoodSurveyActivity.this);
                    intent.putExtra("Survey", survey);
                    startActivity(intent);
                }
            }
        });
    }

    private void storeSurveyInput() {
        boolean referral_required = false;
        String child_condition = null;
        String food_security = foodSpinner1.getSelectedItem().toString();

        String answer1 = ((RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId())).getText().toString();
        boolean is_enough;
        if (answer1.equals("No"))
            is_enough = false;
        else
            is_enough = true;

        String answer2 = ((RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString();
        boolean is_child;
        if (answer2.equals("No")) {
            is_child = false;
            child_condition = null;
        }
        else {
            is_child = true;
            child_condition = foodSpinner2.getSelectedItem().toString();
            if(child_condition.equals("Malnourished")) {
                Toast.makeText(FoodSurveyActivity.this, "Child needs referral immediately!", Toast.LENGTH_LONG).show();
                referral_required = true;
            }
        }
        survey.setChild_condition(child_condition);
        survey.setFood_security(food_security);
        survey.setIs_diet_enough(is_enough);
        survey.setReferral_required(referral_required);
    }

    private boolean validateEntries() {
        if(foodSpinner1.getSelectedItem().toString()=="Choose Option"||
        radioGroup1.getCheckedRadioButtonId() == -1|| radioGroup2.getCheckedRadioButtonId() == -1)
            return false;
        else if(foodSpinner2.getSelectedItem().toString() == "Choose Option" &&  yesRadio2.isChecked()==true )
            return false;
        return true;
    }

    private void createSpinners() {
        Spinner dropdown1 = findViewById(R.id.foodSurveySpinner1);
        String[] items1 = new String[]{"Choose Option", "Good", "Fine", "Poor", "Very Poor"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown1.setAdapter(adapter1);

        Spinner dropdown2 = findViewById(R.id.foodSurveySpinner2);
        String[] items2 = new String[]{"Choose Option", "Malnourished", "Undernourished", "Well Nourished"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.foodSurveyYesRadio2:
                if (checked) {
                    foodSpinner2.setVisibility(View.VISIBLE);
                    findViewById(R.id.textViewfood5).setVisibility(View.VISIBLE);
                }
                break;
            case R.id.foodSurveyNoRadio2:
                if (checked) {
                    foodSpinner2.setVisibility(View.INVISIBLE);
                    findViewById(R.id.textViewfood5).setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(FoodSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(FoodSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(FoodSurveyActivity.this);
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
}