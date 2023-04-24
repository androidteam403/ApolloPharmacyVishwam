package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateOtpRequest(

    @SerializedName("EMPID")
    @Expose
    var EMPID: String,

    @SerializedName("DEVICE_ID")
    @Expose
    var DEVICE_ID: String,

    @SerializedName("FCM_KEY")
    @Expose
    var FCM_KEY: String,
    @SerializedName("VERSION_NAME")
    @Expose
    var VERSION_NAME: String,
    @SerializedName("VERSION_NUMBER")
    @Expose
    var VERSION_NUMBER: String,
    @SerializedName("MOBILE_BRAND_NAME")
    @Expose
    var MOBILE_BRAND_NAME: String,
    @SerializedName("MOBILE_BRAND_VALUE")
    @Expose
    var MOBILE_BRAND_VALUE: String,

    @SerializedName("TYPE")
    @Expose
    var TYPE: String,

//    val EMPID: String,
//    val DEVICE_ID: String,
//    val FCM_KEY: String,
//    val VERSION_NAME: String,
//    val VERSION_NUMBER: String,
//    val MOBILE_BRAND_NAME: String,
//    val MOBILE_BRAND_VALUE: String,
//    val TYPE: String,
)

