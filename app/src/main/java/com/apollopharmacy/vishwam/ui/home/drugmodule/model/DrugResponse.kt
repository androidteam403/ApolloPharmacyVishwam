package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DrugResponse : Serializable {
    @SerializedName("RequestStatus")
    @Expose
    var requestStatus: Boolean? = null

    @SerializedName("Message")
    @Expose
    var message: String? = null

    @SerializedName("ReferenceId")
    @Expose
    var referenceId: String? = null
    fun withRequestStatus(requestStatus: Boolean?): DrugResponse {
        this.requestStatus = requestStatus
        return this
    }

    fun withMessage(message: String?): DrugResponse {
        this.message = message
        return this
    }

    fun withReferenceId(referenceId: String?): DrugResponse {
        this.referenceId = referenceId
        return this
    }



}