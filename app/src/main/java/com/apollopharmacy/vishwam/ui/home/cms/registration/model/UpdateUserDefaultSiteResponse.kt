package com.apollopharmacy.vishwam.ui.home.cms.registration.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateUserDefaultSiteResponse {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null
}