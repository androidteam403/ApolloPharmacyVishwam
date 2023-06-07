package com.apollopharmacy.vishwam.ui.home.champs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SampleModel : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("CategoryDetails")
    @Expose
    var categoryDetails: List<CategoryDetail>? = null

    inner class CategoryDetail : Serializable {
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
        var modifiedBy: Any? = null

        @SerializedName("modified_date")
        @Expose
        var modifiedDate: Any? = null
    }
}