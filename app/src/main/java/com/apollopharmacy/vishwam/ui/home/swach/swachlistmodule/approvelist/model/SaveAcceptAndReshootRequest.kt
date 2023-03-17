package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveAcceptAndReshootRequest {
    @SerializedName("TYPE")
    @Expose
    var type: String? = null

    @SerializedName("SWACHHID")
    @Expose
    var swachhid: String? = null

    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("STATUSID")
    @Expose
    var statusid: String? = null

    @SerializedName("REAMRKS")
    @Expose
    var reamrks: String? = null

    @SerializedName("RATING")
    @Expose
    var rating: String? = null

    @SerializedName("USERID")
    @Expose
    var userid: String? = null

    @SerializedName("IMAGEURLS")
    @Expose
    var imageurls: ArrayList<Imageurl>? = null

    class Imageurl {
        @SerializedName("IMAGEID")
        @Expose
        var imageid: String? = null

        @SerializedName("STATUSID")
        @Expose
        var statusid: String? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: String? = null
    }
}