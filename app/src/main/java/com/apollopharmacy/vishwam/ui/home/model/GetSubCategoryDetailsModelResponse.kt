package com.apollopharmacy.vishwam.ui.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable

class GetSubCategoryDetailsModelResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null


    @SerializedName("SubCategoryDetails")
    @Expose
    var subCategoryDetails: List<SubCategoryDetail>? = null


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

        var givenRating: Float? = null

        @SerializedName("IS_ACTIVE")
        @Expose
        var isActive: Boolean? = null
    }
}