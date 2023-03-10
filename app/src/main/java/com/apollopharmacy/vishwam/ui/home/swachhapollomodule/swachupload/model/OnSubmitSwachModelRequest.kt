package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OnSubmitSwachModelRequest : Serializable {
    @SerializedName("ACTION_EVENT")
    @Expose
    var actionEvent: String? = null

    @SerializedName("STORE_ID")
    @Expose
    var storeId: String? = null

    @SerializedName("USERID_ID")
    @Expose
    var useridId: String? = null

    @SerializedName("CATEGORY_ID")
    @Expose
    var categoryId: String? = null

    @SerializedName("IMAGE_URLS")
    @Expose
    var imageUrls: List<ImageUrl>? = null

    inner class ImageUrl : Serializable {
        @SerializedName("URL")
        @Expose
        var url: String? = null
    }
}