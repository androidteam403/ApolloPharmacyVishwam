package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetImageUrlModelRequest : Serializable {
    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("SWACHH_ID")
    @Expose
    var swachhId: String? = null
}