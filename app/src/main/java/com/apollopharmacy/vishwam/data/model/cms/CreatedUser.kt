package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatedUser (
    @field:SerializedName("first_name")
    val firstName: String? = null,
    @field:SerializedName("middle_name")
    val middleName: String? = null,
    @field:SerializedName("last_name")
    val lastName: String? = null,
) : Serializable
