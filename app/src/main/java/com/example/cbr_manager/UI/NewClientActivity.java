package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

import static java.lang.Integer.parseInt;

public class NewClientActivity extends AppCompatActivity {

    private EditText firstNameTextBox, lastNameTextBox, locationTextBox, disabilityTextBox, ageTextBox, villageNoTextBox;
    private Button submitButton;
    private DatabaseHelper mydb;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, NewClientActivity.class);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        firstNameTextBox = (EditText) findViewById(R.id.firstnameTextBox);
        lastNameTextBox =(EditText)findViewById(R.id.lastnameTextBox);
        locationTextBox = (EditText)findViewById(R.id.locationTextBox);
        disabilityTextBox = (EditText)findViewById(R.id.disabilityTextBox);
        ageTextBox = (EditText)findViewById(R.id.ageTextBox);
        villageNoTextBox = (EditText)findViewById(R.id.villageNoTextBox);

        submitButton = findViewById(R.id.submitButtonClient);

        mydb = new DatabaseHelper(NewClientActivity.this);

        insertClient();
    }

    private void insertClient() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Client client;
                        client = new Client(firstNameTextBox.getText().toString(), lastNameTextBox.getText().toString(),
                                parseInt(ageTextBox.getText().toString()), parseInt(villageNoTextBox.getText().toString()), locationTextBox.getText().toString(),disabilityTextBox.getText().toString());
                        boolean success = mydb.registerClient(client);
                        if(success)
                            Toast.makeText(NewClientActivity.this, "Entry Successful!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(NewClientActivity.this, "Error Occured.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean validateEntries(){
        boolean bool = true;
        if(firstNameTextBox.length() == 0|| lastNameTextBox.length()==0
                ||locationTextBox.length()==0||disabilityTextBox.length()==0 | ageTextBox.length()==0 | villageNoTextBox.length()==0) {
            bool = false;
        }
        return bool;
    }
}