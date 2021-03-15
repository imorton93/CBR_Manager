package com.hha.server.model;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "CLIENT_REFERRALS")
public class Referral {
    @Id
    @Column
    @JsonProperty("ID")
    private Long ID;

    @Column
    @JsonProperty("SERVICE_REQ")
    private String SERVICE_REQ;

    @Column
    @JsonProperty("REFERRAL_PHOTO")
    private byte[] REFERRAL_PHOTO;

    @Column
    @JsonProperty("BASIC_OR_INTER")
    private String BASIC_OR_INTER;

    @Column
    @JsonProperty("HIP_WIDTH")
    private double HIP_WIDTH;

    @Column
    @JsonProperty("HAS_WHEEL_CHAIR")
    private Boolean HAS_WHEEL_CHAIR;

    @Column
    @JsonProperty("WHEEL_CHAIR_REPARABLE")
    private Boolean WHEEL_CHAIR_REPARABLE;

    @Column
    @JsonProperty("BRING_TO_CENTRE")
    private Boolean BRING_TO_CENTRE;

    @Column
    @JsonProperty("CONDITIONS")
    private ArrayList<String> CONDITIONS = new ArrayList<>();

    @Column
    @JsonProperty("INJURY_LOCATION_KNEE")
    private String INJURY_LOCATION_KNEE;

    @Column
    @JsonProperty("INJURY_LOCATION_ELBOW")
    private String INJURY_LOCATION_ELBOW;

    @Column
    @JsonProperty("STATUS")
    private String STATUS;

    @Column
    @JsonProperty("OUTCOME")
    private String OUTCOME;

    @Column
    @JsonProperty("CLIENT_ID")
    private String CLIENT_ID;

    @Column
    @JsonProperty("IS_SYNCED")
    private String isSynced;

    public Referral() {

    }

    public Referral(String SERVICE_REQ, byte[] REFERRAL_PHOTO, String BASIC_OR_INTER, double HIP_WIDTH, Boolean HAS_WHEEL_CHAIR, Boolean WHEEL_CHAIR_REPARABLE, Boolean BRING_TO_CENTRE, ArrayList<String> CONDITIONS, String INJURY_LOCATION_KNEE, String INJURY_LOCATION_ELBOW, String STATUS, String OUTCOME, String CLIENT_ID, String isSynced) {
        this.SERVICE_REQ = SERVICE_REQ;
        this.REFERRAL_PHOTO = REFERRAL_PHOTO;
        this.BASIC_OR_INTER = BASIC_OR_INTER;
        this.HIP_WIDTH = HIP_WIDTH;
        this.HAS_WHEEL_CHAIR = HAS_WHEEL_CHAIR;
        this.WHEEL_CHAIR_REPARABLE = WHEEL_CHAIR_REPARABLE;
        this.BRING_TO_CENTRE = BRING_TO_CENTRE;
        this.CONDITIONS = CONDITIONS;
        this.INJURY_LOCATION_KNEE = INJURY_LOCATION_KNEE;
        this.INJURY_LOCATION_ELBOW = INJURY_LOCATION_ELBOW;
        this.STATUS = STATUS;
        this.OUTCOME = OUTCOME;
        this.CLIENT_ID = CLIENT_ID;
        this.isSynced = isSynced;
    }

    public String getSERVICE_REQ() {
        return SERVICE_REQ;
    }

    public void setSERVICE_REQ(String SERVICE_REQ) {
        this.SERVICE_REQ = SERVICE_REQ;
    }

    public byte[] getREFERRAL_PHOTO() {
        return REFERRAL_PHOTO;
    }

    public void setREFERRAL_PHOTO(byte[] REFERRAL_PHOTO) {
        this.REFERRAL_PHOTO = REFERRAL_PHOTO;
    }

    public String getBASIC_OR_INTER() {
        return BASIC_OR_INTER;
    }

    public void setBASIC_OR_INTER(String BASIC_OR_INTER) {
        this.BASIC_OR_INTER = BASIC_OR_INTER;
    }

    public double getHIP_WIDTH() {
        return HIP_WIDTH;
    }

    public void setHIP_WIDTH(double HIP_WIDTH) {
        this.HIP_WIDTH = HIP_WIDTH;
    }

    public Boolean getHAS_WHEEL_CHAIR() {
        return HAS_WHEEL_CHAIR;
    }

    public void setHAS_WHEEL_CHAIR(Boolean HAS_WHEEL_CHAIR) {
        this.HAS_WHEEL_CHAIR = HAS_WHEEL_CHAIR;
    }

    public Boolean getWHEEL_CHAIR_REPARABLE() {
        return WHEEL_CHAIR_REPARABLE;
    }

    public void setWHEEL_CHAIR_REPARABLE(Boolean WHEEL_CHAIR_REPARABLE) {
        this.WHEEL_CHAIR_REPARABLE = WHEEL_CHAIR_REPARABLE;
    }

    public Boolean getBRING_TO_CENTRE() {
        return BRING_TO_CENTRE;
    }

    public void setBRING_TO_CENTRE(Boolean BRING_TO_CENTRE) {
        this.BRING_TO_CENTRE = BRING_TO_CENTRE;
    }

    public ArrayList<String> getCONDITIONS() {
        return CONDITIONS;
    }

    public void setCONDITIONS(ArrayList<String> CONDITIONS) {
        this.CONDITIONS = CONDITIONS;
    }

    public String getINJURY_LOCATION_KNEE() {
        return INJURY_LOCATION_KNEE;
    }

    public void setINJURY_LOCATION_KNEE(String INJURY_LOCATION_KNEE) {
        this.INJURY_LOCATION_KNEE = INJURY_LOCATION_KNEE;
    }

    public String getINJURY_LOCATION_ELBOW() {
        return INJURY_LOCATION_ELBOW;
    }

    public void setINJURY_LOCATION_ELBOW(String INJURY_LOCATION_ELBOW) {
        this.INJURY_LOCATION_ELBOW = INJURY_LOCATION_ELBOW;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getOUTCOME() {
        return OUTCOME;
    }

    public void setOUTCOME(String OUTCOME) {
        this.OUTCOME = OUTCOME;
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }
}