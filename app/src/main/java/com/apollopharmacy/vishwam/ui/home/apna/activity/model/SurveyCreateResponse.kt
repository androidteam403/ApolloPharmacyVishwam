package com.apollopharmacy.vishwam.ui.home.apna.activity.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class SurveyCreateResponse {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("isUpdate")
        @Expose
        var isUpdate: Boolean? = null
    }
}