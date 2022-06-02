package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseClosedTicketApi(

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("success")
    var success: Boolean,

    @field:SerializedName("data")
    var data: Data,

    @field:SerializedName("zcServerDateTime")
    var zcServerDateTime: String? = null,

    @field:SerializedName("zcServerIp")
    var zcServerIp: String? = null,

    @field:SerializedName("zcServerHost")
    var zcServerHost: String? = null,
    ) : Serializable {

        data class  Data(
            @field:SerializedName("uid")
            var uid: String? = null,


            @field:SerializedName("ticket_id")
            var ticket_id: String? = null,


            @field:SerializedName("isUpdate")
            var isUpdate: Boolean,

            @field:SerializedName("isExecSuccess")
            var isExecSuccess: Boolean,


        ):Serializable
}