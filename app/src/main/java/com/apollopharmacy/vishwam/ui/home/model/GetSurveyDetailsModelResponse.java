package com.apollopharmacy.vishwam.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class GetSurveyDetailsModelResponse implements Serializable {

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

    public class StoreDetail implements Serializable {

        @SerializedName("SITENAME")
        @Expose
        private String sitename;
        @SerializedName("CITY")
        @Expose
        private String city;
        @SerializedName("CHAMPS_REFERNCE_ID")
        @Expose
        private String champsRefernceId;
        @SerializedName("VISIT_DATE")
        @Expose
        private String visitDate;
        @SerializedName("STATUS")
        @Expose
        private String status;
        private final static long serialVersionUID = -461953278304416187L;

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

        public String getChampsRefernceId() {
            return champsRefernceId;
        }

        public void setChampsRefernceId(String champsRefernceId) {
            this.champsRefernceId = champsRefernceId;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}


