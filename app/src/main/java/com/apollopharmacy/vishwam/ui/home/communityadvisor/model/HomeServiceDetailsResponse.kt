package com.apollopharmacy.vishwam.ui.home.communityadvisor.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HomeServiceDetailsResponse : Serializable {
    @SerializedName("detlist")
    var detlist: ArrayList<Detlist> = arrayListOf()

    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("message")
    var message: String? = null

    class Detlist {
        @SerializedName("uniqueId")
        var uniqueId: String? = null

        @SerializedName("serviceDate")
        var serviceDate: String? = null

        @SerializedName("customerName")
        var customerName: String? = null

        @SerializedName("customerMobileno")
        var customerMobileno: String? = null

        @SerializedName("createdBy")
        var createdBy: String? = null

        @SerializedName("serviceId")
        var serviceId: String? = null

        @SerializedName("serviceName")
        var serviceName: String? = null

        @SerializedName("siteId")
        var siteId: String? = null

        @SerializedName("customerInteractionid")
        var customerInteractionid: String? = null

        @SerializedName("customerInteractionremarks")
        var customerInteractionremarks: String? = null

        @SerializedName("others")
        var others: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("serviceType")
        var serviceType: String? = null

    }
}