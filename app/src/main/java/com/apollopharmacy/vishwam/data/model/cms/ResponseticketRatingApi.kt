package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseticketRatingApi(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("zcServerDateTime")
    val zcServerDateTime: String? = null,

    @field:SerializedName("zcServerIp")
    val zcServerIp: String? = null,

    @field:SerializedName("zcServerHost")
    val zcServerHost: String? = null,
) : Serializable {

    data class Data(
        @field:SerializedName("listData")
        val listData: ListData,
    ) : Serializable

    data class ListData(

        @field:SerializedName("rows")
        val rows: ArrayList<Row>,
    ) : Serializable

    data class  Row(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("label")
        val label: String? = null,

        @field:SerializedName("value")
        val value: String? = null,

        @field:SerializedName("uid")
        val uid: String? = null,

        @field:SerializedName("icon")
        val icon: String? = null,

        @field:SerializedName("other")
        val other: Other,

        @field:SerializedName("isDefault")
        val isDefault: String? = null,

    ):Serializable

    data class  Other(
        @field:SerializedName("color")
        val color: String? = null,
    ):Serializable
}