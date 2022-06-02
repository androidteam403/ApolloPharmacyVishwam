package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewTicketHistoryResponse(

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
    val zcServerHost: String? = null,


):Serializable
{
    data class Data(
        @field:SerializedName("listData")
        val listData:ListData
        ) : Serializable

    data class  Status(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        var name: String? = null,

        @field:SerializedName("background_color")
        val background_color: String? = null,

        @field:SerializedName("text_color")
        val text_color: String? = null,
    ):Serializable

    data class ListData(
        @field:SerializedName("records")
        val records: String? = null,

        @field:SerializedName("select")
        val select: Boolean,

        @field:SerializedName("total")
        val total: Int? = null,

        @field:SerializedName("page")
        val page: Int? = null,

        @field:SerializedName("rows")
        var rows: ArrayList<Row>,

        @field:SerializedName("zc_extra")
        val zc_extra: String? = null,

        @field:SerializedName("pivotData")
        val pivotData: String? = null,

        @field:SerializedName("aggregation")
        val aggregation: String? = null,

        @field:SerializedName("size")
        val size: Int? = null

    ):Serializable

   data class  Row(

       @field:SerializedName("uid")
       var uid: String? = null,

       @field:SerializedName("description")
       var description: String? = null,

       @field:SerializedName("ticket")
       var ticket:Ticket,

       @field:SerializedName("created_id")
       var created_id:CreatedId,

       @field:SerializedName("workflow_comment")
       var workflow_comment: String? = null,

       @field:SerializedName("created_time")
       var created_time: String? = null,

      // var status:Status


   ):Serializable

    data class  Ticket(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("ticket_id")
        val ticket_id: String? = null,

        @field:SerializedName("ticket_workflow_history")
        val ticket_workflow_history: ArrayList<String>

    ):Serializable

    data class CreatedId(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("first_name")
        val first_name: String? = null,

        @field:SerializedName("last_name")
        val last_name: String? = null,


        @field:SerializedName("role")
        val role: Role,


        @field:SerializedName("pic")
        val pic: ArrayList<Pic>

        ):Serializable

 data class Role(

     @field:SerializedName("uid")
     val uid: String? = null,

     @field:SerializedName("name")
     val name: String? = null
 ):Serializable

    data class Pic(
        @field:SerializedName("size")
        val size: Int? = null,

        @field:SerializedName("saved")
        val saved: Boolean,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("contentType")
        val contentType: String? = null,

        @field:SerializedName("dimenesions")
        val dimenesions:Dimenesions,

        @field:SerializedName("path")
        val path: String? = null,

        @field:SerializedName("fullPath")
        val fullPath: String? = null,

        @field:SerializedName("created_info")
        val created_info: CreatedInfo
        ):Serializable

data class  Dimenesions(

    @field:SerializedName("200_200")
    val image_200_200: String? = null,

    @field:SerializedName("200_200_fullPath")
    val fullPath_200_200: String? = null,

    @field:SerializedName("100_100")
    val image100_100: String? = null,

    @field:SerializedName("100_100_fullPath")
    val fullPath_100_100: String? = null
    ):Serializable

    data class  CreatedInfo(
        @field:SerializedName("created_on")
        val created_on: String? = null,

        @field:SerializedName("user_id")
        val user_id: String? = null,

        @field:SerializedName("app_user_id")
        val app_user_id: String? = null,

        @field:SerializedName("user_code")
        val user_code: String? = null,

        @field:SerializedName("user_name")
        val user_name: String? = null,

        @field:SerializedName("login_unique")
        val login_unique: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("role_code")
        val role_code: String? = null,

        @field:SerializedName("role_name")
        val role_name: String? = null

    ):Serializable


}
