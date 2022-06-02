package com.apollopharmacy.vishwam.data.model.cms

import com.google.gson.annotations.SerializedName

data class SubmitNewV2Response(

    @field:SerializedName("cmode")
    var cmode: String? = null,

    @field:SerializedName("prescriptionImages")
    val prescriptionImages: ArrayList<PrescriptionImagesItem>,

    @field:SerializedName("problemDrescription")
    val problemDrescription: String? = null,

    @field:SerializedName("staffName")
    val staffName: String? = null,

    @field:SerializedName("branchName")
    var branchName: String? = null,

    @field:SerializedName("siteId")
    var siteId: String? = null,

    @field:SerializedName("problemSince")
    val problemSince: String? = null,

    @field:SerializedName("region")
    var region: String? = null,

    @field:SerializedName("problemType")
    val problemType: String? = null,

    @field:SerializedName("artcode")
    val artCode: String? = null,

    @field:SerializedName("batch")
    var batch: String? = null,

    @field:SerializedName("empid")
    var empId: String = "",

    ) {
    data class PrescriptionImagesItem(
        @field:SerializedName("image")
        val image: String? = null,
    )
}
