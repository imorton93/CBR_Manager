package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

import java.util.ArrayList;
import java.util.List;

public class ClientListActivity extends AppCompatActivity {

    ClientManager clientManager = ClientManager.getInstance(ClientListActivity.this);
    List<Client> searched_clients;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ClientListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        ToolbarButtons();
        sectionDropDownMenu();
        villageDropDownMenu();

        List<Client> clientList = clientManager.getClients();

        populateAllClientsFromList(clientList);
        clickClient();
        searchBoxes();
    }

    private void searchBoxes(){
        AutoCompleteTextView first_name_text = findViewById(R.id.firstName_clientList);
        AutoCompleteTextView last_name_text = findViewById(R.id.lastName_clientList);
        Spinner village_spinner = findViewById(R.id.filter_village_clientList);
        Spinner section_spinner = findViewById(R.id.filter_section_clientList);
        EditText village_num_text = findViewById(R.id.filter_villageNum_clientList);
        Button search_button = findViewById(R.id.search_button_clientList);
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
        Spinner spinner = findViewById(R.id.filter_village_clientList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dashboard_locations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void sectionDropDownMenu(){
        Spinner spinner = findViewById(R.id.filter_section_clientList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void populateAllClientsFromList(List<Client> clientList) {
        this.searched_clients = clientList;
        ArrayAdapter<Client> adapter = new MyListAdapter(clientList);
        ListView list = findViewById(R.id.clientList);
        list.setAdapter(adapter);
    }

    private void clickClient() {
        ListView list = findViewById(R.id.clientList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Client client: searched_clients){

                    System.out.println("First name in click client: " + client.getFirstName());

                }
                Intent intent = ClientInfoActivity.makeIntent(ClientListActivity.this, position, searched_clients.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ClientListActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(ClientListActivity.this);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Client> {
        private List<Client> clients;

        public MyListAdapter(List<Client> clients) {
            super(ClientListActivity.this, R.layout.client_list, clients);
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