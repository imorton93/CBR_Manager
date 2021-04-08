package com.example.cbr_manager.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.BaselineSurvey.HealthSurveyActivity;

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

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(ClientSearchActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

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
            case 3:
                clickNewBaseline();
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
        ImageButton search_button = findViewById(R.id.search_button_clientSearch);
        search_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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

    private void clickNewBaseline() {
        ListView list = findViewById(R.id.search_client_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = HealthSurveyActivity.makeIntent(ClientSearchActivity.this, position, searched_clients.get(position).getId());
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

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(ClientSearchActivity.this);
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

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.numUnread();

        if (badge != null) {
            if (size == 0) {
                if (badge.getVisibility() != View.GONE) {
                    badge.setVisibility(View.GONE);
                }
            } else {
                badge.setText(String.valueOf(Math.min(size, 99)));
                if (badge.getVisibility() != View.VISIBLE) {
                    badge.setVisibility(View.VISIBLE);
                }
            }
        }
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