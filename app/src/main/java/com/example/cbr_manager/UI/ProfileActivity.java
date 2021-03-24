package com.example.cbr_manager.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.CBRWorkerManager;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private CBRWorkerManager cbrWorkerManager = CBRWorkerManager.getInstance(ProfileActivity.this);
    private TextView firstNameTextView, lastNameTextView, emailTextView, zoneTextView;
    private ImageView profilePictureImageView;
    private String firstName, lastName, email;
    private long id;
    private ProfileActivity profileActivity;

    private DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ToolbarButtons();
        profilePageButtons();

        firstNameTextView = (TextView) findViewById(R.id.profileFname);
        lastNameTextView = (TextView) findViewById(R.id.profileLname);
        emailTextView = (TextView) findViewById(R.id.profileUsername);


//        getClientInfo(v);

        //TODO: Include zone and image to database
//        zoneTextView = findViewById(R.id.profileZone);
//        profilePictureImageView = findViewById(R.id.imageView);


    }

    public long getId() {
        return id;
    }

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        this.infoActivity = (ClientInfoActivity)getActivity();
//        View v = inflater.inflate(R.layout.fragment_info, container, false);
//        getClientInfo(v);
//        return v;
//    }

    public void getClientInfo(@Nullable View convertView, @NonNull ViewGroup parent){

        View view = convertView;

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.activity_profile, parent, false);
        }

        CBRWorkerManager cbrWorkerManager = CBRWorkerManager.getInstance(profileActivity);
        CBRWorker currentCBR = cbrWorkerManager.getCBRById(profileActivity.getId());


        TextView firstNameTextView = view.findViewById(R.id.profileFname);
        TextView lastNameTextView = view.findViewById(R.id.profileLname);
        TextView emailTextView = view.findViewById(R.id.profileUsername);

        firstNameTextView.setText(currentCBR.getFirstName());
        lastNameTextView.setText(currentCBR.getLastName());
        emailTextView.setText(currentCBR.getEmail());

    }



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


    private class MyListAdapter extends ArrayAdapter<CBRWorker> {
        private List<CBRWorker> cbr;

        public MyListAdapter(List<CBRWorker> cbr) {
            super(ProfileActivity.this, R.layout.activity_profile, cbr);
            this.cbr = cbr;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.activity_profile, parent, false);
            }

            CBRWorker currentCBR;

            currentCBR = this.cbr.get(position);


            TextView firstNameTextView = view.findViewById(R.id.profileFname);
            TextView lastNameTextView = view.findViewById(R.id.profileLname);
            TextView emailTextView = view.findViewById(R.id.profileUsername);

            firstNameTextView.setText(currentCBR.getFirstName());
            lastNameTextView.setText(currentCBR.getLastName());
            emailTextView.setText(currentCBR.getEmail());

            return view;
        }
    }

}

