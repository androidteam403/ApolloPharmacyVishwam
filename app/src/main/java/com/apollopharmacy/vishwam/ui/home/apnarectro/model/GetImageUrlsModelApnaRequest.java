package com.apollopharmacy.vishwam.ui.home.apnarectro.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetImageUrlsModelApnaRequest implements Serializable
    {

        @SerializedName("RETRO_ID")
        @Expose
        private String retroId;
        @SerializedName("STOREID")
        @Expose
        private String storeid;

        public String getRetroId() {
            return retroId;
        }

        public void setRetroId(String retroId) {
            this.retroId = retroId;
        }

        public String getStoreid() {
            return storeid;
        }

        public void setStoreid(String storeid) {
            this.storeid = storeid;
        }

    }

