package com.apollopharmacy.vishwam.ui.home.planogram.activity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ListBySiteIdResponse : Serializable {
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

                @SerializedName("areas_to_focus_on")
                @Expose
                var areasToFocusOn: String? = null

                @SerializedName("branch_name")
                @Expose
                var branchName: String? = null

                @SerializedName("categories_to_focus_on")
                @Expose
                var categoriesToFocusOn: String? = null

                @SerializedName("site")
                @Expose
                var site: Site? = null

                @SerializedName("overall_score")
                @Expose
                var overallScore: Any? = null

                @SerializedName("created_time")
                @Expose
                var createdTime: String? = null

                @SerializedName("date")
                @Expose
                var date: String? = null

                inner class Site : Serializable {
                    @SerializedName("uid")
                    @Expose
                    var uid: String? = null

                    @SerializedName("site")
                    @Expose
                    var site: String? = null

                    @SerializedName("executive")
                    @Expose
                    var executive: Executive? = null

                    inner class Executive : Serializable {
                        @SerializedName("uid")
                        @Expose
                        var uid: String? = null

                        @SerializedName("first_name")
                        @Expose
                        var firstName: String? = null

                        @SerializedName("last_name")
                        @Expose
                        var lastName: String? = null
                    }
                }

            }
        }
    }
}
