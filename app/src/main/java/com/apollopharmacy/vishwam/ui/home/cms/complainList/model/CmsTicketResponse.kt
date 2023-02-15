package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CmsTicketResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    fun withMessage(message: String?): CmsTicketResponse {
        this.message = message
        return this
    }

    fun withSuccess(success: Boolean?): CmsTicketResponse {
        this.success = success
        return this
    }

    fun withData(data: Data?): CmsTicketResponse {
        this.data = data
        return this
    }

    inner class Data : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("isUpdate")
        @Expose
        var isUpdate: Boolean? = null

        @SerializedName("isExecSuccess")
        @Expose
        var isExecSuccess: Boolean? = null
        fun withUid(uid: String?): Data {
            this.uid = uid
            return this
        }

        fun withIsUpdate(isUpdate: Boolean?): Data {
            this.isUpdate = isUpdate
            return this
        }

        fun withIsExecSuccess(isExecSuccess: Boolean?): Data {
            this.isExecSuccess = isExecSuccess
            return this
        }
    }
}