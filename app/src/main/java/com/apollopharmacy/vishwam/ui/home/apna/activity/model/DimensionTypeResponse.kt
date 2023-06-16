package com.apollopharmacy.vishwam.ui.home.apna.activity.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class DimensionTypeResponse {
    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

     class Data {
        @SerializedName("listData")
        @Expose
        var listData: ListData? = null

         class ListData {
            @SerializedName("rows")
            @Expose
            var rows: List<Row>? = null

              class Row {
                @SerializedName("name")
                @Expose
                var name: String? = null

                @SerializedName("label")
                @Expose
                var label: String? = null

                @SerializedName("value")
                @Expose
                var value: String? = null

                @SerializedName("uid")
                @Expose
                var uid: String? = null

                @SerializedName("icon")
                @Expose
                var icon: Any? = null

                @SerializedName("other")
                @Expose
                var other: Any? = null

                @SerializedName("isDefault")
                @Expose
                var isDefault: Any? = null
            }
        }
    }
}