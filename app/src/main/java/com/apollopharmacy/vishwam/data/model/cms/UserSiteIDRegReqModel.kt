package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName

data class UserSiteIDRegReqModel(
    @field:SerializedName("EMPID")
    val empId: String? = null,

    @field:SerializedName("SITEID")
    val siteId: String? = null
)