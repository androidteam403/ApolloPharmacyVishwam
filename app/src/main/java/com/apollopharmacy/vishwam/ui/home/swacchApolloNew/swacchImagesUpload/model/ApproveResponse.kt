package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ApproveResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("CONFIGLIST")
    @Expose
    var configlist: List<Any>? = null
    fun withStatus(status: Boolean?): ApproveResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): ApproveResponse {
        this.message = message
        return this
    }

    fun withConfiglist(configlist: List<Any>?): ApproveResponse {
        this.configlist = configlist
        return this
    }
}