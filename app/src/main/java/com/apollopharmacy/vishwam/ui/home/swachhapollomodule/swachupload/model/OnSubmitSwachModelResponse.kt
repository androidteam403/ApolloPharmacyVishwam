package com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OnSubmitSwachModelResponse : Serializable {
    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("CONFIGLIST")
    @Expose
    var configlist: List<Any>? = null


}