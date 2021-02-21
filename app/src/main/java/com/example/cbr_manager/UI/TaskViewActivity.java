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
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        ToolbarButtons();
    }

    private boolean connectedToInternet () {
        //https://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
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

        // TODO DELETE THIS BUTTON MAKE IT BASELINE ACTIVITY
//        ImageView newVisit = findViewById(R.id.newVisit);
//        newVisit.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = NewVisitActivity.makeIntent(TaskViewActivity.this);
//                startActivity(intent);
//            }
//        });

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
                    //Setting up request queue for web service
                    RequestQueue requestQueue = Volley.newRequestQueue(TaskViewActivity.this);

                    //Querying local database for unsynced data to send to the server
                    DatabaseHelper mydb = new DatabaseHelper(TaskViewActivity.this);
                    String query = "SELECT * FROM CLIENT_DATA WHERE is_synced = 0;" ; //get only data that is not synced
                    Cursor c = mydb.executeQuery(query);
                    JSONArray localDataJSON = cur2Json(c);

                    String dataToSend =  localDataJSON.toString();

                    //TODO - Replace 'localhost' your WIFI IPv4 address in the URL string, with port 8080
                    String URL = "http://localhost:8080/clients";

                    StringRequest requestToServer = new StringRequest(
                            Request.Method.POST,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray serverData = new JSONArray(response);
                                        Toast.makeText(TaskViewActivity.this, "Synced successful!", Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        Toast.makeText(TaskViewActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
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