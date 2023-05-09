package com.apollopharmacy.vishwam.ui.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable
import java.net.URL

class GetEmailAddressModelResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("emailDetails")
    @Expose
    var emailDetails: List<EmailDetail>? = null

    inner class EmailDetail : Serializable {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

    }
}