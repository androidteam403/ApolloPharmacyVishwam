package com.apollopharmacy.vishwam.ui.home.notification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NotificationModelResponse : Serializable {
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

                    @SerializedName("description")
                    @Expose
                    var description: String? = null

                    @SerializedName("link")
                    @Expose
                    var link: String? = null

                    @SerializedName("title")
                    @Expose
                    var title: String? = null

                    @SerializedName("status")
                    @Expose
                    var status: Status? = null

                    @SerializedName("created_time")
                    @Expose
                    var createdTime: String? = null

                    inner class Status : Serializable {
                        @SerializedName("uid")
                        @Expose
                        var uid: String? = null

                        @SerializedName("name")
                        @Expose
                        var name: String? = null

                        @SerializedName("name_1")
                        @Expose
                        var name1: String? = null

                        @SerializedName("other")
                        @Expose
                        var other: Other? = null

                        @SerializedName("icon")
                        @Expose
                        var icon: Any? = null

                        inner class Other : Serializable {
                            @SerializedName("color")
                            @Expose
                            var color: Any? = null
                        }
                    }
                }
            }
        }

}