package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetSubCategoryDetailsResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("SubCategoryDetails")
    var subCategoryDetails: List<SubCategoryDetails>? = null

    class SubCategoryDetails {
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

        var isRatingInDecimal: Boolean? = null
    }
}