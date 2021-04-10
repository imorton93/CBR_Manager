package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.Database.SyncService;
import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.CBRWorkerManager;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static String username, password;
    public static long id;
    private EditText usernameTextBox, passwordTextBox;
    private Button login_btn;
    private DatabaseHelper mydb;

    private CBRWorkerManager cbrWorkerManager;

    public static CBRWorker currentCBRWorker;

    private SyncService syncService;


    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, LoginActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        syncService = new SyncService(LoginActivity.this);
        mydb = new DatabaseHelper(LoginActivity.this);

        cbrWorkerManager = CBRWorkerManager.getInstance(LoginActivity.this);
        cbrWorkerManager.clear();
        cbrWorkerManager.updateList();

        ClientManager clientManager = ClientManager.getInstance(LoginActivity.this);
        clientManager.clear();
        clientManager.updateList();

        VisitManager visitManager = VisitManager.getInstance(LoginActivity.this);
        visitManager.clear();
        visitManager.updateList();

        ReferralManager referralManager = ReferralManager.getInstance(LoginActivity.this);
        referralManager.clear();
        referralManager.updateList();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(LoginActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        SurveyManager surveyManager = SurveyManager.getInstance(LoginActivity.this);
        surveyManager.clear();
        surveyManager.updateList();

        if (connectedToInternet()) {
            syncService.syncWorkerTable();
        }

        //Starting automatic sync service
        Intent syncServiceIntent = new Intent(LoginActivity.this, SyncService.class);
        syncServiceIntent.setAction("com.example.cbr_manager.Database.SyncService");
        startService(syncServiceIntent);

        buttonsClicked();
    }

    private void buttonsClicked(){
        usernameTextBox = findViewById(R.id.usernameTextBox);
        passwordTextBox = findViewById(R.id.passwordTextBox);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);

        login_btn = findViewById(R.id.loginButton);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTextBox.getText().toString();
                password = passwordTextBox.getText().toString();
                // Do something with login button
                if (mydb.checkUser(username, password)) {
                    currentCBRWorker = cbrWorkerManager.getCBRByUsernameAndPassword(username);
                    Intent intent = TaskViewActivity.makeIntent(LoginActivity.this);

                    sharedPref.edit().putString("username", username).apply();

                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong credentials.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Button signup_btn = (Button) findViewById(R.id.signupButton);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do something with signup button
                Intent intent = SignUpActivity.makeIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    // TODO: implement
    private void forgotButton(){
        Button forgotBtn = findViewById(R.id.forgotButton);
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private boolean connectedToInternet () {
        ConnectivityManager connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();

        if ((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting())) {
            return true;
        } else {
            return false;
        }
    }

}