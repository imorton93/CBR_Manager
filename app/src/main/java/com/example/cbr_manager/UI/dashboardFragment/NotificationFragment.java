package com.example.cbr_manager.UI.dashboardFragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cbr_manager.Database.AdminMessage;
import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.SyncService;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.DashboardActivity;
import com.example.cbr_manager.UI.LoginActivity;

import java.util.List;


public class NotificationFragment extends Fragment {

    private DatabaseHelper mydb;
    private String current_username;
    private DashboardActivity dashboardActivity;
    private AdminMessageManager adminMessageManager;

    private SyncService syncService;

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

        this.dashboardActivity = (DashboardActivity)getActivity();
        mydb = new DatabaseHelper(dashboardActivity);
        mydb.setStatusToRead();
        this.current_username = LoginActivity.username;

        newMsg(V);
        refreshMessages(V);

        adminMessageManager = AdminMessageManager.getInstance(dashboardActivity);

        List<AdminMessage> messageList = adminMessageManager.getMessages();
        populateList(messageList, V);

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

    private void refreshMessages(View view) {
        ImageView syncButton = view.findViewById(R.id.syncMsg);
        syncService = new SyncService(getContext());

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncService.getMessagesFromServer();

                adminMessageManager.clear();
                adminMessageManager.updateList();

                List<AdminMessage> messageList = adminMessageManager.getMessages();
                populateList(messageList, view);
            }
        });
    }

    private void populateList(List<AdminMessage> messageList, View v) {
        ArrayAdapter<AdminMessage> adapter = new MyListAdapter(messageList);
        ListView list = v.findViewById(R.id.notificationList);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<AdminMessage> {
        private List<AdminMessage> messages;

        public MyListAdapter(List<AdminMessage> messages) {
            super(dashboardActivity, R.layout.notification_list, messages);
            this.messages = messages;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.notification_list, parent, false);
            }

            AdminMessage currentMessage;
            currentMessage = this.messages.get(position);

            TextView date = view.findViewById(R.id.dateTitle);
            TextView location = view.findViewById(R.id.locationTitle);
            TextView title = view.findViewById(R.id.titleText);
            TextView message = view.findViewById(R.id.msg);

            date.setText(currentMessage.getDate());
            location.setText(currentMessage.getLocation());
            title.setText(currentMessage.getTitle());
            message.setText(currentMessage.getMessage());

            return view;
        }
    }
}