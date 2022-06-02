package com.apollopharmacy.vishwam.data.model.attendance

import com.google.gson.annotations.SerializedName

data class TaskInfoRes(
    @field:SerializedName("STATUS")
    val status: Boolean = false,

    @field:SerializedName("MESSAGE")
    val message: String? = null
)

