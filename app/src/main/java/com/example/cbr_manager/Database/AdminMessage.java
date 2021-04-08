package com.example.cbr_manager.Database;

public class AdminMessage {
    private int adminID;
    private Long id;
    private String title;
    private String date;
    private String location;
    private String message;
    private int isSynced = 0;
    private int viewedStatus = 0;

    public AdminMessage() {
    }

    public AdminMessage(int adminID, Long id, String title, String date, String location, String message, int isSynced, int viewedStatus) {
        this.adminID = adminID;
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.message = message;
        this.isSynced = isSynced;
        this.viewedStatus = viewedStatus;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public int getViewedStatus() {
        return viewedStatus;
    }

    public void setViewedStatus(int viewedStatus) {
        this.viewedStatus = viewedStatus;
    }
}
