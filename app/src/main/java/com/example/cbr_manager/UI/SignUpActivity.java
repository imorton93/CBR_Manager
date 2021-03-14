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
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameTextBox, lastNameTextBox, emailTextBox, password1TextBox, password2TextBox;
    private Button submitButton;
    private DatabaseHelper mydb;
    private CBRWorker cbrWorker;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, SignUpActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameTextBox = findViewById(R.id.firstnameTextBox);
        lastNameTextBox = findViewById(R.id.lastnameTextBox);
        emailTextBox = findViewById(R.id.emailTextBox);
        password1TextBox = findViewById(R.id.password1TextBox);
        password2TextBox = findViewById(R.id.password2TextBox);

        submitButton = findViewById(R.id.submitButton);

        mydb = new DatabaseHelper(SignUpActivity.this);

        backButton();
        insertWorker();
    }

    private void insertWorker() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEntries() && connectedToInternet()) {
                    if (validatePasswords()) {
                        cbrWorker = new CBRWorker(firstNameTextBox.getText().toString(), lastNameTextBox.getText().toString(),
                                emailTextBox.getText().toString(), BCrypt.withDefaults().hashToString(12, password1TextBox.getText().toString().toCharArray()));
                        boolean success = mydb.registerWorker(cbrWorker);
                        if(success) {
                            cbrWorker.setWorkerId((mydb.getWorkerId(cbrWorker.getUsername())));
                            syncLoginData();
                            Intent intent = LoginActivity.makeIntent(SignUpActivity.this);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(SignUpActivity.this, "Error Occured."+ success, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
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

        if ((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting())) {
            return true;
        } else {
            return false;
        }
    }

    private void syncLoginData() {
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);

        String query = "SELECT * FROM WORKER_DATA WHERE ID = " + cbrWorker.getId();
        Cursor c = mydb.executeQuery(query);
        JSONArray localDataJSON = cur2Json(c);

        String dataToSend = localDataJSON.toString();

        String URL = "https://mycbr-server.herokuapp.com/workers";

        //Reference: https://www.youtube.com/watch?v=V8MWUYpwoTQ&&ab_channel=MijasSiklodi
        StringRequest requestToServer = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Deleting local data
                            String deleteWorkers = "DELETE FROM WORKER_DATA";
                            mydb.executeQuery(deleteWorkers);

                            JSONArray serverData = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            CBRWorker worker = new CBRWorker();

                            for (int i = 0; i < serverData.length(); i++) {
                                object = serverData.getJSONObject(i);

                                worker.setFirstName((String) object.get("FIRST_NAME"));
                                worker.setLastName((String) object.get("LAST_NAME"));
                                worker.setUsername((String) object.get("USERNAME"));
                                worker.setPassword((String) object.get("PASSWORD"));
                                worker.setWorkerId(Integer.parseInt((String) object.get("ID")));

                                mydb.registerWorker(worker);
                            }

                            Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(SignUpActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                if (e.networkResponse.statusCode == 409) { //409 CONFLICT: email already exists on server
                    Toast.makeText(SignUpActivity.this, "Email is already taken. Try again.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign up failed.", Toast.LENGTH_LONG).show();
                }
            }
        })
        {
            @Override
            public String getBodyContentType() { return "application/json; charset=utf-8"; }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return dataToSend == null ? null : dataToSend.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        };

        requestQueue.add(requestToServer);
    }

    public JSONArray cur2Json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Exception Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;
    }
}
