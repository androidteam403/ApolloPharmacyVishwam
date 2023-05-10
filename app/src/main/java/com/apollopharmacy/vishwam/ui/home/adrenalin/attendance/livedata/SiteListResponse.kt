package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SiteListResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("SITELIST")
    @Expose
    var sitelist: List<Site>? = null
    fun withStatus(status: Boolean?): SiteListResponse {
        this.status = status
        return this
    }

    fun withMessage(message: String?): SiteListResponse {
        this.message = message
        return this
    }

    fun withSitelist(sitelist: List<Site>?): SiteListResponse {
        this.sitelist = sitelist
        return this
    }

    inner class Site : Serializable {
        @SerializedName("SITEID")
        @Expose
        var siteid: String? = null

        @SerializedName("SITENAME")
        @Expose
        var sitename: String? = null
        fun withSiteid(siteid: String?): Site {
            this.siteid = siteid
            return this
        }

        fun withSitename(sitename: String?): Site {
            this.sitename = sitename
            return this
        }
    }
}