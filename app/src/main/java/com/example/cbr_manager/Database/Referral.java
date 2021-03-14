package com.example.cbr_manager.Database;

import java.util.ArrayList;

public class Referral {
    public Long id;
    private String serviceReq;
    private byte[] referralPhoto;
    private String basicOrInter;
    private double hipWidth;
    private Boolean hasWheelchair;
    private Boolean wheelchairReparable;
    private Boolean bringToCentre;
    private ArrayList<String> conditions = new ArrayList<>();
    private String injuryLocationKnee;
    private String injuryLocationElbow;
    private String status;
    private String outcome;
    private Long clientID;

    private int isSynced = 0;

    public Referral() {

    }

    public Referral(String serviceReq, byte[] referralPhoto, String basicOrInter, double hipWidth, Boolean hasWheelchair, Boolean wheelchairReparable, Boolean bringToCentre, ArrayList<String> conditions, String injuryLocationKnee, String injuryLocationElbow, String status, String outcome, Long clientID, int isSynced) {
        this.serviceReq = serviceReq;
        this.referralPhoto = referralPhoto;
        this.basicOrInter = basicOrInter;
        this.hipWidth = hipWidth;
        this.hasWheelchair = hasWheelchair;
        this.wheelchairReparable = wheelchairReparable;
        this.bringToCentre = bringToCentre;
        this.conditions = conditions;
        this.injuryLocationKnee = injuryLocationKnee;
        this.injuryLocationElbow = injuryLocationElbow;
        this.status = status;
        this.outcome = outcome;
        this.clientID = clientID;
        this.isSynced = 0;
    }

    public String getServiceReq() {
        return serviceReq;
    }

    public void setServiceReq(String serviceReq) {
        this.serviceReq = serviceReq;
    }

    public byte[] getReferralPhoto() {
        return referralPhoto;
    }

    public void setReferralPhoto(byte[] referralPhoto) {
        this.referralPhoto = referralPhoto;
    }

    public String getBasicOrInter() {
        return basicOrInter;
    }

    public void setBasicOrInter(String basicOrInter) {
        this.basicOrInter = basicOrInter;
    }

    public double getHipWidth() {
        return hipWidth;
    }

    public void setHipWidth(double hipWidth) {
        this.hipWidth = hipWidth;
    }

    public Boolean getHasWheelchair() {
        return hasWheelchair;
    }

    public void setHasWheelchair(Boolean hasWheelchair) {
        this.hasWheelchair = hasWheelchair;
    }

    public Boolean getWheelchairReparable() {
        return wheelchairReparable;
    }

    public void setWheelchairReparable(Boolean wheelchairReparable) {
        this.wheelchairReparable = wheelchairReparable;
    }

    public Boolean getBringToCentre() {
        return bringToCentre;
    }

    public void setBringToCentre(Boolean bringToCentre) {
        this.bringToCentre = bringToCentre;
    }

    public ArrayList<String> getConditions() {
        return conditions;
    }

    public String conditionsToString() {
        return android.text.TextUtils.join(",", conditions);
    }

    public void setConditions(ArrayList<String> conditions) {
        this.conditions = conditions;
    }

    public String getInjuryLocationKnee() {
        return injuryLocationKnee;
    }

    public void setInjuryLocationKnee(String injuryLocationKnee) {
        this.injuryLocationKnee = injuryLocationKnee;
    }

    public String getInjuryLocationElbow() {
        return injuryLocationElbow;
    }

    public void setInjuryLocationElbow(String injuryLocationElbow) {
        this.injuryLocationElbow = injuryLocationElbow;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }
}
