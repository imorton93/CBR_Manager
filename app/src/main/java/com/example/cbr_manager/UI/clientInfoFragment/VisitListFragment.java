package com.example.cbr_manager.UI.clientInfoFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ClientListActivity;
import com.example.cbr_manager.UI.VisitInfoActivity;
import com.example.cbr_manager.UI.clientListFragment.MapsFragment;
import com.example.cbr_manager.UI.clientListFragment.listFragment;

import java.util.List;

public class VisitListFragment extends Fragment {

    private static final String ARG_PARAM1 = "client_id";

    private ClientInfoActivity infoActivity;
    private long client_id;
    private VisitManager visitManager;
    private ClientManager clientManager;

    public VisitListFragment() { }

    public static VisitListFragment newInstance(long client_id) {
        VisitListFragment fragment = new VisitListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, client_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client_id = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.infoActivity = (ClientInfoActivity)getActivity();
        Bundle args = getArguments();
        this.client_id = args.getLong("client_id", 0);
        this.visitManager = VisitManager.getInstance(infoActivity);
        this.clientManager = ClientManager.getInstance(infoActivity);
        View V = inflater.inflate(R.layout.fragment_visit_list, container, false);
        populateListViewFromList(V, client_id);
        clickVisit(V);
        return V;
    }

    private void populateListViewFromList(View V, long id) {
        VisitManager visitManager = VisitManager.getInstance(infoActivity);
        ListView list = V.findViewById(R.id.VisitList);
        ArrayAdapter<Visit> adapter = new VisitListFragment.MyListAdapter(visitManager.getVisits(id));
        list.setAdapter(adapter);
    }

    private void clickVisit(View V) {
        ListView list = V.findViewById(R.id.VisitList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Visit> visits = visitManager.getVisits(client_id);
                Visit currentVisit = visits.get(position);
                Intent intent = VisitInfoActivity.makeIntent(getActivity(), currentVisit.getVisit_id(), position);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Visit> {
        public MyListAdapter(List<Visit> visits) {
            super(infoActivity, R.layout.visit_list, visits);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.visit_list, parent, false);
            }

            Visit currentVisit;
            List<Visit> visits = visitManager.getVisits(client_id);
            currentVisit = visits.get(position);
            Client client = clientManager.getClientById(client_id);

            TextView date = view.findViewById(R.id.dateOfVisit);
            TextView goal = view.findViewById(R.id.goal_vlist);
            TextView purpose = view.findViewById(R.id.purpose_vlist);
            TextView outcome = view.findViewById(R.id.outcome_vlist);

            String dateOfVisit = "<b>" + currentVisit.getDate() + "</b>";
            String goalOfClient = getGoal(client);
            String purposeOfVisit = "<b>Purpose of Visit:</b> " + currentVisit.getPurposeOfVisit();

            date.setText(Html.fromHtml(dateOfVisit));
            goal.setText(Html.fromHtml(goalOfClient));
            purpose.setText(Html.fromHtml(purposeOfVisit));
            outcome.setText(Html.fromHtml(getOutcome(currentVisit)));

            return view;
        }

        private String getGoal(Client client){
            String healthGoal = "<b>Health Goal:</b> " + client.getHealthIndividualGoal() + "<br>";
            String educationGoal = "<b>Education Goal:</b> " + client.getEducationIndividualGoal() + "<br>";
            String socialGoal = "<b>Social Status Goal:</b> " + client.getSocialStatusIndividualGoal();

            return healthGoal + educationGoal + socialGoal;
        }

        private String getOutcome(Visit currentVisit){
            String healthOutcome = "<b>Health Outcome:</b> ";
            String educationOutcome = "<b>Education Outcome:</b> ";
            String socialOutcome = "<b>Social Status Outcome:</b> ";

            if (currentVisit.getHealthGoalMet().equals("Concluded")) {
                healthOutcome += currentVisit.getHealthIfConcluded();
            } else {
                healthOutcome += currentVisit.getHealthGoalMet();
            }

            if (currentVisit.getSocialGoalMet().equals("Concluded")) {
                socialOutcome += currentVisit.getSocialIfConcluded();
            } else {
                socialOutcome += currentVisit.getSocialGoalMet();
            }

            if (currentVisit.getEducationGoalMet().equals("Concluded")) {
                educationOutcome += currentVisit.getEducationIfConcluded();
            } else {
                educationOutcome += currentVisit.getEducationGoalMet();
            }

            return healthOutcome + "<br>" + educationOutcome + "<br>" + socialOutcome;
        }
    }
}