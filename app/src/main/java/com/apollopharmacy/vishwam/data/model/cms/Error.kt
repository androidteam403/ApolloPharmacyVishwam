package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Error (
    @field:SerializedName("msg")
    val msg: String? = null,
) : Serializable

