package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetImageUrlModelResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("CATEGORY_LIST")
    @Expose
    var categoryList: List<Category>? = null

    inner class Category : Serializable {
        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: String? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        var categoryname: String? = null

        @SerializedName("IMAGE_URLS")
        @Expose
        var imageUrls: List<ImageUrl>? = null

        inner class ImageUrl : Serializable {
            @SerializedName("URL")
            @Expose
            var url: String? = null

            @SerializedName("STATUS")
            @Expose
            var status: String? = null

            @SerializedName("REMARKS")
            @Expose
            var remarks: String? = null

            @SerializedName("CATEGORYID")
            @Expose
            var categoryid: Int? = null

            @SerializedName("IMAGEID")
            @Expose
            var imageid: String? = null
        }
    }
}