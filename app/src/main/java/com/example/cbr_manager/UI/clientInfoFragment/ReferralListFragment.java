package com.example.cbr_manager.UI.clientInfoFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.R;


public class ReferralListFragment extends Fragment {

    private static final String ARG_PARAM1 = "client_id";
    private long client_id;

    public ReferralListFragment() { }

    public static ReferralListFragment newInstance(long client_id) {
        ReferralListFragment fragment = new ReferralListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, client_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            client_id = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_referral_list, container, false);
    }
}