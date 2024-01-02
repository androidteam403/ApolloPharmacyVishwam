package com.apollopharmacy.vishwam.ui.home.communityadvisor.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetServicesCustomerResponse : Serializable {

    @SerializedName("listServices")
    var listServices: ArrayList<ListServices>? = null

    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("message")
    var message: String? = null

    class ListServices {

        @SerializedName("serviceId")
        var serviceId: String? = null

        @SerializedName("serviceName")
        var serviceName: String? = null

        @SerializedName("type")
        var type: String? = null
        var isSelected: Boolean? = false

    }

}
