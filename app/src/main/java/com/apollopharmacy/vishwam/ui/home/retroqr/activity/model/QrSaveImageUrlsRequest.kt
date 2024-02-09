package com.apollopharmacy.vishwam.ui.home.retroqr.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QrSaveImageUrlsRequest : Serializable {
    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("USERID")
    @Expose
    var userid: String? = null

    @SerializedName("ISADMIN")
    @Expose
    var isadmin: Boolean? = null

    @SerializedName("storeDetails")
    @Expose
    var storeDetails: List<StoreDetail>? = null

    public class StoreDetail : Serializable {
        @SerializedName("RACKNO")
        @Expose
        var rackno: String? = null

        @SerializedName("QRCODE")
        @Expose
        var qrcode: String? = null

        @SerializedName("IMAGEURL")
        @Expose
        var imageurl: String? = null

        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: Int? = null
    }
}