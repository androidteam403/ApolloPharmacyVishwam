package com.apollopharmacy.vishwam.ui.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreDetailsModelResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("zcServerIp")
    val zcServerIP: String? = null,

    @field:SerializedName("zcServerHost")
    val zcServerHost: String? = null,

    @field:SerializedName("data")
    val data: Data,
) : Serializable {
    data class Data(
        @field:SerializedName("listData")
        val listdata: ListData,
    ) : Serializable

    data class ListData(
        @field:SerializedName("records")
        val records: String? = null,

        @field:SerializedName("select")
        val select: Boolean? = null,

        @field:SerializedName("page")
        val page: Int? = null,

        @field:SerializedName("pivotData")
        val pivotData: Object? = null,

        @field:SerializedName("aggregation")
        val aggregation: Object? = null,

        @field:SerializedName("size")
        val size: Int? = null,

        @field:SerializedName("rows")
        val rows: List<Row>,

        ) : Serializable

    data class Row(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("site")
        val site: String? = null,

        @field:SerializedName("store_name")
        val storeName: String? = null,

        @field:SerializedName("district")
        val district: District,

        @field:SerializedName("state")
        val state: State,

        @field:SerializedName("dc_code")
        val dcCode: DcCode,

        @field:SerializedName("pincode")
        val pincode: String? = null,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("mailid")
        val mailid: Any? = null,

        @field:SerializedName("address")
        val address: String? = null,) : Serializable

    data class District(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("name")
        val name: String? = null,
    ) : Serializable

    data class DcCode(
        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("name")
        val name: String? = null,
        @field:SerializedName("code")
        val code: String? = null,
    ) : Serializable

    data class State(
        @field:SerializedName("uid")
        val uid: String? = null,


        @field:SerializedName("name")
        var name: String? = null,

        ) : Serializable



}