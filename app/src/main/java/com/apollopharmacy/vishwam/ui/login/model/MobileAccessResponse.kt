package com.apollopharmacy.vishwam.ui.login.model

import com.google.gson.annotations.SerializedName

class MobileAccessResponse {

    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("AccessDetails")
    var accessDetails: AccessDetails? = null

    class AccessDetails {

        @SerializedName("RECID")
        var RECID: Int? = null

        @SerializedName("USERID")
        var USERID: String? = null

        @SerializedName("IS_CMS_APP")
        var ISCMSAPP: Boolean? = null

        @SerializedName("IS_DISCOUNT_APP")
        var ISDISCOUNTAPP: Boolean? = null

        @SerializedName("IS_ATTENDENCE_APP")
        var ISATTENDENCEAPP: Boolean? = null

        @SerializedName("IS_SUPER_ADMIN")
        var ISSUPERADMIN: Boolean? = null

        @SerializedName("IS_QCFAIL_APP")
        var ISQCFAILAPP: Boolean? = null

        @SerializedName("IS_SWACHH_APP")
        var ISSWACHHAPP: Boolean? = null

        @SerializedName("IS_NEWDRUG_APP")
        var ISNEWDRUGAPP: Boolean? = null

        @SerializedName("IS_SENSING_APP")
        var ISSENSINGAPP: Boolean? = null

        @SerializedName("IS_CHAMP_APP")
        var ISCHAMPAPP: Boolean? = null

        @SerializedName("IS_APNA_APP")
        var ISAPNAAPP: Boolean? = null

        @SerializedName("IS_APNA_RETRO_APP")
        var ISAPNARETROAPP: Boolean? = null

    }
}