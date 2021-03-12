package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView firstNameTextView, lastNameTextView, emailTextView, zoneTextView;
    private ImageView profilePictureImageView;
    private String firstName, lastName, email;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ToolbarButtons();
        profilePageButtons();
        CBRWorker cbrWorker = new CBRWorker();

        firstNameTextView = findViewById(R.id.profileFname);
        lastNameTextView = findViewById(R.id.profileLname);
        emailTextView = findViewById(R.id.profileUsername);
        zoneTextView = findViewById(R.id.profileZone);
        profilePictureImageView = findViewById(R.id.imageView);


        Cursor cursor = db.viewData();

        if(cursor.moveToFirst()) {
            firstName = cursor.getString(cursor.getColumnIndex(cbrWorker.getFirstName()));
            lastName = cursor.getString(cursor.getColumnIndex(cbrWorker.getLastName()));
            email = cursor.getString(cursor.getColumnIndex(cbrWorker.getEmail()));

            firstNameTextView.setText("First Name: " + firstName);
            lastNameTextView.setText("First Name: " + lastName);
            emailTextView.setText("First Name: " + email);

            Intent intentProfile = new Intent(this, ProfileActivity.class);
            intentProfile.putExtra("key_firstName", firstName);
            intentProfile.putExtra("key_lastName", lastName);
            intentProfile.putExtra("key_email", email);
            startActivity(intentProfile);


            if (cursor != null && !cursor.isClosed())  {
                cursor.close();
            }

        }
    }
//
//    private void viewData() {
//        Cursor cursor = db.viewData();
//        if (cursor.getCount()== 0){
//            Toast.makeText(this,"No data to show", Toast.LENGTH_SHORT).show();
//        }else{
//            while(cursor.moveToNext()){
//
//            }
//        }
//
//    }

    private void profilePageButtons(){
        Button signoutButton = findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });

    }

    private void ToolbarButtons(){
        ImageButton homeBtn = (ImageButton) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(ProfileActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }
}

