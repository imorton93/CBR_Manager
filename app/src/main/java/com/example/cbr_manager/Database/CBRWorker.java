package com.example.cbr_manager.Database;

public class CBRWorker {
    private String firstName;
    private String lastName;
    private String username;
    private String zone;
    private String password;
    private byte[] photo;
    private int id;
    private boolean is_admin;

    public CBRWorker(String firstName, String lastName, String username, String zone, String password,  byte[] photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.zone = zone;
        this.password = password;
        this.photo = photo;
        is_admin = false;
    }
    public CBRWorker(String firstName, String lastName, String username, String zone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.zone = zone;
        this.password = password;
        is_admin = false;
    }

    public CBRWorker(String firstName, String lastName, String username, String password, boolean is_admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.is_admin = is_admin;
    }



    public CBRWorker(){
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName(){
        return firstName +" " + lastName;
    }

    public void setWorkerId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setIs_admin(boolean a){
        is_admin = a;
    }

    public boolean getIs_admin() {
        return is_admin;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
