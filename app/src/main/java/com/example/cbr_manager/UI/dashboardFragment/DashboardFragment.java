package com.example.cbr_manager.UI.dashboardFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.DashboardActivity;

import java.util.List;

public class DashboardFragment extends Fragment {

    private ClientManager clientManager;
    private List<Client> priority_clients;
    private DashboardActivity dashboardActivity;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
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
        this.dashboardActivity = (DashboardActivity)getActivity();
        clientManager = ClientManager.getInstance(dashboardActivity);

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sectionDropDownMenu(v);
        villageDropDownMenu(v);

        this.priority_clients = this.clientManager.getHighPriorityClients();
        populateAllClientsFromList(priority_clients, v);
        clickClient(v);
        dashboardSearchBoxes(v);

        return v;
    }

    private void sectionDropDownMenu(View v){
        Spinner spinner = v.findViewById(R.id.filter_section_dashboard);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dashboardActivity,
                R.array.options_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void villageDropDownMenu(View v){
        Spinner spinner = v.findViewById(R.id.filter_village_dashboard);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dashboardActivity,
                R.array.dashboard_locations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void dashboardSearchBoxes(View view){
        Spinner village_spinner = view.findViewById(R.id.filter_village_dashboard);
        Spinner section_spinner = view.findViewById(R.id.filter_section_dashboard);
        EditText village_num_text = view.findViewById(R.id.filter_villageNum_dashboard);
        ImageButton search_button = view.findViewById(R.id.search_button_dashboard);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String village = village_spinner.getSelectedItem().toString();
                String section = section_spinner.getSelectedItem().toString();
                String village_num = village_num_text.getText().toString().trim();

                priority_clients = clientManager.getDashboardSearchedClients(village, section, village_num);
                populateAllClientsFromList(priority_clients, view);
            }
        });
    }

    private void populateAllClientsFromList(List<Client> priority_clients, View v) {
        ArrayAdapter<Client> adapter = new DashboardFragment.MyListAdapter(priority_clients);
        ListView list = v.findViewById(R.id.dashboard_clients);
        list.setAdapter(adapter);
    }

    private void clickClient(View v) {
        ListView list = v.findViewById(R.id.dashboard_clients);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ClientInfoActivity.makeIntent(dashboardActivity, position, priority_clients.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Client> {
        private List<Client> clients;

        public MyListAdapter(List<Client> clients) {
            super(dashboardActivity, R.layout.client_list, clients);
            this.clients = clients;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.client_list, parent, false);
            }

            Client currentClient;

            currentClient = this.clients.get(position);

            TextView name = view.findViewById(R.id.name_clist);
            TextView village = view.findViewById(R.id.Village_clist);
            TextView villageNum = view.findViewById(R.id.VillageNum_clist);

            String nameConcat = currentClient.getFirstName() + " " + currentClient.getLastName();

            name.setText(nameConcat);
            village.setText(currentClient.getLocation());
            villageNum.setText("" + currentClient.getVillageNumber());

            return view;
        }
    }
}