package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName

data class AtdLogInOutRes(
    @field:SerializedName("STATUS")
    val status: Boolean,

    @field:SerializedName("MESSAGE")
    val message: String
)