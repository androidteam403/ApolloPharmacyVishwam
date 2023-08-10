package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class GetImageUrlModelResponse : Serializable {
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

        @SerializedName("SWACHH_ID")
        @Expose
        var swachhId: String? = null

        @SerializedName("STORE_ID")
        @Expose
        var storeId: String? = null

        @SerializedName("REMARKS")
        @Expose
        var remarks: String? = null

        @SerializedName("RATING")
        @Expose
        var rating: Int? = null

        @SerializedName("USERID")
        @Expose
        var userid: String? = null

        @SerializedName("CREATED_DATE")
        @Expose
        var createdDate: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: Any? = null


    }

    inner class Category : Serializable {
        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: String? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        var categoryname: String? = null

        var categoryPosForUrl:Int?=null

        @SerializedName("IMAGE_URLS")
        @Expose
        var imageUrls: List<ImageUrl>? = null

        inner class ImageUrl : Serializable {
            @SerializedName("URL")
            @Expose
            var url: String? = null

            var file: File?=null

            var imagePosForUrl:Int?=null

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

            @SerializedName("RESHOOT STATUS")
            @Expose
            var isReshootStatus: Boolean? = null
        }
    }
}