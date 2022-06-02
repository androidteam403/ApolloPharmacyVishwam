package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PendingListToAcknowledge(

    @field:SerializedName("data")
    val data: ArrayList<DataItem>,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Serializable

data class DataItem(

    @field:SerializedName("regtime")
    val regtime: String? = null,

    @field:SerializedName("ticketNo")
    val ticketNo: String,

    @field:SerializedName("checkedwith")
    val checkedwith: String? = null,

    @field:SerializedName("problemDrescription")
    val problemDrescription: String? = null,

    @field:SerializedName("closeTime")
    val closeTime: String? = null,

    @field:SerializedName("process_By")
    val processBy: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Serializable
