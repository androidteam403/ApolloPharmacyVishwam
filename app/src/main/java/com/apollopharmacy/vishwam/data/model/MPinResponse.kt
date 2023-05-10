package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MPinResponse(

    @SerializedName("Status")
    @Expose
    val Status: Boolean,

    @SerializedName("Message")
    @Expose
    val Message: String,

    @SerializedName("Mpin")
    @Expose
    val Mpin: String,
)


//package com.apollopharmacy.vishwam.data.model
//
//data class MPinResponse(
//    val Status: Boolean,
//    val Message: String,
//    val Mpin: String,
//)
//
