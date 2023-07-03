package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist
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

        @field:SerializedName("ticket_subworkflow_info")
        var ticketSubworkflowInfo: ResponseNewTicketlist.TicketSubworkflowInfo? = null

        @field:SerializedName("ticket_subworkflow_history")
        var ticket_subworkflow_history: ArrayList<ResponseNewTicketlist.TicketSubworkflowHistory>? =
            null
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
//    class TicketSubworkflowInfo {
//        @field:SerializedName("uid")
//        val uid: String? = null
//
//        @field:SerializedName("assigned_to")
//        val assigned_to: ResponseNewTicketlist.AssignedTo? = null
//
//        @field:SerializedName("subworkflow_step_order")
//        val subworkflow_step_order: Int? = null
//
//        @field:SerializedName("subworkflow_dept")
//        val subworkflow_dept: ResponseNewTicketlist.SubworkflowDept? = null
//
//        @field:SerializedName("subworkflow_role")
//        val subworkflow_role: ResponseNewTicketlist.SubworkflowRole? = null
//
//        @field:SerializedName("subworkflow_action")
//        val subworkflow_action: ResponseNewTicketlist.SubworkflowAction? = null
//    }
//
//    //subworkflow actions history......
//    class TicketSubworkflowHistory : Serializable {
//        @SerializedName("uid")
//        var uid: String? = null
//
//        @SerializedName("status")
//        var status: String? = null
//
//        @SerializedName("step_order")
//        var stepOrder: Int? = null
//
//        @SerializedName("action")
//        var action: ResponseNewTicketlist.Action? = null
//
//        @SerializedName("created_id")
//        var createdId: ResponseNewTicketlist.CreatedIdObj? = null
//
//        @SerializedName("role")
//        var role: ResponseNewTicketlist.RoleObj? = null
//
//        @SerializedName("created_time")
//        var createdTime: String? = null
//
//        @SerializedName("department")
//        var department: ResponseNewTicketlist.DepartmentObj? = null
//
//        @SerializedName("ticket_status")
//        var ticket_status: ResponseNewTicketlist.TicketStatus? = null
//
//
//    }