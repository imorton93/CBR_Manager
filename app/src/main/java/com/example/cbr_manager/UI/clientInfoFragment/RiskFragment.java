package com.example.cbr_manager.UI.clientInfoFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.R;

public class RiskFragment extends Fragment {

    public RiskFragment() {
        // Required empty public constructor
    }

    public static RiskFragment newInstance() {
        RiskFragment fragment = new RiskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: set up health, education and social risk levels + goals
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_risk, container, false);
    }
}