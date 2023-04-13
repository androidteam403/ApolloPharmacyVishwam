package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ApproveRequest  : Serializable{
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
    var imageUrls: List<LineImagesResponse.ImageUrl>? = null


    fun withActionEvent(actionEvent: String?): ApproveRequest {
        this.actionEvent = actionEvent
        return this
    }

    fun withStoreId(storeId: String?): ApproveRequest {
        this.storeId = storeId
        return this
    }

    fun withUseridId(useridId: String?): ApproveRequest {
        this.useridId = useridId
        return this
    }

    fun withCategoryId(categoryId: String?): ApproveRequest {
        this.categoryId = categoryId
        return this
    }

    fun withImageUrls(imageUrls: List<LineImagesResponse.ImageUrl>?): ApproveRequest {
        this.imageUrls = imageUrls
        return this
    }

    inner class ImageUrl :Serializable {
        @SerializedName("URL")
        @Expose
        var url: String? = null
        fun withUrl(url: String?): ImageUrl {
            this.url = url
            return this
        }
    }
}