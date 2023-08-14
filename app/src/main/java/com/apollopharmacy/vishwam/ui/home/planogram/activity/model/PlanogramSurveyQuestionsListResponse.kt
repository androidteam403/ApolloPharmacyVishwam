package com.apollopharmacy.vishwam.ui.home.planogram.activity.model

import com.google.gson.annotations.SerializedName

class PlanogramSurveyQuestionsListResponse {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: Data? = null

    class Questions {

        @SerializedName("name")
        var name: String? = null

        @SerializedName("hintText")
        var hintText: String? = null

    }

    class CategoryType {

        @SerializedName("name")
        var name: String? = null

        @SerializedName("uid")
        var uid: String? = null

        @SerializedName("color")
        var color: String? = null

    }

    class Rows {

        @SerializedName("questions")
        var questions: ArrayList<Questions>? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("category_type")
        var categoryType: CategoryType? = CategoryType()

        @SerializedName("name")
        var name: String? = null

        var isExpanded: Boolean = false

    }

    class ListData {

        @SerializedName("records")
        var records: String? = null

        @SerializedName("select")
        var select: Boolean? = null

        @SerializedName("total")
        var total: Int? = null

        @SerializedName("page")
        var page: Int? = null

        @SerializedName("rows")
        var rows: ArrayList<Rows>? = null

        @SerializedName("zc_extra")
        var zcExtra: String? = null

        @SerializedName("pivotData")
        var pivotData: String? = null

        @SerializedName("aggregation")
        var aggregation: String? = null

        @SerializedName("size")
        var size: Int? = null

    }

    class Data {

        @SerializedName("listData")
        var listData: ListData? = null

    }
}