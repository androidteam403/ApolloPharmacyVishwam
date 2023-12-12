package com.apollopharmacy.vishwam.ui.home.cms.registration.model

import com.google.gson.annotations.SerializedName

class SiteTicketbyReasonResponse {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: Data? = null

    class Data {

        @SerializedName("listData")
        var listData: ListData? = null

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

    class Rows {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("ticket_id")
        var ticketId: String? = null

        @SerializedName("reason")
        var reason: Reason? = null

        @SerializedName("created_id")
        var createdId: CreatedId? = null

        @SerializedName("site")
        var site: Site? = null

        @SerializedName("status")
        var status: Status? = null

        @SerializedName("created_time")
        var createdTime: String? = null

    }

    class Status {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("background_color")
        var backgroundColor: String? = null

        @SerializedName("code")
        var code: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("text_color")
        var textColor: String? = null

    }

    class Site {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("site")
        var site: String? = null

    }

    class CreatedId {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("first_name")
        var firstName: String? = null

        @SerializedName("last_name")
        var lastName: String? = null

    }

    class Reason {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("allow_duplicate_st_creation")
        var allowDuplicateStCreation: AllowDuplicateStCreation? = null

        @SerializedName("name")
        var name: String? = null

    }

    class AllowDuplicateStCreation {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("name_1")
        var name1: String? = null

        @SerializedName("other")
        var other: Other? = null

        @SerializedName("icon")
        var icon: String? = null

    }

    class Other {

        @SerializedName("color")
        var color: String? = null

    }
}