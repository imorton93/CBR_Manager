package com.example.cbr_manager.UI.BaselineSurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.cbr_manager.R;

public class FoodSurveyActivity extends AppCompatActivity {

    private Spinner foodSpinner1, foodSpinner2;
    private RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5, radioGroup6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_survey);
        foodSpinner1 = findViewById(R.id.foodSurveySpinner1);
        foodSpinner2 = findViewById(R.id.foodSurveySpinner2);
        radioGroup1 = findViewById(R.id.foodSurveyRadioGroup1);
        radioGroup2 = findViewById(R.id.foodSurveyRadioGroup2);
        createSpinners();
        nextButton();
    }

    private void nextButton() {
    }

    private void createSpinners() {
        Spinner dropdown1 = findViewById(R.id.foodSurveySpinner1);
        String[] items1 = new String[]{"Good", "Fine", "Poor", "Very Poor"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown1.setAdapter(adapter1);

        Spinner dropdown2 = findViewById(R.id.foodSurveySpinner2);
        String[] items2 = new String[]{"Malnourished", "Undernourished", "Well Nourished"};
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
    
}