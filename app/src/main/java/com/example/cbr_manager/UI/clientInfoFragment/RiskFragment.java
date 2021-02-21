package com.example.cbr_manager.UI.clientInfoFragment;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;

public class RiskFragment extends Fragment {

    private ClientInfoActivity infoActivity;

    public RiskFragment() {
    }

    public static RiskFragment newInstance() {
        RiskFragment fragment = new RiskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.infoActivity = (ClientInfoActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_risk, container, false);
        getClientRiskInfo(v);
        return v;
    }

    public void getClientRiskInfo(View v){
        DatabaseHelper handler = new DatabaseHelper(this.infoActivity);
        Cursor todoCursor = handler.getRow(infoActivity.getId());

        TextView healthGoal = v.findViewById(R.id.goalHealth);
        TextView educationGoal = v.findViewById(R.id.goalEducation);
        TextView socialGoal = v.findViewById(R.id.goalSocial);

        ProgressBar healthRate = v.findViewById(R.id.progressBarHealth);
        ProgressBar educationRate = v.findViewById(R.id.progressBarEducation);
        ProgressBar socialRate = v.findViewById(R.id.progressBarSocial);

        String health_goal = todoCursor.getString(todoCursor.getColumnIndexOrThrow("HEALTH_GOAL"));
        String education_goal = todoCursor.getString(todoCursor.getColumnIndexOrThrow("EDUCATION_GOAL"));
        String social_goal = todoCursor.getString(todoCursor.getColumnIndexOrThrow("SOCIAL_GOAL"));
        String health_goal_req = todoCursor.getString(todoCursor.getColumnIndexOrThrow("HEALTH_REQUIREMENT"));
        String education_goal_req = todoCursor.getString(todoCursor.getColumnIndexOrThrow("EDUCATION_REQUIRE"));
        String social_goal_req = todoCursor.getString(todoCursor.getColumnIndexOrThrow("SOCIAL_REQUIREMENT"));
        String health_rate = todoCursor.getString(todoCursor.getColumnIndexOrThrow("HEALTH_RATE"));
        String education_rate = todoCursor.getString(todoCursor.getColumnIndexOrThrow("EDUCATION_RATE"));
        String social_rate = todoCursor.getString(todoCursor.getColumnIndexOrThrow("SOCIAL_RATE"));

        String final_health = "Goal: " + health_goal + "\n\nRequires: " + health_goal_req;
        String final_education = "Goal: " + education_goal + "\n\nRequires: " + education_goal_req;
        String final_social = "Goal: " + social_goal + "\n\nRequires: " + social_goal_req;

        healthGoal.setText(final_health);
        educationGoal.setText(final_education);
        socialGoal.setText(final_social);

        progressBarUpdate(healthRate, health_rate);
        progressBarUpdate(educationRate, education_rate);
        progressBarUpdate(socialRate, social_rate);
    }

    public void progressBarUpdate(ProgressBar progressBar, String rate) {
        Resources res = getResources();
        String[] riskTypes = res.getStringArray(R.array.risk_type);

        if(rate.equals(riskTypes[0])) {
            progressBar.setProgress(100);
            progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (rate.equals(riskTypes[1])) {
            progressBar.setProgress(75);
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#FFD000"), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (rate.equals(riskTypes[2])) {
            progressBar.setProgress(50);
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#FFF203"), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            progressBar.setProgress(25);
            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}