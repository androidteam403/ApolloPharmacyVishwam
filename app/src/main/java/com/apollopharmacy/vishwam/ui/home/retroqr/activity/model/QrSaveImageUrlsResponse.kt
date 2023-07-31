package com.apollopharmacy.vishwam.ui.home.retroqr.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QrSaveImageUrlsResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("storeDetails")
    @Expose
    var storeDetails: List<Any>? = null
    fun withStatus(status: Boolean?): QrSaveImageUrlsResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): QrSaveImageUrlsResponse {
        this.message = message
        return this
    }

    fun withStoreDetails(storeDetails: List<Any>?): QrSaveImageUrlsResponse {
        this.storeDetails = storeDetails
        return this
    }
}