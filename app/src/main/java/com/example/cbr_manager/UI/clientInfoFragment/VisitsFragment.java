package com.example.cbr_manager.UI.clientInfoFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ClientListActivity;
import com.example.cbr_manager.UI.VisitInfoActivity;

import java.util.List;

public class VisitsFragment extends Fragment {

    private ClientInfoActivity infoActivity;
    private long client_id;
    private VisitManager visitManager;

    public VisitsFragment() {
    }

    public static VisitsFragment newInstance(long client_id) {
        VisitsFragment fragment = new VisitsFragment();
        Bundle args = new Bundle();
        args.putLong("client_id", client_id);
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
        Bundle args = getArguments();
        this.client_id = args.getLong("client_id", 0);
        this.visitManager = VisitManager.getInstance(infoActivity);

        View V = inflater.inflate(R.layout.fragment_visits, container, false);
        populateListViewFromList(V, client_id);
        clickVisit(V);

        return V;
    }

    private void populateListViewFromList(View V, long id) {
        VisitManager visitManager = VisitManager.getInstance(infoActivity);
        ListView list = V.findViewById(R.id.visitList);
        ArrayAdapter<Visit> adapter = new VisitsFragment.MyListAdapter(visitManager.getVisits(id));
        list.setAdapter(adapter);
    }

    private void clickVisit(View V) {
        ListView list = V.findViewById(R.id.visitList);
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

            TextView date = view.findViewById(R.id.dateOfVisit);
            TextView purpose = view.findViewById(R.id.purpose_vlist);

            date.setText(currentVisit.getDate());
            purpose.setText(currentVisit.getPurposeOfVisit());

            return view;
        }
    }
}