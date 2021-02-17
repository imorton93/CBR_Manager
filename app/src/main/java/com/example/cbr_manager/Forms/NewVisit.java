package com.example.cbr_manager.Forms;

import java.util.ArrayList;

public class NewVisit {

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

    public NewVisit() {
    }

    private class Provided{
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

    public void setIfCbr(ArrayList<String> ifCbr) {
        this.ifCbr = ifCbr;
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

    public int healthProvidedSize(){
        return healthProvided.size();
    }

    public String getHealthProvidedCheckboxAt(int index){
        return healthProvided.get(index).getCheckBox();
    }

    public String getHealthProvidedExplanationAt(int index){
        return healthProvided.get(index).getExplanation();
    }

    public void addSocialProvided(String checkBox, String explanation){
        Provided provided = new Provided(checkBox, explanation);
        socialProvided.add(provided);
    }

    public int socialProvidedSize(){
        return socialProvided.size();
    }

    public String getSocialProvidedCheckboxAt(int index){
        return socialProvided.get(index).getCheckBox();
    }

    public String getSocialProvidedExplanationAt(int index){
        return socialProvided.get(index).getExplanation();
    }


    public void addeducationProvided(String checkBox, String explanation){
        Provided provided = new Provided(checkBox, explanation);
        educationProvided.add(provided);
    }

    public int educationProvidedSize(){
        return educationProvided.size();
    }

    public String getEducationProvidedCheckboxAt(int index){
        return educationProvided.get(index).getCheckBox();
    }

    public String getEducationProvidedExplanationAt(int index){
        return educationProvided.get(index).getExplanation();
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
}
