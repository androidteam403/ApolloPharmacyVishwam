package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetCategoryDetailsResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("CategoryDetails")
    @Expose
    var categoryDetails: List<CategoryDetails>? = null

    class CategoryDetails {
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

        @SerializedName("modified_by")
        @Expose
        var modifiedBy: String? = null

        @SerializedName("modified_date")
        @Expose
        var modifiedDate: String? = null

        var isItemExpanded: Boolean? = false

        var sumOfSubCategoryRating: Double = 0.0

        var subCategoryDetailsList: List<GetSubCategoryDetailsResponse.SubCategoryDetails>?=null
    }
}