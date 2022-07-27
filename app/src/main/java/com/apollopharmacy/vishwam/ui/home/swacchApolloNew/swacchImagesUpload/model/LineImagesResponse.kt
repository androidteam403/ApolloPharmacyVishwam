package com.apollopharmacy.vishwam.ui.home.swacchApolloNew.swacchImagesUpload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LineImagesResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null


    @SerializedName("IMAGE_URLS")
    @Expose
    var imageUrls: List<ImageUrl>? = null
    fun withMessage(message: String?): LineImagesResponse {
        this.message = message
        return this
    }

    fun withStatus(status: Boolean?): LineImagesResponse {
        this.status = status
        return this
    }

    fun withImageUrls(imageUrls: List<ImageUrl>?): LineImagesResponse {
        this.imageUrls = imageUrls
        return this
    }

    inner class ImageUrl : Serializable {
        @SerializedName("URL")
        @Expose
        var url: String? = null
        fun withUrl(url: String?): ImageUrl {
            this.url = url
            return this
        }
    }
}