package com.example.cbr_manager.UI.dashboardFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Forms.TextQuestion;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.LoginActivity;
import com.example.cbr_manager.UI.NewClientActivity;
import com.example.cbr_manager.UI.NewVisitActivity;
import com.example.cbr_manager.UI.SignUpActivity;
import com.example.cbr_manager.UI.TaskViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class NewMsgActivity extends AppCompatActivity {

    private AdminMessage adminMessage;
    private DatabaseHelper databaseHelper;
    private int workerID = 0;
    private RequestQueue requestQueue;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, NewMsgActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_msg);

        requestQueue = Volley.newRequestQueue(NewMsgActivity.this);
        databaseHelper = new DatabaseHelper(NewMsgActivity.this);

        String current_username = getIntent().getStringExtra("Worker Username");
        workerID = databaseHelper.getWorkerId(current_username);

        date();
        createMessageButton();
    }

    private void createMessageButton() {
        Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminMessage = new AdminMessage();

                EditText title = findViewById(R.id.titleTextBox);
                EditText date = findViewById(R.id.dateTextBox);
                EditText location = findViewById(R.id.locationTextBox);
                EditText message = findViewById(R.id.messageTextBox);

                adminMessage.setTitle(title.getText().toString());
                adminMessage.setAdminID(workerID);
                adminMessage.setDate(date.getText().toString());
                adminMessage.setLocation(location.getText().toString());
                adminMessage.setMessage(message.getText().toString());
                setUniqueMessageId();

                boolean success = databaseHelper.addMessage(adminMessage);

                if(success) {
                    sendMsgToServer();
                    Intent intent = TaskViewActivity.makeIntent(NewMsgActivity.this);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(NewMsgActivity.this, "Error Occurred.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void date(){
        EditText dateTextBox = findViewById(R.id.dateTextBox);
        Calendar calendar = Calendar.getInstance();
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH)+1;
        int dayOfMonth1 = calendar.get(Calendar.DAY_OF_MONTH);
        String date = dayOfMonth1 + "/" + month1 + "/" + year1;

        dateTextBox.setText(date);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String date = dayOfMonth + "/" + month + "/" + year;
                dateTextBox.setText(date);
            }
        };

        dateTextBox.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(NewMsgActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener, year, month, day);
            dialog.show();
        });
    }

    private void setUniqueMessageId(){
        DatabaseHelper db =  new DatabaseHelper(getApplicationContext());

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        int admin_id = db.getWorkerId(username);
        adminMessage.setAdminID(admin_id);

        int msg_no = db.numberOfMessagesPerAdmin(admin_id);
        msg_no++; //next available msg id

        String uniqueID = String.valueOf(admin_id * 100) + String.valueOf(msg_no);
        long uniqueID_long = Long.parseLong(uniqueID);

        adminMessage.setId(uniqueID_long);
    }

    private void sendMsgToServer () {
        String query = "SELECT * FROM ADMIN_MESSAGES WHERE ID = " + adminMessage.getId();
        Cursor c = databaseHelper.executeQuery(query);
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
                        Toast.makeText(NewMsgActivity.this, "Exception Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;
    }
}