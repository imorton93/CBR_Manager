package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
        ToolbarButtons();
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
                Intent intent = ClientSearchActivity.makeIntent(TaskViewActivity.this, 1);
                startActivity(intent);
            }
        });

        ImageView newReferral = findViewById(R.id.referral);
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
                ClientManager clientManager = ClientManager.getInstance(TaskViewActivity.this);
                clientManager.clear();
                clientManager.updateList();

                VisitManager visitManager = VisitManager.getInstance(TaskViewActivity.this);
                visitManager.clear();
                visitManager.updateList();

                //Steps:
                //- Check if user is connected to the internet
                //  - If not connected, show a message stating so
                //  - If connected, attempt a sync. Show a progress bar(?)

                if (!connectedToInternet()) {
                    Toast.makeText(TaskViewActivity.this, "Not connected to internet.", Toast.LENGTH_LONG).show();
                } else {
                    //Setting up request queue for web service
                    RequestQueue requestQueue = Volley.newRequestQueue(TaskViewActivity.this);

                    //Querying local database for unsynced data to send to the server
                    //TODO: changing the IS_SYNCED column to 1 for rows that are being sent to the server
                    DatabaseHelper mydb = new DatabaseHelper(TaskViewActivity.this);
                    String query = "SELECT * FROM CLIENT_DATA WHERE is_synced = 0;" ; //get only data that is not synced
                    Cursor c = mydb.executeQuery(query);
                    JSONArray localDataJSON = cur2Json(c);

                    String dataToSend =  localDataJSON.toString();

                    String URL = "https://mycbr-server.herokuapp.com/clients";

                    //Reference: https://www.youtube.com/watch?v=V8MWUYpwoTQ&&ab_channel=MijasSiklodi
                    StringRequest requestToServer = new StringRequest(
                            Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //Deleting local data
                                        String deleteClients = "DELETE FROM CLIENT_DATA";
                                        mydb.executeQuery(deleteClients);

                                        JSONArray serverData = new JSONArray(response);

                                        JSONObject object = new JSONObject();
                                        Client client = new Client();

                                        for (int i = 0; i < serverData.length(); i++) {
                                            object = serverData.getJSONObject(i);

                                            client.setId(Long.parseLong((String) object.get("ID")));
                                            client.setConsentToInterview(true);
                                            client.setDate((String) object.get("DATE"));
                                            client.setFirstName((String) object.get("FIRST_NAME"));
                                            client.setLastName((String) object.get("LAST_NAME"));
                                            client.setAge(Integer.parseInt((String) object.get("AGE")));
                                            client.setGender((String) object.get("GENDER"));
                                            client.setLocation((String) object.get("LOCATION"));
                                            client.setVillageNumber(Integer.parseInt((String) object.get("VILLAGE_NUMBER")));
                                            client.setContactPhoneNumber((String) object.get("CONTACT"));
                                            client.setCaregiverPresent(false);
                                            client.setCaregiverPhoneNumber((String) object.get("CAREGIVER_NUMBER"));

                                            //setting disabilities - (doesn't work 100%)
                                            List<String> disabilities = new ArrayList<String>(Arrays.asList(((String) object.get("DISABILITY")).split(", ")));
                                            client.setDisabilities((ArrayList<String>) disabilities);
                                            //--

                                            client.setHealthRate((String) object.get("HEALTH_RATE"));
                                            client.setHealthRequire((String) object.get("HEALTH_REQUIREMENT"));
                                            client.setHealthIndividualGoal((String) object.get("HEALTH_GOAL"));
                                            client.setEducationRate((String) object.get("EDUCATION_RATE"));
                                            client.setEducationRequire((String) object.get("EDUCATION_REQUIRE"));
                                            client.setEducationIndividualGoal((String) object.get("EDUCATION_GOAL"));
                                            client.setSocialStatusRate((String) object.get("SOCIAL_RATE"));
                                            client.setSocialStatusRequire((String) object.get("SOCIAL_REQUIREMENT"));
                                            client.setSocialStatusIndividualGoal((String) object.get("SOCIAL_GOAL"));
                                            client.setIsSynced(1);

                                            mydb.registerClient(client);
                                        }

                                        Toast.makeText(TaskViewActivity.this, "Sync Successful!", Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        Toast.makeText(TaskViewActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse (VolleyError e) {
                                        Toast.makeText(TaskViewActivity.this, "Sync failed.", Toast.LENGTH_LONG).show();
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
            }
        });

        ImageView dashboard = findViewById(R.id.dashboard);
        dashboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(TaskViewActivity.this);
                String current_username = getIntent().getStringExtra("Worker Username");
                intent.putExtra("Worker Username", current_username);
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
                        Toast.makeText(TaskViewActivity.this, "Exception Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;
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