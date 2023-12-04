package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubworkFlowAssignedtoMeResponse {
    @Expose
    @SerializedName("message")
    val message: String? = null

    @Expose
    @SerializedName("success")
    val success: Boolean? = null

    @Expose
    @SerializedName("data")
    val data: Data? = null

    class Data {
        @Expose
        @SerializedName("uid")
        val uid: String? = null

        @Expose
        @SerializedName("isUpdate")
        val isUpdate: Boolean? = null

        @Expose
        @SerializedName("isExecSuccess")
        val isExecSuccess: Boolean? = null

    }
}