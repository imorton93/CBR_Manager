package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClientManager implements Iterable<Client>{

    private List<Client> clients = new ArrayList<>();
    private static ClientManager instance;
    private DatabaseHelper databaseHelper;
    private static final int NUMBER_OF_CLIENTS = 10;

    private static final String client_id = "ID";
    private static final String client_consent = "CONSENT";
    private static final String client_date = "DATE";
    private static final String client_first_name = "FIRST_NAME";
    private static final String client_last_name = "LAST_NAME";
    private static final String client_age = "AGE";
    private static final String client_gender = "GENDER";
    private static final String client_village_no = "VILLAGE_NUMBER";
    private static final String client_location = "LOCATION";
    private static final String client_latitude = "LATITUDE";
    private static final String client_longitude = "LONGITUDE";
    private static final String client_contact = "CONTACT";
    private static final String client_caregiver_presence = "CAREGIVER_PRESENCE";
    private static final String client_caregiver_number = "CAREGIVER_NUMBER";
    private static final String client_photo = "PHOTO";
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
    private static final String client_worker_id = "WORKER_ID";

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

    // TODO: I don't like how I did this. The id should in theory always exists but in
    // TODO: the case it doesn't the app display null because a new empty client will be returned.
    public Client getClientById(long id){
        for(Client client : clients){
            if(client.getId() == id){
                return client;
            }
        }
        return new Client();
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
        int latitudeI = c.getColumnIndex(client_latitude);
        int longitudeI = c.getColumnIndex(client_longitude);
        int contactI = c.getColumnIndex(client_contact);
        int caregiverPresentI = c.getColumnIndex(client_caregiver_presence);
        int photoI = c.getColumnIndex(client_photo);
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
        int client_worker_idI = c.getColumnIndex(client_worker_id);

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
            double latitude = c.getDouble(latitudeI);
            double longitude = c.getDouble(longitudeI);
            String contactPhoneNumber = c.getString(contactI);
            Boolean caregiverPresent = (c.getInt(caregiverPresentI) > 0);
            String caregiverPhoneNumber = c.getString(caregiverNumI);
            byte[] photo = c.getBlob(photoI);
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
            int workerId = c.getInt(client_worker_idI);

            Client newClient = new Client(consent, date, firstName, lastName, age, gender, location,
                    villageNumber, latitude, longitude, contactPhoneNumber, caregiverPresent, caregiverPhoneNumber,photo, disabilities,
                    healthRate, healthRequire, healthIndividualGoal, educationRate, educationRequire,
                    educationIndividualGoal, socialStatusRate, socialStatusRequire, socialStatusIndividualGoal, workerId, 0);

            newClient.setId(id);

            clients.add(newClient);
        }
    }

    public void addClient(Client client){
        clients.add(0, client);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Client> getSearchedClients(String first_name, String last_name, String village,
                                           String section, String village_num){
        if(first_name.isEmpty() && last_name.isEmpty() && village.equals("Villages") &&
                section.equals("Overall") && village_num.isEmpty()){
            return this.clients;
        }

        boolean village_num_exists = true;
        int village_number = -999;

        try{
            village_number = Integer.parseInt(village_num);
        }catch(NumberFormatException e){
            village_num_exists = false;
        }

        return getSearchedClientsHelper(first_name, last_name, village, section, village_number, village_num_exists);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Client> getSearchedClientsHelper(String first_name, String last_name, String village,
                                                  String section, int village_number, boolean village_num_exists){
        List<Client> searched_clients = new ArrayList<>();
        searched_clients.addAll(this.clients);
        if(!first_name.isEmpty()) {
            searched_clients.removeIf(i -> !i.getFirstName().equals(first_name));
        }
        if(!last_name.isEmpty()){
            searched_clients.removeIf(i -> !i.getLastName().equals(last_name));
        }
        if(!village.equals("Villages")){
            searched_clients.removeIf(i -> !i.getLocation().equals(village));
        }
        if(village_num_exists){
            searched_clients.removeIf(i -> i.getVillageNumber() != village_number);
        }
        if(section.equals("Critical Health")){
            searched_clients.removeIf(i -> !i.getHealthRate().equals("Critical Risk"));
        }
        if(section.equals("Critical Education")) {
            searched_clients.removeIf(i -> !i.getEducationRate().equals("Critical Risk"));
        }
        if(section.equals("Critical Social Status")) {
            searched_clients.removeIf(i -> !i.getSocialStatusRate().equals("Critical Risk"));
        }
        return searched_clients;
    }

    public List<Client> getDashboardSearchedClients(String village, String section, String village_num){
        if(village.equals("Villages") && section.equals("Overall") && village_num.isEmpty()){
            return getHighPriorityClients();
        }

        boolean village_num_exists = true;
        int village_number = -999;

        try{
            village_number = Integer.parseInt(village_num);
        }catch(NumberFormatException e){
            village_num_exists = false;
        }

        return getDashboardSearchedClientsHelper(village, section, village_number, village_num_exists);
    }

    private List<Client> getDashboardSearchedClientsHelper(String village, String section, int village_number, boolean village_num_exists){
        List<Client> priority_clients = new ArrayList<>(clients);
        calculatePriorityOfClients();
        List<Client> priority_clients_filtered;

        if(!village.isEmpty() && village_num_exists) {
            priority_clients_filtered = filterByVillage(village, village_number, priority_clients, village_num_exists);
        }else{
            priority_clients_filtered = new ArrayList<>(clients);
        }

        if(section.equals("Critical Health")){
            filterByHealth(priority_clients_filtered);
        }else if(section.equals("Critical Education")){
            filterByEducation(priority_clients_filtered);
        }else if(section.equals("Critical Social Status")){
            filterBySocial(priority_clients_filtered);
        }

        Collections.sort(priority_clients_filtered, Collections.reverseOrder());
        int min = Math.min(priority_clients_filtered.size(), NUMBER_OF_CLIENTS);

        return priority_clients_filtered.subList(0, min);
    }

    private List<Client> filterByVillage(String village, int village_number,
                                 List<Client> priority_clients, boolean village_num_exists){
        List<Client> priority_clients_filtered = new ArrayList<>();

        for(Client client : priority_clients){
            if(!village.isEmpty() && client.getLocation().equals(village)){
                priority_clients_filtered.add(client);
            }else if(village_num_exists && client.getVillageNumber() == village_number){
                priority_clients_filtered.add(client);
            }
        }
        return priority_clients_filtered;
    }

    private void filterByHealth(List<Client> priority_clients){
        for(Client client : priority_clients){
            client.setPriority(calculatePriority(client.getHealthRate()));
        }
    }

    private void filterByEducation(List<Client> priority_clients){
        for(Client client : priority_clients){
            client.setPriority(calculatePriority(client.getEducationRate()));
        }
    }

    private void filterBySocial(List<Client> priority_clients){
        for(Client client : priority_clients){
            client.setPriority(calculatePriority(client.getSocialStatusRate()));
        }
    }

    public List<Client> getHighPriorityClients(){
        List<Client> priority_clients = new ArrayList<>(clients);
        calculatePriorityOfClients();
        Collections.sort(priority_clients, Collections.reverseOrder());
        int min = Math.min(priority_clients.size(), NUMBER_OF_CLIENTS);
        return priority_clients.subList(0, min);
    }

    private void calculatePriorityOfClients(){
        for(Client client : clients){
            client.setPriority(calculatePriority(client.getHealthRate()));
            client.setPriority( (client.getPriority() + calculatePriority(client.getSocialStatusRate()) ));
            client.setPriority( (client.getPriority() + calculatePriority(client.getEducationRate()) ));
        }
    }

    private int calculatePriority(String risk_level){
        int priorityScore = 0;
        if(risk_level.equals("Critical Risk")){
            priorityScore = 4;
        }else if(risk_level.equals("High Risk")){
            priorityScore = 3;
        }else if(risk_level.equals("Medium Risk")){
            priorityScore = 2;
        }else if(risk_level.equals("Low Risk")){
            priorityScore = 1;
        }
        return priorityScore;
    }

    public long getClientIndexByLatLng(LatLng positionLatLng) {
        for (Client client : clients) {
            LatLng currentLatLng = new LatLng(client.getLatitude(), client.getLongitude());
            if (positionLatLng.equals(currentLatLng)) {
                return client.getId();
            }
        }

        return -1;
    }

}
