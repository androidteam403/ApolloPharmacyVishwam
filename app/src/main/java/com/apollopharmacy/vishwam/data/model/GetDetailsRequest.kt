package com.apollopharmacy.vishwam.data.model

data class GetDetailsRequest(
    val REQUESTURL: String,
    val REQUESTTYPE: String,
    val REQUESTJSON: String,
    val HEADERTOKENKEY: String,
    val HEADERTOKENVALUE: String
)