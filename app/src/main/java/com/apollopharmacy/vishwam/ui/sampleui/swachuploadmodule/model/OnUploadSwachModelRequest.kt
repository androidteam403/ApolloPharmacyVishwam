package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OnUploadSwachModelRequest : Serializable {
    @SerializedName("ACTION_EVENT")
    @Expose
    var actionEvent: String? = null

    @SerializedName("USERID")
    @Expose
    var userid: String? = null

    @SerializedName("SWACHHID")
    @Expose
    var swachhId: String? = null

    @SerializedName("SWACHH_ID")
    @Expose
    var swachhIdRehoot: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: String? = null

    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

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

        @SerializedName("IMAGEID")
        @Expose
        var imageId: String? = null
    }
}