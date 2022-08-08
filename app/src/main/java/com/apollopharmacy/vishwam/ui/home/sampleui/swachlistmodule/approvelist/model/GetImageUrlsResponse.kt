package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetImageUrlsResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    private val message: String? = null

    @SerializedName("STATUS")
    @Expose
    private val status: Boolean? = null

    @SerializedName("CATEGORY_LIST")
    @Expose
    private val categoryList: List<Category>? = null

    internal inner class Category {
        @SerializedName("CATEGORYID")
        @Expose
        private val categoryid: String? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        private val categoryname: String? = null

        @SerializedName("IMAGE_URLS")
        @Expose
        private val imageUrls: List<ImageUrl>? = null
    }

    inner class ImageUrl {
        @SerializedName("URL")
        @Expose
        private val url: String? = null

        @SerializedName("STATUS")
        @Expose
        private val status: String? = null

        @SerializedName("REMARKS")
        @Expose
        private val remarks: String? = null

        @SerializedName("CATEGORYID")
        @Expose
        private val categoryid: Int? = null

        @SerializedName("IMAGEID")
        @Expose
        private val imageid: String? = null
    }
}