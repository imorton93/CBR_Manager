package com.hha.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "CLIENT_VISITS")
public class Visit {
    @Id
    @Column
    @JsonProperty("ID")
    private String visit_id;

    @Column
    @JsonProperty("VISIT_DATE")
    private String date;

    @Column
    @JsonProperty("PURPOSE_OF_VISIT")
    private String purposeOfVisit;

    @Column
    @JsonProperty("IF_CBR")
    private String ifCbr;

    @Column
    @JsonProperty("LOCATION")
    private String location;

    @Column
    @JsonProperty("VILLAGE_NUMBER")
    private int villageNumber;

    @Column
    @JsonProperty("HEALTH_PROVIDED")
    private String healthProvided;

    @Column
    @JsonProperty("HEALTH_GOAL_STATUS")
    private String healthGoalMet;

    @Column
    @JsonProperty("HEALTH_OUTCOME")
    private String healthIfConcluded;

    @Column
    @JsonProperty("EDU_PROVIDED")
    private String educationProvided;

    @Column
    @JsonProperty("EDU_GOAL_STATUS")
    private String educationGoalMet;

    @Column
    @JsonProperty("EDUCATION_OUTCOME")
    private String educationIfConcluded;

    @Column
    @JsonProperty("SOCIAL_PROVIDED")
    private String socialProvided;

    @Column
    @JsonProperty("SOCIAL_GOAL_STATUS")
    private String socialGoalMet;

    @Column
    @JsonProperty("SOCIAL_OUTCOME")
    private String socialIfConcluded;

    @Column
    @JsonProperty("CLIENT_ID")
    private String client_id;

    @Column
    @JsonProperty("IS_SYNCED")
    private String isSynced;

    public Visit() {
    }

    public Visit(String visit_id, String purposeOfVisit, String ifCbr, String date, String location, int villageNumber, String healthProvided, String healthGoalMet, String healthIfConcluded, String socialProvided, String socialGoalMet, String socialIfConcluded, String educationProvided, String educationGoalMet, String educationIfConcluded, String client_id, String isSynced) {
        this.visit_id = visit_id;
        this.purposeOfVisit = purposeOfVisit;
        this.ifCbr = ifCbr;
        this.date = date;
        this.location = location;
        this.villageNumber = villageNumber;
        this.healthProvided = healthProvided;
        this.healthGoalMet = healthGoalMet;
        this.healthIfConcluded = healthIfConcluded;
        this.socialProvided = socialProvided;
        this.socialGoalMet = socialGoalMet;
        this.socialIfConcluded = socialIfConcluded;
        this.educationProvided = educationProvided;
        this.educationGoalMet = educationGoalMet;
        this.educationIfConcluded = educationIfConcluded;
        this.client_id = client_id;
        this.isSynced = isSynced;
    }

    public String getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(String visit_id) {
        this.visit_id = visit_id;
    }

    public String getPurposeOfVisit() {
        return purposeOfVisit;
    }

    public void setPurposeOfVisit(String purposeOfVisit) {
        this.purposeOfVisit = purposeOfVisit;
    }

    public String getIfCbr() {
        return ifCbr;
    }

    public void setIfCbr(String ifCbr) {
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

    public String getHealthProvided() {
        return healthProvided;
    }

    public void setHealthProvided(String healthProvided) {
        this.healthProvided = healthProvided;
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

    public String getSocialProvided() {
        return socialProvided;
    }

    public void setSocialProvided(String socialProvided) {
        this.socialProvided = socialProvided;
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

    public String getEducationProvided() {
        return educationProvided;
    }

    public void setEducationProvided(String educationProvided) {
        this.educationProvided = educationProvided;
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

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }
}