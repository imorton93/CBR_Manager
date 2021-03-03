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
import android.widget.CursorAdapter;
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

//        populateAllClientList();

        populateAllClientsFromList();
        clickClient();

    }

    private void villageDropDownMenu(){
        Spinner spinner = findViewById(R.id.filter_village_clientList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);

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

    private void populateAllClientsFromList() {
        ClientManager clientManager = ClientManager.getInstance(ClientListActivity.this);
        clientManager.updateList();

        ArrayAdapter<Client> adapter = new MyListAdapter(clientManager.getClients());
        ListView list = findViewById(R.id.clientList);
        list.setAdapter(adapter);
    }

    private void clickClient() {
        ListView list = findViewById(R.id.clientList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ClientInfoActivity.makeIntent(ClientListActivity.this, position);
                startActivity(intent);
            }
        });
    }


//    private void populateAllClientList() {
//        DatabaseHelper handler = new DatabaseHelper(this);
//        Cursor todoCursor = handler.getAllRows();
//        ListView lvItems = findViewById(R.id.clientList);
//        TodoCursorAdapter todoAdapter = new TodoCursorAdapter(this, todoCursor);
//        lvItems.setAdapter(todoAdapter);
//    }
//
//    private void clickClient() {
//        ListView list = findViewById(R.id.clientList);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = ClientInfoActivity.makeIntent(ClientListActivity.this, id);
//                startActivity(intent);
//            }
//        });
//    }

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

    public class TodoCursorAdapter extends CursorAdapter {
        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.client_list, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView firstName = view.findViewById(R.id.fname_clist);
            TextView lastName = view.findViewById(R.id.lname_clist);
            TextView village = view.findViewById(R.id.Village_clist);

            String first_name = cursor.getString(cursor.getColumnIndexOrThrow("FIRST_NAME"));
            String last_name = cursor.getString(cursor.getColumnIndexOrThrow("LAST_NAME"));
            String villageString = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION"));

            firstName.setText(first_name);
            lastName.setText(last_name);
            village.setText(villageString);
        }
    }

    private class MyListAdapter extends ArrayAdapter<Client> {
        public MyListAdapter(List<Client> clients) {
            super(ClientListActivity.this, R.layout.client_list, clients);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.client_list, parent, false);
            }

            Client currentClient;
            ClientManager clientManager = ClientManager.getInstance(ClientListActivity.this);
            List<Client> clients = clientManager.getClients();

            currentClient = clients.get(position);

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