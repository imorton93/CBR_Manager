package com.example.cbr_manager.UI.visitInfoFragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.VisitInfoActivity;

public class VisitInfoFragment extends Fragment {
    private VisitInfoActivity visitInfoActivity;

    public VisitInfoFragment() {}

    public static VisitInfoFragment newInstance() {
        VisitInfoFragment fragment = new VisitInfoFragment();
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
        View v = inflater.inflate(R.layout.fragment_visit_info, container, false);
        getVisitInfo(v);
        return v;
    }

    private void getVisitInfo(View v) {
        DatabaseHelper handler = new DatabaseHelper(this.visitInfoActivity);

        Cursor todoCursor = handler.getVisit(visitInfoActivity.getVisit_id());

        TextView purposeOfVisit = v.findViewById(R.id.purposeOfVisit);
        TextView dateOfVisit = v.findViewById(R.id.dateOfVisit2);
        TextView locationOfVisit = v.findViewById(R.id.locationOfVisit);
        TextView villageNumber = v.findViewById(R.id.villageNumber);

        if(todoCursor.getCount() >= 1) {
            String purpose_of_visit = todoCursor.getString(todoCursor.getColumnIndexOrThrow("PURPOSE_OF_VISIT"));
            String date_of_visit = todoCursor.getString(todoCursor.getColumnIndexOrThrow("VISIT_DATE"));
            String location_of_visit = todoCursor.getString(todoCursor.getColumnIndexOrThrow("LOCATION"));
            String village_number = todoCursor.getString(todoCursor.getColumnIndexOrThrow("VILLAGE_NUMBER"));

            purposeOfVisit.setText(purpose_of_visit);
            dateOfVisit.setText(date_of_visit);
            locationOfVisit.setText(location_of_visit);
            villageNumber.setText(village_number);
        }
    }
}