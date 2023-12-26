package com.apollopharmacy.vishwam.ui.home.communityadvisor.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HomeServicesSaveDetailsRequest : Serializable {
    @SerializedName("serviceDate")
    var serviceDate: String? = null

    @SerializedName("customername")
    var customername: String? = null

    @SerializedName("customerMobileno")
    var customerMobileno: String? = null

    @SerializedName("createdby")
    var createdby: String? = null

    @SerializedName("others")
    var others: String? = null

    @SerializedName("serviceid")
    var serviceid: String? = null

    @SerializedName("siteid")
    var siteid: String? = null

    @SerializedName("customerInteractionid")
    var customerInteractionid: String? = null

    @SerializedName("customerInteractionremarks")
    var customerInteractionremarks: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("serviceType")
    var serviceType: String?=null

    @SerializedName("serviceName")
    var serviceName:String?=null
}
