package com.apollopharmacy.vishwam.ui.home.apna.activity.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class StateListResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data : Serializable {
        @SerializedName("listData")
        @Expose
        var listData: ListData? = null

        class ListData : Serializable {
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

            class Row : Serializable {
                @SerializedName("uid")
                @Expose
                var uid: String? = null

                @SerializedName("code")
                @Expose
                var code: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("status")
                @Expose
                var status: Status? = null

                class Status : Serializable {
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
                    var icon: Any? = null

                    class Other : Serializable {
                        @SerializedName("color")
                        @Expose
                        var color: Any? = null
                    }
                }
            }
        }
    }
}