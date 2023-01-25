package com.apollopharmacy.vishwam.ui.home.cms.registration.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateUserDefaultSiteRequest {

    @SerializedName("emp_id")
    @Expose
    var empId: String? = null

    @SerializedName("site")
    @Expose
    var site: String? = null

}