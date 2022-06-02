package com.apollopharmacy.vishwam.data.model

data class ValidateOtpRequest(
    val EMPID: String,
    val DEVICE_ID: String,
    val FCM_KEY: String,
    val VERSION_NAME: String,
    val VERSION_NUMBER: String,
    val MOBILE_BRAND_NAME: String,
    val MOBILE_BRAND_VALUE: String,
    val TYPE: String,
)

