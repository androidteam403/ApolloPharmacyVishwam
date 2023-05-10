package com.apollopharmacy.vishwam.ui.home.apnarectro.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetRetroPendindAndApproverequest : Serializable {
    @SerializedName("STOREID")
    @Expose
    var storeid: String? = null

    @SerializedName("EMPID")
    @Expose
    var empid: String? = null

    @SerializedName("FROMDATE")
    @Expose
    var fromdate: String? = null

    @SerializedName("TODATE")
    @Expose
    var todate: String? = null
    fun withStoreid(storeid: String?): GetRetroPendindAndApproverequest {
        this.storeid = storeid
        return this
    }

    fun withEmpid(empid: String?): GetRetroPendindAndApproverequest {
        this.empid = empid
        return this
    }

    fun withFromdate(fromdate: String?): GetRetroPendindAndApproverequest {
        this.fromdate = fromdate
        return this
    }

    fun withTodate(todate: String?): GetRetroPendindAndApproverequest {
        this.todate = todate
        return this
    }
}