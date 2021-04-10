package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VisitManager implements Iterable<Visit>{

    private static final String TAG = "HEY" ;
    private List<Visit> visits = new ArrayList<>();
    private static VisitManager instance;
    private DatabaseHelper databaseHelper;

    private static final String visit_id = "ID";
    private static final String visit_date = "VISIT_DATE";
    private static final String visit_purpose = "PURPOSE_OF_VISIT";
    private static final String if_cbr = "IF_CBR";
    private static final String visit_location = "LOCATION";
    private static final String visit_village_no = "VILLAGE_NUMBER";
    private static final String health_provided = "HEALTH_PROVIDED";
    private static final String health_goal_status = "HEALTH_GOAL_STATUS";
    private static final String health_outcome = "HEALTH_OUTCOME";
    private static final String education_provided = "EDU_PROVIDED";
    private static final String edu_goal_status = "EDU_GOAL_STATUS";
    private static final String education_outcome = "EDUCATION_OUTCOME";
    private static final String social_provided = "SOCIAL_PROVIDED";
    private static final String social_goal_status = "SOCIAL_GOAL_STATUS";
    private static final String social_outcome = "SOCIAL_OUTCOME";
    private static final String client_visit_id = "CLIENT_ID";

    public VisitManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public static VisitManager getInstance(Context context) {
        if (instance == null) {
            instance = new VisitManager(context);
        }

        return instance;
    }

    public Visit getVisitByPosition(int position){
        return visits.get(position);
    }

    public List<Visit> getVisits(){
        return visits;
    }

    public Visit getVisitById(long id){
        for(Visit visit : visits){
            if(visit.getVisit_id() == id){
                return visit;
            }
        }
        return new Visit();
    }


    public void updateList() {
        Cursor c = databaseHelper.getAllVisits();

        int idI = c.getColumnIndex(visit_id);
        int dateI = c.getColumnIndex(visit_date);
        int purposeI = c.getColumnIndex(visit_purpose);
        int if_cbrI = c.getColumnIndex(if_cbr);
        int locationI = c.getColumnIndex(visit_location);
        int villageNoI = c.getColumnIndex(visit_village_no);
        int healthProvidedI = c.getColumnIndex(health_provided);
        int healthGoalI = c.getColumnIndex(health_goal_status);
        int healthOutcomeI = c.getColumnIndex(health_outcome);
        int educationProvidedI = c.getColumnIndex(education_provided);
        int educationGoalI = c.getColumnIndex(edu_goal_status);
        int educationOutcomeI = c.getColumnIndex(education_outcome);
        int socialProvidedI = c.getColumnIndex(social_provided);
        int socialGoalI = c.getColumnIndex(social_goal_status);
        int socialOutcomeI = c.getColumnIndex(social_outcome);
        int client_visit_idI = c.getColumnIndex(client_visit_id);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            long id = c.getLong(idI);
            String date = c.getString(dateI);
            String purpose = c.getString(purposeI);
            ArrayList<String> if_cbr = new ArrayList<>(Arrays.asList(c.getString(if_cbrI).split(",")));
            String location = c.getString(locationI);
            int villageNumber = c.getInt(villageNoI);
            ArrayList<String> healthProvided = new ArrayList<>(Arrays.asList(c.getString(healthProvidedI).split(",")));
            String healthGoal = c.getString(healthGoalI);
            String healthOutcome = c.getString(healthOutcomeI);
            ArrayList<String> educationProvided = new ArrayList<>(Arrays.asList(c.getString(educationProvidedI).split(",")));
            String educationGoal = c.getString(educationGoalI);
            String educationOutcome = c.getString(educationOutcomeI);
            ArrayList<String> socialProvided = new ArrayList<>(Arrays.asList(c.getString(socialProvidedI).split(",")));
            String socialGoal = c.getString(socialGoalI);
            String socialOutcome = c.getString(socialOutcomeI);
            long client_visit_id = c.getLong(client_visit_idI);

            visits.add(new Visit(client_visit_id, purpose, if_cbr, date, location, villageNumber, healthProvided, healthGoal, healthOutcome,
                        socialProvided, socialGoal, socialOutcome, educationProvided, educationGoal, educationOutcome, id, 0));
        }
    }

    public void addVisit(Visit visit){
        visits.add(0, visit);
    }


    @NonNull
    @Override
    public Iterator<Visit> iterator() {
        return visits.iterator();
    }

    public List<Visit> getVisits(long id) {
        List<Visit> finalVisits = new ArrayList<>();

        for (Visit currentVisit : this.visits) {
            if(currentVisit.getClient_id() == id) {
                finalVisits.add(currentVisit);
            }
        }

        return finalVisits;
    }

    public void clear() {
        visits.clear();
    }

    public String getPreviousVisitOutcome(long clientId){
        List<Visit> allOtherVisits = new ArrayList<>();
        System.out.println(clientId);
        for(Visit currentVisit : this.visits){
            if(currentVisit.getClient_id() == clientId){
                allOtherVisits.add(currentVisit);
            }
        }
        System.out.println("SIZE OF allothervisits: " + allOtherVisits.size());
        if(allOtherVisits.isEmpty()){
            return "<b>Outcome Of Previous Visit:</b><br>There are no previous visits to display.";
        }

        Visit previousVisit = getPreviousVisit(allOtherVisits);
        String outcomeOfPrevious = getOutcome(previousVisit);
        return outcomeOfPrevious;
    }

    private Visit getPreviousVisit(List<Visit> allOtherVisits){
        long highestVisitID = -1;
        Visit previousVisit = allOtherVisits.get(0);
        for(Visit currentVisit : allOtherVisits){
            if(currentVisit.getClient_id() > highestVisitID){
                previousVisit = currentVisit;
            }
        }
        return previousVisit;
    }

    private String getOutcome(Visit currentVisit){
        String outcomeHeading = "<b>Outcome Of Previous Visit:</b> ";
        String healthOutcome = "<b>Health Outcome:</b> ";
        String educationOutcome = "<b>Education Outcome:</b> ";
        String socialOutcome = "<b>Social Status Outcome:</b> ";

        if(currentVisit.getHealthGoalMet().equals("Concluded")){
            healthOutcome += currentVisit.getHealthIfConcluded();
        }else{
            healthOutcome += currentVisit.getHealthGoalMet();
        }

        if(currentVisit.getSocialGoalMet().equals("Concluded")){
            socialOutcome += currentVisit.getSocialIfConcluded();
        }else{
            socialOutcome += currentVisit.getSocialGoalMet();
        }

        if(currentVisit.getEducationGoalMet().equals("Concluded")){
            educationOutcome += currentVisit.getEducationIfConcluded();
        }else{
            educationOutcome += currentVisit.getEducationGoalMet();
        }

        return outcomeHeading + "<br><br>" + healthOutcome + "<br><br>" + educationOutcome + "<br><br>" + socialOutcome;
    }
}
