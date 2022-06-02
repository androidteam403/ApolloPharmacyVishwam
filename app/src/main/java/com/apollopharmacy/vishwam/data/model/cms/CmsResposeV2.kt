package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName

data class CmsResposeV2(

    @field:SerializedName("data")
    val data: ArrayList<DataItem>,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) {

    data class TrackingDetailsItem(

        @field:SerializedName("process_By")
        val processBy: String? = null,

        @field:SerializedName("dttime")
        val dttime: String,

        @field:SerializedName("checkedWith")
        val checkedWith: String? = null,

        @field:SerializedName("status")
        val status: String? = null,

        @field:SerializedName("processingStaffName")
        val processingStaffName: String? = null,

        @field:SerializedName("remarks")
        val remarks: String? = null
    )

    data class DataItem(

        @field:SerializedName("regtime")
        val regtime: String,

        @field:SerializedName("cmode")
        val cmode: String? = null,

        @field:SerializedName("ticketNo")
        val ticketNo: String,

        @field:SerializedName("ticketRate")
        val ticketRate: String? = null,

        @field:SerializedName("problemDrescription")
        val problemDrescription: String? = null,

        @field:SerializedName("staffName")
        val staffName: String? = null,

        @field:SerializedName("branchName")
        val branchName: String? = null,

        @field:SerializedName("problemSince")
        val problemSince: String? = null,

        @field:SerializedName("statusDrescription")
        val statusDrescription: String? = null,

        @field:SerializedName("trackingDetails")
        val trackingDetails: ArrayList<TrackingDetailsItem>,

        @field:SerializedName("region")
        val region: String? = null,

        @field:SerializedName("problemType")
        val problemType: String? = null,

        @field:SerializedName("siteId")
        val siteId: String,

        @field:SerializedName("imageUrl")
        val imageUrl: ArrayList<ImageUrl>,

        var isExpanded: Boolean = false
    )

    data class ImageUrl(@field:SerializedName("url") val url: String)
}