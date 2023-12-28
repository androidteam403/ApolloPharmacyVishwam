package com.apollopharmacy.vishwam.ui.home.communityadvisor.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HomeServicesSaveDetailsResponse : Serializable {
    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("message")
    var message: String? = null

}