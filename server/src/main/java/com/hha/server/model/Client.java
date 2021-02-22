package com.hha.server.model;

import java.util.ArrayList;

public class Client {
    //Saved answers to questions
    //all open to change depending on saving to database
    private Boolean consentToInterview;
    private String date;
    private String firstName;
    private String lastName;
    private int age = -1;
    private String gender;
    private String location;
    private int villageNumber = -1;
    private String contactPhoneNumber;
    private Boolean caregiverPresent;
    private String caregiverPhoneNumber;
    private ArrayList<String> disabilities = new ArrayList<>();
    private String healthRate;
    private String healthRequire;
    private String healthIndividualGoal;
    private String educationRate;
    private String educationRequire;
    private String educationIndividualGoal;
    private String socialStatusRate;

    public Client(Boolean consentToInterview, String date, String firstName, String lastName, int age, String gender, String location, int villageNumber, String contactPhoneNumber, Boolean caregiverPresent, String caregiverPhoneNumber, ArrayList<String> disabilities, String healthRate, String healthRequire, String healthIndividualGoal, String educationRate, String educationRequire, String educationIndividualGoal, String socialStatusRate, String socialStatusRequire, String socialStatusIndividualGoal) {
        this.consentToInterview = consentToInterview;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.villageNumber = villageNumber;
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
    }

    private String socialStatusRequire;
    private String socialStatusIndividualGoal;

    public Client() {
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

    public ArrayList<String> getDisabilities() {
        return this.disabilities;

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
}
