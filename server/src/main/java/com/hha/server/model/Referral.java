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
    private String id;

    @Column
    @JsonProperty("SERVICE_REQUIRED")
    private String serviceReq;

    @Column(length = 10485760)
    @JsonProperty("REFERRAL_PHOTO")
    private String referralPhoto;

    @Column
    @JsonProperty("BASIC_OR_INTERMEDIATE")
    private String basicOrInter;

    @Column
    @JsonProperty("HIP_WIDTH")
    private String hipWidth;

    @Column
    @JsonProperty("HAS_WHEELCHAIR")
    private String hasWheelChair;

    @Column
    @JsonProperty("WHEELCHAIR_REPAIRABLE")
    private String wheelchairRepairable;

    @Column
    @JsonProperty("BRING_TO_CENTRE")
    private String bringToCentre;

    @Column
    @JsonProperty("CONDITIONS")
    private String conditions;

    @Column
    @JsonProperty("INJURY_LOCATION_KNEE")
    private String injuryLocationKnee;

    @Column
    @JsonProperty("INJURY_LOCATION_ELBOW")
    private String injuryLocationElbow;

    @Column
    @JsonProperty("REFERRAL_STATUS")
    private String status;

    @Column
    @JsonProperty("REFERRAL_OUTCOME")
    private String outcome;

    @Column
    @JsonProperty("CLIENT_ID")
    private String clientID;

    @Column
    @JsonProperty("IS_SYNCED")
    private String isSynced;

    public Referral() {

    }

    public Referral(String id, String serviceReq, String referralPhoto, String basicOrInter, String hipWidth, String hasWheelChair, String wheelchairRepairable, String bringToCentre, String conditions, String injuryLocationKnee, String injuryLocationElbow, String status, String outcome, String clientID, String isSynced) {
        this.id = id;
        this.serviceReq = serviceReq;
        this.referralPhoto = referralPhoto;
        this.basicOrInter = basicOrInter;
        this.hipWidth = hipWidth;
        this.hasWheelChair = hasWheelChair;
        this.wheelchairRepairable = wheelchairRepairable;
        this.bringToCentre = bringToCentre;
        this.conditions = conditions;
        this.injuryLocationKnee = injuryLocationKnee;
        this.injuryLocationElbow = injuryLocationElbow;
        this.status = status;
        this.outcome = outcome;
        this.clientID = clientID;
        this.isSynced = isSynced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceReq() {
        return serviceReq;
    }

    public void setServiceReq(String serviceReq) {
        this.serviceReq = serviceReq;
    }

    public String getReferralPhoto() {
        return referralPhoto;
    }

    public void setReferralPhoto(String referralPhoto) {
        this.referralPhoto = referralPhoto;
    }

    public String getBasicOrInter() {
        return basicOrInter;
    }

    public void setBasicOrInter(String basicOrInter) {
        this.basicOrInter = basicOrInter;
    }

    public String getHipWidth() {
        return hipWidth;
    }

    public void setHipWidth(String hipWidth) {
        this.hipWidth = hipWidth;
    }

    public String getHasWheelChair() {
        return hasWheelChair;
    }

    public void setHasWheelChair(String hasWheelChair) {
        this.hasWheelChair = hasWheelChair;
    }

    public String getWheelchairRepairable() {
        return wheelchairRepairable;
    }

    public void setWheelchairRepairable(String wheelchairRepairable) {
        this.wheelchairRepairable = wheelchairRepairable;
    }

    public String getBringToCentre() {
        return bringToCentre;
    }

    public void setBringToCentre(String bringToCentre) {
        this.bringToCentre = bringToCentre;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
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

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }
}