package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseNewComplaintRegistration(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("zcServerDateTime")
    val zcServerDateTime: String? = null,

    @field:SerializedName("zcServerIp")
    val zcServerIp: String? = null,

    @field:SerializedName("zcServerHost")
    val zcServerHost: String? = null,

    @field:SerializedName("data")
    val data: Data,
) : Serializable {
    data class Data(
        @field:SerializedName("uid")
        val uid: String? = null,
        @field:SerializedName("created_user")
        var createdUser: CreatedUser? = null,


        @field:SerializedName("ticket_id")
        val ticket_id: String? = null,

        @field:SerializedName("isUpdate")
        val isUpdate: Boolean,

        @field:SerializedName("errors")
        val errors: ArrayList<Error>? = null,
        ) : Serializable





}



