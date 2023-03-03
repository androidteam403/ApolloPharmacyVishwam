package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.SerializedName

class SubWorkflowAcceptResponse {
    @field:SerializedName("message")
    val message: String? = null

    @field:SerializedName("success")
    val success: Boolean? = null

    @field:SerializedName("data")
    val data: Data? = null

    class Data {
        @field:SerializedName("uid")
        val uid: String? = null

        @field:SerializedName("isUpdate")
        val isUpdate: String? = null

        @field:SerializedName("isExecSuccess")
        val isExecSuccess: String? = null
    }
}