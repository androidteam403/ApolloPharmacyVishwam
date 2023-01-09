package com.apollopharmacy.vishwam.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MPinRequest(

    @SerializedName("EmpId")
    @Expose
    val EmpId: String,

    @SerializedName("Mpin")
    @Expose
    val Mpin: String,

    @SerializedName("Type")
    @Expose
    val Type: String,
)




//package com.apollopharmacy.vishwam.data.model
//
//data class MPinRequest(
//    val EmpId: String,
//    val Mpin: String,
//    val Type: String,
//)
//
