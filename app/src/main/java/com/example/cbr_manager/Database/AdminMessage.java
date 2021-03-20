package com.example.cbr_manager.Database;

public class AdminMessage {
    private int workerID;
    private String date;
    private String message;
    private int isSynced = 0;
    private int viewedStatus = 0;

    public AdminMessage() {
    }

    public AdminMessage(int workerID, String date, String message, int isSynced, int viewedStatus) {
        this.workerID = workerID;
        this.date = date;
        this.message = message;
        this.isSynced = isSynced;
        this.viewedStatus = viewedStatus;
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
