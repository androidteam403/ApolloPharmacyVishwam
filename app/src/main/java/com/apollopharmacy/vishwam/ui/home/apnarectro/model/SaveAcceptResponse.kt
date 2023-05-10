package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveAcceptResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("RETROID")
    @Expose
    var retroid: Any? = null

    @SerializedName("CONFIGLIST")
    @Expose
    var configlist: List<Any>? = null
    fun withStatus(status: Boolean?): SaveAcceptResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): SaveAcceptResponse {
        this.message = message
        return this
    }

    fun withRetroid(retroid: Any?): SaveAcceptResponse {
        this.retroid = retroid
        return this
    }

    fun withConfiglist(configlist: List<Any>?): SaveAcceptResponse {
        this.configlist = configlist
        return this
    }
}