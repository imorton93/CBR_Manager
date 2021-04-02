package com.example.cbr_manager.UI.statsFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.R;


public class ClientStatsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public ClientStatsFragment() {}

    public static ClientStatsFragment newInstance(String param1) {
        ClientStatsFragment fragment = new ClientStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_stats, container, false);
    }
}