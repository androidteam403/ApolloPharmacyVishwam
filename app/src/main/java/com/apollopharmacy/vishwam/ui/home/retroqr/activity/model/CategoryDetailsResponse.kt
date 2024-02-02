package com.apollopharmacy.vishwam.ui.home.retroqr.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CategoryDetailsResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("storeDetails")
    @Expose
    var storeDetails: List<Any>? = null

    @SerializedName("IMAGEURL")
    @Expose
    var imageurl: Any? = null

    @SerializedName("CategoryDetails")
    @Expose
    var categoryDetails: List<CategoryDetail>? = null

    inner class CategoryDetail : Serializable {
        @SerializedName("CATEGORYID")
        @Expose
        var categoryid: Int? = null

        @SerializedName("CATEGORYNAME")
        @Expose
        var categoryname: String? = null
    }
}