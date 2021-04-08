package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.TextView;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.R;

import static com.example.cbr_manager.UI.LoginActivity.currentCBRWorker;

public class ProfileActivity extends AppCompatActivity {

    private TextView firstNameTextView, lastNameTextView, emailTextView, zoneTextView;
    private ImageView profilePictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ToolbarButtons();

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(ProfileActivity.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        profilePageButtons();

        firstNameTextView = findViewById(R.id.profileFname);
        lastNameTextView = findViewById(R.id.profileLname);
        emailTextView = findViewById(R.id.profileUsername);
        zoneTextView = findViewById(R.id.profileZone);

        firstNameTextView.setText(currentCBRWorker.getFirstName());
        lastNameTextView.setText(currentCBRWorker.getLastName());
        emailTextView.setText(currentCBRWorker.getUsername());
        zoneTextView.setText(currentCBRWorker.getZone());

        //TODO: Include image to database
//        profilePictureImageView = findViewById(R.id.imageView);
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

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(ProfileActivity.this);
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

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.numUnread();

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

    public static Intent makeIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }
}

