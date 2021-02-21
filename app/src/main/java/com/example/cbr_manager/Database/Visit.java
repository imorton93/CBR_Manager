package com.example.cbr_manager.Database;

import java.util.ArrayList;

public class Visit {

    private long client_id;
    private String purposeOfVisit;
    private ArrayList<String> ifCbr = new ArrayList<>();
    private String date;
    private String location;
    private int villageNumber;
    private ArrayList<Provided> healthProvided = new ArrayList<>();
    private String healthGoalMet;
    private  String healthIfConcluded;
    private ArrayList<Provided> socialProvided = new ArrayList<>();
    private String socialGoalMet;
    private String socialIfConcluded;
    private ArrayList<Provided> educationProvided = new ArrayList<>();
    private String educationGoalMet;
    private String educationIfConcluded;


    public Visit() {
    }

    public class Provided{
        private String checkBox;
        private String explanation;

        public Provided(String checkBox, String explanation) {
            this.checkBox = checkBox;
            this.explanation = explanation;
        }

        public String getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(String checkBox) {
            this.checkBox = checkBox;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }

        public String toString () { return checkBox + ": " + explanation; }
    }

    public String getPurposeOfVisit() {
        return purposeOfVisit;
    }

    public void setPurposeOfVisit(String purposeOfVisit) {
        this.purposeOfVisit = purposeOfVisit;
    }

    public ArrayList<String> getIfCbr() {
        return ifCbr;
    }

    public void addToIfCbr(String string){
        ifCbr.add(string);
    }

    public void clearIfCbr(){
        ifCbr.clear();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public void addHealthProvided(String checkBox, String explanation){
        Provided provided = new Provided(checkBox, explanation);
        healthProvided.add(provided);
    }

    public void clearHealthProvided(){
        healthProvided.clear();
    }

    public void addSocialProvided(String checkBox, String explanation){
        Provided provided = new Provided(checkBox, explanation);
        socialProvided.add(provided);
    }

    public void clearSocialProvided(){
        socialProvided.clear();
    }


    public void addEducationProvided(String checkBox, String explanation){
        Provided provided = new Provided(checkBox, explanation);
        educationProvided.add(provided);
    }

    public void clearEducationProvided(){
        educationProvided.clear();
    }

    public String getHealthGoalMet() {
        return healthGoalMet;
    }

    public void setHealthGoalMet(String healthGoalMet) {
        this.healthGoalMet = healthGoalMet;
    }

    public String getHealthIfConcluded() {
        return healthIfConcluded;
    }

    public void setHealthIfConcluded(String healthIfConcluded) {
        this.healthIfConcluded = healthIfConcluded;
    }

    public long getClientID() {
        return this.client_id;
    }

    public void setClientID(long client_id) {
        this.client_id = client_id;
    }

    public String getSocialGoalMet() {
        return socialGoalMet;
    }

    public void setSocialGoalMet(String socialGoalMet) {
        this.socialGoalMet = socialGoalMet;
    }

    public String getSocialIfConcluded() {
        return socialIfConcluded;
    }

    public void setSocialIfConcluded(String socialIfConcluded) {
        this.socialIfConcluded = socialIfConcluded;
    }


    public String getEducationGoalMet() {
        return educationGoalMet;
    }

    public void setEducationGoalMet(String educationGoalMet) {
        this.educationGoalMet = educationGoalMet;
    }

    public String getEducationIfConcluded() {
        return educationIfConcluded;
    }

    public void setEducationIfConcluded(String educationIfConcluded) {
        this.educationIfConcluded = educationIfConcluded;
    }

    public ArrayList<Provided> getHealthProvided() { return healthProvided; }

    //TODO - look over the ProvToString() functions
    public String healthProvToString() {
        String result = "";

        for (Provided prov : healthProvided) {
            result += prov.toString() + ", ";
        }

        return result;
    }

    public ArrayList<Provided> getSocialProvided() {
        return socialProvided;
    }

    public String socialProvToString() {
        String result = "";

        for (Provided prov : socialProvided) {
            result += prov.toString() + ", ";
        }

        return result;
    }

    public ArrayList<Provided> getEducationProvided() {
        return educationProvided;
    }

    public String eduProvToString() {
        String result = "";

        for (Provided prov : educationProvided) {
            result += prov.toString() + ", ";
        }

        return result;
    }
}
