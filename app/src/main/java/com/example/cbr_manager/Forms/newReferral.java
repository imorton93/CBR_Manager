package com.example.cbr_manager.Forms;

public class newReferral {
    String service;
    String levelWheelchairUser;
    int hipWidth;
    Boolean existingWheelchair;
    Boolean canChairRepair;
    String clientCondition;
    String injuryLocation;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLevelWheelchairUser() {
        return levelWheelchairUser;
    }

    public void setLevelWheelchairUser(String levelWheelchairUser) {
        this.levelWheelchairUser = levelWheelchairUser;
    }

    public int getHipWidth() {
        return hipWidth;
    }

    public void setHipWidth(int hipWidth) {
        this.hipWidth = hipWidth;
    }

    public Boolean getExistingWheelchair() {
        return existingWheelchair;
    }

    public void setExistingWheelchair(Boolean existingWheelchair) {
        this.existingWheelchair = existingWheelchair;
    }

    public Boolean getCanChairRepair() {
        return canChairRepair;
    }

    public void setCanChairRepair(Boolean canChairRepair) {
        this.canChairRepair = canChairRepair;
    }

    public String getClientCondition() {
        return clientCondition;
    }

    public void setClientCondition(String clientCondition) {
        this.clientCondition = clientCondition;
    }

    public String getInjuryLocation() {
        return injuryLocation;
    }

    public void setInjuryLocation(String injuryLocation) {
        this.injuryLocation = injuryLocation;
    }
}
