package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LastUploadedDateResponse() : Serializable {

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("UPLOADEDDATE")
    @Expose
    var uploadedDate: String? = null
}