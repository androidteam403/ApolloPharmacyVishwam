package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName

data class AtdLogInOutReq(
    @field:SerializedName("EMPID")
    val empId: String,

    @field:SerializedName("LATITUDE")
    val latitude: String,

    @field:SerializedName("LONGITUDE")
    val longitude: String,

    @field:SerializedName("IMAGEURL")
    val imageUrl: String,

    @field:SerializedName("ADDRESS")
    val address: String,

    @field:SerializedName("CITY")
    val city: String,

    @field:SerializedName("STATE")
    val state: String
)