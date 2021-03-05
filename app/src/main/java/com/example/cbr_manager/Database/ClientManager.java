package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cbr_manager.UI.ClientListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import static android.content.ContentValues.TAG;

public class ClientManager implements Iterable<Client>{

    private List<Client> clients = new ArrayList<>();
    private static ClientManager instance;
    private DatabaseHelper databaseHelper;

    private static final String client_id = "ID";
    private static final String client_consent = "CONSENT";
    private static final String client_date = "DATE";
    private static final String client_first_name = "FIRST_NAME";
    private static final String client_last_name = "LAST_NAME";
    private static final String client_age = "AGE";
    private static final String client_gender = "GENDER";
    private static final String client_village_no = "VILLAGE_NUMBER";
    private static final String client_location = "LOCATION";
    private static final String client_contact = "CONTACT";
    private static final String client_caregiver_presence = "CAREGIVER_PRESENCE";
    private static final String client_caregiver_number = "CAREGIVER_NUMBER";
    private static final String client_disability = "DISABILITY";
    private static final String client_heath_rate = "HEALTH_RATE";
    private static final String client_health_requirement = "HEALTH_REQUIREMENT";
    private static final String client_health_goal = "HEALTH_GOAL";
    private static final String client_education_rate = "EDUCATION_RATE";
    private static final String client_education_requirement = "EDUCATION_REQUIRE";
    private static final String client_education_goal = "EDUCATION_GOAL";
    private static final String client_social_rate = "SOCIAL_RATE";
    private static final String client_social_requirement= "SOCIAL_REQUIREMENT";
    private static final String client_social_goal = "SOCIAL_GOAL";

    public ClientManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public static ClientManager getInstance(Context context) {
        if (instance == null) {
            instance = new ClientManager(context);
        }

        return instance;
    }

    public Client getClientByPosition(int position){
        return clients.get(position);
    }

    public void updateList() {

        Cursor c = databaseHelper.getAllRows();

        int idI = c.getColumnIndex(client_id);
        int consentI = c.getColumnIndex(client_consent);
        int dateI = c.getColumnIndex(client_date);
        int firstNameI = c.getColumnIndex(client_first_name);
        int lastNameI = c.getColumnIndex(client_last_name);
        int ageI = c.getColumnIndex(client_age);
        int genderI = c.getColumnIndex(client_gender);
        int villageNoI = c.getColumnIndex(client_village_no);
        int villageLocationI = c.getColumnIndex(client_location);
        int contactI = c.getColumnIndex(client_contact);
        int caregiverPresentI = c.getColumnIndex(client_caregiver_presence);
        int caregiverNumI = c.getColumnIndex(client_caregiver_number);
        int disabilityI = c.getColumnIndex(client_disability);
        int healthRateI = c.getColumnIndex(client_heath_rate);
        int healthReqI = c.getColumnIndex(client_health_requirement);
        int healthGoalI = c.getColumnIndex(client_health_goal);
        int educationRateI = c.getColumnIndex(client_education_rate);
        int educationReqI = c.getColumnIndex(client_education_requirement);
        int educationGoalI = c.getColumnIndex(client_education_goal);
        int socialRateI = c.getColumnIndex(client_social_rate);
        int socialReqI = c.getColumnIndex(client_social_requirement);
        int socialGoalI = c.getColumnIndex(client_social_goal);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

            long id = c.getLong(idI);
            Boolean consent = (c.getInt(consentI) > 0);
            String date = c.getString(dateI);
            String firstName = c.getString(firstNameI);
            String lastName = c.getString(lastNameI);
            int age = c.getInt(ageI);
            String gender = c.getString(genderI);
            String location = c.getString(villageLocationI);
            int villageNumber = c.getInt(villageNoI);
            String contactPhoneNumber = c.getString(contactI);
            Boolean caregiverPresent = (c.getInt(caregiverPresentI) > 0);
            String caregiverPhoneNumber = c.getString(caregiverNumI);
            ArrayList<String> disabilities = new ArrayList<>(Arrays.asList(c.getString(disabilityI).split(",")));
            String healthRate = c.getString(healthRateI);
            String healthRequire = c.getString(healthReqI);
            String healthIndividualGoal = c.getString(healthGoalI);
            String educationRate = c.getString(educationRateI);
            String educationRequire = c.getString(educationReqI);
            String educationIndividualGoal = c.getString(educationGoalI);
            String socialStatusRate = c.getString(socialRateI);
            String socialStatusRequire = c.getString(socialReqI);
            String socialStatusIndividualGoal = c.getString(socialGoalI);

            Client newClient = new Client(consent, date, firstName, lastName, age, gender, location,
                    villageNumber, contactPhoneNumber, caregiverPresent, caregiverPhoneNumber, disabilities,
                    healthRate, healthRequire, healthIndividualGoal, educationRate, educationRequire,
                    educationIndividualGoal, socialStatusRate, socialStatusRequire, socialStatusIndividualGoal);

            newClient.setId(id);

            clients.add(newClient);
        }
    }

    @NonNull
    @Override
    public Iterator<Client> iterator() {
        return clients.iterator();
    }

    public List<Client> getClients() {
        return clients;
    }

    public void clear() {
        clients.clear();
    }
}
