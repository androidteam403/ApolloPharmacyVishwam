package com.apollopharmacy.vishwam.ui.home.champs.survey.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GlobalConfigurationResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data : Serializable {
        @SerializedName("listData")
        @Expose
        var listData: ListData? = null

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

            inner class Row : Serializable {
                @SerializedName("uid")
                @Expose
                var uid: String? = null

                @SerializedName("key")
                @Expose
                var key: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("value")
                @Expose
                var value: String? = null


            }
        }
    }
}