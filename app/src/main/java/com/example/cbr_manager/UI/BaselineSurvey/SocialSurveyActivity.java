package com.example.cbr_manager.UI.BaselineSurvey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.ProfileActivity;
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

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(SocialSurveyActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);
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

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(SocialSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(SocialSurveyActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(SocialSurveyActivity.this);
                startActivity(intent);
            }
        });
    }

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.size();

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
