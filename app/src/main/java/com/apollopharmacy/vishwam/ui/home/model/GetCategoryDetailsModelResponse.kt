package com.apollopharmacy.vishwam.ui.home.model

import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class GetCategoryDetailsModelResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("emailDetails")
    @Expose
    var emailDetails: List<EmailDetail>? = null


    inner class EmailDetail : Serializable {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null


        var sumOfSubCategoryRating: Float? = 0f

        var imageDataDto:ArrayList<File>?=null
        inner class ImgeDtcl(var file: File?)

         var imageUrls: ArrayList<String>?=null

//        inner class ImageUrl : Serializable {
//
//            @SerializedName("URL")
//            @Expose
//            var url: String? = null
//
//        }

        var subCategoryDetails: List<GetSubCategoryDetailsModelResponse.SubCategoryDetail>? = null

        inner class SubCategoryDetail : Serializable {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("sub_category_name")
            @Expose
            var subCategoryName: String? = null

            @SerializedName("category_name")
            @Expose
            var categoryName: String? = null

            @SerializedName("rating")
            @Expose
            var rating: String? = null

            @SerializedName("created_date")
            @Expose
            var createdDate: String? = null

            var givenRating: Float? = 0f

            }


//        inner class ImgeDtcl(var file: File?)

    }
}