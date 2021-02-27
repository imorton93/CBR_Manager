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

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

public class ClientListActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, ClientListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        ToolbarButtons();
        createDropDownMenu();

        populateListView();
        clickClient();
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

    private void populateListView() {
        DatabaseHelper handler = new DatabaseHelper(this);
        Cursor todoCursor = handler.getAllRows();
        ListView lvItems = findViewById(R.id.clientList);
        TodoCursorAdapter todoAdapter = new TodoCursorAdapter(this, todoCursor);
        lvItems.setAdapter(todoAdapter);
    }

    private void clickClient() {
        ListView list = findViewById(R.id.clientList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = ClientInfoActivity.makeIntent(ClientListActivity.this, id);
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

}