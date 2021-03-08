package com.example.cbr_manager.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.R;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ClientManager clientManager = ClientManager.getInstance(DashboardActivity.this);
    private List<Client> priority_clients;


    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, DashboardActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ToolbarButtons();
        sectionDropDownMenu();
        villageDropDownMenu();

        this.priority_clients = this.clientManager.getHighPriorityClients();
        populateAllClientsFromList(priority_clients);

        clickClient();
        dashboardSearchBoxes();
    }

    private void sectionDropDownMenu(){
        Spinner spinner = findViewById(R.id.filter_section_dashboard);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void villageDropDownMenu(){
        Spinner spinner = findViewById(R.id.filter_village_dashboard);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dashboard_locations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void dashboardSearchBoxes(){
        Spinner village_spinner = findViewById(R.id.filter_village_dashboard);
        Spinner section_spinner = findViewById(R.id.filter_section_dashboard);
        EditText village_num_text = findViewById(R.id.filter_villageNum_dashboard);
        Button search_button = findViewById(R.id.search_button_dashboard);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String village = village_spinner.getSelectedItem().toString();
                String section = section_spinner.getSelectedItem().toString();
                String village_num = village_num_text.getText().toString().trim();

                priority_clients = clientManager.getDashboardSearchedClients(village, section, village_num);
                populateAllClientsFromList(priority_clients);
            }
        });
    }

    private void populateAllClientsFromList(List<Client> priority_clients) {
        ArrayAdapter<Client> adapter = new DashboardActivity.MyListAdapter(priority_clients);
        ListView list = findViewById(R.id.dashboard_clients);
        list.setAdapter(adapter);
    }

    private void clickClient() {
        ListView list = findViewById(R.id.dashboard_clients);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ClientInfoActivity.makeIntent(DashboardActivity.this, position, priority_clients.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(DashboardActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(DashboardActivity.this);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Client> {
        private List<Client> clients;

        public MyListAdapter(List<Client> clients) {
            super(DashboardActivity.this, R.layout.client_list, clients);
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

            TextView firstName = view.findViewById(R.id.fname_clist);
            TextView lastName = view.findViewById(R.id.lname_clist);
            TextView village = view.findViewById(R.id.Village_clist);

            firstName.setText(currentClient.getFirstName());
            lastName.setText(currentClient.getLastName());
            village.setText(currentClient.getLocation());

            return view;
        }
    }

}