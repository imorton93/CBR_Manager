package com.example.cbr_manager.Database;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class AutomaticSyncService extends Service {
    private Handler handler;
    DatabaseHelper mydb;
    private RequestQueue requestQueue;

    private Runnable autoSync = new Runnable() {
        @Override
        public void run() {
            if (connectedToInternet()) {
                getMessagesFromServer();
            }

            handler.postDelayed(autoSync, 300000); //checks every 5 minutes
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
        mydb = new DatabaseHelper(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                            String deleteWorkers = "DELETE FROM ADMIN_MESSAGES";
                            mydb.executeQuery(deleteWorkers);

                            JSONArray serverData = new JSONArray(response);

                            for (int i = 0; i < serverData.length(); i++) {
                                mydb.addMessage(jsonToMessage(serverData.getJSONObject(i)));
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


