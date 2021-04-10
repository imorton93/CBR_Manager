package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReferralManager implements Iterable<Referral>{

    private List<Referral> referrals = new ArrayList<>();
    private static ReferralManager instance;
    private DatabaseHelper databaseHelper;

    private static final String referral_id = "ID";
    private static final String service_req = "SERVICE_REQUIRED";
    private static final String referral_photo = "REFERRAL_PHOTO";
    private static final String basic_or_inter = "BASIC_OR_INTERMEDIATE";
    private static final String hip_width = "HIP_WIDTH";
    private static final String has_wheelchair = "HAS_WHEELCHAIR";
    private static final String wheelchair_repairable = "WHEELCHAIR_REPAIRABLE";
    private static final String bring_to_centre = "BRING_TO_CENTRE";
    private static final String conditions = "CONDITIONS";
    private static final String injury_location_knee = "INJURY_LOCATION_KNEE";
    private static final String injury_location_elbow = "INJURY_LOCATION_ELBOW";
    private static final String referral_status = "REFERRAL_STATUS";
    private static final String referral_outcome = "REFERRAL_OUTCOME";
    private static final String client_referral_id = "CLIENT_ID";

    public ReferralManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public static ReferralManager getInstance(Context context) {
        if (instance == null) {
            instance = new ReferralManager(context);
        }

        return instance;
    }

    public List<Referral> getReferrals(){
        return referrals;
    }

    public void updateList() {
        Cursor c = databaseHelper.getAllReferrals();

        int idI = c.getColumnIndex(referral_id);
        int serviceReqI = c.getColumnIndex(service_req);
        int referralPhotoI = c.getColumnIndex(referral_photo);
        int basicOrInterI = c.getColumnIndex(basic_or_inter);
        int hipWidthI = c.getColumnIndex(hip_width);
        int hasWheelchairI = c.getColumnIndex(has_wheelchair);
        int wheelchairReparableI = c.getColumnIndex(wheelchair_repairable);
        int bringToCentreI = c.getColumnIndex(bring_to_centre);
        int conditionI = c.getColumnIndex(conditions);
        int injuryLocationKneeI = c.getColumnIndex(injury_location_knee);
        int injuryLocationElbowI = c.getColumnIndex(injury_location_elbow);
        int statusI = c.getColumnIndex(referral_status);
        int outcomeI = c.getColumnIndex(referral_outcome);
        int clientIDI = c.getColumnIndex(client_referral_id);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Long id = c.getLong(idI);
            String serviceReq = c.getString(serviceReqI);
            byte[] referralPhoto = c.getBlob(referralPhotoI);
            String basicOrInter = c.getString(basicOrInterI);
            int hipWidth = c.getInt(hipWidthI);
            Boolean hasWheelchair = c.getInt(hasWheelchairI) > 0;
            Boolean wheelchairReparable = c.getInt(wheelchairReparableI) > 0;
            Boolean bringToCentre = c.getInt(bringToCentreI) > 0;
            String condition = c.getString(conditionI);
            String injuryLocation;
            if(serviceReq.equalsIgnoreCase("Prosthetic")){
                injuryLocation = c.getString(injuryLocationKneeI);
            }
            else{
                injuryLocation = c.getString(injuryLocationElbowI);
            }
            String status = c.getString(statusI);
            String outcome = c.getString(outcomeI);
            Long clientID = c.getLong(clientIDI);

            Referral newReferral = new Referral(serviceReq, referralPhoto, basicOrInter, hipWidth, hasWheelchair, wheelchairReparable, bringToCentre, condition,
                    injuryLocation, status, outcome, clientID);
            newReferral.setId(id);
            referrals.add(newReferral);
        }
    }

    public void addReferral(Referral referral){
        referrals.add(0, referral);
    }


    public List<Referral> getUnresolvedReferrals(){
        List<Referral> finalReferrals = new ArrayList<>();

        for (Referral currentReferral : this.referrals) {
            if(currentReferral.getOutcome() != null) {
                if (currentReferral.getOutcome().equals("UNRESOLVED")) {
                    finalReferrals.add(currentReferral);
                }
            }
        }

        return finalReferrals;
    }

    public Referral getReferralById(long id){
        Referral referral = new Referral();
        for(Referral ref : referrals){
            if(ref.getId()  == id){
                referral = ref;
                break;
            }
        }
        return referral;
    }

    public List<Referral> getReferrals(long id) {
        List<Referral> finalReferrals = new ArrayList<>();

        for (Referral currentReferral : this.referrals) {
            if(currentReferral.getClientID() == id) {
                finalReferrals.add(currentReferral);
            }
        }

        return finalReferrals;
    }

    public void clear() {
        referrals.clear();
    }

    @NonNull
    @Override
    public Iterator<Referral> iterator() {
        return referrals.iterator();
    }
}
