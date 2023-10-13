package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceHistoryRes(
    @field:SerializedName("EMPID")
    val empId: String,

    @field:SerializedName("SIGNIN_DATE")
    val signInDate: String,

    @field:SerializedName("SIGNOUT_DATE")
    val signOutDate: String,

    @field:SerializedName("LATITUDE")
    val latitude: String,

    @field:SerializedName("LONGITUDE")
    val longitude: String,

    @field:SerializedName("DURIATION")
    val duration: String,
    var isExpanded: Boolean

    )