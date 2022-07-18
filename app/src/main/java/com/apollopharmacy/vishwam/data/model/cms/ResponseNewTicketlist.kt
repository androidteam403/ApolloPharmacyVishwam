package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class ResponseNewTicketlist(
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
) : Serializable {
    data class Data(
        @field:SerializedName("listData")
        val listData: ListData

        ) : Serializable

    data class ListData(

        @field:SerializedName("records")
        val records: String? = null,

        @field:SerializedName("select")
        val select: Boolean,

        @field:SerializedName("total")
        val total: Int? = null,

        @field:SerializedName("page")
        val page: Int? = null,

        @field:SerializedName("zc_extra")
        val zc_extra: String? = null,

        @field:SerializedName("pivotData")
        val pivotData: String? = null,

        @field:SerializedName("aggregation")
        val aggregation: String? = null,

        @field:SerializedName("size")
        val size: Int? = null,

        @field:SerializedName("rows")
        val rows: ArrayList<Row>
    ) : Serializable

    data class Row(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("priority")
        var priority: Priority,

        @field:SerializedName("ticket_id")
        var ticket_id: String? = null,

        @field:SerializedName("parent")
        var parent: Parent,

        @field:SerializedName("cluster")
        var cluster: Cluster,

        @field:SerializedName("created_id")
        var created_id: CreatedId,

        @field:SerializedName("category")
        var category: Category,

        @field:SerializedName("department")
        var department: Department,

        @field:SerializedName("region")
        var region: Region,

        @field:SerializedName("reason")
        var reason: Reason,

        @field:SerializedName("level")
        var level: Level,

        @field:SerializedName("status")
        var status: Status,

        @field:SerializedName("site")
        var site: Site,

        @field:SerializedName("subcategory")
        var subcategory: Subcategory,

        @field:SerializedName("user")
        var user: User,

        @field:SerializedName("created_time")
        var created_time: String? = null,

        @field:SerializedName("modified_time")
        var modified_time: String? = null,

        @field:SerializedName("closed_date")
        var closed_date: String? = null,

        @field:SerializedName("problem_images")
        var problem_images: ProblemImages? = null,

        var isExpanded:Boolean,

        var Tickethistory:NewTicketHistoryResponse
    ) : Serializable

    data class  ProblemImages(
        @field:SerializedName("images")
        val images: ArrayList<Image>
    ):Serializable

    data class  Image(
        @field:SerializedName("url")
        val url: String? = null
    ):Serializable

    data class Priority(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("other")
        val other: Other,

        @field:SerializedName("icon")
        val icon: String? = null
    ) : Serializable

    data class Parent(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("ticket_id")
        val ticket_id: String? = null
    ) : Serializable

    data class  Cluster(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null
    ):Serializable

    data class  CreatedId(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("first_name")
        val first_name: String? = null,

        @field:SerializedName("last_name")
        val last_name: String? = null,

        @field:SerializedName("login_unique")
        val login_unique: String? = null,

        @field:SerializedName("role")
        val role: Role
        ):Serializable

    data class  Category(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null
        ):Serializable

    data class  Department(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null
    ):Serializable

    data class  Region(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null
    ):Serializable

    data class  Reason(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null
    ):Serializable

     data class  Level(
         @field:SerializedName("uid")
         val uid: String? = null,

         @field:SerializedName("code")
         val code: String? = null,

         @field:SerializedName("name")
         val name: String? = null
     ):Serializable

    data class  Status(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("background_color")
        val background_color: String? = null,

        @field:SerializedName("text_color")
        val text_color: String? = null
    ):Serializable

    data class  Site(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("site")
        val site: String? = null,

        @field:SerializedName("store_name")
        val store_name: String? = null
    ):Serializable


    data class  Subcategory(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("name")
        val name: String? = null
    ):Serializable

    data class  User(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("first_name")
        val first_name: String? = null,

        @field:SerializedName("last_name")
        val last_name: String? = null,

        @field:SerializedName("login_unique")
        val login_unique: String? = null
        ):Serializable

    data class  Role(

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("code")
        val code: String? = null

    ):Serializable

    data class  Other(

        @field:SerializedName("color")
        val color: String? = null

    ):Serializable

    //ticket Histroy Response.........
    data class NewTicketHistoryResponse(

        @field:SerializedName("message")
        var message: String? = null,

        @field:SerializedName("success")
        var success: Boolean,

        @field:SerializedName("data")
        var data: Data,

        @field:SerializedName("zcServerDateTime")
        var zcServerDateTime: String? = null,

        @field:SerializedName("zcServerIp")
        var zcServerIp: String? = null,

        @field:SerializedName("zcServerHost")
        var zcServerHost: String? = null


        ):Serializable
    {
        data class Data(
            @field:SerializedName("listData")
            var listData:ListData
        ) : Serializable

        data class  Status(
            @field:SerializedName("uid")
            var uid: String? = null,

            @field:SerializedName("code")
            var code: String? = null,

            @field:SerializedName("name")
            var name: String? = null,

            @field:SerializedName("background_color")
            var background_color: String? = null,

            @field:SerializedName("text_color")
            var text_color: String? = null
        ):Serializable

        data class ListData(
            @field:SerializedName("records")
            var records: String? = null,

            @field:SerializedName("select")
            var select: Boolean,

            @field:SerializedName("total")
            var total: Int? = null,

            @field:SerializedName("page")
            var page: Int? = null,

            @field:SerializedName("rows")
            var rows: ArrayList<Row>,

            @field:SerializedName("zc_extra")
            var zc_extra: String? = null,

            @field:SerializedName("pivotData")
            var pivotData: String? = null,

            @field:SerializedName("aggregation")
            var aggregation: String? = null,

            @field:SerializedName("size")
            var size: Int? = null

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

            var status:String?=null


        ):Serializable

        data class  Ticket(

            @field:SerializedName("uid")
            var uid: String? = null,

            @field:SerializedName("ticket_id")
            var ticket_id: String? = null,

            @field:SerializedName("ticket_workflow_history")
            var ticket_workflow_history: ArrayList<TicketWorkflowHistory>

        ):Serializable

        data class TicketWorkflowHistory(
            @field:SerializedName("uid")
            var uid: String? = null,

            @field:SerializedName("comment")
            var comment: String? = null
        ):Serializable

        data class CreatedId(

            @field:SerializedName("uid")
            var uid: String? = null,

            @field:SerializedName("first_name")
            var first_name: String? = null,

            @field:SerializedName("last_name")
            var last_name: String? = null,


            @field:SerializedName("role")
            var role: Role,


            @field:SerializedName("pic")
            var pic: ArrayList<Pic>

        ):Serializable

        data class Role(

            @field:SerializedName("uid")
            var uid: String? = null,

            @field:SerializedName("name")
            var name: String? = null
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

}



