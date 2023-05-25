package com.apollopharmacy.vishwam.ui.home.apollosensing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveImageUrlsRequest {

    @SerializedName("SITEID")
    @Expose
    var siteId: String? = null

    @SerializedName("TYPE")
    @Expose
    var type: String? = null

    @SerializedName("REQUESTEDBY")
    @Expose
    var requestedBy: String? = null

    @SerializedName("CUSTOMERNAME")
    @Expose
    var customerName: String? = null

    @SerializedName("MOBNO")
    @Expose
    var mobNo: String? = null

    @SerializedName("BASE64IMAGE")
    @Expose
    var base64ImageList: List<Base64Image>? = null

    class Base64Image {
        @SerializedName("BASE64IMAGE")
        @Expose
        var base64Image: String? = null

    }
}