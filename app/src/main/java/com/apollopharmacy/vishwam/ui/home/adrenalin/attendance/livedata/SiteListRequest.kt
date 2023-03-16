package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SiteListRequest(
    @field:Expose @field:SerializedName("TYPE") var type: String,
    @field:Expose @field:SerializedName(
        "SITEID") var siteid: String
) : Serializable {

    fun withType(type: String): SiteListRequest {
        this.type = type
        return this
    }

    fun withSiteid(siteid: String): SiteListRequest {
        this.siteid = siteid
        return this
    }
}