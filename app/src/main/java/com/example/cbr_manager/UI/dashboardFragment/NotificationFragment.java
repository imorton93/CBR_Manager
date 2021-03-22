package com.example.cbr_manager.UI.dashboardFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.TaskViewActivity;


public class NotificationFragment extends Fragment {

    private static final String TAG = "USERNAME";
    private DatabaseHelper mydb;
    private String current_username;
    private DashboardActivity dashboardActivity;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String current_username) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString("Worker Username", current_username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V =  inflater.inflate(R.layout.fragment_notification, container, false);
        Bundle args = getArguments();

        this.dashboardActivity = (DashboardActivity)getActivity();
        mydb = new DatabaseHelper(dashboardActivity);
        this.current_username = args.getString("current_username", "");

        newMsg(V);

        return V;
    }

    private void newMsg(View view) {
        int invisible = view.INVISIBLE;
        ImageView addButton = view.findViewById(R.id.newMsg);

        if(!mydb.isAdmin(current_username)){
            addButton.setVisibility(invisible);
        } else {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = NewMsgActivity.makeIntent(dashboardActivity);
                    intent.putExtra("Worker Username", current_username);
                    startActivity(intent);
                }
            });
        }

    }


}