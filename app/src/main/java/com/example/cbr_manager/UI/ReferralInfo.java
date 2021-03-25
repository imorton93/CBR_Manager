package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.cbr_manager.R;

public class ReferralInfo extends AppCompatActivity {

    private long referral_id;
    private int position;
    public static final String R_REFERRAL_ID_PASSED_IN = "r_referral_id_passed_in";
    public static final String R_REFERRAL_POSITION_PASSED_IN = "r_referral_position_passed_in";

    public static Intent makeIntent(Context context, long id, int position) {
        Intent intent =  new Intent(context, ReferralInfo.class);
        intent.putExtra(R_REFERRAL_ID_PASSED_IN, id);
        intent.putExtra(R_REFERRAL_POSITION_PASSED_IN, position);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        referral_id = intent.getLongExtra(R_REFERRAL_ID_PASSED_IN, 0);
        position = intent.getIntExtra(R_REFERRAL_POSITION_PASSED_IN, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_info);
        extractIntent();
    }
}