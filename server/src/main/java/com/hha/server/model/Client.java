package com.hha.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT_DATA")
public class Client {
    @Id
    @Column
    @JsonProperty("ID")
    private String ID;

    @Column
    @JsonProperty("CONSENT")
    private String CONSENT;

    @Column
    @JsonProperty("DATE")
    private String DATE;

    @Column
    @JsonProperty("FIRST_NAME")
    private String FIRST_NAME;

    @Column
    @JsonProperty("LAST_NAME")
    private String LAST_NAME;

    @Column
    @JsonProperty("AGE")
    private String AGE;

    @Column
    @JsonProperty("GENDER")
    private String GENDER;

    @Column
    @JsonProperty("LOCATION")
    private String LOCATION;

    @Column
    @JsonProperty("VILLAGE_NUMBER")
    private String VILLAGE_NUMBER;

    @Column
    @JsonProperty("CONTACT")
    private String CONTACT;

    @Column
    @JsonProperty("CAREGIVER_PRESENCE")
    private String CAREGIVER_PRESENCE;

    @Column
    @JsonProperty("CAREGIVER_NUMBER")
    private String CAREGIVER_NUMBER;

    @Column
    @JsonProperty("DISABILITY")
    private String DISABILITY;

    @Column
    @JsonProperty("HEALTH_RATE")
    private String HEALTH_RATE;

    @Column
    @JsonProperty("HEALTH_REQUIREMENT")
    private String HEALTH_REQUIREMENT;

    @Column
    @JsonProperty("HEALTH_GOAL")
    private String HEALTH_GOAL;

    @Column
    @JsonProperty("EDUCATION_RATE")
    private String EDUCATION_RATE;

    @Column
    @JsonProperty("EDUCATION_REQUIRE")
    private String EDUCATION_REQUIRE;

    @Column
    @JsonProperty("EDUCATION_GOAL")
    private String EDUCATION_GOAL;

    @Column
    @JsonProperty("SOCIAL_RATE")
    private String SOCIAL_RATE;

    @Column
    @JsonProperty("SOCIAL_REQUIREMENT")
    private String SOCIAL_REQUIREMENT;

    @Column
    @JsonProperty("SOCIAL_GOAL")
    private String SOCIAL_GOAL;

    @Column
    @JsonProperty("LATITUDE")
    private String LATITUDE;

    @Column
    @JsonProperty("LONGITUDE")
    private String LONGITUDE;

    @Column
    @JsonProperty("WORKER_ID")
    private String WORKER_ID;

    @Column
    @JsonProperty("IS_SYNCED")
    private String IS_SYNCED;


    public Client() {}

