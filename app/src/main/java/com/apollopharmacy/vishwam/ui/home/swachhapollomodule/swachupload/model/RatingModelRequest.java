package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RatingModelRequest implements Serializable {

    @SerializedName("TYPE")
    @Expose
    private String type;
    @SerializedName("SWACHHID")
    @Expose
    private String swachhid;
    @SerializedName("STOREID")
    @Expose
    private String storeid;
    @SerializedName("STATUSID")
    @Expose
    private String statusid;
    @SerializedName("REAMRKS")
    @Expose
    private String reamrks;
    @SerializedName("RATING")
    @Expose
    private String rating;
    @SerializedName("USERID")
    @Expose
    private String userid;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSwachhid() {
        return swachhid;
    }

    public void setSwachhid(String swachhid) {
        this.swachhid = swachhid;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getStatusid() {
        return statusid;
    }

    public void setStatusid(String statusid) {
        this.statusid = statusid;
    }

    public String getReamrks() {
        return reamrks;
    }

    public void setReamrks(String reamrks) {
        this.reamrks = reamrks;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}

