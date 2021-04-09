package com.example.cbr_manager.UI.clientInfoFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;

public class SurveyInfoActivity extends AppCompatActivity {
    private long id;
    private int position;

    public static final String R_CLIENT_ID_PASSED_IN = "r_client_id_passed_in";
    public static final String R_CLIENT_POS_PASSED_IN = "r_client_POS_passed_in";

    public static Intent makeIntent(Context context, long id, int position) {
        Intent intent =  new Intent(context, SurveyInfoActivity.class);
        intent.putExtra(R_CLIENT_ID_PASSED_IN, id);
        intent.putExtra(R_CLIENT_POS_PASSED_IN, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_info);
    }
}