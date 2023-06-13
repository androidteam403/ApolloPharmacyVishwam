package com.apollopharmacy.vishwam.ui.home.apollosensing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveImageUrlsResponse {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = false

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

}