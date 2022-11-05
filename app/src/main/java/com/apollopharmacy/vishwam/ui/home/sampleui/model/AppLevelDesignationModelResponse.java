package com.apollopharmacy.vishwam.ui.home.sampleui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


    public class AppLevelDesignationModelResponse implements Serializable
    {

        @SerializedName("STATUS")
        @Expose
        private Boolean status;
        @SerializedName("MESSAGE")
        @Expose
        private String message;

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

    }

