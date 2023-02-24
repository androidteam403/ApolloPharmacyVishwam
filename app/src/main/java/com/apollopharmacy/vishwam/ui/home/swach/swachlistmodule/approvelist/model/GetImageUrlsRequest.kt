package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetImageUrlsRequest {

    @SerializedName("STOREID")
    @Expose
    var storeId: String? = null

    @SerializedName("SWACHH_ID")
    @Expose
    var swachhId: String? = null
}