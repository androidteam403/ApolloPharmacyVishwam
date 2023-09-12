package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ActionResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null
    var order: String? = null

    fun setorder(pos: String) {
        order = pos
    }
    @SerializedName("HSITORYDETAILS")
    @Expose
    var hsitorydetails: List<Hsitorydetail>? = null
    fun withStatus(status: Boolean?): ActionResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): ActionResponse {
        this.message = message
        return this
    }

    fun withHsitorydetails(hsitorydetails: List<Hsitorydetail>?): ActionResponse {
        this.hsitorydetails = hsitorydetails
        return this
    }

     class Hsitorydetail :Serializable{
        @SerializedName("ACTIONBY")
        @Expose
        var actionby: String? = null

        @SerializedName("ACTIONDATE")
        @Expose
        var actiondate: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null
        fun withActionby(actionby: String?): Hsitorydetail {
            this.actionby = actionby
            return this
        }

        fun withActiondate(actiondate: String?): Hsitorydetail {
            this.actiondate = actiondate
            return this
        }

        fun withStatus(status: String?): Hsitorydetail {
            this.status = status
            return this
        }
    }
}