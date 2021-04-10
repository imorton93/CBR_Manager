package com.example.cbr_manager.UI.clientInfoFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ReferralInfo;

import java.util.List;

public class BaselineListFragment extends Fragment {

    private static final String ARG_PARAM1 = "client_id";
    private long client_id;
    private ClientInfoActivity infoActivity;
    private SurveyManager surveyManager;

    public BaselineListFragment() {
        // Required empty public constructor
    }

    public static BaselineListFragment newInstance(long client_id) {
        BaselineListFragment fragment = new BaselineListFragment();
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
        this.infoActivity = (ClientInfoActivity)getActivity();
        Bundle args = getArguments();
        this.client_id = args.getLong("client_id", 0);
        this.surveyManager = SurveyManager.getInstance(infoActivity);

        View V = inflater.inflate(R.layout.fragment_baseline_list, container, false);
        populateListViewFromList(V, client_id);
        clickSurvey(V);
        return V;
    }

    private void populateListViewFromList(View V, long id) {
        this.surveyManager = SurveyManager.getInstance(infoActivity);
        ListView list = V.findViewById(R.id.baselineList);
        ArrayAdapter<Survey> adapter = new BaselineListFragment.MyListAdapter(surveyManager.getSurveys(id));
        list.setAdapter(adapter);
    }

    private void clickSurvey(View V) {
        ListView list = V.findViewById(R.id.baselineList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Survey> surveys = surveyManager.getSurveys(client_id);
                Survey currentSurvey = surveys.get(position);
                Intent intent = SurveyInfoActivity.makeIntent(getActivity(), currentSurvey.getId(), position);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Survey> {
        public MyListAdapter(List<Survey> surveys) {
            super(infoActivity, R.layout.baseline_list, surveys);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.baseline_list, parent, false);
            }

            List<Survey> surveys = surveyManager.getSurveys(client_id);

            TextView outcome = view.findViewById(R.id.baselineNumber);
            String outcomeS = "<b>Survey Number: </b> #" + (surveys.size() - position);
            outcome.setText(Html.fromHtml(outcomeS));

            return view;
        }

    }
}