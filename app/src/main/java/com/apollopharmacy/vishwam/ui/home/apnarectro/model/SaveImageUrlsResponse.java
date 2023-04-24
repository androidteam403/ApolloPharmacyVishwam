package com.apollopharmacy.vishwam.ui.home.apnarectro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SaveImageUrlsResponse implements Serializable
    {

        @SerializedName("STATUS")
        @Expose
        private Boolean status;
        @SerializedName("MESSAGE")
        @Expose
        private String message;
        @SerializedName("RETROID")
        @Expose
        private String retroid;
        @SerializedName("CONFIGLIST")
        @Expose
        private List<Object> configlist;

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

        public String getRetroid() {
            return retroid;
        }

        public void setRetroid(String retroid) {
            this.retroid = retroid;
        }

        public List<Object> getConfiglist() {
            return configlist;
        }

        public void setConfiglist(List<Object> configlist) {
            this.configlist = configlist;
        }

    }

