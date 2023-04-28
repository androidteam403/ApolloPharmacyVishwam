package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetImageUrlsResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    val message: String? = null

    @SerializedName("STATUS")
    @Expose
    val status: Boolean? = null

    @SerializedName("CATEGORY_LIST")
    @Expose
    var categoryList: List<Category>? = null

    class Category : Serializable {
        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: String? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        var categoryname: String? = null

        @SerializedName("IMAGE_URLS")
        @Expose
        var imageUrls: ArrayList<ImageUrl>? = null
    }

    class ImageUrl : Serializable {
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

        var mainCategoryId: String? = null
        var statusId: String? = null

        var categoryname: String? = null
        var isVerified: Boolean? = false
    }
}