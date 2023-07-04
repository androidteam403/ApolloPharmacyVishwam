package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.SerializedName

class TicketSubworkflowActionUpdateRequest {
    @field:SerializedName("comment")
    var comment: String? = null

    @field:SerializedName("uid")
    var uid: String? = null

    @field:SerializedName("subworkflow")
    var subworkflow: Subworkflow? = null

    @field:SerializedName("employee_id")
    var employee_id: String? = null

    @SerializedName("to_user")
    var toUser: ToUser? = null

    class Subworkflow {
        @field:SerializedName("uid")
        var uid: String? = null
    }

    class ToUser {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("first_name")
        var firstName: String? = null

        @SerializedName("last_name")
        var lastName: String? = null

        @SerializedName("middle_name")
        var middleName: String? = null

        @SerializedName("login_unique")
        var loginUnique: String? = null

        @SerializedName("role")
        var role: Role? = null

        @SerializedName("level")
        var level: Level? = null

    }

    class Level {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

    }

    class Role {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null

        @SerializedName("name")
        var name: String? = null

    }
}