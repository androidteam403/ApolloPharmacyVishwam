package com.apollopharmacy.vishwam.ui.home.apnarectro.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetStorePendingAndApprovedListReq implements Serializable
    {

        @SerializedName("STOREID")
        @Expose
        private String storeid;
        @SerializedName("EMPID")
        @Expose
        private String empid;
        @SerializedName("FROMDATE")
        @Expose
        private String fromdate;
        @SerializedName("TODATE")
        @Expose
        private String todate;

        public String getStoreid() {
            return storeid;
        }

        public void setStoreid(String storeid) {
            this.storeid = storeid;
        }

        public String getEmpid() {
            return empid;
        }

        public void setEmpid(String empid) {
            this.empid = empid;
        }

        public String getFromdate() {
            return fromdate;
        }

        public void setFromdate(String fromdate) {
            this.fromdate = fromdate;
        }

        public String getTodate() {
            return todate;
        }

        public void setTodate(String todate) {
            this.todate = todate;
        }

    }

