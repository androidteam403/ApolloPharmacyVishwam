package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateSwachhDefaultSiteResponse {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null


}