package com.example.cbr_manager.UI.dashboardFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cbr_manager.Database.AdminMessage;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.Database.SyncService;

import com.example.cbr_manager.UI.TaskViewActivity;
import java.util.Calendar;

public class NewMsgActivity extends AppCompatActivity {

    private AdminMessage adminMessage;
    private DatabaseHelper databaseHelper;
    private int workerID = 0;

    private SyncService syncService;

    public static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, NewMsgActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_msg);

        syncService = new SyncService(NewMsgActivity.this);
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

                EditText title = findViewById(R.id.first);
                EditText date = findViewById(R.id.last);
                EditText location = findViewById(R.id.user);
                EditText message = findViewById(R.id.messageTextBox);

                adminMessage.setTitle(title.getText().toString());
                adminMessage.setAdminID(workerID);
                adminMessage.setDate(date.getText().toString());
                adminMessage.setLocation(location.getText().toString());
                adminMessage.setMessage(message.getText().toString());
                setUniqueMessageId();

                boolean success = databaseHelper.addMessage(adminMessage);

                if(success) {
                    syncService.sendMsgToServer(adminMessage);
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
        EditText dateTextBox = findViewById(R.id.last);
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


}