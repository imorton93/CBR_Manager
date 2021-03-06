package com.example.cbr_manager.UI.clientInfoFragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;

import java.util.Arrays;
import java.util.List;

public class InfoFragment extends Fragment {

    private ClientInfoActivity infoActivity;

    public InfoFragment() {
    }

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
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
        this.infoActivity = (ClientInfoActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        getClientInfo(v);
        return v;
    }

    public void getClientInfo(View v){
        ClientManager clientManager = ClientManager.getInstance(infoActivity);
        Client currentClient = clientManager.getClientById(infoActivity.getId());

        TextView name = v.findViewById(R.id.name);
        TextView gender = v.findViewById(R.id.genderAns);
        TextView age = v.findViewById(R.id.ageAns);
        TextView location = v.findViewById(R.id.locationAns);
        TextView disability = v.findViewById(R.id.disabilityAns);

        String name_string = currentClient.getFirstName() + " " + currentClient.getLastName();

        String disabilities = Arrays.toString(currentClient.getDisabilities().toArray()).replace("[", "").replace("]", "");

        name.setText(name_string);
        gender.setText(currentClient.getGender());
        age.setText(String.valueOf(currentClient.getAge()));
        location.setText(currentClient.getLocation());
        disability.setText(disabilities);
    }

}