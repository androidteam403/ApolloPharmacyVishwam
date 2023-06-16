package com.apollopharmacy.vishwam.ui.home.apollosensing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendGlobalSmsResponse {
    @SerializedName("Status")
    @Expose
    var status: Boolean? = false

    @SerializedName("Message")
    @Expose
    var message: String? = null

    @SerializedName("Otp")
    @Expose
    var otp: String? = null
}