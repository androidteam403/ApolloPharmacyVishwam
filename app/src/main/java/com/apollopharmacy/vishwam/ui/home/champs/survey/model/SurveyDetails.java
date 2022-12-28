package com.apollopharmacy.vishwam.ui.home.champs.survey.model;

public class SurveyDetails {
    public static final int PENDING_LAYOUT = 0;
    public static final int COMPLETED_LAYOUT = 1;
    private int viewTye;

    private String storeId;
    private String location;
    private String status;
    private String executive;

    //pending
    public SurveyDetails(int viewTye, String storeId, String location, String status, String executive) {
        this.viewTye = viewTye;
        this.storeId = storeId;
        this.location = location;
        this.status = status;
        this.executive = executive;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecutive() {
        return executive;
    }

    public void setExecutive(String executive) {
        this.executive = executive;
    }

    public int getViewTye() {
        return viewTye;
    }

    public void setViewTye(int viewTye) {
        this.viewTye = viewTye;
    }

    //completed
    private String dateOfVisit;

    public SurveyDetails(int viewTye, String storeId, String location, String status, String executive, String dateOfVisit) {
        this.viewTye = viewTye;
        this.storeId = storeId;
        this.location = location;
        this.status = status;
        this.executive = executive;
        this.dateOfVisit = dateOfVisit;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }
}
