package com.hha.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT_DATA")
public class Client {
    @Id
    @Column
    @JsonProperty("ID")
    private String id;

    @Column(length = 1)
    @JsonProperty("CONSENT")
    private String consent;

    @Column
    @JsonProperty("DATE")
    private String date;

    @Column
    @JsonProperty("FIRST_NAME")
    private String firstName;

    @Column
    @JsonProperty("LAST_NAME")
    private String lastName;

    @Column
    @JsonProperty("AGE")
    private String age;

    @Column
    @JsonProperty("GENDER")
    private String gender;

    @Column
    @JsonProperty("LOCATION")
    private String location;

    @Column
    @JsonProperty("VILLAGE_NUMBER")
    private String villageNumber;

    @Column
    @JsonProperty("CONTACT")
    private String contact;

    @Column
    @JsonProperty("CAREGIVER_PRESENCE")
    private String caregiverPresence;

    @Column
    @JsonProperty("CAREGIVER_NUMBER")
    private String caregiverNumber;

    @Column(length = 10485760)
    @JsonProperty("PHOTO")
    private String photo;

    @Column
    @JsonProperty("DISABILITY")
    private String disability;

    @Column
    @JsonProperty("HEALTH_RATE")
    private String healthRate;

    @Column
    @JsonProperty("HEALTH_REQUIREMENT")
    private String healthReq;

    @Column
    @JsonProperty("HEALTH_GOAL")
    private String healthGoal;

    @Column
    @JsonProperty("EDUCATION_RATE")
    private String educationRate;

    @Column
    @JsonProperty("EDUCATION_REQUIRE")
    private String educationReq;

    @Column
    @JsonProperty("EDUCATION_GOAL")
    private String educationGoal;

    @Column
    @JsonProperty("SOCIAL_RATE")
    private String socialRate;

    @Column
    @JsonProperty("SOCIAL_REQUIREMENT")
    private String socialReq;

    @Column
    @JsonProperty("SOCIAL_GOAL")
    private String socialGoal;

    @Column
    @JsonProperty("LATITUDE")
    private String latitude;

    @Column
    @JsonProperty("LONGITUDE")
    private String longitude;

    @Column
    @JsonProperty("WORKER_ID")
    private String workerID;

    @Column(length = 1)
    @JsonProperty("IS_SYNCED")
    private String isSynced;


    public Client() {}

    public Client(String id, String consent, String date, String firstName, String lastName, String age, String gender, String location, String villageNumber, String contact, String caregiverPresence, String caregiverNumber, String photo, String disability, String healthRate, String healthReq, String healthGoal, String educationRate, String educationReq, String educationGoal, String socialRate, String socialReq, String socialGoal, String latitude, String longitude, String workerID, String isSynced) {
        this.id = id;
        this.consent = consent;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.villageNumber = villageNumber;
        this.contact = contact;
        this.caregiverPresence = caregiverPresence;
        this.caregiverNumber = caregiverNumber;
        this.photo = photo;
        this.disability = disability;
        this.healthRate = healthRate;
        this.healthReq = healthReq;
        this.healthGoal = healthGoal;
        this.educationRate = educationRate;
        this.educationReq = educationReq;
        this.educationGoal = educationGoal;
        this.socialRate = socialRate;
        this.socialReq = socialReq;
        this.socialGoal = socialGoal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.workerID = workerID;
        this.isSynced = isSynced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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

    public String getVillageNumber() {
        return villageNumber;
    }

    public void setVillageNumber(String villageNumber) {
        this.villageNumber = villageNumber;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCaregiverPresence() {
        return caregiverPresence;
    }

    public void setCaregiverPresence(String caregiverPresence) {
        this.caregiverPresence = caregiverPresence;
    }

    public String getCaregiverNumber() {
        return caregiverNumber;
    }

    public void setCaregiverNumber(String caregiverNumber) {
        this.caregiverNumber = caregiverNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getHealthRate() {
        return healthRate;
    }

    public void setHealthRate(String healthRate) {
        this.healthRate = healthRate;
    }

    public String getHealthReq() {
        return healthReq;
    }

    public void setHealthReq(String healthReq) {
        this.healthReq = healthReq;
    }

    public String getHealthGoal() {
        return healthGoal;
    }

    public void setHealthGoal(String healthGoal) {
        this.healthGoal = healthGoal;
    }

    public String getEducationRate() {
        return educationRate;
    }

    public void setEducationRate(String educationRate) {
        this.educationRate = educationRate;
    }

    public String getEducationReq() {
        return educationReq;
    }

    public void setEducationReq(String educationReq) {
        this.educationReq = educationReq;
    }

    public String getEducationGoal() {
        return educationGoal;
    }

    public void setEducationGoal(String educationGoal) {
        this.educationGoal = educationGoal;
    }

    public String getSocialRate() {
        return socialRate;
    }

    public void setSocialRate(String socialRate) {
        this.socialRate = socialRate;
    }

    public String getSocialReq() {
        return socialReq;
    }

    public void setSocialReq(String socialReq) {
        this.socialReq = socialReq;
    }

    public String getSocialGoal() {
        return socialGoal;
    }

    public void setSocialGoal(String socialGoal) {
        this.socialGoal = socialGoal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }
}
