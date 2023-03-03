package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.SerializedName

class SubWorkflowRejectRequest {

    @field:SerializedName("comment")
    var comment: String? = null

    @field:SerializedName("uid")
    var uid: String? = null

    @field:SerializedName("status")
    var status: String? = null

    @field:SerializedName("employee_id")
    var employee_id: String? = null

    @field:SerializedName("ticket_id")
    var ticket_id: String? = null

}