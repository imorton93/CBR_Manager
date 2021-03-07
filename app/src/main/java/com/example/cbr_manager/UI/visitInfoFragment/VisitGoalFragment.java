package com.example.cbr_manager.UI.visitInfoFragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.VisitInfoActivity;

import java.util.Arrays;

public class VisitGoalFragment extends Fragment {

    private VisitInfoActivity visitInfoActivity;

    public VisitGoalFragment() {

    }

    public static VisitGoalFragment newInstance() {
        VisitGoalFragment fragment = new VisitGoalFragment();
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
        this.visitInfoActivity = (VisitInfoActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_visit_goal, container, false);
        getVisitGoal(v);
        return v;
    }

    private void getVisitGoal(View v) {
        VisitManager visitManager = VisitManager.getInstance(visitInfoActivity);
        Visit currentVisit = visitManager.getVisitById(visitInfoActivity.getVisit_id());

        TextView educationGoal = v.findViewById(R.id.educationGaol);
        TextView socialGoal = v.findViewById(R.id.socialGoal);
        TextView healthGoal = v.findViewById(R.id.healthGoal);

        String healthProvided = Arrays.toString(currentVisit.getHealthProvided().toArray()).replace("[", "").replace("]", "");
        String educationProvided = Arrays.toString(currentVisit.getEducationProvided().toArray()).replace("[", "").replace("]", "");
        String socialProvided = Arrays.toString(currentVisit.getSocialProvided().toArray()).replace("[", "").replace("]", "");

        String education = "Education Goal: " + currentVisit.getEducationGoalMet() + "\n" +
                "Provided: " + educationProvided + "\n" +
                "Outcome: " + currentVisit.getEducationIfConcluded();
        String social = "Social Goal: " + currentVisit.getSocialGoalMet() + "\n" +
                "Provided: " + socialProvided + "\n" +
                "Outcome: " + currentVisit.getSocialIfConcluded();
        String health = "Health Goal: " + currentVisit.getHealthGoalMet() + "\n" +
                "Provided: " + healthProvided + "\n" +
                "Outcome: " + currentVisit.getHealthIfConcluded();

        educationGoal.setText(education);
        socialGoal.setText(social);
        healthGoal.setText(health);
    }
}