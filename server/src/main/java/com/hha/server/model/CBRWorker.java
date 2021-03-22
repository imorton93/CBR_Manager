package com.hha.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "WORKER_DATA")
public class CBRWorker {
    @Column
    @JsonProperty("FIRST_NAME")
    private String FIRST_NAME;

    @Column
    @JsonProperty("LAST_NAME")
    private String LAST_NAME;

    @Column
    @JsonProperty("USERNAME")
    private String USERNAME;

    @Column
    @JsonProperty("PASSWORD")
    private String PASSWORD;

    @Id
    @Column
    @JsonProperty("ID")
    private String ID;

    public CBRWorker(String ID, String FIRST_NAME, String LAST_NAME, String USERNAME, String PASSWORD) {
        this.ID = ID;
        this.FIRST_NAME = FIRST_NAME;
        this.LAST_NAME = LAST_NAME;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public CBRWorker(){
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

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
