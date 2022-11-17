package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.fragment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetpendingAndApprovedListRequest() :Serializable{
    @SerializedName("EMPID")
    @Expose
    var empid: String? = null

    @SerializedName("FROMDATE")
    @Expose
    var fromdate: String? = null

    @SerializedName("TODATE")
    @Expose
    var todate: String? = null

    @SerializedName("STOREID")
    @Expose
    var storeId: String? = null

    @SerializedName("STARTPAGENO")
    @Expose
    var startpageno: Int? = null

    @SerializedName("ENDPAGENO")
    @Expose
    var endpageno: Int? = null


}