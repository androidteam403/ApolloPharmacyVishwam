package com.apollopharmacy.vishwam.ui.rider.orderdelivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderStatusHistoryListRequest {
    @SerializedName("uid")
    @Expose
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
