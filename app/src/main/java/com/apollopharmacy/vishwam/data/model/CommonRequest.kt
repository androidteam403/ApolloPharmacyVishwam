package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommonRequest(
    @SerializedName("Encrypt")
    @Expose var Encrypt: String,
)