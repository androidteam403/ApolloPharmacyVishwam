package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.model;

public class SiteData {
    private String siteId;
    private String request;
    private String pending;
    private String approved;
    private String rejected;

    public SiteData(String siteId, String request, String pending, String approved, String rejected) {
        this.siteId = siteId;
        this.request = request;
        this.pending = pending;
        this.approved = approved;
        this.rejected = rejected;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }
}
