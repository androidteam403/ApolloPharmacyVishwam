package com.apollopharmacy.vishwam.ui.home.planogram.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



    public class PlanogramSaveUpdateResponse implements Serializable
    {

        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("data")
        @Expose
        private Data data;
        @SerializedName("message")
        @Expose
        private String message;
        private final static long serialVersionUID = -353018678434473861L;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public class Data implements Serializable
        {

            @SerializedName("zcDebugLogs")
            @Expose
            private Object zcDebugLogs;

            public Object getZcDebugLogs() {
                return zcDebugLogs;
            }

            public void setZcDebugLogs(Object zcDebugLogs) {
                this.zcDebugLogs = zcDebugLogs;
            }

        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }





