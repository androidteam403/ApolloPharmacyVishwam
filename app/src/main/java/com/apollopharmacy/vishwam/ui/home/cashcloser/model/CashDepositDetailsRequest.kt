package com.apollopharmacy.vishwam.ui.home.cashcloser.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CashDepositDetailsRequest {
    @SerializedName("SITEID")
    @Expose
    var siteid: String? = null

    @SerializedName("IMAGEURL")
    @Expose
    var imageurl: String? = null

    @SerializedName("AMOUNT")
    @Expose
    var amount: String? = null

    @SerializedName("REMARKS")
    @Expose
    var remarks: String? = null

    @SerializedName("DCID")
    @Expose
    var dcid: String? = null

    @SerializedName("CREATEDBY")
    @Expose
    var createdby: String? = null
}