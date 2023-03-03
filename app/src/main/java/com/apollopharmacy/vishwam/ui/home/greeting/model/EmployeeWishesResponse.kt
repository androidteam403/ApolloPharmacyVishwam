package com.apollopharmacy.vishwam.ui.home.greeting.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmployeeWishesResponse {
    @SerializedName("InsertEMPResult")
    @Expose
    var insertEMPResult: InsertEMPResult? = null

    class InsertEMPResult {
        @SerializedName("Message")
        @Expose
        var Message: String? = null

        @SerializedName("Status")
        @Expose
        var Status: Boolean? = null
    }
}