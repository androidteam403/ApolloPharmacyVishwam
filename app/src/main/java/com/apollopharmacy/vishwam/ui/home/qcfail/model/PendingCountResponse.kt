package com.apollopharmacy.vishwam.ui.home.qcfail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
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
        @SerializedName("STOREID")
        @Expose
        var storeid: String? = null

        @SerializedName("EXECUTIVECOUNT")
        @Expose
        var executivecount: Int? = null

        @SerializedName("MANAGERCOUNT")
        @Expose
        var managercount: Int? = null

        @SerializedName("GENERALMANGERCOUNT")
        @Expose
        var generalmangercount: Int? = null
        fun withStoreid(storeid: String?): Pendingcount {
            this.storeid = storeid
            return this
        }

        fun withExecutivecount(executivecount: Int?): Pendingcount {
            this.executivecount = executivecount
            return this
        }

        fun withManagercount(managercount: Int?): Pendingcount {
            this.managercount = managercount
            return this
        }

        fun withGeneralmangercount(generalmangercount: Int?): Pendingcount {
            this.generalmangercount = generalmangercount
            return this
        }
    }
}