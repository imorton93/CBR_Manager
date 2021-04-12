package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.CBRWorkerManager;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.SyncService;
import com.example.cbr_manager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import at.favre.lib.crypto.bcrypt.BCrypt;

import static com.example.cbr_manager.UI.LoginActivity.currentCBRWorker;

public class EditCBRActivity extends AppCompatActivity {

    EditText firstName, lastName, username, zone;
    private DatabaseHelper mydb;
    private CBRWorker cbrWorker;

    private SyncService syncService;

    public static Intent makeIntent(Context context) {
        return new Intent(context, EditCBRActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_c_b_r);

        syncService = new SyncService(EditCBRActivity.this);
        mydb = new DatabaseHelper(EditCBRActivity.this);

        CBRWorkerManager manager = CBRWorkerManager.getInstance(this);
        cbrWorker = manager.getCBRByUsernameAndPassword(currentCBRWorker.getUsername());
        syncService.syncWorkerTable();

        setText();
        buttons();
    }

    private void setText() {
        firstName = findViewById(R.id.first);
        lastName = findViewById(R.id.last);
        username = findViewById(R.id.user);
        zone = findViewById(R.id.zone);

        firstName.setText(currentCBRWorker.getFirstName());
        lastName.setText(currentCBRWorker.getLastName());
        username.setText(currentCBRWorker.getUsername());
        zone.setText(currentCBRWorker.getZone());
    }

    private void buttons() {
        Button backBtn = findViewById(R.id.backButtonSignup);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button submitBtn = findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectedToInternet()) {
                    Toast.makeText(EditCBRActivity.this, "Please connect to the internet and try again!", Toast.LENGTH_LONG).show();
                } else {
                    if (validateEntries()) {
                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
                        String curr_username = sharedPref.getString("username", null);

                        cbrWorker.setWorkerId(mydb.getWorkerId(curr_username));
                        cbrWorker.setFirstName(firstName.getText().toString());
                        cbrWorker.setLastName(lastName.getText().toString());
                        cbrWorker.setUsername(username.getText().toString());
                        cbrWorker.setZone(zone.getText().toString());
                        cbrWorker.setPassword(BCrypt.withDefaults().hashToString(12, currentCBRWorker.getPassword().toCharArray()));

                        if ((mydb.getWorkerId(cbrWorker.getUsername()) == cbrWorker.getId())
                                || (mydb.getWorkerId(cbrWorker.getUsername()) == -1)) {
                            boolean success = mydb.updateWorker(cbrWorker);
                            if (success) {
                                cbrWorker.setWorkerId((mydb.getWorkerId(cbrWorker.getUsername())));
                                sharedPref.edit().putString("username", cbrWorker.getUsername()).apply();

                                currentCBRWorker = cbrWorker;

                                syncService.sendWorkerToServer(cbrWorker);

                                Intent intent = ProfileActivity.makeIntent(EditCBRActivity.this);
                                startActivity(intent);
                            } else
                                Toast.makeText(EditCBRActivity.this, "Error Occurred." + success, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditCBRActivity.this, "Username is already registered in the database.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(EditCBRActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateEntries(){
        boolean bool = true;
        if(firstName.length() == 0|| lastName.length() == 0 || username.length() == 0) {
            bool = false;
        }
        return bool;
    }

    private boolean connectedToInternet () {
        //Reference: https://developer.android.com/training/monitoring-device-state/connectivity-status-type
        ConnectivityManager connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();
        return (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }
}
