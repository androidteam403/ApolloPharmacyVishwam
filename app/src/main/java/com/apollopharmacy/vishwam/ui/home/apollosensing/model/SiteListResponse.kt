package com.apollopharmacy.vishwam.ui.home.apollosensing.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class SiteListResponse : Serializable {
    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    fun getMessage(): Any? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

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
            private var zcExtra: String? = null

            @SerializedName("pivotData")
            @Expose
            private var pivotData: String? = null

            @SerializedName("aggregation")
            @Expose
            private var aggregation: String? = null

            @SerializedName("size")
            @Expose
            var size: Int? = null
            fun getZcExtra(): Any? {
                return zcExtra
            }

            fun setZcExtra(zcExtra: String?) {
                this.zcExtra = zcExtra
            }

            fun getPivotData(): Any? {
                return pivotData
            }

            fun setPivotData(pivotData: String?) {
                this.pivotData = pivotData
            }

            fun getAggregation(): Any? {
                return aggregation
            }

            fun setAggregation(aggregation: String?) {
                this.aggregation = aggregation
            }

            inner class Row : Serializable {
                @SerializedName("uid")
                @Expose
                var uid: String? = null

                @SerializedName("site")
                @Expose
                var site: String? = null

                @SerializedName("store_name")
                @Expose
                var storeName: String? = null

                @SerializedName("dc_code")
                @Expose
                var dcCode: DcCode? = null

                @SerializedName("cluster")
                @Expose
                var cluster: Cluster? = null

                inner class Cluster : Serializable {
                    @SerializedName("uid")
                    @Expose
                    var uid: String? = null

                    @SerializedName("name")
                    @Expose
                    var name: String? = null
                }

                inner class DcCode : Serializable {
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