package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName

data class GetTaskListResponse(
    @field:SerializedName("TASKID")
    val taskId: String,

    @field:SerializedName("TASKNAME")
    val taskName: String,

    @field:SerializedName("EMPID")
    val empId: String,

    @field:SerializedName("SIGNIN_DATE")
    val signInDate: String,

    @field:SerializedName("SIGNIN_LATITUDE")
    val signInLatitude: String,

    @field:SerializedName("SIGNIN_LONGITUDE")
    val signInLongitude: String,

    @field:SerializedName("SIGNOUT_DATE")
    val signOutDate: String,

    @field:SerializedName("SIGNOUT_LATITUDE")
    val signOutLatitude: String,

    @field:SerializedName("SIGNOUT_LONGITUDE")
    val signOutLongitude: String,

    @field:SerializedName("DURIATION")
    val duration: String,

    var isExpanded: Boolean = false
)