package com.apollopharmacy.vishwam.ui.home.apollosensing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckScreenStatusResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("CUSTLINK")
    @Expose
    var CUSTLINK: Boolean? = null

    @SerializedName("STORELINK")
    @Expose
    var STORELINK: Boolean? = null
}