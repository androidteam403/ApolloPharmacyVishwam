package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class GetStorePersonHistoryodelRequest : Serializable {
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

    @SerializedName("STARTPAGENO")
    @Expose
    var startpageno: Int? = null

    @SerializedName("ENDPAGENO")
    @Expose
    var endpageno: Int? = null

    @SerializedName("STATUS")
    @Expose
    var status: String? = null
}