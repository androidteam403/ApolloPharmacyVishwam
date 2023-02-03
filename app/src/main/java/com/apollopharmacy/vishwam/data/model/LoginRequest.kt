package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("PASSWORD")
    @Expose
    val PASSWORD: String,

    @SerializedName("EMPID")
    @Expose
    val EMPID: String,

    @SerializedName("COMPANY")
    @Expose
    val COMPANY:String
)

