package com.apollopharmacy.vishwam.ui.home.cms.complainList.model

import com.google.gson.annotations.SerializedName

class TicketSubworkflowActionUpdateRequest {
    @field:SerializedName("comment")
    var comment: String? = null

    @field:SerializedName("uid")
    var uid: String? = null

    @field:SerializedName("subworkflow")
    var subworkflow: Subworkflow? = null

    class Subworkflow {
        @field:SerializedName("uid")
        var uid: String? = null
    }
}