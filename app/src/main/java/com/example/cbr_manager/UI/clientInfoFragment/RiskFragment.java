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

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
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
        ClientManager clientManager = ClientManager.getInstance(infoActivity);
        Client currentClient = clientManager.getClientById(infoActivity.getId());

        TextView healthGoal = v.findViewById(R.id.goalHealth);
        TextView educationGoal = v.findViewById(R.id.goalEducation);
        TextView socialGoal = v.findViewById(R.id.goalSocial);

        ProgressBar healthRate = v.findViewById(R.id.progressBarHealth);
        ProgressBar educationRate = v.findViewById(R.id.progressBarEducation);
        ProgressBar socialRate = v.findViewById(R.id.progressBarSocial);

        TextView healthRisk = v.findViewById(R.id.healthRisk);
        TextView educationRisk = v.findViewById(R.id.educationRisk);
        TextView socialRisk = v.findViewById(R.id.socialRisk);


        String final_health = "Goal: " + currentClient.getHealthIndividualGoal() + "\n\nRequires: " + currentClient.getHealthRequire();
        String final_education = "Goal: " + currentClient.getEducationIndividualGoal() + "\n\nRequires: " + currentClient.getEducationRequire();
        String final_social = "Goal: " + currentClient.getSocialStatusIndividualGoal() + "\n\nRequires: " + currentClient.getSocialStatusRequire();

        healthGoal.setText(final_health);
        educationGoal.setText(final_education);
        socialGoal.setText(final_social);

        healthRisk.setText(currentClient.getHealthRate());
        educationRisk.setText(currentClient.getEducationRate());
        socialRisk.setText(currentClient.getSocialStatusRate());

        progressBarUpdate(healthRate, currentClient.getHealthRate());
        progressBarUpdate(educationRate, currentClient.getEducationRate());
        progressBarUpdate(socialRate, currentClient.getSocialStatusRate());
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