package com.example.cbr_manager.UI.visitInfoFragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.VisitInfoActivity;

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
        // Inflate the layout for this fragment
        this.visitInfoActivity = (VisitInfoActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_visit_goal, container, false);
        getVisitGoal(v);
        return v;
    }

    private void getVisitGoal(View v) {
        DatabaseHelper handler = new DatabaseHelper(this.visitInfoActivity);

        Cursor todoCursor = handler.getVisit(visitInfoActivity.getVisit_id());

        TextView educationGoal = v.findViewById(R.id.educationGaol);
        TextView socialGoal = v.findViewById(R.id.socialGoal);
        TextView healthGoal = v.findViewById(R.id.healthGoal);

        if(todoCursor.getCount() >= 1) {
            String education_goal = todoCursor.getString(todoCursor.getColumnIndexOrThrow("EDU_GOAL_STATUS"));
            String social_goal = todoCursor.getString(todoCursor.getColumnIndexOrThrow("SOCIAL_GOAL_STATUS"));
            String health_goal = todoCursor.getString(todoCursor.getColumnIndexOrThrow("HEALTH_GOAL_STATUS"));
            String education_outcome = todoCursor.getString(todoCursor.getColumnIndexOrThrow("EDUCATION_OUTCOME"));
            String social_outcome = todoCursor.getString(todoCursor.getColumnIndexOrThrow("SOCIAL_OUTCOME"));
            String health_outcome = todoCursor.getString(todoCursor.getColumnIndexOrThrow("HEALTH_OUTCOME"));
            String education_provided = todoCursor.getString(todoCursor.getColumnIndexOrThrow("EDU_PROVIDED"));
            String social_provided = todoCursor.getString(todoCursor.getColumnIndexOrThrow("SOCIAL_PROVIDED"));
            String health_provided = todoCursor.getString(todoCursor.getColumnIndexOrThrow("HEALTH_PROVIDED"));

            String education = "Education Goal: " + education_goal + "\n" + "Provided: " + education_provided + "\n" + "Outcome: " + education_outcome;
            String social = "Social Goal: " + social_goal + "\n" + "Provided: " + social_provided + "\n" + "Outcome: " + social_outcome;
            String health = "Health Goal: " + health_goal + "\n" + "Provided: " + health_provided + "\n" + "Outcome: " + health_outcome;

            // Populate fields with extracted properties
            educationGoal.setText(education);
            socialGoal.setText(social);
            healthGoal.setText(health);
        }

    }
}