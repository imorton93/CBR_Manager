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

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.VisitInfoActivity;

import java.util.List;

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
        VisitManager visitManager = VisitManager.getInstance(visitInfoActivity);
        Visit currentVisit = visitManager.getVisitById(visitInfoActivity.getVisit_id());
        System.out.println("In VISIT INFO FRAG ID: " + visitInfoActivity.getVisit_id());

        TextView purposeOfVisit = v.findViewById(R.id.purposeOfVisit);
        TextView dateOfVisit = v.findViewById(R.id.dateOfVisit2);
        TextView locationOfVisit = v.findViewById(R.id.locationOfVisit);
        TextView villageNumber = v.findViewById(R.id.villageNumber);

        purposeOfVisit.setText(currentVisit.getPurposeOfVisit());
        dateOfVisit.setText(currentVisit.getDate());
        locationOfVisit.setText(currentVisit.getLocation());
        villageNumber.setText(String.valueOf(currentVisit.getVillageNumber()));
    }
}