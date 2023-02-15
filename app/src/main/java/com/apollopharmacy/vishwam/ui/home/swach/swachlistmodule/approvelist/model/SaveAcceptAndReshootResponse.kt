package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SaveAcceptAndReshootResponse {

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

}