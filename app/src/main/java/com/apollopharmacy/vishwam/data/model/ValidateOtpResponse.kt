package com.apollopharmacy.vishwam.data.model

data class ValidateOtpResponse(
    val STATUS: Boolean,
    val MESSAGE: String,
    val OTP: String,
    val MOBILENO: String,
)

