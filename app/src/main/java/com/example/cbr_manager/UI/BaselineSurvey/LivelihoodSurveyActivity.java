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

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.ProfileActivity;
import com.example.cbr_manager.UI.TaskViewActivity;

public class LivelihoodSurveyActivity extends AppCompatActivity {

    private Button nextButton, backButton;
    private RadioGroup radio1, radio3, radio4, radio5;
    private RadioButton yesRadio1, noRadio1;
    private EditText editText1;
    private Spinner spinner1;
    Survey survey;

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
        radio3 = findViewById(R.id.livelihoodSurveyRadioGroup3);
        radio4 = findViewById((R.id.livelihoodSurveyRadioGroup4));
        radio5 = findViewById((R.id.livelihoodSurveyRadioGroup5));
        spinner1 = findViewById(R.id.livelihoodSurveySpinner1);
        editText1 = findViewById(R.id.livelihoodSurveyEditText1);
        yesRadio1 = findViewById(R.id.livelihoodSurveyYesRadio1);
        noRadio1 = findViewById(R.id.livelihoodSurveyNoRadio1);
        survey = (Survey) getIntent().getSerializableExtra("Survey");

        createSpinner();
        nextButton();
        backButton();
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(LivelihoodSurveyActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);
    }

    private void createSpinner() {
        String[] items1 = new String[]{"Choose Option", "Self-Employed", "Employed", "Other"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.livelihoodSurveyYesRadio1:
                if (checked) {
                    editText1.setVisibility(View.VISIBLE);
                    editText1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.livelihoodSurveyNoRadio1:
                if (checked) {
                    editText1.setVisibility(View.INVISIBLE);
                    editText1.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    void nextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEntries())
                    Toast.makeText(LivelihoodSurveyActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                else {
                    storeSurveyInput();
                    Intent intent = FoodSurveyActivity.makeIntent(LivelihoodSurveyActivity.this);
                    intent.putExtra("Survey", survey);
                    startActivity(intent);
                }
            }
        });
    }

    private void storeSurveyInput() {
        String is_self = spinner1.getSelectedItem().toString();

        String answer2 = ((RadioButton) findViewById(radio1.getCheckedRadioButtonId())).getText().toString();
        boolean is_working;
        String work_type = null;
        if (answer2.equals("No"))
            is_working = false;
        else {
            is_working = true;
            work_type = editText1.getText().toString();
        }

        String answer4 = ((RadioButton) findViewById(radio3.getCheckedRadioButtonId())).getText().toString();
        boolean needs_met;
        if (answer4.equals("No"))
            needs_met = false;
        else
            needs_met = true;

        String answer5 = ((RadioButton) findViewById(radio4.getCheckedRadioButtonId())).getText().toString();
        boolean is_affected;
        if (answer5.equals("No"))
            is_affected = false;
        else
            is_affected = true;

        String answer6 = ((RadioButton) findViewById(radio5.getCheckedRadioButtonId())).getText().toString();
        boolean want_work;
        if (answer6.equals("No"))
            want_work = false;
        else
            want_work = true;

        survey.setIs_working(is_working);
        survey.setWant_work(want_work);
        survey.setIs_work_affected(is_affected);
        survey.setIs_self_employed(is_self);
        survey.setNeeds_met(needs_met);
        survey.setWork_type(work_type);
    }

    void backButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    boolean validateEntries() {
        boolean bool = true;
        if (spinner1.getSelectedItem().toString() == "Choose Option" || radio5.getCheckedRadioButtonId() == -1 ||
                radio4.getCheckedRadioButtonId() == -1 || radio3.getCheckedRadioButtonId() == -1 ||
                radio1.getCheckedRadioButtonId() == -1) {
            bool = false;
        } else if (yesRadio1.isChecked() && editText1.getText().toString().equals(""))
            bool = false;
        return bool;
    }

    private void ToolbarButtons() {
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