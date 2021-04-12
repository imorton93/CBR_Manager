package com.example.cbr_manager.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.Database.AdminMessageManager;
import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.Database.DatabaseHelper;
import com.example.cbr_manager.Database.Referral;
import com.example.cbr_manager.Database.ReferralManager;
import com.example.cbr_manager.R;

import java.util.Arrays;

public class ReferralInfo extends AppCompatActivity {

    private long referral_id;
    private int position;
    public static final String R_REFERRAL_ID_PASSED_IN = "r_referral_id_passed_in";
    public static final String R_REFERRAL_POSITION_PASSED_IN = "r_referral_position_passed_in";
    private View undoButton, resolveButton;
    private DatabaseHelper myDb;

    public static Intent makeIntent(Context context, long id, int position) {
        Intent intent = new Intent(context, ReferralInfo.class);
        intent.putExtra(R_REFERRAL_ID_PASSED_IN, id);
        intent.putExtra(R_REFERRAL_POSITION_PASSED_IN, position);
        return intent;
    }

    private void extractIntent() {
        Intent intent = getIntent();
        referral_id = intent.getLongExtra(R_REFERRAL_ID_PASSED_IN, 0);
        position = intent.getIntExtra(R_REFERRAL_POSITION_PASSED_IN, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_info);
        extractIntent();
        ToolbarButtons();

        myDb = new DatabaseHelper(ReferralInfo.this);
        undoButton = findViewById(R.id.button3);
        resolveButton = findViewById(R.id.button2);
        if(myDb.isResolved(referral_id))
            resolveButton.setVisibility(View.GONE);
        else
            undoButton.setVisibility(View.GONE);

        AdminMessageManager adminMessageManager = AdminMessageManager.getInstance(ReferralInfo.this);
        adminMessageManager.clear();
        adminMessageManager.updateList();

        TextView badgeOnToolBar = findViewById(R.id.cart_badge2);
        badgeNotification(adminMessageManager, badgeOnToolBar);

        getClientInfo();
    }

    private void getClientInfo() {
        ReferralManager referralManager = ReferralManager.getInstance(ReferralInfo.this);
        Referral currentReferral = referralManager.getReferralById(referral_id);

        ImageView referralPic = findViewById(R.id.referralImage);
        TextView serviceReq = findViewById(R.id.serviceReqInfo);
        TextView otherInformation = findViewById(R.id.OtherInformation);

        String otherInfo = concatOtherInformation(currentReferral);
        String service = "<b>Service Required:</b> " + currentReferral.getServiceReq();

        if (currentReferral.getReferralPhoto() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(currentReferral.getReferralPhoto(), 0, currentReferral.getReferralPhoto().length);
            referralPic.setImageBitmap(bmp);
        }

        serviceReq.setText(Html.fromHtml(service));
        otherInformation.setText(Html.fromHtml(otherInfo));
    }

    private String concatOtherInformation(Referral ref) {
        String otherInformation = "";
        if (ref.getServiceReq().equals("Physiotherapy")) {
            otherInformation += "<b>Condition of client:</b> " + ref.getCondition() + "<br>";
            if (ref.getCondition().equals("Other")) {
                otherInformation += "<b>Condition Explanation:</b> " + ref.getConditionOtherExplanation() + "<br>";
            }
        }else if(ref.getServiceReq().equals("Prosthetic")){
            otherInformation += "<b>Injury location(above or below the knee):</b> " + ref.getInjuryLocation() + "<br>";
        }else if(ref.getServiceReq().equals("Orthotic")){
            otherInformation += "<b>Injury location(above or below the elbow):</b> " + ref.getInjuryLocation() + "<br>";
        } else if (ref.getServiceReq().equals("Wheelchair")) {
            otherInformation += "<b>User Status (Basic or Intermediate):</b> " + ref.getBasicOrInter() + "<br>";
            otherInformation += "<b>Clients hip width:</b> " + ref.getHipWidth() + "<br>";
            otherInformation += "<b>Does the Client have an existing wheelchair:</b> " + ref.getHasWheelchair() + "<br>";
            if (ref.getHasWheelchair().equals("Yes")) {
                otherInformation += "<b>Is the clients wheelchair repairable:</b> " + ref.getWheelchairReparable() + "<br>";
            }
        } else {
            otherInformation += "<b>Description of Condition:</b> " + ref.getOtherExplanation() + "<br>";
        }
        return otherInformation;
    }

    private void ToolbarButtons() {
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskViewActivity.makeIntent(ReferralInfo.this);
                startActivity(intent);
            }
        });

        ImageButton notificationBtn = findViewById(R.id.notificationButton);
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DashboardActivity.makeIntent(ReferralInfo.this);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profileButton);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileActivity.makeIntent(ReferralInfo.this);
                startActivity(intent);
            }
        });
    }

    private void badgeNotification(AdminMessageManager adminMessageManager, TextView badge) {
        int size = adminMessageManager.numUnread();

        if (badge != null) {
            if (size == 0) {
                if (badge.getVisibility() != View.GONE) {
                    badge.setVisibility(View.GONE);
                }
            } else {
                badge.setText(String.valueOf(Math.min(size, 99)));
                if (badge.getVisibility() != View.VISIBLE) {
                    badge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void resolveReferral(View view) {
        DatabaseHelper db = new DatabaseHelper(ReferralInfo.this);
        db.resolveReferral(referral_id);
        db.setRefferalToNotSynced(referral_id);
        ReferralManager referralManager = ReferralManager.getInstance(this);
        Referral referral = referralManager.getReferralById(referral_id);
        referral.setOutcome("RESOLVED");
        resolveButton.setVisibility(View.GONE);
        undoButton.setVisibility(View.VISIBLE);
        Toast.makeText(ReferralInfo.this, "Referral Resolved!", Toast.LENGTH_LONG).show();
        //TODO: START THE REFERRAL FRAGMENT
        //Intent intent = .makeIntent(ReferralInfo.this);
        //startActivity(intent);
    }

    public void undoResolveReferral(View view) {
        DatabaseHelper db = new DatabaseHelper(ReferralInfo.this);
        db.undoResolveReferral(referral_id);
        db.setRefferalToNotSynced(referral_id);
        ReferralManager referralManager = ReferralManager.getInstance(this);
        Referral referral = referralManager.getReferralById(referral_id);
        referral.setOutcome("UNRESOLVED");
        undoButton.setVisibility(View.GONE);
        resolveButton.setVisibility(View.VISIBLE);
        Toast.makeText(ReferralInfo.this, "Changes Saved!", Toast.LENGTH_LONG).show();
        //TODO: START THE REFERRAL FRAGMENT
        //Intent intent = .makeIntent(ReferralInfo.this);
        //startActivity(intent);
    }
}