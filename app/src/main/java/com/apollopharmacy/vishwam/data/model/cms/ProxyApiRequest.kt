package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProxyApiRequest(
    @field:SerializedName("REQUESTURL")
    var REQUESTURL: String?= null,

    @field:SerializedName("REQUESTTYPE")
    var REQUESTTYPE: String? = null,

    @field:SerializedName("REQUESTJSON")
    var REQUESTJSON: String? = null
):Serializable
{
}