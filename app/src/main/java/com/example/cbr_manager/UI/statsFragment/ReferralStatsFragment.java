package com.example.cbr_manager.UI.statsFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.StatsActivity;

import java.util.HashMap;
import java.util.List;


public class ReferralStatsFragment extends Fragment {

    private StatsActivity statsActivity;
    private ReferralManager referralManager;
    private HashMap<String, Integer> resourceCounts = new HashMap<String, Integer>();
    private int wheelchair_count = 0;
    private int wheelchairRepair_count = 0;
    private int prosthetics_count = 0;
    private int orthotic_count = 0;
    private int physiotherapy_count = 0;
    private int amputee_count = 0;
    private int polio_count = 0;
    private int spinalCordInjury_count = 0;
    private int cerebralPalsy_count = 0;
    private int spinaBifida_count = 0;
    private int hydrocephalus_count = 0;
    private int visualImpairment_count = 0;
    private int hearingImpairment_count = 0;
    private int other_count = 0;




    public ReferralStatsFragment() {}

    public static ReferralStatsFragment newInstance() {
        ReferralStatsFragment fragment = new ReferralStatsFragment();
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
        this.statsActivity = (StatsActivity) getActivity();
        this.referralManager = ReferralManager.getInstance(statsActivity);

        View view = inflater.inflate(R.layout.fragment_referral_stats, container, false);
        return view;
    }

    private void findCounts(){
        List<Referral> referrals = referralManager.getReferrals();

        for(Referral referral : referrals){
            if(referral.getServiceReq().equals("Wheelchair")){
                if(referral.getHasWheelchair() && referral.getWheelchairReparable()){
                    wheelchairRepair_count++;
                }else{
                    wheelchair_count++;
                }
            }
        }
    }

    private void setWheelChairCounts(View view){
        TextView wheelchair = view.findViewById(R.id.referral_wheelchair_stats);
        TextView wheelchairRepair = view.findViewById(R.id.referral_wheelchairRepair_stats);


    }
}