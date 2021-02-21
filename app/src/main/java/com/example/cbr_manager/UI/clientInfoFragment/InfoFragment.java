package com.example.cbr_manager.UI.clientInfoFragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;

public class InfoFragment extends Fragment {

    private ClientInfoActivity infoActivity;

    public InfoFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        this.infoActivity = (ClientInfoActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        getClientInfo(v);
        return v;
    }

    public void getClientInfo(View v){
        DatabaseHelper handler = new DatabaseHelper(this.infoActivity);
        Cursor todoCursor = handler.getRow(infoActivity.getId());
        TextView name = v.findViewById(R.id.name);
        TextView gender = v.findViewById(R.id.genderAns);
        TextView age = v.findViewById(R.id.ageAns);
        TextView location = v.findViewById(R.id.locationAns);
        TextView disability = v.findViewById(R.id.disabilityAns);

        // Extract properties from cursor
        String first_name = todoCursor.getString(todoCursor.getColumnIndexOrThrow("FIRST_NAME"));
        String last_name = todoCursor.getString(todoCursor.getColumnIndexOrThrow("LAST_NAME"));
        String ageString = todoCursor.getString(todoCursor.getColumnIndexOrThrow("AGE"));
        String genderString = todoCursor.getString(todoCursor.getColumnIndexOrThrow("GENDER"));
        String villageString = todoCursor.getString(todoCursor.getColumnIndexOrThrow("LOCATION"));
        String disabilityString = todoCursor.getString(todoCursor.getColumnIndexOrThrow("DISABILITY"));
        String name_string = first_name + " " + last_name;

        // Populate fields with extracted properties
        name.setText(name_string);
        gender.setText(genderString);
        age.setText(ageString);
        location.setText(villageString);
        disability.setText(disabilityString);
    }

}