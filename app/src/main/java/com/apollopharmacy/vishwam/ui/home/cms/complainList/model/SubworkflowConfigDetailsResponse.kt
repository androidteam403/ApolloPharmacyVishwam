package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.SerializedName

class SubworkflowConfigDetailsResponse {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: Data? = null

    class Other {
        @SerializedName("color")
        var color: String? = null
    }

    class AssignToUser {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("other")
        var other: Other? = null

        @SerializedName("icon")
        var icon: String? = null
    }

    class ExtrnlDependency {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("other")
        var other: Other? = null

        @SerializedName("icon")
        var icon: String? = null
    }

    class ShowTicketActions {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("other")
        var other: Other? = null

        @SerializedName("icon")
        var icon: String? = null
    }

    class TicketEscalation {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("other")
        var other: Other? = null

        @SerializedName("icon")
        var icon: String? = null
    }

    class FromDept {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null
    }

    class Action {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("action")
        var action: String? = null

        @SerializedName("background_color")
        var backgroundColor: String? = null

        @SerializedName("code")
        var code: String? = null

        @SerializedName("icon")
        var icon: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("text_color")
        var textColor: String? = null
    }

    class ToRole {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null


    }

    class TicketStatus {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null

    }

    class Reason {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

    }

    class FromRole {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null
    }

    class ToDept {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null
    }

    class Rows {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("action_status")
        var actionStatus: String? = null

        @SerializedName("assign_to_user")
        var assignToUser: AssignToUser? = null

        @SerializedName("extrnl_api")
        var extrnlApi: String? = null

        @SerializedName("extrnl_dependency")
        var extrnlDependency: ExtrnlDependency? = null

        @SerializedName("seq_order")
        var seqOrder: Int? = null

        @SerializedName("show_ticket_actions")
        var showTicketActions: ShowTicketActions? = null

        @SerializedName("step_order")
        var stepOrder: Int? = null

        @SerializedName("ticket_escalation")
        var ticketEscalation: TicketEscalation? = null

        @SerializedName("from_dept")
        var fromDept: FromDept? = null

        @SerializedName("action")
        var action: Action? = null

        @SerializedName("to_role")
        var toRole: ToRole? = null

        @SerializedName("ticket_status")
        var ticketStatus: TicketStatus? = null

        @SerializedName("reason")
        var reason: Reason? = null

        @SerializedName("from_role")
        var fromRole: FromRole? = null

        @SerializedName("to_dept")
        var toDept: ToDept? = null
    }

    class ListData {
        @SerializedName("records")
        var records: String? = null

        @SerializedName("select")
        var select: Boolean? = null

        @SerializedName("total")
        var total: Int? = null

        @SerializedName("page")
        var page: Int? = null

        @SerializedName("rows")
        var rows: ArrayList<Rows>? = null

        @SerializedName("zc_extra")
        var zcExtra: String? = null

        @SerializedName("pivotData")
        var pivotData: String? = null

        @SerializedName("aggregation")
        var aggregation: String? = null

        @SerializedName("size")
        var size: Int? = null
    }

    class Data {
        @SerializedName("listData")
        var listData: ListData? = ListData()
    }
}
