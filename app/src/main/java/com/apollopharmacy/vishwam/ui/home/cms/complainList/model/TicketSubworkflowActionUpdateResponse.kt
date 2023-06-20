package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.SerializedName

class TicketSubworkflowActionUpdateResponse {
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
        val isUpdate: Boolean? = null

        @field:SerializedName("isExecSuccess")
        val isExecSuccess: Boolean? = null

        @field:SerializedName("status")
        val status: Status? = null
    }

    class Status {
        @field:SerializedName("uid")
        val uid: String? = null

        @field:SerializedName("code")
        val code: String? = null

        @field:SerializedName("name")
        val name: String? = null

        @field:SerializedName("text_color")
        val text_color: String? = null

        @field:SerializedName("background_color")
        val background_color: String? = null

    }
}