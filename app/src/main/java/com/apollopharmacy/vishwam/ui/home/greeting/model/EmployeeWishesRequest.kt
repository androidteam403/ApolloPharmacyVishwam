package com.apollopharmacy.vishwam.ui.home.greeting.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmployeeWishesRequest {
    @SerializedName("empdetails")
    @Expose
    var empdetails: EmpDetails? = null

    class EmpDetails {
        @SerializedName("EmpID")
        @Expose
        var EmpID: String? = null

        @SerializedName("EmpName")
        @Expose
        var EmpName: String? = null

        @SerializedName("ImageUrl")
        @Expose
        var ImageUrl: String? = null

        @SerializedName("SignatureUrl")
        @Expose
        var SignatureUrl: String? = null

        @SerializedName("Siteid")
        @Expose
        var Siteid: Int? = null

        @SerializedName("SiteName")
        @Expose
        var SiteName: String? = null

        @SerializedName("Contents")
        @Expose
        var Contents: String? = null

        @SerializedName("organization")
        @Expose
        var organization: String? = null

        @SerializedName("Location")
        @Expose
        var Location: String? = null

        @SerializedName("Mobilenumber")
        @Expose
        var Mobilenumber: String? = null
    }
}