package com.example.cbr_manager.Database;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cbr_manager.UI.BaselineSurvey.EmpowermentandShelterSurveyActivity;
import com.example.cbr_manager.UI.SignUpActivity;
import com.example.cbr_manager.UI.TaskViewActivity;
import com.example.cbr_manager.UI.dashboardFragment.NewMsgActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SyncService extends Service {
    private Handler handler;
    private DatabaseHelper mydb;
    private RequestQueue requestQueue;
    private Context context;

    private CBRWorkerManager cbrWorkerManager;
    private ClientManager clientManager;
    private VisitManager visitManager;
    private ReferralManager referralManager;
    private AdminMessageManager adminMessageManager;
    private SurveyManager surveyManager;

    public SyncService() {
    }

    public SyncService(Context context) {
        mydb = new DatabaseHelper(context);
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;

        cbrWorkerManager = CBRWorkerManager.getInstance(context);
        clientManager = ClientManager.getInstance(context);
        visitManager = VisitManager.getInstance(context);
        referralManager = ReferralManager.getInstance(context);
        adminMessageManager = AdminMessageManager.getInstance(context);
        surveyManager = SurveyManager.getInstance(context);
    }

    private Runnable autoSync = new Runnable() {
        @Override
        public void run() {
            if (connectedToInternet()) {
                Log.d(TAG,"SYNCED ADMIN MESSAGES");
                getMessagesFromServer();
            }

            handler.postDelayed(autoSync, 300000); //checks every 5 minutes
        }
    };

    @Override
    public void onCreate() { //Only called once when app is started, using startService()
        super.onCreate();

        Log.d(TAG,"SERVICE STARTED IN BACKGROUND");

        handler = new Handler();
        mydb = new DatabaseHelper(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        adminMessageManager = AdminMessageManager.getInstance(context);

        handler.post(autoSync);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getMessagesFromServer() {
        String URL = "https://mycbr-server.herokuapp.com/get-admin-messages";

        StringRequest requestToServer = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray serverData = new JSONArray(response);
                            Long msgID;

                            for (int i = 0; i < serverData.length(); i++) {
                                msgID = Long.parseLong((String) serverData.getJSONObject(i).get("ID"));
                                if (!mydb.msgAlreadyExists(msgID)) {
                                    mydb.addMessage(jsonToMessage(serverData.getJSONObject(i)));
                                }

                                adminMessageManager.clear();
                                adminMessageManager.updateList();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                e.printStackTrace();
            }
        })
        {
            @Override
            public String getBodyContentType() { return "application/json; charset=utf-8"; }
        };

        requestQueue.add(requestToServer);
    }

    public void sendMsgToServer (AdminMessage adminMessage) {
        String query = "SELECT * FROM ADMIN_MESSAGES WHERE ID = " + adminMessage.getId();
        Cursor c = mydb.executeQuery(query);
        JSONArray localDataJSON = cur2Json(c);

        String dataToSend = localDataJSON.toString();

        String URL = "https://mycbr-server.herokuapp.com/admin-messages";

        //Reference: https://www.youtube.com/watch?v=V8MWUYpwoTQ&&ab_channel=MijasSiklodi
        StringRequest requestToServer = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //assume server isn't sending anything back for now
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {

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

    public void syncLoginData(CBRWorker cbrWorker) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.registerWorker(jsonToWorker(serverData.getJSONObject(i)));
                            }

                            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_LONG).show();
                            cbrWorkerManager.clear();
                            cbrWorkerManager.updateList();
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                if (e.networkResponse.statusCode == 409) { //409 CONFLICT: email already exists on server
                    Toast.makeText(context, "Email is already taken. Try again.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Sign up failed.", Toast.LENGTH_LONG).show();
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

    public void syncWorkerTable() {
        String URL = "https://mycbr-server.herokuapp.com/get-workers";

        StringRequest requestToServer = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String deleteWorkers = "DELETE FROM WORKER_DATA";
                            mydb.executeQuery(deleteWorkers);

                            JSONArray serverData = new JSONArray(response);

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.registerWorker(jsonToWorker(serverData.getJSONObject(i)));
                            }

                            cbrWorkerManager.clear();
                            cbrWorkerManager.updateList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                e.printStackTrace();
            }
        })
        {
            @Override
            public String getBodyContentType() { return "application/json; charset=utf-8"; }
        };

        requestQueue.add(requestToServer);
    }

    public void sendWorkerToServer (CBRWorker cbrWorker) {
        String query = "SELECT * FROM WORKER_DATA WHERE ID = " + cbrWorker.getId();
        Cursor c = mydb.executeQuery(query);
        JSONArray localDataJSON = cur2Json(c);

        String workerArr = localDataJSON.toString();
        String dataToSend = workerArr.substring(1, workerArr.length() - 1);

        String URL = "https://mycbr-server.herokuapp.com/update-worker/" + cbrWorker.getId();

        StringRequest requestToServer = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray serverData = new JSONArray(response);

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.registerWorker(jsonToWorker(serverData.getJSONObject(i)));
                            }

                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                if (e.networkResponse.statusCode == 409) { //409 CONFLICT: email already exists on server
                    Toast.makeText(context, "Username is already taken. Try again.", Toast.LENGTH_LONG).show();
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(cbrWorker.getId()));

                return params;
            }

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

                            clientManager.clear();
                            clientManager.updateList();
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                Toast.makeText(context, "Sync failed.", Toast.LENGTH_LONG).show();
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

                            visitManager.clear();
                            visitManager.updateList();
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                Toast.makeText(context, "Sync failed.", Toast.LENGTH_LONG).show();
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

                            referralManager.clear();
                            referralManager.updateList();
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                Toast.makeText(context, "Sync failed.", Toast.LENGTH_LONG).show();
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

    public void syncSurveyTable() {
        String query = "SELECT * FROM CLIENT_SURVEYS WHERE IS_SYNCED = 0;" ; //get only data that is not synced
        Cursor c = mydb.executeQuery(query);
        JSONArray localDataJSON = cur2Json(c);

        String dataToSend =  localDataJSON.toString();

        String URL = "https://mycbr-server.herokuapp.com/surveys";

        //Reference: https://www.youtube.com/watch?v=V8MWUYpwoTQ&&ab_channel=MijasSiklodi
        StringRequest requestToServer = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Deleting local data
                            String deleteClients = "DELETE FROM CLIENT_SURVEYS";
                            mydb.executeQuery(deleteClients);

                            JSONArray serverData = new JSONArray(response);

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.addSurvey(jsonToSurvey(serverData.getJSONObject(i)));
                            }

                            surveyManager.clear();
                            surveyManager.updateList();
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError e) {
                Toast.makeText(context, "Sync failed.", Toast.LENGTH_LONG).show();
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

    CBRWorker jsonToWorker (JSONObject object) throws JSONException {
        CBRWorker worker = new CBRWorker();

        worker.setFirstName((String) object.get("FIRST_NAME"));
        worker.setLastName((String) object.get("LAST_NAME"));
        worker.setUsername((String) object.get("USERNAME"));

        if (!object.isNull("ZONE"))  {
            worker.setZone((String) object.get("ZONE"));
        }

        if (!object.isNull("PHOTO"))  {
            worker.setPhoto(strToByteArr((String) object.get("PHOTO")));
        }

        worker.setPassword((String) object.get("PASSWORD"));
        worker.setWorkerId(Integer.parseInt((String) object.get("ID")));
        worker.setIs_admin("1".equals((String) object.get("IS_ADMIN")));

        return worker;
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

        if (!object.isNull("REFERRAL_PHOTO")) {
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
            referral.setServiceReq("Other");
        }

        referral.setHasWheelchair(strToBool((String) object.get("HAS_WHEELCHAIR")));
        referral.setWheelchairReparable(strToBool((String) object.get("WHEELCHAIR_REPAIRABLE")));
        referral.setBringToCentre(strToBool((String) object.get("BRING_TO_CENTRE")));
        referral.setClientID(Long.parseLong((String) object.get("CLIENT_ID")));
        referral.setIsSynced(1);

        if (!object.isNull("REFERRAL_STATUS")) {
            referral.setStatus((String) object.get("REFERRAL_STATUS"));
        }

        if (!object.isNull("REFERRAL_OUTCOME")) {
            referral.setOutcome((String) object.get("REFERRAL_OUTCOME"));
        }

        return referral;
    }

    public AdminMessage jsonToMessage (JSONObject object) throws JSONException {
        AdminMessage message = new AdminMessage();

        message.setId(Long.parseLong((String) object.get("ID")));
        message.setTitle((String) object.get("TITLE"));
        message.setDate((String) object.get("DATE"));
        message.setLocation((String) object.get("LOCATION"));
        message.setMessage((String) object.get("MESSAGE"));
        message.setAdminID(Integer.parseInt((String) object.get("ADMIN_ID")));
        message.setViewedStatus(Integer.parseInt((String) object.get("IS_VIEWED")));
        message.setIsSynced(Integer.parseInt((String) object.get("IS_SYNCED")));

        return message;
    }

    public Survey jsonToSurvey (JSONObject object) throws JSONException {
        Survey survey = new Survey();
        Integer value;

        //health
        survey.setId(Long.parseLong((String) object.get("ID")));

        value = Integer.parseInt((String) object.get("HEALTH_CONDITION"));
        survey.setHealth_condition(value.byteValue());

        survey.setHave_rehab_access(strToBool((String) object.get("HAVE_REHAB_ACCESS")));
        survey.setNeed_rehab_access(strToBool((String) object.get("NEED_REHAB_ACCESS")));
        survey.setHave_device(strToBool((String) object.get("HAVE_DEVICE")));
        survey.setDevice_condition(strToBool((String) object.get("DEVICE_CONDITION")));
        survey.setNeed_device(strToBool((String) object.get("NEED_DEVICE")));
        survey.setDevice_type((String) object.get("DEVICE_TYPE"));

        value = Integer.parseInt((String) object.get("IS_SATISFIED"));
        survey.setIs_satisfied(value.byteValue());

        //education
        survey.setIs_student(strToBool((String) object.get("IS_STUDENT")));

        value = Integer.parseInt((String) object.get("GRADE_NO"));
        survey.setGrade_no(value.byteValue());

        if (!object.isNull("REASON")) {
            survey.setReason_no_school((String) object.get("REASON"));
        }

        survey.setWas_student(strToBool((String) object.get("WAS_STUDENT")));
        survey.setWant_school(strToBool((String) object.get("WANT_SCHOOL")));

        //social
        survey.setIs_valued(strToBool((String) object.get("IS_VALUED")));
        survey.setIs_independent(strToBool((String) object.get("IS_INDEPENDENT")));
        survey.setIs_social(strToBool((String) object.get("IS_SOCIAL")));
        survey.setIs_socially_affected(strToBool((String) object.get("IS_SOCIALLY_AFFECTED")));
        survey.setWas_discriminated(strToBool((String) object.get("WAS_DISCRIMINATED")));

        //livelihood
        survey.setIs_working(strToBool((String) object.get("IS_WORKING")));

        if (!object.isNull("WORK_TYPE")) {
            survey.setWork_type((String) object.get("IS_WORKING"));
        }

        survey.setIs_self_employed((String) object.get("IS_SELF_EMPLOYED"));
        survey.setNeeds_met(strToBool((String) object.get("NEEDS_MET")));
        survey.setIs_work_affected(strToBool((String) object.get("IS_WORK_AFFECTED")));
        survey.setWant_work(strToBool((String) object.get("WANT_WORK")));

        //food and nutrition
        survey.setFood_security((String) object.get("FOOD_SECURITY"));
        survey.setIs_diet_enough(strToBool((String) object.get("IS_DIET_ENOUGH")));

        if (!object.isNull("CHILD_CONDITION")) {
            survey.setChild_condition((String) object.get("CHILD_CONDITION"));
        }

        survey.setReferral_required(strToBool((String) object.get("REFERRAL_REQUIRED")));

        //empowerment
        survey.setIs_member(strToBool((String) object.get("IS_MEMBER")));

        if (!object.isNull("ORGANISATION")) {
            survey.setOrganisation((String) object.get("ORGANISATION"));
        }

        survey.setIs_aware(strToBool((String) object.get("IS_AWARE")));
        survey.setIs_influence(strToBool((String) object.get("IS_INFLUENCE")));

        //shelter and care
        survey.setIs_shelter_adequate(strToBool((String) object.get("IS_SHELTER_ADEQUATE")));
        survey.setItems_access(strToBool((String) object.get("ITEMS_ACCESS")));

        //sync
        survey.setClient_id(Long.parseLong((String) object.get("CLIENT_ID")));
        survey.setIs_synced(true);

        return survey;
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
                        Toast.makeText(getApplicationContext(), "Exception Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;
    }

    public boolean connectedToInternet () {
        ConnectivityManager connectManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();

        if ((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting())) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] strToByteArr (String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

    public Boolean strToBool (String s) {
        if (s.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
