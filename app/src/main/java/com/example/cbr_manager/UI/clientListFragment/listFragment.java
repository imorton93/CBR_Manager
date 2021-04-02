package com.example.cbr_manager.UI.clientListFragment;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ClientListActivity;

import java.util.List;

public class listFragment extends Fragment {

    private ClientListActivity clientListActivity;
    private ClientManager clientManager;
    private List<Client> searched_clients;

    public listFragment() {
        // Required empty public constructor
    }

    public static listFragment newInstance() {
        listFragment fragment = new listFragment();
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
        this.clientListActivity = (ClientListActivity) getActivity();
        clientManager = ClientManager.getInstance(clientListActivity);

        View V = inflater.inflate(R.layout.fragment_list, container, false);

        sectionDropDownMenu(V);
        villageDropDownMenu(V);

        List<Client> clientList = clientManager.getClients();

        populateAllClientsFromList(clientList, V);
        clickClient(V);
        searchBoxes(V);

        return V;
    }

    private void searchBoxes(View v){
        AutoCompleteTextView first_name_text = v.findViewById(R.id.firstName_clientList);
        AutoCompleteTextView last_name_text = v.findViewById(R.id.lastName_clientList);
        Spinner village_spinner = v.findViewById(R.id.filter_village_clientList);
        Spinner section_spinner = v.findViewById(R.id.filter_section_clientList);
        EditText village_num_text = v.findViewById(R.id.filter_villageNum_clientList);
        ImageButton search_button = v.findViewById(R.id.search_button_clientList);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = first_name_text.getText().toString().trim();
                String last_name = last_name_text.getText().toString().trim();
                String village = village_spinner.getSelectedItem().toString();
                String section = section_spinner.getSelectedItem().toString();
                String village_num = village_num_text.getText().toString().trim();
                searched_clients =  clientManager.getSearchedClients(first_name,
                        last_name, village, section, village_num);
                populateAllClientsFromList(searched_clients, v);
            }
        });

    }

    private void villageDropDownMenu(View v){
        Spinner spinner = v.findViewById(R.id.filter_village_clientList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(clientListActivity,
                R.array.dashboard_locations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void sectionDropDownMenu(View v){
        Spinner spinner = v.findViewById(R.id.filter_section_clientList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(clientListActivity,
                R.array.options_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void populateAllClientsFromList(List<Client> clientList, View v) {
        this.searched_clients = clientList;
        ArrayAdapter<Client> adapter = new MyListAdapter(clientList);
        ListView list = v.findViewById(R.id.clientList);
        list.setAdapter(adapter);
    }

    private void clickClient(View v) {
        ListView list = v.findViewById(R.id.clientList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ClientInfoActivity.makeIntent(clientListActivity, position, searched_clients.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Client> {
        private List<Client> clients;

        public MyListAdapter(List<Client> clients) {
            super(clientListActivity, R.layout.client_list, clients);
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