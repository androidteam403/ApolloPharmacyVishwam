package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LineImagesRequest : Serializable {
    @SerializedName("CATEGORYID")
    @Expose
    var categoryid: Int? = null

    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("CATEGORY_AUTO_ID")
    @Expose
    var categoryAutoId: String? = null

    @SerializedName("SWACHH_ID")
    @Expose
    var swachhId: String? = null

    constructor(categoryid: Int?, storeid: String?, categoryAutoId: String?) {
        this.categoryid = categoryid
        this.storeid = storeid
        this.categoryAutoId = categoryAutoId
    }

    fun withCategoryid(categoryid: Int?): LineImagesRequest {
        this.categoryid = categoryid
        return this
    }

    fun withStoreid(storeid: String?): LineImagesRequest {
        this.storeid = storeid
        return this
    }

    fun withCategoryAutoId(categoryAutoId: String?): LineImagesRequest {
        this.categoryAutoId = categoryAutoId
        return this
    }
}