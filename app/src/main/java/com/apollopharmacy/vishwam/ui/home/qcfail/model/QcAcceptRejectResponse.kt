package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QcAcceptRejectResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null
    fun withStatus(status: Boolean?): QcAcceptRejectResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): QcAcceptRejectResponse {
        this.message = message
        return this
    }
}