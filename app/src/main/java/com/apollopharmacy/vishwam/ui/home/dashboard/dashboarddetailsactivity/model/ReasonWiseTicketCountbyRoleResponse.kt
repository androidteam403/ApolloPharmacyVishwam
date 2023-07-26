package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class ReasonWiseTicketCountbyRoleResponse {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: Data? = null


    class Rows {
        //        var rowsList: Map<String, ArrayList<Rows>?>? = null
        var value: Map<String, String>? = null

        /*  @SerializedName("name")
          var name: String? = null

          @SerializedName("Data is irrevalant")
          var dataisirrevalant: Int? = null

          @SerializedName("Brand Image")
          var brandImage: Int? = null

          @SerializedName("Offline billing amount mismatched")
          var offlineBillingAmountMismatched: Int? = null

          @SerializedName("Poor Customer Service Experiences")
          var poorCustomerServiceExperiences: Int? = null

          @SerializedName("Dates expire data mismatched")
          var datesExpireDataMismatched: Int? = null

          @SerializedName("sample")
          var sample: Int? = null

          @SerializedName("Acessories online sales")
          var acessoriesOnlinesales: Int? = null

          @SerializedName("Billing software not working properly")
          var billingSoftwareNotWorkingProperly: Int? = null

          @SerializedName("Digital Marketing")
          var digitalMarketing: Int? = null

          @SerializedName("Total")
          var Total: Int? = null*/

    }

    class Data1 {

        @SerializedName("name")
        var name: String? = null

        @SerializedName("code")
        var code: String? = null

        var isSelsected: Boolean? = false

        var isDescending: Boolean? = false

    }

    class ZcExtra {
        @SerializedName("data1")
        var data1: ArrayList<Data1>? = null

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
//        var rows: ArrayList<Rows>? = null
        var rows: ArrayList<JsonObject>? = null

        @SerializedName("zc_extra")
        var zcExtra: ZcExtra? = null

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