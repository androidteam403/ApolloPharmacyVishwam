package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveCategoryConfigurationDetailsRequest {
    @SerializedName("categoryDetails")
    @Expose
    var categoryDetails: List<CategoryDetail>? = null

    @SerializedName("subCategoryDetails")
    @Expose
    var subCategoryDetails: List<SubCategoryDetail>? = null

     class CategoryDetail {

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("modified_by")
        @Expose
        var modifiedBy: String? = null


    }

     class SubCategoryDetail {

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null

        @SerializedName("sub_category_name")
        @Expose
        var subCategoryName: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("modified_by")
        @Expose
        var modifiedBy: String? = null

    }
}

