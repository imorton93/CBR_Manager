package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    TextView textView = findViewById(R.id.profileData);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ToolbarButtons();
        profilePageButtons();


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

}

