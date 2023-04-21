package com.apollopharmacy.vishwam.ui.home.apna.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocationListResponse {
    @SerializedName("message")
    @Expose
    var message: Any? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("listData")
        @Expose
        var listData: ListData? = null

        inner class ListData {
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

            inner class Row {
                @SerializedName("uid")
                @Expose
                var uid: String? = null

                @SerializedName("code")
                @Expose
                var code: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("city")
                @Expose
                var city: City? = null

                @SerializedName("region")
                @Expose
                var region: Region? = null

                inner class City {
                    @SerializedName("uid")
                    @Expose
                    var uid: String? = null

                    @SerializedName("code")
                    @Expose
                    var code: String? = null

                    @SerializedName("name")
                    @Expose
                    var name: String? = null

                    @SerializedName("state")
                    @Expose
                    var state: State? = null

                    inner class State {
                        @SerializedName("uid")
                        @Expose
                        var uid: String? = null

                        @SerializedName("code")
                        @Expose
                        var code: String? = null

                        @SerializedName("name")
                        @Expose
                        var name: String? = null
                    }
                }

                inner class Region {
                    @SerializedName("uid")
                    @Expose
                    var uid: String? = null

                    @SerializedName("name")
                    @Expose
                    var name: String? = null
                }
            }
        }
    }
}