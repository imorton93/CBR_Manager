package com.example.cbr_manager.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.CBRWorkerManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.cbrInfoFragment.CBRFragment;
import com.example.cbr_manager.UI.clientInfoFragment.InfoFragment;
import com.example.cbr_manager.UI.clientInfoFragment.RiskFragment;
import com.example.cbr_manager.UI.clientInfoFragment.VisitsFragment;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private CBRWorkerManager cbrWorkerManager = CBRWorkerManager.getInstance(ProfileActivity.this);
    private TextView firstNameTextView, lastNameTextView, emailTextView, zoneTextView;
    private ImageView profilePictureImageView;
    private String firstName, lastName, email;
    private long id;
    private ProfileActivity profileActivity;

    private DatabaseHelper db;
    ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ToolbarButtons();
        profilePageButtons();


        firstNameTextView = (TextView) findViewById(R.id.profileFname);
        lastNameTextView = (TextView) findViewById(R.id.profileLname);
        emailTextView = (TextView) findViewById(R.id.profileUsername);
//
//        viewPager = findViewById(R.id.viewPager);
//
//        viewPager.setAdapter(createCardAdapter());
//
        cbrActivity = (ProfileActivity)getActivity()
        CBRWorkerManager cbrWorkerManager = CBRWorkerManager.getInstance(cbrActivity);
        CBRWorker currentCBR = cbrWorkerManager.getCBRById(cbrActivity.getId());

        firstNameTextView.setText(currentCBR.getFirstName());
        lastNameTextView.setText(currentCBR.getLastName());
        emailTextView.setText(currentCBR.getEmail());
        //TODO: Include zone and image to database
//        zoneTextView = findViewById(R.id.profileZone);
//        profilePictureImageView = findViewById(R.id.imageView);


    }

    public long getId() {
        return id;
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        private static final int CARD_ITEM_SIZE = 3;
        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return CBRFragment.newInstance(ProfileActivity.this.getId());
                }
                case 1: {
                    CBRFragment cbrFragment = CBRFragment.newInstance(ProfileActivity.this.getId());
                    Bundle bundle = new Bundle();
                    bundle.putLong("worker_id", ProfileActivity.this.getId());
                    cbrFragment.setArguments(bundle);
                    return cbrFragment;
                }
                default:
                    return CBRFragment.newInstance(pos);
            }
        }
        @Override public int getItemCount() {
            return CARD_ITEM_SIZE;
        }
    }

    private ProfileActivity.ViewPagerAdapter createCardAdapter() {
        ProfileActivity.ViewPagerAdapter adapter = new ProfileActivity.ViewPagerAdapter(this);
        return adapter;
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
//
//    public void getClientInfo(@Nullable View convertView, @NonNull ViewGroup parent){
//
//        View view = convertView;
//
//        if (view == null) {
//            view = getLayoutInflater().inflate(R.layout.activity_profile, parent, false);
//        }
//
//        CBRWorkerManager cbrWorkerManager = CBRWorkerManager.getInstance(profileActivity);
//        CBRWorker currentCBR = cbrWorkerManager.getCBRById(profileActivity.getId());
//
//
//        TextView firstNameTextView = view.findViewById(R.id.profileFname);
//        TextView lastNameTextView = view.findViewById(R.id.profileLname);
//        TextView emailTextView = view.findViewById(R.id.profileUsername);
//
//        firstNameTextView.setText(currentCBR.getFirstName());
//        lastNameTextView.setText(currentCBR.getLastName());
//        emailTextView.setText(currentCBR.getEmail());
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

