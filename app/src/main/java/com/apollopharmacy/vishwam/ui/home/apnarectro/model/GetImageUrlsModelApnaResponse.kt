package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class GetImageUrlsModelApnaResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("REMARKS")
    @Expose
    var remarks: List<Remark>? = null

    @SerializedName("CATEGORY_LIST")
    @Expose
    var categoryList: List<Category>? = null

     class Remark : Serializable {
        @SerializedName("RECID")
        @Expose
        var recid: Int? = null

        @SerializedName("RETRO_AUTO_ID")
        @Expose
        var retroAutoId: String? = null

        @SerializedName("STORE_ID")
        @Expose
        var storeId: String? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: String? = null

        @SerializedName("RATING")
        @Expose
        var rating: Int? = null

        @SerializedName("CREATED_BY")
        @Expose
        var createdBy: String? = null

        @SerializedName("CREATED_DATE")
        @Expose
        var createdDate: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: Any? = null

        @SerializedName("STAGE")
        @Expose
        var stage: Any? = null
    }

     class Category : Serializable {
        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: String? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        var categoryname: String? = null

        @SerializedName("IMAGE_URLS")
        @Expose
        var imageUrls: List<ImageUrl>? = null
        var groupingImageUrlList: List<ArrayList<ImageUrl>>? = null

          class ImageUrl : Serializable {
            @SerializedName("URL")
            @Expose
            var url: String? = null

            @SerializedName("STATUS")
            @Expose
            var status: String? = null

            @SerializedName("REMARKS")
            @Expose
            var remarks: Any? = null

            @SerializedName("RETORAUTOID")
            @Expose
            var retorautoid: Int? = null

            @SerializedName("IMAGEID")
            @Expose
            var imageid: String? = null

            @SerializedName("STAGE")
            @Expose
            var stage: String? = null

            @SerializedName("CATEGORYID")
            @Expose
            var categoryid: Int? = null

            @SerializedName("POSITION")
            @Expose
            var position: Int? = null

            var file: File?=null
              var imageUploadStatusUpdate: Boolean = false
              var imageUpload: Boolean = false

            var isReshootStatus: Boolean? = null

              var statusStore:String?=null
        }
    }
}