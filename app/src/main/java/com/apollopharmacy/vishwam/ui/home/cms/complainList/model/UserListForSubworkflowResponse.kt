package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserListForSubworkflowResponse : Serializable {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: Data? = null

    class Data : Serializable {
        @SerializedName("listData")
        var listData: ListData? = null
    }

    class ListData : Serializable {

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

    class Rows : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("available_status")
        var availableStatus: AvailableStatus? = null

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

        @SerializedName("ou")
        var ou: Ou? = null

        @SerializedName("level")
        var level: Level? = null

        @SerializedName("department")
        var department: Department? = null

    }

    class Department : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("allocation_levels")
        var allocationLevels: Int? = null

    }

    class Level : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

    }

    class Ou : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("city")
        var city: City? = null

        @SerializedName("state")
        var state: State? = null

        @SerializedName("organization")
        var organization: Organization? = null

    }

    class Organization : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null

    }

    class State : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null
    }

    class City : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

    }

    class Role : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("code")
        var code: String? = null

        @SerializedName("name")
        var name: String? = null

    }

    class AvailableStatus : Serializable {

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("other")
        var other: Other? = null

        @SerializedName("icon")
        var icon: String? = null

    }
}
