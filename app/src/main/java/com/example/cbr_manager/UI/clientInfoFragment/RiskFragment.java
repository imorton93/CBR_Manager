package com.example.cbr_manager.UI.clientInfoFragment;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
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

        ImageView socialDot = v.findViewById(R.id.SocialDot);
        ImageView educationDot = v.findViewById(R.id.EducationDot);
        ImageView healthDot = v.findViewById(R.id.healthDot);

        riskDot(currentClient.getHealthRate(), v, healthDot);
        riskDot(currentClient.getEducationRate(), v, educationDot);
        riskDot(currentClient.getSocialStatusRate(), v, socialDot);
    }

    public void riskDot(String rate, View v, ImageView imageView) {
        Resources res = getResources();
        String[] riskTypes = res.getStringArray(R.array.risk_type);

        if(rate.equals(riskTypes[0])) {
            imageView.setImageResource(R.drawable.red);
        } else if (rate.equals(riskTypes[1])) {
            imageView.setImageResource(R.drawable.orange);
        } else if (rate.equals(riskTypes[2])) {
            imageView.setImageResource(R.drawable.yellow);
        } else {
            imageView.setImageResource(R.drawable.green);
        }
    }
}