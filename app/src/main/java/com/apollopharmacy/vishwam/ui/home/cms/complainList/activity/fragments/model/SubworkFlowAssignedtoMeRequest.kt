package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubworkFlowAssignedtoMeRequest {
    @Expose
    @SerializedName("employee_id")
    var employee_id: String? = null

    @Expose
    @SerializedName("subworkflow_seq_order")
    var subworkflow_seq_order: Int? = null

    @Expose
    @SerializedName("subworkflow_step_order")
    var subworkflow_step_order: Int? = null

    @Expose
    @SerializedName("uid")
    var uid: String? = null

    @Expose
    @SerializedName("reason")
    var reason: String? = null


}