package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cbr_manager.R;

public class DashboardActivity extends AppCompatActivity {

    private Spinner spinner;
    private static final String[] dropdownMenuOptions = {"General", "Client Health", "Client Education", "Client Social Status"};

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, DashboardActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        createDropDownMenu();

        // TODO: populate listView
    }

    private void createDropDownMenu(){
        spinner = (Spinner)findViewById(R.id.dashboard_filter);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

//    @Override
//    private void onItemSelected(AdapterView<?> parent, View v, int position, long id){
//
//    }
}