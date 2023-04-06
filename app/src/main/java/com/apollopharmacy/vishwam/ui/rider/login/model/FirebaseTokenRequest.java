package com.apollopharmacy.vishwam.ui.rider.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirebaseTokenRequest {
    @SerializedName("firebaseToken")
    @Expose
    private String firebaseToken;

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
