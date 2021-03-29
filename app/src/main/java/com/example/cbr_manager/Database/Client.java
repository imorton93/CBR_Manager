package com.example.cbr_manager.Database;

import android.renderscript.Double4;

import java.util.ArrayList;

public class Client implements Comparable<Client>{
    //Saved answers to questions
    //all open to change depending on saving to database
    private long id;
    private Boolean consentToInterview;
    private String date;
    private String firstName;
    private String lastName;
    private int age = -1;
    private String gender;
    private String location;
    private int villageNumber = -1;
    private double latitude;
    private double longitude;
    private String contactPhoneNumber;
    private Boolean caregiverPresent;
    private String caregiverPhoneNumber;
    private ArrayList<String> disabilities = new ArrayList<>();
    private String otherExplanation;
    private String healthRate;
    private String healthRequire;
    private String healthIndividualGoal;
    private String educationRate;
    private String educationRequire;
    private String educationIndividualGoal;
    private String socialStatusRate;
    private int client_worker_id;

    private byte[] photo;

    private String socialStatusRequire;
    private String socialStatusIndividualGoal;

    private int priority = 0; // For dashboard to calculate priority of a client

    private int isSynced = 0; // default 0, changes to 1 when sent to server




    public Client(Boolean consentToInterview, String date, String firstName, String lastName,

                  int age, String gender, String location, int villageNumber, double latitude, double longitude, String contactPhoneNumber,
                  Boolean caregiverPresent, String caregiverPhoneNumber, byte[] photo, ArrayList<String> disabilities,
                  String healthRate, String healthRequire, String healthIndividualGoal, String educationRate,
                  String educationRequire, String educationIndividualGoal, String socialStatusRate,
                  String socialStatusRequire, String socialStatusIndividualGoal, int client_worker_id, int isSynced) {

        this.consentToInterview = consentToInterview;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.villageNumber = villageNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactPhoneNumber = contactPhoneNumber;
        this.caregiverPresent = caregiverPresent;
        this.caregiverPhoneNumber = caregiverPhoneNumber;
        this.disabilities = disabilities;
        this.healthRate = healthRate;
        this.healthRequire = healthRequire;
        this.healthIndividualGoal = healthIndividualGoal;
        this.educationRate = educationRate;
        this.educationRequire = educationRequire;
        this.educationIndividualGoal = educationIndividualGoal;
        this.socialStatusRate = socialStatusRate;
        this.socialStatusRequire = socialStatusRequire;
        this.socialStatusIndividualGoal = socialStatusIndividualGoal;
        this.client_worker_id = client_worker_id;
        this.photo = photo;
        this.isSynced = isSynced;
    }

    public Client() {
    }

    public String getOtherExplanation() {
        return otherExplanation;
    }

    public void setOtherExplanation(String otherExplanation) {
        this.otherExplanation = otherExplanation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getConsentToInterview() {
        return consentToInterview;
    }

    public void setConsentToInterview(Boolean consentToInterview) {
        this.consentToInterview = consentToInterview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getVillageNumber() {
        return villageNumber;
    }

    public void setVillageNumber(int villageNumber) {
        this.villageNumber = villageNumber;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public Boolean getCaregiverPresent() {
        return caregiverPresent;
    }

    public void setCaregiverPresent(Boolean caregiverPresent) {
        this.caregiverPresent = caregiverPresent;
    }

    public String getCaregiverPhoneNumber() {
        return caregiverPhoneNumber;
    }

    public void setCaregiverPhoneNumber(String caregiverPhoneNumber) {
        this.caregiverPhoneNumber = caregiverPhoneNumber;
    }

    public void addToDisabilities(String disability){
        disabilities.add(disability);
    }

    public void clearDisabilities(){
        disabilities.clear();
    }

    public void setDisabilities(ArrayList<String> disabilities) {
        this.disabilities = disabilities;
    }

    public ArrayList<String> getDisabilities() {
        return this.disabilities;

    }
    public String disabilitiesToString(){
        return android.text.TextUtils.join(",", disabilities);
    }
    public Boolean isDisabilitiesEmpty(){
        return disabilities.isEmpty();
    }

    public String getHealthRate() {
        return healthRate;
    }

    public void setHealthRate(String healthRate) {
        this.healthRate = healthRate;
    }

    public String getHealthRequire() {
        return healthRequire;
    }

    public void setHealthRequire(String healthRequire) {
        this.healthRequire = healthRequire;
    }

    public String getHealthIndividualGoal() {
        return healthIndividualGoal;
    }

    public void setHealthIndividualGoal(String healthIndividualGoal) {
        this.healthIndividualGoal = healthIndividualGoal;
    }

    public String getEducationRate() {
        return educationRate;
    }

    public void setEducationRate(String educationRate) {
        this.educationRate = educationRate;
    }

    public String getEducationRequire() {
        return educationRequire;
    }

    public void setEducationRequire(String educationRequire) {
        this.educationRequire = educationRequire;
    }

    public String getEducationIndividualGoal() {
        return educationIndividualGoal;
    }

    public void setEducationIndividualGoal(String educationIndividualGoal) {
        this.educationIndividualGoal = educationIndividualGoal;
    }

    public String getSocialStatusRate() {
        return socialStatusRate;
    }

    public void setSocialStatusRate(String socialStatusRate) {
        this.socialStatusRate = socialStatusRate;
    }

    public String getSocialStatusRequire() {
        return socialStatusRequire;
    }

    public void setSocialStatusRequire(String socialStatusRequire) {
        this.socialStatusRequire = socialStatusRequire;
    }

    public String getSocialStatusIndividualGoal() {
        return socialStatusIndividualGoal;
    }

    public void setSocialStatusIndividualGoal(String socialStatusIndividualGoal) {
        this.socialStatusIndividualGoal = socialStatusIndividualGoal;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Client client) {
        return Integer.compare(this.priority, client.getPriority());

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public int getClient_worker_id() {
        return client_worker_id;
    }

    public void setClient_worker_id(int client_worker_id) {
        this.client_worker_id = client_worker_id;
    }
}

