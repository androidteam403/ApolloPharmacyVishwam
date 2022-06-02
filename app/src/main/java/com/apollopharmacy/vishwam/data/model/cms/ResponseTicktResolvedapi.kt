package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseTicktResolvedapi (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("zcServerDateTime")
    val zcServerDateTime: String? = null,

    @field:SerializedName("zcServerIp")
    val zcServerIp: String? = null,

    @field:SerializedName("zcServerHost")
    val zcServerHost: String? = null
    ):Serializable
{
    data class Data(
        @field:SerializedName("ticket_id")
        val ticket_id: String? = null,

        @field:SerializedName("site_name")
        val site_name: String? = null,

        @field:SerializedName("site_id")
        val site_id: String? = null,

        @field:SerializedName("created_user")
        val created_user: CreatedUser,

        @field:SerializedName("ticket_created_time")
        val ticket_created_time: String? = null,

        @field:SerializedName("ticket_reason_name")
        val ticket_reason_name: String? = null,

        @field:SerializedName("ticket_uid")
        val ticket_uid: String? = null,

        @field:SerializedName("ticket_level_uid")
        val ticket_level_uid: String? = null,

        @field:SerializedName("ticket_user_uid")
        val ticket_user_uid: String? = null,

        @field:SerializedName("ticket_site_uid")
        val ticket_site_uid: String? = null,

        @field:SerializedName("errors")
        val errors: ArrayList<Error>,

        @field:SerializedName("message")
        val message: String? = null,

    ):Serializable

    data class  CreatedUser(
        @field:SerializedName("first_name")
        val first_name: String? = null,

        @field:SerializedName("last_name")
        val last_name: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("phone_no")
        val phone_no: String? = null,

        @field:SerializedName("login_unique")
        val login_unique: String? = null,

    ):Serializable

    data class  Error(
        @field:SerializedName("msg")
        val msg: String? = null,


    ):Serializable

}
