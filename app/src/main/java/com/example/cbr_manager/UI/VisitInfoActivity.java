package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.clientInfoFragment.VisitsFragment;

public class VisitInfoActivity extends AppCompatActivity {

    public static final String R_VISIT_ID_PASSED_IN = "r_visit_id_passed_in";

    private long visit_id;

    public static Intent makeIntent(Context context, long id) {
        Intent intent =  new Intent(context, VisitInfoActivity.class);
        intent.putExtra(R_VISIT_ID_PASSED_IN, id);
        return intent;
    }

    private void extractIntent(){
        Intent intent = getIntent();
        visit_id = intent.getLongExtra(R_VISIT_ID_PASSED_IN, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_info);

        extractIntent();
    }

    private void getVisitInfo(){
        DatabaseHelper handler = new DatabaseHelper(VisitInfoActivity.this);
        Cursor c = handler.getVisit(visit_id);
    }
}