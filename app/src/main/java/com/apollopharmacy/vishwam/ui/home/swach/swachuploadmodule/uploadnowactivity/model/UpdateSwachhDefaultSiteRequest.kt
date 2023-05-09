package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateSwachhDefaultSiteRequest {
    @SerializedName("emp_id")
    @Expose
    var empId: String? = null

    @SerializedName("site")
    @Expose
    var site: String? = null
}