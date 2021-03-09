package com.example.cbr_manager.Database;

public class CBRWorker {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int id;
    private boolean is_admin;

    public CBRWorker(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        is_admin = false;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setIs_admin(boolean a){
        is_admin = a;
    }

    public boolean getIs_admin() {
        return is_admin;
    }

}
