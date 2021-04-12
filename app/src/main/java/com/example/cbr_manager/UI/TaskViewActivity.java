package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cbr_manager.Database.AdminMessage;
import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.Database.SyncService;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.BaselineSurvey.HealthSurveyActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.cbr_manager.UI.LoginActivity.username;

public class TaskViewActivity extends AppCompatActivity {
    private SyncService syncService;
    TextView badge;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, TaskViewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        syncService = new SyncService(TaskViewActivity.this);

        badge = findViewById(R.id.cart_badge);
        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);

        clickIcons();
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(TaskViewActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        badgeNotification(adminMessageManager, badge);
        badgeNotification(adminMessageManager, badgeOnToolBar);
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

    private void clickIcons() {
        ImageView newClient = findViewById(R.id.newClient);
        newClient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewClientActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });

        ImageView newVisit = findViewById(R.id.newVisit);
        newVisit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ClientSearchActivity.makeIntent(TaskViewActivity.this, 1);
                startActivity(intent);
            }
        });

        ImageView newReferral = findViewById(R.id.newReferral);
        newReferral.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ClientSearchActivity.makeIntent(TaskViewActivity.this, 2);
                startActivity(intent);
            }
        });

        ImageView sync = findViewById(R.id.sync);
        sync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Steps:
                //- Check if user is connected to the internet
                //  - If not connected, show a message stating so
                //  - If connected, attempt a sync. Show a progress bar(?)

                if (!connectedToInternet()) {
                    Toast.makeText(TaskViewActivity.this, "Please connect to the internet and try again!", Toast.LENGTH_LONG).show();
                } else {
                    syncService.syncClientsTable();
                    syncService.syncVisitTable();
                    syncService.syncReferralTable();
                    syncService.syncSurveyTable();

                    Toast.makeText(TaskViewActivity.this, "Sync Successful!", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageView dashboard = findViewById(R.id.dashboard);
        dashboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });

        ImageView clientList = findViewById(R.id.allClients);
        clientList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ClientListActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });

        ImageView stats = findViewById(R.id.stats);
        stats.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StatsActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });

        ImageView baseline = findViewById(R.id.baselineSurvey);
        baseline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ClientSearchActivity.makeIntent(TaskViewActivity.this, 3);
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

    private void ToolbarButtons(){
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });
    }
}
