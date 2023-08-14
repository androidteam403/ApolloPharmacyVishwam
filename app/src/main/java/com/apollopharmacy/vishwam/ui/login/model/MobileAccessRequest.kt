package com.apollopharmacy.vishwam.ui.login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MobileAccessRequest {
    @Expose
    @SerializedName("empid")
    var empid: String? = null

    @Expose
    @SerializedName("company")
    var company: String? = null

}