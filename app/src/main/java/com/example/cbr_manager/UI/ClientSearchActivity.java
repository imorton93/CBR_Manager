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

import java.util.List;

public class ClientSearchActivity extends AppCompatActivity {

    private ClientManager clientManager = ClientManager.getInstance(ClientSearchActivity.this);
    private List<Client> searched_clients;
    private int type;
    public static final String R_CLIENT_TYPE_PASSED_IN = "r_client_type_passed_in";

    public static Intent makeIntent(Context context, int type) {
        Intent intent =  new Intent(context, ClientSearchActivity.class);
        intent.putExtra(R_CLIENT_TYPE_PASSED_IN, type);
        return intent;
    }
    private void extractIntent(){
        Intent intent = getIntent();
        this.type = intent.getIntExtra(R_CLIENT_TYPE_PASSED_IN, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_search);
        extractIntent();
        ToolbarButtons();
        villageDropDownMenu();
        sectionDropDownMenu();

        List<Client> clientList = clientManager.getClients();

        populateAllClientsFromList(clientList);
        switch(this.type){
            case 1:
                clickNewVisit();
                break;
            case 2:
                clickNewReferral();
                break;
        }

        searchBoxes();
    }

    private void searchBoxes(){
        AutoCompleteTextView first_name_text = findViewById(R.id.firstName_clientSearch);
        AutoCompleteTextView last_name_text = findViewById(R.id.lastName_clientSearch);
        Spinner village_spinner = findViewById(R.id.filter_village_clientSearch);
        Spinner section_spinner = findViewById(R.id.filter_section_clientSearch);
        EditText village_num_text = findViewById(R.id.filter_villageNum_clientSearch);
        Button search_button = findViewById(R.id.search_button_clientSearch);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = first_name_text.getText().toString().trim();
                String last_name = last_name_text.getText().toString().trim();
                String village = village_spinner.getSelectedItem().toString();
                String section = section_spinner.getSelectedItem().toString();
                String village_num = village_num_text.getText().toString().trim();
                searched_clients =  clientManager.getSearchedClients(first_name,
                        last_name, village, section, village_num);
                populateAllClientsFromList(searched_clients);
            }
        });
    }

    private void villageDropDownMenu(){
        Spinner spinner = findViewById(R.id.filter_village_clientSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dashboard_locations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void sectionDropDownMenu(){
        Spinner spinner = findViewById(R.id.filter_section_clientSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void populateAllClientsFromList(List<Client> clientList) {
        this.searched_clients = clientList;
        ArrayAdapter<Client> adapter = new ClientSearchActivity.MyListAdapter(clientList);
        ListView list = findViewById(R.id.search_client_list);
        list.setAdapter(adapter);
    }

    private void clickNewVisit() {
        ListView list = findViewById(R.id.search_client_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = NewVisitActivity.makeIntent(ClientSearchActivity.this, position, searched_clients.get(position).getId());
                startActivity(intent);
            }
        });
    }

    // TODO make it go to new referral page (need to switch intent and how the back end works)
    private void clickNewReferral() {
        ListView list = findViewById(R.id.search_client_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = NewReferralActivity.makeIntent(ClientSearchActivity.this, position, searched_clients.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ClientSearchActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(ClientSearchActivity.this);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Client> {
        private List<Client> clients;

        public MyListAdapter(List<Client> clients) {
            super(ClientSearchActivity.this, R.layout.client_list, clients);
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