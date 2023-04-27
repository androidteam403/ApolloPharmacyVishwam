package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveImagesUrlsRequest : Serializable {
    @SerializedName("ACTION_EVENT")
    @Expose
    var actionEvent: String? = null

    @SerializedName("RETROAUTOID")
    @Expose
    var retroautoid: String? = null

    @SerializedName("USERID")
    @Expose
    var userid: String? = null

    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("STAGE")
    @Expose
    var stage: String? = null

    @SerializedName("IMAGE_URLS")
    @Expose
    var imageUrls: List<ImageUrl>? = null

    inner class ImageUrl : Serializable {
        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: String? = null

        @SerializedName("URL")
        @Expose
        var url: String? = null

        @SerializedName("POSITION")
        @Expose
        var position: Int? = null
    }
}