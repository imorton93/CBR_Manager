package com.example.cbr_manager.Database;

import java.util.ArrayList;

public class Referral {
    public Long id;
    private String serviceReq;
    private byte[] referralPhoto;
    private String basicOrInter;
    private int hipWidth;
    private Boolean hasWheelchair;
    private Boolean wheelchairReparable;
    private Boolean bringToCentre;
    private String condition;
    private String conditionOtherExplanation;
    private String injuryLocation;
    private String status;
    private String outcome;
    private Long clientID;
    private String otherExplanation;

    private int isSynced = 0;

    public Referral() {

    }

    public Referral(String serviceReq, byte[] referralPhoto, String basicOrInter, int hipWidth, Boolean hasWheelchair, Boolean wheelchairReparable, Boolean bringToCentre, String condition, String injuryLocation, String status, String outcome, Long clientID) {
        this.serviceReq = serviceReq;
        this.referralPhoto = referralPhoto;
        this.basicOrInter = basicOrInter;
        this.hipWidth = hipWidth;
        this.hasWheelchair = hasWheelchair;
        this.wheelchairReparable = wheelchairReparable;
        this.bringToCentre = bringToCentre;
        this.condition = condition;
        this.injuryLocation = injuryLocation;
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

    public int getHipWidth() {
        return hipWidth;
    }

    public void setHipWidth(int hipWidth) {
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getInjuryLocation() {
        return injuryLocation;
    }

    public void setInjuryLocation(String injuryLocation) {
        this.injuryLocation = injuryLocation;
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

    public String getOtherExplanation() {
        return otherExplanation;
    }

    public void setOtherExplanation(String otherExplanation) {
        this.otherExplanation = otherExplanation;
    }

    public String getConditionOtherExplanation() {
        return conditionOtherExplanation;
    }

    public void setConditionOtherExplanation(String conditionOtherExplanation) {
        this.conditionOtherExplanation = conditionOtherExplanation;
    }
}
