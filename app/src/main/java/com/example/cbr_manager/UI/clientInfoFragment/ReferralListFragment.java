package com.example.cbr_manager.UI.clientInfoFragment;

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
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ReferralInfo;

import java.util.List;


public class ReferralListFragment extends Fragment {

    private static final String ARG_PARAM1 = "client_id";
    private long client_id;
    private ClientInfoActivity infoActivity;
    private ReferralManager referralManager;

    public ReferralListFragment() {
    }

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
        this.infoActivity = (ClientInfoActivity) getActivity();
        Bundle args = getArguments();
        this.client_id = args.getLong("client_id", 0);
        this.referralManager = ReferralManager.getInstance(infoActivity);
        View V = inflater.inflate(R.layout.fragment_referral_list, container, false);
        populateListViewFromList(V, client_id);
        clickReferral(V);
        return V;
    }

    private void populateListViewFromList(View V, long id) {
        this.referralManager = ReferralManager.getInstance(infoActivity);
        ListView list = V.findViewById(R.id.referralList);
        ArrayAdapter<Referral> adapter = new ReferralListFragment.MyListAdapter(referralManager.getReferrals(id));
        list.setAdapter(adapter);
    }

    private void clickReferral(View V) {
        ListView list = V.findViewById(R.id.referralList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Referral> referrals = referralManager.getReferrals(client_id);
                Referral currentReferral = referrals.get(position);
                Intent intent = ReferralInfo.makeIntent(getActivity(), currentReferral.getId(), position);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Referral> {
        public MyListAdapter(List<Referral> referrals) {
            super(infoActivity, R.layout.referral_lists, referrals);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.referral_lists, parent, false);
            }

            List<Referral> referrals = referralManager.getReferrals(client_id);
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