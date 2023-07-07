package com.apollopharmacy.vishwam.ui.home.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StoreDetailsModelResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("StoreDetails")
    @Expose
    private List<StoreDetail> storeDetails;


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoreDetail> getStoreDetails() {
        return storeDetails;
    }

    public void setStoreDetails(List<StoreDetail> storeDetails) {
        this.storeDetails = storeDetails;
    }
    public class StoreDetail implements Serializable
    {

        @SerializedName("SITEID")
        @Expose
        private String siteid;
        @SerializedName("SITENAME")
        @Expose
        private String sitename;
        @SerializedName("CITY")
        @Expose
        private String city;
        @SerializedName("REGION")
        @Expose
        private String region;

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public String getSitename() {
            return sitename;
        }

        public void setSitename(String sitename) {
            this.sitename = sitename;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

    }

}



