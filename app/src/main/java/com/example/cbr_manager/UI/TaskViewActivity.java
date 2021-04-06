package com.example.cbr_manager.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.Database.Visit;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.cbr_manager.UI.LoginActivity.username;

public class TaskViewActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private DatabaseHelper mydb;

    TextView badge;

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
                    syncClientsTable();
                    syncVisitTable();
                    syncReferralTable();

                    Toast.makeText(TaskViewActivity.this, "Sync Successful!", Toast.LENGTH_LONG).show();
                }

                ClientManager clientManager = ClientManager.getInstance(TaskViewActivity.this);
                clientManager.clear();
                clientManager.updateList();

                VisitManager visitManager = VisitManager.getInstance(TaskViewActivity.this);
                visitManager.clear();
                visitManager.updateList();

                ReferralManager referralManager = ReferralManager.getInstance(TaskViewActivity.this);
                referralManager.clear();
                referralManager.updateList();

                AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(TaskViewActivity.this);
                adminMessageManager.clear();
                adminMessageManager.updateList();

                badgeNotification(adminMessageManager, badge);
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
            }
        });
    }

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.size();

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

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.registerClient(jsonToClient(serverData.getJSONObject(i)));
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

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.addVisit(jsonToVisit(serverData.getJSONObject(i)));
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
        String query = "SELECT * FROM CLIENT_REFERRALS WHERE IS_SYNCED = 0;" ; //get only data that is not synced
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

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.addReferral(jsonToReferral(serverData.getJSONObject(i)));
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

    public JSONArray cur2Json(Cursor cursor) {
        byte[] photoArr;
        String base64Photo;
        String data;

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    if ((cursor.getColumnName(i).equals("PHOTO")) ||
                            (cursor.getColumnName(i).equals("REFERRAL_PHOTO"))) {
                        photoArr = cursor.getBlob(i);

                        if (photoArr != null) {
                            base64Photo = Base64.encodeToString(photoArr, Base64.DEFAULT);
                            data = base64Photo;
                        } else {
                            data = "";
                        }
                    } else {
                        data = cursor.getString(i);
                    }

                    try {
                        rowObject.put(cursor.getColumnName(i), data);
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

    public Boolean strToBool (String s) {
        if (s.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] strToByteArr (String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

    Client jsonToClient (JSONObject object) throws JSONException {
        Client client = new Client();

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
        client.setLongitude(Double.parseDouble((String) object.get("LONGITUDE")));
        client.setContactPhoneNumber((String) object.get("CONTACT"));
        client.setCaregiverPresent(strToBool((String) object.get("CAREGIVER_PRESENCE")));
        client.setCaregiverPhoneNumber((String) object.get("CAREGIVER_NUMBER"));

        if (!object.isNull("PHOTO")) {
            client.setPhoto(strToByteArr((String) object.get("PHOTO")));
        }

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

        return client;
    }

    Visit jsonToVisit (JSONObject object) throws JSONException {
        Visit visit = new Visit();

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

        return visit;
    }

    Referral jsonToReferral (JSONObject object) throws JSONException {
        Referral referral = new Referral();

        referral.setId(Long.parseLong((String) object.get("ID")));
        referral.setServiceReq((String) object.get("SERVICE_REQUIRED"));

        if (!object.isNull("PHOTO")) {
            referral.setReferralPhoto(strToByteArr((String) object.get("REFERRAL_PHOTO")));
        }

        if (referral.getServiceReq().equals("Physiotherapy")) {
            List<String> conditions = new ArrayList<String>(Arrays.asList(((String) object.get("CONDITIONS")).split(", ")));
            referral.setCondition(conditions.toString());
        }

        else if (referral.getServiceReq().equals("Prosthetic")) {
            referral.setInjuryLocation(((String) object.get("INJURY_LOCATION_KNEE")));
        }

        else if (referral.getServiceReq().equals("Orthotic")) {
            referral.setInjuryLocation(((String) object.get("INJURY_LOCATION_ELBOW")));
        }

        else if (referral.getServiceReq().equals("Wheelchair")) {
            referral.setBasicOrInter((String) object.get("BASIC_OR_INTERMEDIATE"));
            referral.setHipWidth(Integer.parseInt((String) object.get("HIP_WIDTH")));
        }

        else {
            referral.setOtherExplanation(referral.getServiceReq());
        }

        referral.setHasWheelchair(strToBool((String) object.get("HAS_WHEELCHAIR")));
        referral.setWheelchairReparable(strToBool((String) object.get("WHEELCHAIR_REPAIRABLE")));
        referral.setBringToCentre(strToBool((String) object.get("BRING_TO_CENTRE")));
        referral.setClientID(Long.parseLong((String) object.get("CLIENT_ID")));
        referral.setIsSynced(1);

        /*referral.setStatus((String) object.get("REFERRAL_STATUS"));
        referral.setOutcome((String) object.get("REFERRAL_OUTCOME"));*/

        return referral;
    }
}