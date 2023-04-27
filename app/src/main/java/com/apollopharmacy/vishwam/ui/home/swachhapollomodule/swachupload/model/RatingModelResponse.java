package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class RatingModelResponse implements Serializable {

    @SerializedName("STATUS")
    @Expose
    private Boolean status;
    @SerializedName("MESSAGE")
    @Expose
    private String message;
    @SerializedName("CONFIGLIST")
    @Expose
    private List<Object> configlist = null;

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

    public List<Object> getConfiglist() {
        return configlist;
    }

    public void setConfiglist(List<Object> configlist) {
        this.configlist = configlist;
    }

}

