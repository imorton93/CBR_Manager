package com.hha.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "ADMIN_MESSAGES")
public class AdminMessage {
    @Id
    @JsonProperty("ID")
    private String messageID;

    @Column
    @JsonProperty("ADMIN_ID")
    private String adminID;

    @Column
    @JsonProperty("TITLE")
    private String title;

    @Column
    @JsonProperty("DATE")
    private String date;

    @Column
    @JsonProperty("LOCATION")
    private String location;

    @Column
    @JsonProperty("MESSAGE")
    private String message;

    @Column
    @JsonProperty("IS_SYNCED")
    private String isSynced;

    @Column
    @JsonProperty("IS_VIEWED")
    private String viewedStatus;

    public AdminMessage() {
    }

    public AdminMessage(String messageID, String adminID, String title, String date, String location, String message, String isSynced, String viewedStatus) {
        this.messageID = messageID;
        this.adminID = adminID;
        this.title = title;
        this.date = date;
        this.location = location;
        this.message = message;
        this.isSynced = isSynced;
        this.viewedStatus = viewedStatus;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
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

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public String getViewedStatus() {
        return viewedStatus;
    }

    public void setViewedStatus(String viewedStatus) {
        this.viewedStatus = viewedStatus;
    }
}
