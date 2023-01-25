package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse.Pendingcount
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import java.io.Serializable

class PendingCountResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("PENDINGCOUNT")
    @Expose
    var pendingcount: List<Pendingcount>? = null
    fun withStatus(status: Boolean?): PendingCountResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): PendingCountResponse {
        this.message = message
        return this
    }

    fun withPendingcount(pendingcount: List<Pendingcount>?): PendingCountResponse {
        this.pendingcount = pendingcount
        return this
    }

    inner class Pendingcount : Serializable {
        @SerializedName("SITEID")
        @Expose
        var siteid: String? = null

        @SerializedName("EMPID")
        @Expose
        var empid: String? = null

        @SerializedName("DESIGNATION")
        @Expose
        var designation: String? = null

        @SerializedName("PENDINGCOUNT")
        @Expose
        var pendingcount: Int? = null

        fun withSiteid(siteid: String?): Pendingcount {
            this.siteid = siteid
            return this
        }

        fun withEmpid(empid: String?): Pendingcount {
            this.empid = empid
            return this
        }

        fun withDesignation(designation: String?): Pendingcount {
            this.designation = designation
            return this
        }

        fun withPendingcount(pendingcount: Int?): Pendingcount {
            this.pendingcount = pendingcount
            return this
        }


    }
}