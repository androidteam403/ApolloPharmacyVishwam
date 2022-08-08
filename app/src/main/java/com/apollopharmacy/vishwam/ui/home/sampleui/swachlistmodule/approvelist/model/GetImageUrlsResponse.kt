package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model

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
    val categoryList: List<Category>? = null

    inner class Category {
        @SerializedName("CATEGORYID")
        @Expose
        val categoryid: String? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        val categoryname: String? = null

        @SerializedName("IMAGE_URLS")
        @Expose
        val imageUrls: List<ImageUrl>? = null
    }

    inner class ImageUrl {
        @SerializedName("URL")
        @Expose
        val url: String? = null

        @SerializedName("STATUS")
        @Expose
        val status: String? = null

        @SerializedName("REMARKS")
        @Expose
        val remarks: String? = null

        @SerializedName("CATEGORYID")
        @Expose
        val categoryid: Int? = null

        @SerializedName("IMAGEID")
        @Expose
        val imageid: String? = null
    }
}