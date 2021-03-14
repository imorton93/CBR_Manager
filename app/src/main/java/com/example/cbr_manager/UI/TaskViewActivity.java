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
import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.Visit;
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
    private RequestQueue requestQueue;
    private DatabaseHelper mydb;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, TaskViewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        //Setting up request queue for web service
        requestQueue = Volley.newRequestQueue(TaskViewActivity.this);
         mydb = new DatabaseHelper(TaskViewActivity.this);

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
                String current_username = getIntent().getStringExtra("Worker Username");
                Intent intent = NewClientActivity.makeIntent(TaskViewActivity.this);
                intent.putExtra("Worker Username", current_username);
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
                //Steps:
                //- Check if user is connected to the internet
                //  - If not connected, show a message stating so
                //  - If connected, attempt a sync. Show a progress bar(?)

                if (!connectedToInternet()) {
                    Toast.makeText(TaskViewActivity.this, "Not connected to internet.", Toast.LENGTH_LONG).show();
                } else {
                    syncClientsTable();
                    //syncVisitTable();
                    //syncReferralTable();

                    Toast.makeText(TaskViewActivity.this, "Sync Successful!", Toast.LENGTH_LONG).show();
                }

                ClientManager clientManager = ClientManager.getInstance(TaskViewActivity.this);
                clientManager.clear();
                clientManager.updateList();

                VisitManager visitManager = VisitManager.getInstance(TaskViewActivity.this);
                visitManager.clear();
                visitManager.updateList();
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

    public void syncClientsTable() {
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
                                client.setConsentToInterview(strToBool((String) object.get("CONSENT")));
                                client.setDate((String) object.get("DATE"));
                                client.setFirstName((String) object.get("FIRST_NAME"));
                                client.setLastName((String) object.get("LAST_NAME"));
                                client.setAge(Integer.parseInt((String) object.get("AGE")));
                                client.setGender((String) object.get("GENDER"));
                                client.setLocation((String) object.get("LOCATION"));
                                client.setVillageNumber(Integer.parseInt((String) object.get("VILLAGE_NUMBER")));
                                client.setLatitude(Double.parseDouble((String) object.get("LATITUDE")));
                                client.setLatitude(Double.parseDouble((String) object.get("LONGITUDE")));
                                client.setContactPhoneNumber((String) object.get("CONTACT"));
                                client.setCaregiverPresent(strToBool((String) object.get("CAREGIVER_PRESENCE")));
                                client.setCaregiverPhoneNumber((String) object.get("CAREGIVER_NUMBER"));

                                //setting disabilities
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

                                client.setClient_worker_id(Integer.parseInt((String) object.get("WORKER_ID")));
                                client.setIsSynced(1);

                                mydb.registerClient(client);
                            }
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

    public void syncVisitTable() {
        String query = "SELECT * FROM CLIENT_VISITS WHERE IS_SYNCED = 0;" ; //get only data that is not synced
        Cursor c = mydb.executeQuery(query);
        JSONArray localDataJSON = cur2Json(c);

        String dataToSend =  localDataJSON.toString();

        String URL = "https://mycbr-server.herokuapp.com/visits";

        //Reference: https://www.youtube.com/watch?v=V8MWUYpwoTQ&&ab_channel=MijasSiklodi
        StringRequest requestToServer = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Deleting local data
                            String deleteClients = "DELETE FROM CLIENT_VISITS";
                            mydb.executeQuery(deleteClients);

                            JSONArray serverData = new JSONArray(response);

                            JSONObject object = new JSONObject();
                            Visit visit = new Visit();

                            for (int i = 0; i < serverData.length(); i++) {
                                object = serverData.getJSONObject(i);

                                visit.setVisit_id(Long.parseLong((String) object.get("ID")));
                                visit.setPurposeOfVisit((String) object.get("PURPOSE_OF_VISIT"));

                                List<String> ifCbr = new ArrayList<String>(Arrays.asList(((String) object.get("IF_CBR")).split(", ")));
                                visit.setIfCbr((ArrayList<String>) ifCbr);

                                visit.setDate((String) object.get("VISIT_DATE"));
                                visit.setLocation((String) object.get("LOCATION"));
                                visit.setVillageNumber((Integer) object.get("VILLAGE_NUMBER"));

                                visit.stringToHealthProvided((String) object.get("HEALTH_PROVIDED"));
                                visit.setHealthGoalMet((String) object.get("HEALTH_GOAL_STATUS"));
                                visit.setHealthIfConcluded((String) object.get("HEALTH_OUTCOME"));

                                visit.stringToEduProvided((String) object.get("EDU_PROVIDED"));
                                visit.setEducationGoalMet((String) object.get("EDU_GOAL_STATUS"));
                                visit.setEducationIfConcluded((String) object.get("EDUCATION_OUTCOME"));

                                visit.stringToSocialProvided((String) object.get("SOCIAL_PROVIDED"));
                                visit.setSocialGoalMet((String) object.get("SOCIAL_GOAL_STATUS"));
                                visit.setSocialIfConcluded((String) object.get("SOCIAL_OUTCOME"));

                                visit.setClientID(Long.parseLong((String) object.get("CLIENT_ID")));
                                visit.setIsSynced(1);

                                mydb.addVisit(visit);
                            }
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

    public void syncReferralTable() {
        String query = "SELECT * FROM CLIENT_REFERRALS" ; //get only data that is not synced
        Cursor c = mydb.executeQuery(query);
        JSONArray localDataJSON = cur2Json(c);

        String dataToSend =  localDataJSON.toString();

        String URL = "https://mycbr-server.herokuapp.com/referrals";

        //Reference: https://www.youtube.com/watch?v=V8MWUYpwoTQ&&ab_channel=MijasSiklodi
        StringRequest requestToServer = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Deleting local data
                            String deleteClients = "DELETE FROM CLIENT_REFERRALS";
                            mydb.executeQuery(deleteClients);

                            JSONArray serverData = new JSONArray(response);

                            JSONObject object = new JSONObject();
                            Referral referral = new Referral();

                            for (int i = 0; i < serverData.length(); i++) {
                                object = serverData.getJSONObject(i);

                                referral.setId(Long.parseLong((String) object.get("ID")));
                                referral.setServiceReq((String) object.get("SERVICE_REQUIRED"));
                                referral.setBasicOrInter((String) object.get("BASIC_OR_INTERMEDIATE"));
                                referral.setHipWidth(Integer.parseInt((String) object.get("HIP_WIDTH")));
                                referral.setHasWheelchair(strToBool((String) object.get("HAS_WHEELCHAIR")));
                                referral.setWheelchairReparable(strToBool((String) object.get("WHEELCHAIR_REPAIRABLE")));
                                referral.setBringToCentre(strToBool((String) object.get("BRING_TO_CENTRE")));

                                List<String> conditions = new ArrayList<String>(Arrays.asList(((String) object.get("CONDITIONS")).split(", ")));
                                referral.setCondition(conditions.toString());

                                referral.setInjuryLocation((String) object.get("INJURY_LOCATION_KNEE"));
                                referral.setStatus((String) object.get("REFERRAL_STATUS"));
                                referral.setOutcome((String) object.get("REFERRAL_OUTCOME"));
                                referral.setClientID(Long.parseLong((String) object.get("CLIENT_ID")));
                                referral.setIsSynced(1);

                                mydb.addReferral(referral);
                            }
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

    public Boolean strToBool (String s) {
        if (s.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}