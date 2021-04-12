package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.SyncService;
import com.example.cbr_manager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameTextBox, lastNameTextBox, emailTextBox, zoneTextBox, password1TextBox, password2TextBox;
    private Button submitButton;
    private DatabaseHelper mydb;
    private CBRWorker cbrWorker;

    private SyncService syncService;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, SignUpActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        syncService = new SyncService(SignUpActivity.this);

        firstNameTextBox = findViewById(R.id.firstTextBox);
        lastNameTextBox = findViewById(R.id.lastTextBox);
        emailTextBox = findViewById(R.id.userTextBox);

        zoneTextBox = findViewById(R.id.zoneTextBox);
        password1TextBox = findViewById(R.id.password1TextBox);
        password2TextBox = findViewById(R.id.messageTextBox);

        submitButton = findViewById(R.id.submitButton);

        mydb = new DatabaseHelper(SignUpActivity.this);

        backButton();
        insertWorker();
    }

    private void insertWorker() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectedToInternet()) {
                    Toast.makeText(SignUpActivity.this, "Please connect to the internet and try again!", Toast.LENGTH_LONG).show();
                } else {
                    if (validateEntries()) {
                        if (validatePasswords()) {
                            cbrWorker = new CBRWorker(firstNameTextBox.getText().toString(), lastNameTextBox.getText().toString(),
                                    emailTextBox.getText().toString(), zoneTextBox.getText().toString(), BCrypt.withDefaults().hashToString(12, password1TextBox.getText().toString().toCharArray()));
                            cbrWorker.setWorkerId(mydb.numberOfWorkers() + 1);
                            boolean success = mydb.registerWorker(cbrWorker);
                            if(success) {
                                cbrWorker.setWorkerId((mydb.getWorkerId(cbrWorker.getUsername())));
                                syncService.syncLoginData(cbrWorker);

                                Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                                startActivity(intent);
                            } else
                                Toast.makeText(SignUpActivity.this, "Error Occurred." + success, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateEntries(){
        boolean bool = true;
        if(firstNameTextBox.length() == 0|| lastNameTextBox.length()==0
                ||password2TextBox.length()==0||emailTextBox.length()==0) {
            bool = false;
        }
        return bool;
    }


    private boolean validatePasswords(){
        return password2TextBox.getText().toString().equals(password1TextBox.getText().toString());
    }

    private void backButton(){
        Button backbtn = (Button) findViewById(R.id.backButtonSignup);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                startActivity(intent);
            }
        });
    }

    private boolean connectedToInternet () {
        //Reference: https://developer.android.com/training/monitoring-device-state/connectivity-status-type
        ConnectivityManager connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();
        return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }

}
