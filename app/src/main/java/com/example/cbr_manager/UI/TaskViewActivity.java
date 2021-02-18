package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cbr_manager.R;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class TaskViewActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, TaskViewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        clickIcons();
    }

    private boolean connectedToInternet () {
        //https://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }
    
    private void clickIcons() {
        ImageView newClient = findViewById(R.id.newclient);
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
                Intent intent = NewVisitActivity.makeIntent(TaskViewActivity.this);
                startActivity(intent);
            }
        });

        ImageView newReferral = findViewById(R.id.referral);
        newReferral.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewReferralActivity.makeIntent(TaskViewActivity.this);
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
                    Toast.makeText(TaskViewActivity.this, "Not connected to internet.", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(TaskViewActivity.this, "Syncing...", Toast.LENGTH_LONG).show();

                    //Setting up request queue for web service
                    RequestQueue requestQueue = Volley.newRequestQueue(TaskViewActivity.this);

                    //Setting up a JsonArrayRequest to get data from the server
                    String dataToSend = "[\n" +
                            "    {\n" +
                            "        \"firstName\": \"Test\",\n" +
                            "        \"lastName\": \"#1\",\n" +
                            "        \"age\": 25,\n" +
                            "        \"villageNo\": 3,\n" +
                            "        \"location\": \"BidiBidi Zone 3\",\n" +
                            "        \"disabilityType\": \"Amputee\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"firstName\": \"Test\",\n" +
                            "        \"lastName\": \"#2\",\n" +
                            "        \"age\": 3,\n" +
                            "        \"villageNo\": 3,\n" +
                            "        \"location\": \"BidiBidi Zone 2\",\n" +
                            "        \"disabilityType\": \"Polio\"\n" +
                            "    }\n" +
                            "]"; //get data from local database, store it in this variable in JSON format


                    //TODO - Replace 'localhost' your WIFI IPv4 address in the URL string, with port 8080
                    String URL = "http://localhost:8080/clients";

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray serverData = new JSONArray(response);
                                        Toast.makeText(TaskViewActivity.this, "Created Array Successfully", Toast.LENGTH_LONG).show();
                                        //do something with the serverData here (adding to the database)

                                    } catch (JSONException e) {
                                        Toast.makeText(TaskViewActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse (VolleyError e) {
                                        Toast.makeText(TaskViewActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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

                    requestQueue.add(stringRequest);
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
    }


}