    public Client(String ID, String CONSENT, String DATE, String FIRST_NAME, String LAST_NAME, String AGE, String GENDER, String LOCATION, String VILLAGE_NUMBER, String CONTACT, String CAREGIVER_PRESENCE, String CAREGIVER_NUMBER, String DISABILITY, String HEALTH_RATE, String HEALTH_REQUIREMENT, String HEALTH_GOAL, String EDUCATION_RATE, String EDUCATION_REQUIRE, String EDUCATION_GOAL, String SOCIAL_RATE, String SOCIAL_REQUIREMENT, String SOCIAL_GOAL, String LATITUDE, String LONGITUDE, String WORKER_ID, String IS_SYNCED) {
        this.ID = ID;
        this.CONSENT = CONSENT;
        this.DATE = DATE;
        this.FIRST_NAME = FIRST_NAME;
        this.LAST_NAME = LAST_NAME;
        this.AGE = AGE;
        this.GENDER = GENDER;
        this.LOCATION = LOCATION;
        this.VILLAGE_NUMBER = VILLAGE_NUMBER;
        this.CONTACT = CONTACT;
        this.CAREGIVER_PRESENCE = CAREGIVER_PRESENCE;
        this.CAREGIVER_NUMBER = CAREGIVER_NUMBER;
        this.DISABILITY = DISABILITY;
        this.HEALTH_RATE = HEALTH_RATE;
        this.HEALTH_REQUIREMENT = HEALTH_REQUIREMENT;
        this.HEALTH_GOAL = HEALTH_GOAL;
        this.EDUCATION_RATE = EDUCATION_RATE;
        this.EDUCATION_REQUIRE = EDUCATION_REQUIRE;
        this.EDUCATION_GOAL = EDUCATION_GOAL;
        this.SOCIAL_RATE = SOCIAL_RATE;
        this.SOCIAL_REQUIREMENT = SOCIAL_REQUIREMENT;
        this.SOCIAL_GOAL = SOCIAL_GOAL;
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
        this.WORKER_ID = WORKER_ID;
        this.IS_SYNCED = IS_SYNCED;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCONSENT() {
        return CONSENT;
    }

    public void setCONSENT(String CONSENT) {
        this.CONSENT = CONSENT;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getVILLAGE_NUMBER() {
        return VILLAGE_NUMBER;
    }

    public void setVILLAGE_NUMBER(String VILLAGE_NUMBER) {
        this.VILLAGE_NUMBER = VILLAGE_NUMBER;
    }

    public String getCONTACT() {
        return CONTACT;
    }

    public void setCONTACT(String CONTACT) {
        this.CONTACT = CONTACT;
    }

    public String getCAREGIVER_PRESENCE() {
        return CAREGIVER_PRESENCE;
    }

    public void setCAREGIVER_PRESENCE(String CAREGIVER_PRESENCE) {
        this.CAREGIVER_PRESENCE = CAREGIVER_PRESENCE;
    }

    public String getCAREGIVER_NUMBER() {
        return CAREGIVER_NUMBER;
    }

    public void setCAREGIVER_NUMBER(String CAREGIVER_NUMBER) {
        this.CAREGIVER_NUMBER = CAREGIVER_NUMBER;
    }

    public String getDISABILITY() {
        return DISABILITY;
    }

    public void setDISABILITY(String DISABILITY) {
        this.DISABILITY = DISABILITY;
    }

    public String getHEALTH_RATE() {
        return HEALTH_RATE;
    }

    public void setHEALTH_RATE(String HEALTH_RATE) {
        this.HEALTH_RATE = HEALTH_RATE;
    }

    public String getHEALTH_REQUIREMENT() {
        return HEALTH_REQUIREMENT;
    }

    public void setHEALTH_REQUIREMENT(String HEALTH_REQUIREMENT) {
        this.HEALTH_REQUIREMENT = HEALTH_REQUIREMENT;
    }

    public String getHEALTH_GOAL() {
        return HEALTH_GOAL;
    }

    public void setHEALTH_GOAL(String HEALTH_GOAL) {
        this.HEALTH_GOAL = HEALTH_GOAL;
    }

    public String getEDUCATION_RATE() {
        return EDUCATION_RATE;
    }

    public void setEDUCATION_RATE(String EDUCATION_RATE) {
        this.EDUCATION_RATE = EDUCATION_RATE;
    }

    public String getEDUCATION_REQUIRE() {
        return EDUCATION_REQUIRE;
    }

    public void setEDUCATION_REQUIRE(String EDUCATION_REQUIRE) {
        this.EDUCATION_REQUIRE = EDUCATION_REQUIRE;
    }

    public String getEDUCATION_GOAL() {
        return EDUCATION_GOAL;
    }

    public void setEDUCATION_GOAL(String EDUCATION_GOAL) {
        this.EDUCATION_GOAL = EDUCATION_GOAL;
    }

    public String getSOCIAL_RATE() {
        return SOCIAL_RATE;
    }

    public void setSOCIAL_RATE(String SOCIAL_RATE) {
        this.SOCIAL_RATE = SOCIAL_RATE;
    }

    public String getSOCIAL_REQUIREMENT() {
        return SOCIAL_REQUIREMENT;
    }

    public void setSOCIAL_REQUIREMENT(String SOCIAL_REQUIREMENT) {
        this.SOCIAL_REQUIREMENT = SOCIAL_REQUIREMENT;
    }

    public String getSOCIAL_GOAL() {
        return SOCIAL_GOAL;
    }

    public void setSOCIAL_GOAL(String SOCIAL_GOAL) {
        this.SOCIAL_GOAL = SOCIAL_GOAL;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getWORKER_ID() {
        return WORKER_ID;
    }

    public void setWORKER_ID(String WORKER_ID) {
        this.WORKER_ID = WORKER_ID;
    }

    public String getIS_SYNCED() {
        return IS_SYNCED;
    }

    public void setIS_SYNCED(String IS_SYNCED) {
        this.IS_SYNCED = IS_SYNCED;
    }
}
