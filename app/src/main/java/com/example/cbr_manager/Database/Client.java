package com.example.cbr_manager.Database;

public class Client {

    private String firstName;
    private String lastName;
    private String location;
    private int villageNo;
    private int age;
    private String disabilityType;

    public Client() {
    }

    public Client(String firstName, String lastName, int age, int villageNo, String location, String disabilityType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.villageNo = villageNo;
        this.age = age;
        this.disabilityType = disabilityType;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getVillageNo() {
        return villageNo;
    }

    public void setVillageNo(int villageNo) {
        this.villageNo = villageNo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }


}