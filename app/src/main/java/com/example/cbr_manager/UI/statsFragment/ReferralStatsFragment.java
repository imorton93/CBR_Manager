package com.example.cbr_manager.UI.statsFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.Database.VisitManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ReferralInfo;
import com.example.cbr_manager.UI.StatsActivity;
import com.example.cbr_manager.UI.clientInfoFragment.ReferralListFragment;

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
        findCounts();
        setCounts(view);
        setPhysioCounts(view);
        populateListViewFromList(view);
        clickReferral(view);
        return view;
    }

    private void findCounts(){
        List<Referral> referrals = referralManager.getReferrals();
        System.out.println("Referral size: " + referrals.size());
        for(Referral referral : referrals){
            switch (referral.getServiceReq()) {
                case "Wheelchair":
                    if (referral.getHasWheelchair() && referral.getWheelchairReparable()) {
                        wheelchairRepair_count++;
                    } else {
                        wheelchair_count++;
                    }
                    break;
                case "Prosthetic":
                    prosthetics_count++;
                    break;
                case "Orthotic":
                    orthotic_count++;
                    break;
                case "Physiotherapy":
                    physiotherapy_count++;
                    findPhysioCount(referral.getCondition());
                    break;
            }
        }
    }

    private void findPhysioCount(String physioRequired){
        if(physioRequired.equals("Amputee")){
            amputee_count++;
        }else if(physioRequired.equals("Polio")){
            polio_count++;
        }else if(physioRequired.equals("Spinal Cord Injury")){
            spinalCordInjury_count++;
        }else if(physioRequired.equals("Cerebral Palsy")){
            cerebralPalsy_count++;
        }else if(physioRequired.equals("Spina Bifida")){
            spinaBifida_count++;
        }else if(physioRequired.equals("Hydrocephalus")){
            hydrocephalus_count++;
        }else if(physioRequired.equals("Visual Impairment")){
            visualImpairment_count++;
        }else if(physioRequired.equals("Hearing Impairment")){
            hearingImpairment_count++;
        }else if(physioRequired.equals("Other")){
            other_count++;
        }
    }

    private void setCounts(View view){
        TextView wheelchair = view.findViewById(R.id.referral_wheelchair_stats);
        TextView wheelchairRepair = view.findViewById(R.id.referral_wheelchairRepair_stats);
        TextView prosthetics = view.findViewById(R.id.referral_prosthetics_stats);
        TextView orthotics = view.findViewById(R.id.referral_orthotics_stats);

        wheelchair.setText(String.valueOf(wheelchair_count));
        wheelchairRepair.setText(String.valueOf(wheelchairRepair_count));
        prosthetics.setText(String.valueOf(prosthetics_count));
        orthotics.setText(String.valueOf(orthotic_count));
    }

    private void setPhysioCounts(View view){
        TextView physio = view.findViewById(R.id.referral_physiotherapy_stats);
        TextView amputee = view.findViewById(R.id.referral_amputee_stats);
        TextView polio = view.findViewById(R.id.referral_polio_stats);
        TextView spinalCord = view.findViewById(R.id.referral_spinalCord_stats);
        TextView cerebralPalsy = view.findViewById(R.id.referral_cerebralPalsy_stats);
        TextView spinaBifida = view.findViewById(R.id.referral_spinaBifida_stats);
        TextView hydrocephalus = view.findViewById(R.id.referral_hydrocephalus_stats);
        TextView visualImpairment = view.findViewById(R.id.referral_visualImpairment_stats);
        TextView hearingImpairment = view.findViewById(R.id.referral_hearingImpairment_stats);
        TextView other = view.findViewById(R.id.referral_other_stats);

        physio.setText(String.valueOf(physiotherapy_count));
        amputee.setText(String.valueOf(amputee_count));
        polio.setText(String.valueOf(polio_count));
        spinalCord.setText(String.valueOf(spinalCordInjury_count));
        cerebralPalsy.setText(String.valueOf(cerebralPalsy_count));
        spinaBifida.setText(String.valueOf(spinaBifida_count));
        hydrocephalus.setText(String.valueOf(hydrocephalus_count));
        visualImpairment.setText(String.valueOf(visualImpairment_count));
        hearingImpairment.setText(String.valueOf(hearingImpairment_count));
        other.setText(String.valueOf(other_count));
    }

    private void populateListViewFromList(View V) {
        this.referralManager = ReferralManager.getInstance(statsActivity);
        ListView list = V.findViewById(R.id.UnresolvedReferralList);
        TextView textview =  V.findViewById(R.id.unresolvedReferralTotal);
        List<Referral> unresolved = referralManager.getUnresolvedReferrals();
        ArrayAdapter<Referral> adapter = new ReferralStatsFragment.MyListAdapter(unresolved);
        int unresolved_num = referralManager.getUnresolvedReferrals().size();
        int total_referrals = referralManager.getReferrals().size();
        String numberOfResolved = unresolved_num + "/" + total_referrals;
        textview.setText(numberOfResolved);
        list.setAdapter(adapter);
    }

    private void clickReferral(View V) {
        ListView list = V.findViewById(R.id.UnresolvedReferralList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Referral> referrals = referralManager.getUnresolvedReferrals();
                Referral currentReferral = referrals.get(position);
                Intent intent = ReferralInfo.makeIntent(getActivity(), currentReferral.getId(), position);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Referral> {
        public MyListAdapter(List<Referral> referrals) {
            super(statsActivity, R.layout.referral_lists, referrals);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.referral_lists, parent, false);
            }

            List<Referral> referrals = referralManager.getUnresolvedReferrals();
            Referral currentReferral = referrals.get(position);

            ImageView referralPhoto = view.findViewById(R.id.referral_image);
            TextView serviceReq = view.findViewById(R.id.serviceReq);
            TextView outcome = view.findViewById(R.id.referralNumber);

            View resolved = view.findViewById(R.id.resolvedTextView);
            DatabaseHelper myDb = new DatabaseHelper(getActivity());
            if(myDb.isResolved(currentReferral.getId()))
                resolved.setVisibility(View.VISIBLE);

            String serviceReqS = "<b>Service Required: </b> " + currentReferral.getServiceReq();
            String outcomeS = "<b>Referral Number: </b> #" + (referrals.size() - position);

            if (currentReferral.getReferralPhoto() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(currentReferral.getReferralPhoto(), 0, currentReferral.getReferralPhoto().length);
                referralPhoto.setImageBitmap(bmp);
            }
            serviceReq.setText(Html.fromHtml(serviceReqS));
            outcome.setText(Html.fromHtml(outcomeS));

            return view;
        }

    }
}