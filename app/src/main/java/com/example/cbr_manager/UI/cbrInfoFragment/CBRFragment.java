package com.example.cbr_manager.UI.cbrInfoFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cbr_manager.Database.CBRWorker;
import com.example.cbr_manager.Database.CBRWorkerManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ProfileActivity;

public class CBRFragment extends Fragment {

    private ProfileActivity cbrActivity;

    public CBRFragment() {
    }

    public static CBRFragment newInstance(long id) {
        CBRFragment fragment = new CBRFragment();
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
        this.cbrActivity = (ProfileActivity)getActivity();
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        getCBRInfo(v);
        return v;
    }

    public View getCBRInfo(View view){
        CBRWorkerManager cbrWorkerManager = CBRWorkerManager.getInstance(cbrActivity);
        CBRWorker currentCBR = cbrWorkerManager.getCBRById(cbrActivity.getId());
        TextView firstNameTextView = view.findViewById(R.id.profileFname);
        TextView lastNameTextView = view.findViewById(R.id.profileLname);
        TextView emailTextView = view.findViewById(R.id.profileUsername);

        firstNameTextView.setText(currentCBR.getFirstName());
        lastNameTextView.setText(currentCBR.getLastName());
        emailTextView.setText(currentCBR.getEmail());

        return view;
    }
}
