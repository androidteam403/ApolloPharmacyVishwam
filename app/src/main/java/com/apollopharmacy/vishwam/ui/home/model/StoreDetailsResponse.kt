package com.apollopharmacy.vishwam.ui.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StoreDetailsResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    fun withMessage(message: Any?): StoreDetailsResponse {
        this.message = message
        return this
    }

    fun withSuccess(success: Boolean?): StoreDetailsResponse {
        this.success = success
        return this
    }

    fun withData(data: Data?): StoreDetailsResponse {
        this.data = data
        return this
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

    inner class DcCode : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withUid(uid: String?): DcCode {
            this.uid = uid
            return this
        }

        fun withCode(code: String?): DcCode {
            this.code = code
            return this
        }

        fun withName(name: String?): DcCode {
            this.name = name
            return this
        }
    }

    inner class District : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null
        fun withUid(uid: String?): District {
            this.uid = uid
            return this
        }

        fun withName(name: String?): District {
            this.name = name
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

    inner class Row : Serializable {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("mailid")
        @Expose
        var mailid: Any? = null

        @SerializedName("pincode")
        @Expose
        var pincode: Int? = null

        @SerializedName("site")
        @Expose
        var site: String? = null

        @SerializedName("store_name")
        @Expose
        var storeName: String? = null

        @SerializedName("dc_code")
        @Expose
        var dcCode: DcCode? = null

        @SerializedName("district")
        @Expose
        var district: District? = null

        @SerializedName("state")
        @Expose
        var state: State? = null

        @SerializedName("address")
        @Expose
        var address: String? = null
        fun withUid(uid: String?): Row {
            this.uid = uid
            return this
        }

        fun withCity(city: String?): Row {
            this.city = city
            return this
        }

        fun withMailid(mailid: Any?): Row {
            this.mailid = mailid
            return this
        }

        fun withPincode(pincode: Int?): Row {
            this.pincode = pincode
            return this
        }

        fun withSite(site: String?): Row {
            this.site = site
            return this
        }

        fun withStoreName(storeName: String?): Row {
            this.storeName = storeName
            return this
        }

        fun withDcCode(dcCode: DcCode?): Row {
            this.dcCode = dcCode
            return this
        }

        fun withDistrict(district: District?): Row {
            this.district = district
            return this
        }

        fun withState(state: State?): Row {
            this.state = state
            return this
        }

        fun withAddress(address: String?): Row {
            this.address = address
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
        fun withUid(uid: String?): State {
            this.uid = uid
            return this
        }

        fun withName(name: String?): State {
            this.name = name
            return this
        }
    }
}