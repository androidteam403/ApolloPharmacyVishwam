package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetStorePersonHistoryodelResponse : Serializable {
    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    @SerializedName("STATUS")
    @Expose
    var status: Boolean? = null

    @SerializedName("getList")
    @Expose
    var getList: ArrayList<Get>? = null

    inner class Get : Serializable {
        @SerializedName("SWACHHID")
        @Expose
        var swachhid: String? = null

        @SerializedName("STORE_ID")
        @Expose
        var storeId: String? = null

        @SerializedName("UPLOADED_BY")
        @Expose
        var uploadedBy: String? = null

        @SerializedName("APPROVED_BY")
        @Expose
        var approvedBy: String? = null

        @SerializedName("RESHOOT_BY")
        @Expose
        var reshootBy: String? = null

        @SerializedName("PARTIALLY_APPROVED_BY")
        @Expose
        var partiallyApprovedBy: String? = null

        @SerializedName("UPLOADED_DATE")
        @Expose
        var uploadedDate: String? = null

        @SerializedName("APPROVED_DATE")
        @Expose
        var approvedDate: String? = null

        @SerializedName("RESHOOT_DATE")
        @Expose
        var reshootDate: String? = null

        @SerializedName("PARTIALLY_APPROVED_DATE")
        @Expose
        var partiallyApprovedDate: String? = null

        @SerializedName("STATUS")
        @Expose
        var status: String? = null
    }
}