package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RequestCMSLogin(
    @field:SerializedName("appUserName")
    var appUserName: String? = null,

    @field:SerializedName("appPassword")
    var appPassword: String? = null,
):Serializable
{
}