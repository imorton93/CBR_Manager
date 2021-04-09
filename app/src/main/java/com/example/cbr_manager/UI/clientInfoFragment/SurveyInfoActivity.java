package com.example.cbr_manager.UI.clientInfoFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cbr_manager.Database.Survey;
import com.example.cbr_manager.Database.SurveyManager;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;

public class SurveyInfoActivity extends AppCompatActivity {
    private long id;
    private int position;

    SurveyManager surveyManager;

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

        surveyManager = SurveyManager.getInstance(SurveyInfoActivity.this);
//        setHealthTableValues();
    }

//    private void setHealthTableValues(){
//        TextView accessToRehab_yes = findViewById(R.id.accessToRehab_yes);
//        TextView accessToRehab_no = findViewById(R.id.accessToRehab_no);
//        TextView needAccessToRehab_yes = findViewById(R.id.needAccessToRehab_yes);
//        TextView needAccessToRehab_no = findViewById(R.id.needAccessToRehab_no);
//        TextView haveAssistiveDevice_yes = findViewById(R.id.haveAssistiveDevice_yes);
//        TextView haveAssistiveDevice_no = findViewById(R.id.haveAssistiveDevice_no);
//        TextView assistiveDeviceWorking_yes = findViewById(R.id.assistiveDeviceWorking_yes);
//        TextView assistiveDeviceWorking_no = findViewById(R.id.assistiveDeviceWorking_no);
//        TextView needAssistiveDevice_yes = findViewById(R.id.needAssistiveDevice_yes);
//        TextView needAssistiveDevice_no = findViewById(R.id.needAssistiveDevice_no);
//
//        String accessToRehab_yes_string = "0%";
//        String accessToRehab_no_string = "0%";
//        String needAccessToRehab_yes_string = "0%";
//        String needAccessToRehab_no_string = "0%";
//        String haveAssistiveDevice_yes_string = "0%";
//        String haveAssistiveDevice_no_string = "0%";
//        String assistiveDeviceWorking_yes_string = "0%";
//        String assistiveDeviceWorking_no_string = "0%";
//        String needAssistiveDevice_yes_string = "0%";
//        String needAssistiveDevice_no_string = "0%";
//
//        if(totalSurveys != 0){
//            int valuedCommunityMember_yes_percent = (int) Math.round(((double)accessToRehab_yes_count/totalSurveys)*100);
//            accessToRehab_yes_string = valuedCommunityMember_yes_percent + "%";
//
//            int accessToRehab_no_percent = (int) Math.round(((double)accessToRehab_no_count/totalSurveys)*100);
//            accessToRehab_no_string = accessToRehab_no_percent + "%";
//
//            int needAccessToRehab_yes_percent = (int) Math.round(((double)needAccessToRehab_yes_count/totalSurveys)*100);
//            needAccessToRehab_yes_string = needAccessToRehab_yes_percent + "%";
//
//            int needAccessToRehab_no_percent = (int) Math.round(((double)needAccessToRehab_no_count/totalSurveys)*100);
//            needAccessToRehab_no_string = needAccessToRehab_no_percent + "%";
//
//            int haveAssistiveDevice_yes_percent = (int) Math.round(((double)haveAssistiveDevice_yes_count/totalSurveys)*100);
//            haveAssistiveDevice_yes_string = haveAssistiveDevice_yes_percent + "%";
//
//            int haveAssistiveDevice_no_percent = (int) Math.round(((double)haveAssistiveDevice_no_count/totalSurveys)*100);
//            haveAssistiveDevice_no_string = haveAssistiveDevice_no_percent + "%";
//
//            int assistiveDeviceWorking_yes_percent = (int) Math.round(((double)assistiveDeviceWorking_yes_count/totalSurveys)*100);
//            assistiveDeviceWorking_yes_string = assistiveDeviceWorking_yes_percent + "%";
//
//            int assistiveDeviceWorking_no_percent = (int) Math.round(((double)assistiveDeviceWorking_no_count/totalSurveys)*100);
//            assistiveDeviceWorking_no_string = assistiveDeviceWorking_no_percent + "%";
//
//            int needAssistiveDevice_yes_percent = (int) Math.round(((double)needAssistiveDevice_yes_count/totalSurveys)*100);
//            needAssistiveDevice_yes_string = needAssistiveDevice_yes_percent + "%";
//
//            int needAssistiveDevice_no_percent = (int) Math.round(((double)needAssistiveDevice_no_count/totalSurveys)*100);
//            needAssistiveDevice_no_string = needAssistiveDevice_no_percent + "%";
//        }
//
//        accessToRehab_yes.setText(accessToRehab_yes_string);
//        accessToRehab_no.setText(accessToRehab_no_string);
//        needAccessToRehab_yes.setText(needAccessToRehab_yes_string);
//        needAccessToRehab_no.setText(needAccessToRehab_no_string);
//        haveAssistiveDevice_yes.setText(haveAssistiveDevice_yes_string);
//        haveAssistiveDevice_no.setText(haveAssistiveDevice_no_string);
//        assistiveDeviceWorking_yes.setText(assistiveDeviceWorking_yes_string);
//        assistiveDeviceWorking_no.setText(assistiveDeviceWorking_no_string);
//        needAssistiveDevice_yes.setText(needAssistiveDevice_yes_string);
//        needAssistiveDevice_no.setText(needAssistiveDevice_no_string);
//    }
}