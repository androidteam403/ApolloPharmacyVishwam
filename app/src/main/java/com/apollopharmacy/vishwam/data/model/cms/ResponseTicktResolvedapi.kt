package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import com.google.gson.annotations.Expose

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


        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("mailid")
        val mailid: String,
        @Expose
        @SerializedName("phone_no")
        val phone_no: String,
        @Expose
        @SerializedName("site")
        val site: String,
        @Expose
        @SerializedName("store_name")
        val store_name: String,
        @Expose
        @SerializedName("cluster")
        var cluster: Cluster,
        @Expose
        @SerializedName("executive")
        var executive: Executive,
        @Expose
        @SerializedName("ou")
        var ou: Ou,
        @Expose
        @SerializedName("region")
        var region: Region,
        @Expose
        @SerializedName("hod")
        var hod: Hod,
        @Expose
        @SerializedName("manager")
        var manager: Manager,
        @Expose
        @SerializedName("region_head")
        var region_head: Region_head
    ):Serializable

    data class Region_head(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("email")
        val email: String,
        @Expose
        @SerializedName("first_name")
        val first_name: String,
        @Expose
        @SerializedName("role")
        var role: Role
    )

    data class Role(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("code")
        val code: String
    )

    data class Manager(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("email")
        val email: String,
        @Expose
        @SerializedName("first_name")
        val first_name: String,
        @Expose
        @SerializedName("status")
        var status: Status,
        @Expose
        @SerializedName("login_unique")
        val login_unique: String,
        @Expose
        @SerializedName("role")
        var role: Role
    )



    data class Status(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("name")
        val name: String,
//        @Expose
//        @SerializedName("other")
//        var other: Other
    )

//    data class Other(
//
//    )

    data class Hod(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("email")
        val email: String,
        @Expose
        @SerializedName("role")
        var role: Role
    )



    data class Region(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("code")
        val code: String,
        @Expose
        @SerializedName("name")
        val name: String,
        @Expose
        @SerializedName("status")
        var status: Status?
    )



    data class Ou(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("code")
        val code: String,
        @Expose
        @SerializedName("name")
        val name: String,
        @Expose
        @SerializedName("organization")
        var organization: Organization,
        @Expose
        @SerializedName("state")
        var state: State
    )

    data class State(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("name")
        val name: String
    )

    data class Organization(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("name")
        val name: String,
        @Expose
        @SerializedName("code")
        val code: String
    )

    data class Executive(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("first_name")
        val first_name: String,
        @Expose
        @SerializedName("status")
        var status: Status,
        @Expose
        @SerializedName("login_unique")
        val login_unique: String,
        @Expose
        @SerializedName("role")
        var role: Role
    )

    data class Cluster(
        @Expose
        @SerializedName("uid")
        val uid: String,
        @Expose
        @SerializedName("code")
        val code: String,
        @Expose
        @SerializedName("name")
        val name: String,
        @Expose
        @SerializedName("status")
        var status: Status?
    )

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
