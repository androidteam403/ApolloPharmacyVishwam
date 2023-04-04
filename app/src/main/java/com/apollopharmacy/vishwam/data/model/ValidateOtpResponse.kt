package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateOtpResponse(
    @SerializedName("STATUS")
    @Expose
    var STATUS: Boolean,

    @SerializedName("MESSAGE")
    @Expose
    var MESSAGE: String,

    @SerializedName("OTP")
    @Expose
    var OTP: String,

    @SerializedName("MOBILENO")
    @Expose
    var MOBILENO: String,


//    val STATUS: Boolean,
//    val MESSAGE: String,
//    val OTP: String,
//    val MOBILENO: String,
)

