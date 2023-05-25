package com.apollopharmacy.vishwam.ui.home.apollosensing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendGlobalSmsRequest {
    @SerializedName("SOURCEFOR")
    @Expose
    var sourceFor: String? = null

    @SerializedName("TYPE")
    @Expose
    var type: String? = null

    @SerializedName("MOBILENO")
    @Expose
    var mobileNo: String? = null

    @SerializedName("LINK")
    @Expose
    var link: String? = null

}