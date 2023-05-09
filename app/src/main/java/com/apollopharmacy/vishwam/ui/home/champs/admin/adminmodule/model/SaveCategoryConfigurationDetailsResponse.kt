package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveCategoryConfigurationDetailsResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}