package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName

data class LoginInfoRes(
    @field:SerializedName("STATUS")
    val status: Boolean = false,

    @field:SerializedName("MESSAGE")
    val message: String? = null,

    @field:SerializedName("LASTSIGNINDATE")
    val lastLoginDate: String? = null,

    @field:SerializedName("LASTSIGNOUTDATE")
    val lastLogoutDate: String? = null
)

