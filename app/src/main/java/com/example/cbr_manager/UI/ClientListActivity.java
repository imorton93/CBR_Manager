package com.example.cbr_manager.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.R;

import java.util.List;

public class ClientListActivity extends AppCompatActivity {

    private ClientManager clientManager = ClientManager.getInstance();


    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, ClientListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        ListView list = findViewById(R.id.clientList);
        list.setEmptyView(findViewById(android.R.id.empty));
        ToolbarButtons();
        createDropDownMenu();
    }

    private void createDropDownMenu(){
        Spinner spinner = findViewById(R.id.filterDropdownButton);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    // TODO
    private void populateListView() {
        ArrayAdapter<Client> adapter = new MyListAdapter(clientManager.getClientsAsLists());
        ListView list = findViewById(R.id.clientList);
        list.setAdapter(adapter);
    }

    // TODO
    private class MyListAdapter extends ArrayAdapter<Client> {
        public MyListAdapter(List<Client> clientList) {
            super(ClientListActivity.this, R.layout.client_list, clientList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.client_list, parent, false);
            }

            Client currentClient;
            currentClient = clientManager.getClientAtIndex(position);

            return itemView;
        }
    }
}