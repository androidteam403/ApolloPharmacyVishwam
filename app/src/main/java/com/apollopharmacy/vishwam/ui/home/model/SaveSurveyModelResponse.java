package com.apollopharmacy.vishwam.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SaveSurveyModelResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("champ_Reference_Id")
    @Expose
    private String champReferenceId;

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

    public String getChampReferenceId() {
        return champReferenceId;
    }

    public void setChampReferenceId(String champReferenceId) {
        this.champReferenceId = champReferenceId;
    }

}

