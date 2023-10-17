package com.apollopharmacy.vishwam.ui.home.apna.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SurveyListResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    fun withMessage(message: Any?): SurveyListResponse {
        this.message = message
        return this
    }

    fun withSuccess(success: Boolean?): SurveyListResponse {
        this.success = success
        return this
    }

    fun withData(data: Data?): SurveyListResponse {
        this.data = data
        return this
    }

    inner class City : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withUid(uid: String?): City {
            this.uid = uid
            return this
        }

        fun withName(name: String?): City {
            this.name = name
            return this
        }
    }

    inner class CreatedId : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("first_name")
        @Expose
        var firstName: String? = null

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null

        @SerializedName("login_unique")
        @Expose
        var loginUnique: String? = null

        @SerializedName("middle_name")
        @Expose
        var middleName: String? = null

        fun withUid(uid: String?): CreatedId {
            this.uid = uid
            return this
        }

        fun withFirstName(firstName: String?): CreatedId {
            this.firstName = firstName
            return this
        }

        fun withLastName(lastName: String?): CreatedId {
            this.lastName = lastName
            return this
        }

        fun withLoginUnique(loginUnique: String?): CreatedId {
            this.loginUnique = loginUnique
            return this
        }
    }

    inner class Data : Serializable {
        @SerializedName("listData")
        @Expose
        var listData: ListData? = null
        fun withListData(listData: ListData?): Data {
            this.listData = listData
            return this
        }
    }

    inner class ListData : Serializable {
        @SerializedName("records")
        @Expose
        var records: String? = null

        @SerializedName("select")
        @Expose
        var select: Boolean? = null

        @SerializedName("total")
        @Expose
        var total: Int? = null

        @SerializedName("page")
        @Expose
        var page: Int? = null

        @SerializedName("rows")
        @Expose
        var rows: List<Row>? = null

        @SerializedName("zc_extra")
        @Expose
        var zcExtra: Any? = null

        @SerializedName("pivotData")
        @Expose
        var pivotData: Any? = null

        @SerializedName("aggregation")
        @Expose
        var aggregation: Any? = null

        @SerializedName("size")
        @Expose
        var size: Int? = null
        fun withRecords(records: String?): ListData {
            this.records = records
            return this
        }

        fun withSelect(select: Boolean?): ListData {
            this.select = select
            return this
        }

        fun withTotal(total: Int?): ListData {
            this.total = total
            return this
        }

        fun withPage(page: Int?): ListData {
            this.page = page
            return this
        }

        fun withRows(rows: List<Row>?): ListData {
            this.rows = rows
            return this
        }

        fun withZcExtra(zcExtra: Any?): ListData {
            this.zcExtra = zcExtra
            return this
        }

        fun withPivotData(pivotData: Any?): ListData {
            this.pivotData = pivotData
            return this
        }

        fun withAggregation(aggregation: Any?): ListData {
            this.aggregation = aggregation
            return this
        }

        fun withSize(size: Int?): ListData {
            this.size = size
            return this
        }
    }

    inner class Location : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withUid(uid: String?): Location {
            this.uid = uid
            return this
        }

        fun withName(name: String?): Location {
            this.name = name
            return this
        }
    }

    inner class Other : Serializable {
        @SerializedName("color")
        @Expose
        var color: String? = null
        fun withColor(color: String?): Other {
            this.color = color
            return this
        }
    }

    public class Row : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("status")
        @Expose
        var status: Status? = null

        @SerializedName("survey_id")
        @Expose
        var surveyId: String? = null

        @SerializedName("created_id")
        @Expose
        var createdId: CreatedId? = null

        @SerializedName("surveyed_on")
        @Expose
        var surveyed_on: String? = null


//        @SerializedName("location")
//        @Expose
//        var location: Location? = null

//        @SerializedName("city")
//        @Expose
//        var city: City? = null

//        @SerializedName("state")
//        @Expose
//        var state: State? = null

//        @SerializedName("state")
//        @Expose
//        var state: String? = null

        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("state")
        @Expose
        var state: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("created_time")
        @Expose
        var createdTime: String? = null

        @SerializedName("modified_time")
        @Expose
        var modifiedTime: String? = null

        @SerializedName("landmarks")
        @Expose
        var landmarks: String? = null

        @SerializedName("pincode")
        @Expose
        var pincode: Int? = null

        var isLoading: String? = null
        fun withUid(uid: String?): Row {
            this.uid = uid
            return this
        }

        fun withStatus(status: Status?): Row {
            this.status = status
            return this
        }

        fun withSurveyId(surveyId: String?): Row {
            this.surveyId = surveyId
            return this
        }

        fun withCreatedId(createdId: CreatedId?): Row {
            this.createdId = createdId
            return this
        }

//        fun withLocation(location: Location?): Row {
//            this.location = location
//            return this
//        }

//        fun withCity(city: City?): Row {
//            this.city = city
//            return this
//        }

//        fun withState(state: State?): Row {
//            this.state = state
//            return this
//        }

        fun withCreatedTime(createdTime: String?): Row {
            this.createdTime = createdTime
            return this
        }

        fun withModifiedTime(modifiedTime: String?): Row {
            this.modifiedTime = modifiedTime
            return this
        }
    }

    inner class State : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
//        fun withUid(uid: String?): State {
//            this.uid = uid
//            return this
//        }

//        fun withName(name: String?): State {
//            this.name = name
//            return this
//        }
    }

    inner class Status : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("other")
        @Expose
        var other: Other? = null

        @SerializedName("icon")
        @Expose
        var icon: String? = null

        @SerializedName("background_color")
        @Expose
        var backgroundColor: String? = null

        @SerializedName("text_color")
        @Expose
        var textColor: String? = null

        fun withUid(uid: String?): Status {
            this.uid = uid
            return this
        }

        fun withName(name: String?): Status {
            this.name = name
            return this
        }

        fun withOther(other: Other?): Status {
            this.other = other
            return this
        }

        fun withIcon(icon: String?): Status {
            this.icon = icon
            return this
        }
    }
}